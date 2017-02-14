package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class SearchIdActivity extends NAppCompatActivity implements View.OnClickListener,I_startFinish,I_loaddata,I_dialogdata{
    final int CALLTYPE_SAVE = 1;
    private Common common = null;
    Intent intent = null;
    EditText etMobile = null;
    Button btnSendAuthCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        common=new Common(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);
        setTvTitle("아이디 찾기");
        etMobile = (EditText) findViewById(R.id.etMobile);
        etMobile.getText();
        btnSendAuthCode = (Button) findViewById(R.id.btnSendAuthCode);
    }

    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        switch (view.getId()){
            case R.id.btnSendAuthCode :
                if(etMobile.getText().toString().length()<10){
                    dialogType=1;
                    Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, getString(R.string.msg_inputMobile), getString(R.string.btn_ok),null, false, false);
                }else{
                    dialogType=2;
                    String mobile=etMobile.getText().toString();
                    mobile=mobile.substring(0,3)+'-'+mobile.substring(3,mobile.length()-4)+'-'+mobile.substring(mobile.length()-4);
                    Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), mobile, getString(R.string.msg_sendCodeThisMoblie), getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
                }
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
                    String mobile = etMobile.getText().toString();
                    intent = new Intent(SearchIdActivity.this,SearchIdAuthActivity.class);
                    intent.putExtra("MOBILE_TEL",mobile);
                    startActivity(intent);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    @Override
    public void dialogHandler(String result) {
        String mobile = etMobile.getText().toString();
        if(result.equals("ok") && dialogType == 2){
            Object[][] params = {
                    {"MOBILE", mobile}
            };
            //인증번호 발송
            common.loadData(CALLTYPE_SAVE, getString(R.string.url_searchId), params);
        }
    }
}
