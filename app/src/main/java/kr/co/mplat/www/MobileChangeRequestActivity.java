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

public class MobileChangeRequestActivity extends NAppCompatActivity implements NTextView.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    private int mnMiliSecond = 1000;
    private int value;
    private int mnExitDelay = 61;
    private CountDownTimer timer;
    private String mobile = "";

    final int CALLTYPE_SAVE = 1;
    final int CALLTYPE_RESEND = 2;
    Intent intent = null;
    Common common = null;
    TextView TvCount = null;
    private int dialogType = 0;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_change_request);
        setTvTitle("인증번호 확인");

        common = new Common(this);
        TvCount = (TextView) findViewById(R.id.mobileChangeRequest_tvCount);
        AuthcodeTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mobileChangeRequest_btnReSendAuthCode:
                mobile = getIntent().getStringExtra("MOBILE_TEL");
                Object[][] params1 = {
                        {"MOBILE", mobile}
                };
                //인증번호 재발송
                common.loadData(CALLTYPE_RESEND, getString(R.string.url_mobileChange), params1);
                break;
            case R.id.mobileChangeRequest_btnOk:
                mobile = getIntent().getStringExtra("MOBILE_TEL");
                EditText etAuthcode = (EditText)findViewById(R.id.mobileChangeRequest_etAuthCode);
                //인증번호 확인
                Object[][] params2 = {
                        {"MOBILE", mobile}
                        ,{"AUTHCODE", etAuthcode.getText().toString()}
                };
                common.loadData(CALLTYPE_SAVE, getString(R.string.url_mobileChangeRequest), params2);
                break;
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SAVE){
            saveHandler(str);
        }else if (calltype == CALLTYPE_RESEND){
            resendHandler(str);
        }
    }

    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    dialogType = 1;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "휴대폰 번호 변경이 완료되었습니다.", getString(R.string.btn_ok),null, false, false);

                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //재발송 처리
    public void resendHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    timer.cancel();//기존타이머 취소
                    AuthcodeTimer();//새 타이머 호출
                    TextView etMobile = (TextView)findViewById(R.id.mobileChangeRequest_etAuthCode);//Tv 초기화
                    etMobile.setText("");
                }
            } else {
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);

            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
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
    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 1){
            intent = new Intent(MobileChangeRequestActivity.this,MypageActivity.class);
            startActivity(intent);
        }

    }

}
