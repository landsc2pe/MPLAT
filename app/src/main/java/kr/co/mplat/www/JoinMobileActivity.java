package kr.co.mplat.www;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class JoinMobileActivity extends NAppCompatActivity implements View.OnClickListener,I_startFinish,I_dialogdata,I_loaddata{
    final int CALLTYPE_SAVE = 1;
    Intent intent = null;
    TextView etMobile = null;
    Common common = null;
    String mobile,sns_id,sns_type,sns_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_mobile);
        setTvTitle("휴대폰 인증");
        common = new Common(this);

        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-joinMobileActivity","UID==>"+UID);
        Log.i("comm-joinMobileActivity","KEY==>"+KEY);


        etMobile = (EditText) findViewById(R.id.joinmobile_etMobile);

        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();


    }
    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        switch (view.getId()){
            //인증번호 전송 버튼 클릭시
            case R.id.joinmobile_btnSendAuthCode:
                if(etMobile.getText().toString().equals("")){
                    dialogType=1;
                    Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, getString(R.string.dial_msg5), getString(R.string.btn_ok),null, false, false);

                }else{
                    dialogType=2;
                    mobile=etMobile.getText().toString();
                    mobile=mobile.substring(0,3)+'-'+mobile.substring(3,mobile.length()-4)+'-'+mobile.substring(mobile.length()-4);
                    Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), mobile, getString(R.string.msg_sendCodeThisMoblie), getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
                    return;
                }
                break;
        }
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2){
            Object[][] params = {
                    {"MOBILE", etMobile.getText().toString()}
            };
            //인증번호 발송
            common.loadData(CALLTYPE_SAVE, getString(R.string.url_joinmobile), params);
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SAVE) saveHandler(str);
    }
    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    //인증코드 입력화면으로 이동
                    intent = new Intent(JoinMobileActivity.this,JoinAuthActivity.class);
                    intent.putExtra("MOBILE_TEL",etMobile.getText().toString());
                    intent.putExtra("SNS_ID",sns_id);
                    intent.putExtra("SNS_TYPE",sns_type);
                    intent.putExtra("SNS_EMAIL",sns_email);

                    startActivity(intent);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, err, getString(R.string.btn_ok),null, false, false);
                return;
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, e.toString(), getString(R.string.btn_ok),null, false, false);
            //Log.d("MYLOG", e.toString());
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
