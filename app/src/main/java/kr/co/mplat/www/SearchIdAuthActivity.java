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

public class SearchIdAuthActivity extends NAppCompatActivity implements NTextView.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
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
        setContentView(R.layout.activity_search_id_auth);
        setTvTitle("인증번호 확인");

        common = new Common(this);
        TvCount = (TextView) findViewById(R.id.searchidauth_tvCount);
        AuthcodeTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchidauth_btnReSendAuthCode:
                mobile = getIntent().getStringExtra("MOBILE_TEL");
                Object[][] params1 = {
                        {"MOBILE", mobile}
                };
                //인증번호 재발송
                common.loadData(CALLTYPE_RESEND, getString(R.string.url_searchId), params1);
                break;
            case R.id.searchidauth_btnOk:
                mobile = getIntent().getStringExtra("MOBILE_TEL");
                EditText etAuthcode = (EditText)findViewById(R.id.searchidauth_etAuthCode);
                //인증번호 확인
                Object[][] params2 = {
                        {"MOBILE", mobile}
                       ,{"AUTHCODE", etAuthcode.getText().toString()}
                };
                common.loadData(CALLTYPE_SAVE, "http://app.mplat.co.kr:8080/searchIdAuth", params2);
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
                String id = json.getString("ID");
                if (result.equals("OK")) {
                    intent = new Intent(SearchIdAuthActivity.this,SearchIdResultActivity.class);
                    intent.putExtra("ID",id);
                    startActivity(intent);
                }
            } else {
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
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
                    TextView etMobile = (TextView)findViewById(R.id.searchidauth_etAuthCode);//Tv 초기화
                    etMobile.setText("");
                }
            } else {
                Log.i("wtKim","3333333");
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);

            }
        } catch (Exception e) {
            Log.i("wtKim","444444444");
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

    }
}
