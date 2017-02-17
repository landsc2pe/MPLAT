package kr.co.mplat.www;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gdfwo on 2016-11-21.
 */

public class Common {
    private Activity activity = null;
    private AlertDialog checkNetworkDialog = null;
    private AlertDialog updateCheckDialog = null;
    static private String preferenceName = "userinfo";
    static private String secureKey = "app_mplat_1.0";
    static private AlertDialog commonDialog = null;
    ProgressDialog progressDialog;


    public Common(Activity activity) {
        this.activity = activity;
    }

    //인터넷 연결상태 확인
    public boolean isConnected() {
        if (activity == null) return false;
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            hideCheckNetworkDialog();
            return true;
        }
        return false;
    }

    //앱 업데이트 알림 띄우기
    public void showAppUpdateCheckDialog() {
        if (activity != null) {
            final View innerView = activity.getLayoutInflater().inflate(R.layout.custom_dialog_app_update_check, null);
            AlertDialog.Builder ab = new AlertDialog.Builder(activity);
            ab.setView(R.layout.custom_dialog_app_update_check);
            ab.setCancelable(false);
            updateCheckDialog = ab.create();
        }
        if (!updateCheckDialog.isShowing()) checkNetworkDialog.show();
    }
    public void hideUpdateCheckDialog() {
        if (updateCheckDialog != null) {
            if (updateCheckDialog.isShowing()) updateCheckDialog.dismiss();
            updateCheckDialog = null;
        }
    }

    //인터넷 연결안됨 경고 띄우기
    public void showCheckNetworkDialog() {
        if (checkNetworkDialog == null && activity != null) {
            final View innerView = activity.getLayoutInflater().inflate(R.layout.custom_dialog_checknetwork, null);
            AlertDialog.Builder ab = new AlertDialog.Builder(activity);
            ab.setView(R.layout.custom_dialog_checknetwork);
            ab.setCancelable(false);
            checkNetworkDialog = ab.create();
        }
        if (!checkNetworkDialog.isShowing()) checkNetworkDialog.show();
    }



    public void LoadingDialog(){

    }
    public void hideCheckNetworkDialog() {
        if (checkNetworkDialog != null) {
            if (checkNetworkDialog.isShowing()) checkNetworkDialog.dismiss();
            checkNetworkDialog = null;
        }
    }

    public void warningCheckNetwork() {
        if (activity != null)
            Common.createDialog(activity, activity.getString(R.string.txt_title).toString(),null, activity.getString(R.string.msg_checkNetwork), activity.getString(R.string.btn_ok),null,false, false);
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 저장 정보 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    static public String getPreference(Context context,String prefKey){
        SecurePreferences preferences = new SecurePreferences(context, preferenceName,secureKey, true);
        String result = preferences.getString(prefKey);
        if(result==null)result="";
        return result;
    }
    static public void setPreference(Context context,String prefKey,String prefValue){
        SecurePreferences preferences = new SecurePreferences(context, preferenceName,secureKey, true);
        preferences.put(prefKey,prefValue);
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 저장 정보 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //임의의 다이어로그 생성
    public static void createDialog(Activity activity, String title,String subtitle, String contents, String btn1,String btn2, boolean resume, boolean finish) {
        try {//화면 회전시 오류가 생김
            if (commonDialog != null && commonDialog.isShowing()) commonDialog.dismiss();
        } catch (Exception e) {
            //Log.d("MYLOG",e.toString());
        }
        final View innerView = activity.getLayoutInflater().inflate(R.layout.custom_dialog_common, null);
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setView(R.layout.custom_dialog_common);
        if (finish) ab.setCancelable(false);//finish가 되어야 하는 경우에는 cancel이 불가능해야함
        commonDialog = ab.create();
        commonDialog.show();
        TextView txt_title = (TextView) commonDialog.findViewById(R.id.txt_title);
        TextView txt_subtitle = (TextView) commonDialog.findViewById(R.id.txt_subtitle);
        TextView txt_contents = (TextView) commonDialog.findViewById(R.id.txt_contents);
        TextView btn_common_ok = (TextView) commonDialog.findViewById(R.id.btn_common_ok);
        TextView btn_common_cancel = (TextView) commonDialog.findViewById(R.id.btn_common_cancel);
        Object[] tag = {activity, resume, finish, commonDialog};
        txt_title.setText(title);
        txt_subtitle.setText(subtitle);
        txt_contents.setText(contents);
        btn_common_ok.setText(btn1);
        //Log.i("wtKim",btn_common_cancel.getText().toString());
        if(btn2 == null||btn2.equals("")){
            btn_common_cancel.setVisibility(View.GONE);
            //btn_common_cancel.setText("");
            //btn_common_cancel.setWidth(0);
        }else{
            btn_common_cancel.setText(btn2);
        }
        btn_common_ok.setTag(tag);
        btn_common_ok.setOnClickListener(new Common.dialogOnClickListener());
        btn_common_cancel.setTag(tag);
        btn_common_cancel.setOnClickListener(new Common.dialogOnClickListener());
    }

    public static class dialogOnClickListener implements View.OnClickListener {
        public void onClick(View view) {
            String dialogAnswer="";
            if(view.getId()==R.id.btn_common_ok){
                dialogAnswer = "ok";
            } else if(view.getId()==R.id.btn_common_cancel){
                dialogAnswer = "cancel";
            }
            Object[] tag = (Object[]) view.getTag();
            I_startFinish activity = (I_startFinish) tag[0];
            if(tag[0] instanceof I_dialogdata){
                I_dialogdata dialdata = (I_dialogdata) tag[0];
                dialdata.dialogHandler(dialogAnswer);
            }

            boolean resume = (boolean) tag[1];
            boolean finish = (boolean) tag[2];
            AlertDialog dialog = (AlertDialog) tag[3];


            if (resume) activity.start(null);
            if (finish) activity.finish();

            dialog.dismiss();
        }
    }
    static class HttpPingAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                // HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) new URL(urls[0]).openConnection();
                con.setInstanceFollowRedirects(false);
                con.setRequestMethod("HEAD");
                return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                return false;
            }
        }
    }
    //숫자를 입력받는 edittext에 콤마 추가
    public static class CustomTextWathcer implements TextWatcher {
        @SuppressWarnings("unused")
        private EditText mEditText;
        String strAmount = ""; // 임시 저장값 (콤마)

        public CustomTextWathcer(EditText e) {
            mEditText = e;
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {    //텍스트가 변경될때마다 실행
            if (!s.toString().equals(strAmount)) { // StackOverflow 방지
                strAmount = makeStringComma(s.toString().replace(",", ""));
                mEditText.setText(strAmount);
                Editable e = mEditText.getText();
                Selection.setSelection(e, strAmount.length());
            }
        }

        protected String makeStringComma(String str) {    // 천단위 콤마 처리
            if (str.length() == 0)
                return "";
            long value = Long.parseLong(str);
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(value);
        }

    }

    //tv 금액 콤마
    public static String getTvComma(String str) {
        if (str.length() == 0){return "0";}
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
    public static void Logg(String str1){
        Log.d("wtKim",str1);
    }
    //JSONArray 병합
    static public JSONArray concatJSONArray(JSONArray ...arrays) throws JSONException {
        JSONArray result = new JSONArray();
        for(JSONArray array:arrays){
            for(int i=0;i<array.length();i++){
                result.put(array.get(i));
            }
        }
        return result;
    }
    //이메일 검증
    public static class EmailValidator {
        private Pattern pattern;
        private Matcher matcher;
        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        public EmailValidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }
        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
    //휴대폰번호 검증
    public static class MobileValidator {
        private Pattern pattern;
        private Matcher matcher;
        private static final String MOBILE_PATTERN = "^(01)[0-9]{8,9}";
        public MobileValidator() {
            pattern = Pattern.compile(MOBILE_PATTERN);
        }
        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
    //ID 검증
    public static class IdValidator {
        private Pattern pattern;
        private Matcher matcher;
        private static final String ID_PATTERN ="[A-Za-z0-9]{6,20}";
        public IdValidator() {
            pattern = Pattern.compile(ID_PATTERN);
        }
        public boolean validate(final String hex) {
            matcher = pattern.matcher(hex);
            return matcher.matches();
        }
    }
    //키보드 가리기
    public static void hideKeyboard(Activity activity){
        //키보드 가리기
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    //전화번호 분리
    public static String getMobileNum(String mobile){
        mobile = mobile.replace("-","");
        mobile = mobile.substring(0,3)+"-"+mobile.substring(3,mobile.length()-4)+"-"+mobile.substring(mobile.length()-4);
        return mobile;
    }
    //앱 버전 가져오기
    public static String getVer(){
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionName+"."+versionCode;
    }
    public static int getVerCode(){
        int versionCode = BuildConfig.VERSION_CODE;
        return versionCode;
    }
    //뱃지 표시
    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
    //비밀번호 정규식
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("[a-zA-Z0-9]*[^a-zA-Z0-9\\n]+[a-zA-Z0-9]*$"); // 4자리 ~ 16자리까지 가능

    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }
    //이메일 정규식
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
    //계좌번호 정규식
    public static final  Pattern VALID_ACCOUNT_NUM_REGEX = Pattern.compile("[0-9]{6,16}$", Pattern.CASE_INSENSITIVE);
    public static boolean validateAccountNum(String accountNumStr) {
        Matcher matcher = VALID_ACCOUNT_NUM_REGEX.matcher(accountNumStr);
        return matcher.find();
    }
    public static String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);
        strDate = strDate.replaceAll("-","");

        return strDate;
    }
    public static String getDateString(String type) {
        String strDate = "";
        if(type.equals("yyyy-MM-dd")){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",java.util.Locale.getDefault());
            Date date = new Date();
            strDate = dateFormat.format(date);
        }
        return strDate;
    }

    public static String getRandomString(int i) {
        String str = "";
        while (str.length()<i){
            int rndNum = (int)(Math.random()*10)+1;
            str+=String.valueOf(rndNum);
        }

        return str;
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 데이터 불러오기 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public static String GET(String targetURL,String urlParameters){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"ERR\":\"Exception1\"}";
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        public int calltype;
        public Object[][] datas;
        private String params = "";
        private I_loaddata i_activity = (I_loaddata)activity;
        @Override
        protected String doInBackground(String... urls){
            try {
                //기본적으로 UID와 KEY를 전달
                params = "";
                if(activity!=null)params += "UID=" + URLEncoder.encode(Common.getPreference(activity.getApplicationContext(),"UID"), "UTF-8");
                if(activity!=null)params += "&KEY=" + URLEncoder.encode(Common.getPreference(activity.getApplicationContext(),"KEY"), "UTF-8");

                //추가로 입력받은 파라메터가 있다면 전달
                if(datas!=null) {
                    int i;
                    Object[] data;
                    String key, value;
                    for (i = 0; i < datas.length; i++) {
                        data = datas[i];
                        if(data[0]==null)continue;
                        else key = (String) data[0];
                        if(data[1]==null)value="";
                        else value = (String) data[1];
                        params += "&" + key + "=" + URLEncoder.encode(value, "UTF-8");
                    }
                }
                Log.d("UID&KEY",params);
            } catch(Exception e){
                params = "";
                Log.d("wtkim",e.toString());
            }
            return GET(urls[0],params);
        }
        @Override
        protected void onPostExecute(String result){
            String err = "";
            String toast = "";
            String postResult = "";
            loading_cnt--;
            try {
                if(loading_cnt==0)progressDialog.dismiss();
                JSONObject json = new JSONObject(result);
                err = json.getString("ERR");
                postResult = json.getString("RESULT");
                //출력해야 할 토스트가 있다면 출력함
                if(json.has("TOAST")){
                    toast = json.getString("TOAST");
                    if(activity!=null)if(!toast.equals("")) Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show();
                }
            } catch(Exception e){
                err = e.toString();
            }
            if(postResult.equals("LOGOUT")){
                setPreference(activity, "UID", "");
                setPreference(activity, "KEY", "");
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivityForResult(intent, 0);
                return;
            }
            if(i_activity!=null)i_activity.loaddataHandler(calltype,result);
        }
    }

    public void loadDataNoDialog(int calltype,String url,Object[][] params){
        loading_cnt++;
        HttpAsyncTask hat = new HttpAsyncTask();
        hat.calltype=calltype;
        hat.datas =params;
        hat.execute(url);
    }

    int loading_cnt=0;
    public void loadData(int calltype,String url,Object[][] params){
        loading_cnt++;
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("잠시만 기다려 주세요.");
            progressDialog.show();

        }catch (Exception e){

        }

        HttpAsyncTask hat = new HttpAsyncTask();
        hat.calltype=calltype;
        hat.datas =params;
        hat.execute(url);
    }
    public static final String getKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:%s", keyHash);
                return keyHash;
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.d("getKeyHash Error:%s", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("getKeyHash Error:%s", e.getMessage());
        }
        return "";
    }

    public static String getAppVersion(Context context) {
        String versionName = "";
        versionName="A"+BuildConfig.VERSION_NAME+'.'+Integer.toString(BuildConfig.VERSION_CODE);


        /*
        // application version
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("wtKim","getAppVersion",e);
            //StatusLogger.e(TAG, "", e);
        }
        */
        return versionName;
    }
}
