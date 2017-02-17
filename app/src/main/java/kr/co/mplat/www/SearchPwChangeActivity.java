package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

public class SearchPwChangeActivity extends NAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    EditText tvPw1 = null;
    EditText tvPw2 = null;
    private final int CALLTYPE_SAVE = 1;
    Common common = null;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw_change);
        setTvTitle("비밀번호 변경");
        common = new Common(this);
        tvPw1 = (EditText) findViewById(R.id.searchpwchange_tvPw1);
        tvPw1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvPw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tvPw2 = (EditText) findViewById(R.id.searchpwchange_tvPw2);
        tvPw2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvPw2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        tvPw1.setPrivateImeOptions("defaultInputmode=english;");
        tvPw2.setPrivateImeOptions("defaultInputmode=english;");
    }
    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        switch (view.getId()){
            case R.id.searchpwchange_btnSearchPw :
                String pw1 = tvPw1.getText().toString();
                String pw2 = tvPw2.getText().toString();

                boolean regPwCheck1 = Common.validatePassword(pw1);
                int regPwCheck2 = pw1.length();
                Log.i("wtKim","regPwCheck1==>"+regPwCheck1);
                if(regPwCheck1==false || regPwCheck2<6){
                    dialogType = 9;
                    Common.createDialog(this, "비밀번호 변경", null, "비밀번호 형식에 맞지 않습니다.", getString(R.string.btn_ok),null, false, false);
                }else if(!pw1.equals(pw2)){
                    dialogType = 1;
                    Common.createDialog(this, "비밀번호 변경", null, "비밀번호가 일치하지 않습니다.", getString(R.string.btn_ok),null, false, false);
                }else {
                    String id = getIntent().getStringExtra("ID").toString();
                    String mobile = getIntent().getStringExtra("MOBILE_TEL").toString();
                    String authcode = getIntent().getStringExtra("AUTHCODE").toString();
                    String pw = tvPw1.getText().toString();
                    Object[][] params = {
                            {"ID", id}
                            ,{"MOBILE", mobile}
                            ,{"AUTHCODE", authcode}
                            ,{"PW", pw}
                    };
                    //인증번호 발송
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_searchPwChange), params);
                }
                break;
        }

    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2){
            intent = new Intent(SearchPwChangeActivity.this,LoginActivity.class);
             startActivity(intent);
           };

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
                    dialogType = 2;
                    Common.createDialog(this, "비밀번호 변경", null,"비밀번호가 변경되었습니다.", getString(R.string.btn_ok),null ,false, false);
                }
            } else {
                dialogType = 3;
                Common.createDialog(this, "비밀번호 변경",null, err, getString(R.string.btn_ok),null, false, false);

            }
        } catch (Exception e) {
            dialogType = 4;
            Common.createDialog(this, "비밀번호 변경",null, e.toString(), getString(R.string.btn_ok),null, false, false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
