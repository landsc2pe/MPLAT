package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

public class AuthWvActivity extends AppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    Common common = null;
    private final int CALLTYPE_AUTH = 0;
    private int dialogType = 0;
    WebView wv_auth = null;
    private String auth_uid = "";
    private String auth_name = "";
    private String auth_gender = "";
    private String auth_birth_date = "";
    private String pre_activity = "";
    private String pre_campaign_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_wv);
        common = new Common(this);
        intent = getIntent();
        String url = intent.getExtras().getString("URL");
        if(intent.hasExtra("PRE_ACTIVITY")){
            pre_activity = intent.getExtras().getString("PRE_ACTIVITY");
        }else{
            pre_activity = "";
        }

        pre_campaign_code = intent.getExtras().getString("PRE_CAMPAIGN_CODE");



        Log.i("wtkim","url==>"+url);
        WebView wv_auth = (WebView) findViewById(R.id.authWv_wv1);
        WebSettings webSettings = wv_auth.getSettings();
        webSettings.setJavaScriptEnabled(true); //자바스크립트 허용
        wv_auth.setWebViewClient(new WebViewClient());    //웹뷰 안에서 다른 페이지로 이동할 경우 웹뷰 안에서 이동하도록 함
        wv_auth.addJavascriptInterface(new WebAppInterface(this), "AndroidAPP"); //함수 호출을 위함
        wv_auth.loadUrl(url);
    }

    private class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        //인증실패
        @JavascriptInterface
        public void auth_fail(final String msg){
            dialogType = 9;
            runOnUiThread(new Runnable() {
                public void run() {
                    String fail_msg = getString(R.string.dial_msg8);
                    if(!msg.equals(""))fail_msg = msg;
                    Common.createDialog(AuthWvActivity.this, getString(R.string.app_name).toString(),null, "본인인증에 실패하였습니다.", getString(R.string.btn_ok),null, false, false);
                }
            });
        }
        //인증성공
        @JavascriptInterface
        public void auth_success(final String name, final String birth_date, final String gender){
        /*    setDismiss(dialog);
            dialog = createDialog(R.layout.dialog_loading);
            dialog.show();*/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    auth_name = name;
                    auth_gender = gender;
                    auth_birth_date = birth_date;

                    Object[][] param = {
                             {"NAME", auth_name}
                            ,{"GENDER", auth_gender}
                            ,{"BIRTH_DATE", auth_birth_date}
                    };

                    common.loadData(CALLTYPE_AUTH, getString(R.string.url_authComplete), param);
                }
            });
        }
    }
    //저장 후 처리
    public void authHandler(String str) {
        //Log.d("MYLOG", "STR:" + str.substring(0, str.length() - 1));
        //setDismiss(dialog);
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    Common.createDialog(AuthWvActivity.this, getString(R.string.app_name).toString(),null, "본인인증이 정상적으로 완료되었습니다.", getString(R.string.btn_ok),null, false, false);
                }
            } else {
                Common.createDialog(AuthWvActivity.this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            //Log.d("MYLOG", e.toString());
            //Log.d("MYLOG", "STR:" + str.substring(0, str.length() - 1));
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private AlertDialog createDialog(int layoutResource) {
        //if (dialog != null && dialog.isShowing()) return dialog;
        final View innerView = getLayoutInflater().inflate(layoutResource, null);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(layoutResource);
        return ab.create();
    }

    public void setDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
    @Override
    public void dialogHandler(String result) {
        Log.i("wtkim","pre_activity(AuthWv)==>"+pre_activity);
        if(pre_activity.equals("")){
            if(dialogType == 1){
                finish();
                intent = new Intent(AuthWvActivity.this, BankActivity.class);
                intent.putExtra("PRE_ACTIVITY","");
                startActivity(intent);
            }else if(dialogType == 9){
                intent = new Intent(AuthWvActivity.this, PointActivity.class);
                intent.putExtra("PRE_ACTIVITY","");
                startActivity(intent);
                finish();
            }
        }else if(pre_activity.equals("AuthDescActivity")){
            intent = new Intent(AuthWvActivity.this, MypageActivity.class);
            intent.putExtra("PRE_ACTIVITY","");
            startActivity(intent);
            finish();
        }else if(pre_activity.equals("CashAuthDescActivity")) {
            intent = new Intent(AuthWvActivity.this, BankActivity.class);
            intent.putExtra("PRE_ACTIVITY","CashAuthDescActivity");
            startActivity(intent);
            finish();
        }else if(pre_activity.equals("SurveyAuthActivity")) {
            intent = new Intent(AuthWvActivity.this, SurveyActivity.class);
            startActivity(intent);
            finish();
        }else if(pre_activity.equals("ReviewDetailActivity")) {
            intent = new Intent(AuthWvActivity.this, ReviewDetailActivity.class);
            intent.putExtra("CAMPAIGN_CODE",pre_campaign_code);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_AUTH) saveHandler(str);
    }
    //인증 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    dialogType = 1;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "본인인증이 정상적으로 완료되었습니다.", getString(R.string.btn_ok),null, false, false);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
            //Log.d("wtKim", e.toString());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }


    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
    }
}
