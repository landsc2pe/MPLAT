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

public class SearchPwAuthActivity extends NAppCompatActivity implements NTextView.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    private int mnMiliSecond = 1000;
    private int value;
    private int mnExitDelay = 61;
    private CountDownTimer timer;
    private String etAuthcode = "";

    private String id = "";
    private String mobile = "";
    private String authcode = "";

    final int CALLTYPE_SAVE = 1;
    final int CALLTYPE_RESEND = 2;
    final int CALLTYPE_OK = 3;
    Intent intent = null;
    Common common = null;
    TextView TvCount = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw_auth);
        setTvTitle("인증번호 확인");
        common = new Common(this);
        TvCount = (TextView) findViewById(R.id.searchpwauth_tvCount);
        AuthcodeTimer();

    }

    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        mobile = getIntent().getStringExtra("MOBILE_TEL").toString();
        id = getIntent().getStringExtra("ID").toString();
        EditText etAuthcode = (EditText)findViewById(R.id.searchpwauth_etAuthcode);
        String authcode = etAuthcode.getText().toString();
        switch (view.getId()){
            case R.id.searchpwauth_btnReSendAuthCode:
                dialogType=1;
                Object[][] params1 = {
                        {"ID", id}
                        ,{"MOBILE", mobile}
                };

                //인증번호 재발송
                common.loadData(CALLTYPE_RESEND, getString(R.string.url_searchPwMobile), params1);
                break;
            case R.id.searchpwauth_btnOk:
                dialogType=2;
                //인증번호 확인
                Object[][] params2 = {
                         {"MOBILE", mobile}
                        ,{"ID", id}
                        ,{"AUTHCODE", authcode}
                };
                common.loadData(CALLTYPE_OK, getString(R.string.url_searchPwAuth), params2);
                break;
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
    @Override
    public void dialogHandler(String result) {

    }
    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_OK){
            saveHandler(str);
        }else if(calltype == CALLTYPE_RESEND){
            resencHandler(str);
        }

    }
    //재발송 처리
    public void resencHandler(String str) {
        EditText etAuthcode = (EditText)findViewById(R.id.searchpwauth_etAuthcode);
        String authcode = etAuthcode.getText().toString();
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    timer.cancel();//기존타이머 취소
                    AuthcodeTimer();//새 타이머 호출
                    //Tv 초기화
                    TextView etMobile = (TextView)findViewById(R.id.searchpwauth_etAuthcode);
                    etMobile.setText("");
                }
            } else {
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    //저장 처리
    public void saveHandler(String str) {
        EditText etAuthcode = (EditText)findViewById(R.id.searchpwauth_etAuthcode);
        String authcode = etAuthcode.getText().toString();
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.d("wtKim","result=="+result);
                if (result.equals("OK")) {
                    intent = new Intent(SearchPwAuthActivity.this,SearchPwChangeActivity.class);
                    intent.putExtra("ID",id);
                    intent.putExtra("MOBILE_TEL",mobile);
                    intent.putExtra("AUTHCODE",authcode);
                    startActivity(intent);
                }
            } else {
                Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
