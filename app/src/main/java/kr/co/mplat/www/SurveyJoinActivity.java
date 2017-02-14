package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

public class SurveyJoinActivity extends AppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    Common common = null;
    private final int CALLTYPE_AUTH = 0;
    private int dialogType = 0;
    WebView wv_auth = null;
    private String auth_uid = "";
    private String pre_activity = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_survey_join);
        common = new Common(this);
        intent = getIntent();
        String url = intent.getExtras().getString("URL");
        Log.i("wtkim","url==>"+url);
        /*if(intent.hasExtra("PRE_ACTIVITY")){
            pre_activity = intent.getExtras().getString("PRE_ACTIVITY");
        }else{
            pre_activity = "";
        }*/
        WebView wv_auth = (WebView) findViewById(R.id.surveyJoin_wv);
        WebSettings webSettings = wv_auth.getSettings();
        webSettings.setJavaScriptEnabled(true); //자바스크립트 허용
        wv_auth.setWebViewClient(new WebViewClient());    //웹뷰 안에서 다른 페이지로 이동할 경우 웹뷰 안에서 이동하도록 함
        wv_auth.addJavascriptInterface(new SurveyJoinActivity.WebAppInterface(this), "AndroidAPP"); //함수 호출을 위함
        wv_auth.loadUrl(url);
    }

    private class WebAppInterface {
        Context mContext;
        WebAppInterface(Context c) {
            mContext = c;
        }
        //실패
        @JavascriptInterface
        public void fail(final String err){
            dialogType = 9;
            runOnUiThread(new Runnable() {
                public void run() {
                    Common.createDialog(SurveyJoinActivity.this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                }
            });
        }
        //성공
        @JavascriptInterface
        public void success(final String RESULT_CODE, final String point, final String total_point){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("wtkim","RESULT_CODE==>"+RESULT_CODE);
                    Log.i("wtkim","point==>"+point);
                    Log.i("wtkim","total_point==>"+total_point);
                    intent = new Intent(SurveyJoinActivity.this,SurveyResultActivity.class);
                    intent.putExtra("RESULT_CODE",RESULT_CODE);
                    intent.putExtra("POINT",point);
                    intent.putExtra("TOTAL_POINT",total_point);
                    startActivity(intent);
                    finish();
                }
            });
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

    @Override
    public void dialogHandler(String result) {
        intent = new Intent(SurveyJoinActivity.this,SurveyActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
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


}
