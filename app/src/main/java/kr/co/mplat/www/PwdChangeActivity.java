package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class PwdChangeActivity extends NAppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,I_loaddata,I_startFinish,I_dialogdata{
    EditText etPw1 = null;
    EditText etPw2 = null;
    Button btnUpdatePw = null;
    private final int CALLTYPE_PWDUPDATE = 1;
    Common common = null;
    Intent intent = null;
    Boolean pw1CheckOK = false;
    Boolean pw2CheckOK = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_change);

        setTvTitle("비밀번호 재설정");
        common = new Common(this);
        etPw1 = (EditText) findViewById(R.id.pwdChange_etPw1);
        etPw1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPw2 = (EditText) findViewById(R.id.pwdChange_etPw2);
        etPw2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPw2.setTransformationMethod(PasswordTransformationMethod.getInstance());

        etPw1.setPrivateImeOptions("defaultInputmode=english;");
        etPw2.setPrivateImeOptions("defaultInputmode=english;");

        btnUpdatePw = (Button)findViewById(R.id.pwdChange_btnUpdatePw);

        //keyup 이벤트 추가
        etPw1.addTextChangedListener(textWatcherEtPw1);
        etPw2.addTextChangedListener(textWatcherEtPw2);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
    private int textWatcherType=0;
    //비밀번호 체크1
    TextWatcher textWatcherEtPw1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 1;
            String pw1 = editable.toString();
            String pw2 = etPw2.getText().toString();

            if(pw1.equals("") || pw2.equals("")){
                btnUpdatePw.setBackgroundResource(R.color.primaryDisabled);
            }else{
                btnUpdatePw.setBackgroundResource(R.color.primary);
            }

            if(!pw1.equals(pw2)) {
                pw2CheckOK = false;
            }
            //pw 정규식체크후, 맞을경우 db에서 한번더 체크

            boolean regPwCheck1 = Common.validatePassword(pw1);
            int regPwCheck2 = pw1.length();

            if(regPwCheck1 && regPwCheck2>=6){
                pw1CheckOK = true;
            }else{
                pw1CheckOK = false;
            }
        }
    };
    //비밀번호 체크2
    TextWatcher textWatcherEtPw2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 2;
            String pw1 = etPw1.getText().toString();
            String pw2 = editable.toString();

            if(pw1.equals("") || pw2.equals("")){
                btnUpdatePw.setBackgroundResource(R.color.primaryDisabled);
            }else{
                btnUpdatePw.setBackgroundResource(R.color.primary);
            }


            if((etPw1.getText().toString()).equals(etPw2.getText().toString()) && !(etPw1.getText().toString().equals(""))){
                pw2CheckOK = true;
            }else{
                pw2CheckOK = false;
            }
        }
    };

    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        switch (view.getId()){
            case R.id.pwdChange_btnUpdatePw :
                Log.i("wtkim","버튼클릭!");
                String pw1 = etPw1.getText().toString();
                String pw2 = etPw2.getText().toString();

                boolean regPwCheck1 = Common.validatePassword(pw1);
                int regPwCheck2 = pw1.length();
                if(regPwCheck1==false || regPwCheck2<6){
                    Log.i("wtkim","!1111111111");
                    dialogType = 9;
                    Common.createDialog(this, "비밀번호 변경", null, "비밀번호 형식에 맞지 않습니다.", getString(R.string.btn_ok),null, false, false);
                }else if(!pw1.equals(pw2)){
                    Log.i("wtkim","22222222222");
                    dialogType = 1;
                    Common.createDialog(this, "비밀번호 변경", null, "비밀번호가 일치하지 않습니다.", getString(R.string.btn_ok),null, false, false);
                }else {
                    Log.i("wtkim","3333333333333");
                    String pre_pw = getIntent().getStringExtra("PW").toString();
                    String new_pw = etPw1.getText().toString();

                    Object[][] params = {
                             {"PW", pre_pw}
                            ,{"NEW_PW", new_pw}
                    };
                    Log.i("wtkim","pre_pw==>"+pre_pw);
                    Log.i("wtkim","NEW_PW==>"+new_pw);
                    //인증번호 발송
                    common.loadData(CALLTYPE_PWDUPDATE, getString(R.string.url_pwdChange), params);
                }
                break;
        }
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2){
            finish();
            intent = new Intent(PwdChangeActivity.this,MypageActivity.class);
            startActivity(intent);
        };
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        Log.i("wtkim","loaddataHandler()호출!");
        if (calltype == CALLTYPE_PWDUPDATE) updateHandler(str);
    }

    //저장 처리
    public void updateHandler(String str) {
        Log.i("wtkim","updateHandler()호출!");
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.i("wtKim","result=>"+result);
                if (result.equals("OK")) {
                    dialogType = 2;
                    String key = json.getString("KEY");
                    Common.setPreference(getApplicationContext(), "KEY", key);
                    Common.createDialog(this, "비밀번호 변경", null,"비밀번호가 변경되었습니다.", getString(R.string.btn_ok),null ,false, false);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, "비밀번호 변경",null, err, getString(R.string.btn_ok),null, false, false);

            }
        } catch (Exception e) {
            dialogType = 9;
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
