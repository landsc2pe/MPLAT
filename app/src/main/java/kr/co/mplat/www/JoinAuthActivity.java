package kr.co.mplat.www;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class JoinAuthActivity extends NAppCompatActivity implements NTextView.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    private int mnMiliSecond = 1000;
    private int value;
    private int mnExitDelay = 61;
    private CountDownTimer timer;
    private String mobile = "";
    private String authcode = "";

    final int CALLTYPE_RESEND = 1;
    final int CALLTYPE_OK = 2;
    Intent intent = null;
    Common common = null;
    TextView TvCount = null;
    String sns_id,sns_type,sns_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_auth);
        setTvTitle("휴대폰 인증");

        common = new Common(this);

        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-joinAuthActivity","UID==>"+UID);
        Log.i("comm-joinAuthActivity","KEY==>"+KEY);

        TvCount = (TextView) findViewById(R.id.joinauth_tvCount);
        AuthcodeTimer();

        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();


    }
    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        mobile = getIntent().getStringExtra("MOBILE_TEL").toString();
        EditText etAuthcode = (EditText)findViewById(R.id.joinauth_etAuthcode);
        authcode = etAuthcode.getText().toString();
        switch (view.getId()) {
            case R.id.joinauth_btnReSendAuthCode:
                dialogType=1;
                Object[][] params1 = {
                        {"MOBILE", mobile}
                };
                //인증번호 재발송
                common.loadData(CALLTYPE_RESEND, getString(R.string.url_joinMobile), params1);
                break;
            case R.id.joinauth_btnOk:
                dialogType=2;
                //인증번호 확인
                Object[][] params2 = {
                         {"MOBILE", mobile}
                        ,{"AUTHCODE", authcode}
                };
                common.loadData(CALLTYPE_OK, getString(R.string.url_joinAuth), params2);
                break;
        }
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_OK && dialogType==2){
            saveHandler(str);
        } else if (calltype == CALLTYPE_RESEND && dialogType==1) {
            resendHandler(str);
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

    public void AuthcodeTimer(){
        value = 61;
        int delay = mnExitDelay * mnMiliSecond;
        timer = new CountDownTimer(delay,1000) {
            @Override
            public void onTick(long l) {
                value--;
                String strValue = "";
                if(value<10){
                    strValue = "0"+value;
                }else{
                    strValue = value+"";
                }
                TvCount.setText("00:"+strValue);
            }

            @Override
            public void onFinish() {
                TvCount.setText("00:00");
            }
        };
        timer.start();

    }
    //재발송 처리
    public void resendHandler(String str) {
        Log.i("wtKim","resendHandler() call!");
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK") && dialogType == 1) {
                    Log.i("wtKim","resendHandler() ==>1 call!");
                    EditText etAuthcode = (EditText)findViewById(R.id.joinauth_etAuthcode);
                    authcode = etAuthcode.getText().toString();
                    timer.cancel();//기존타이머 취소
                    AuthcodeTimer();//새 타이머 호출
                    etAuthcode.setText("");
                }
            } else {
                Log.i("wtKim","resendHandler() ==>2 call!");
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtKim","resendHandler() ==>3 call!");
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //저장 처리
    public void saveHandler(String str) {
        Log.i("wtKim","saveHandler() call!");
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {

                String result = json.getString("RESULT");
                String join_queue_seq = json.getString("JOIN_QUEUE_SEQ");
                if (result.equals("OK") && dialogType == 2) {
                    Log.i("wtKim","saveHandler() ==> 1 call!");
                    intent = new Intent(JoinAuthActivity.this, JoinActivity.class);
                    intent.putExtra("MOBILE_TEL",mobile);
                    intent.putExtra("JOIN_QUEUE_SEQ",join_queue_seq);
                    intent.putExtra("SNS_ID",sns_id);
                    intent.putExtra("SNS_TYPE",sns_type);
                    intent.putExtra("SNS_EMAIL",sns_email);

                    startActivity(intent);
                }
            } else {
                Log.i("wtKim","saveHandler() ==> 2 call!");
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtKim","saveHandler() ==> 3 call!");
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

}
