package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class JoinActivity extends NAppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    TextView etId,etIdCheck, etPw1,etPw1Check,etPw2,etPw2Check,etEmail,etEmailCheck = null;
    private int callType_validate = 0;
    //private int callType_save = 1;

    final int CHANNEL_TYPE_MPLAT = 1;
    final int CHANNEL_TYPE_NAVER = 2;
    final int CHANNEL_TYPE_FACEBOOK = 3;
    final int CHANNEL_TYPE_KAKAO = 4;
    final int CHANNEL_TYPE_GOOGLE = 5;
    Common common = null;
    String mobile = "";
    String joinQueueSeq = "";
    String id = "";
    String pw1 = "";
    String pw2 = "";
    String email = "";
    String sns_id,sns_type,sns_email = "";
    String tvPw1 = "";

    Boolean idCheckOK = false;
    Boolean pw1CheckOK = false;
    Boolean pw2CheckOK = false;
    Boolean emailCheckOK = false;

    //색
    String green = "#76B87B";
    String red = "#D76151";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        setTvTitle("회원정보 입력");
        common = new Common(this);
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-joinActivity","UID==>"+UID);
        Log.i("comm-joinActivity","KEY==>"+KEY);

        mobile = getIntent().getStringExtra("MOBILE_TEL").toString();
        joinQueueSeq = getIntent().getStringExtra("JOIN_QUEUE_SEQ").toString();

        TextView tvId = (TextView)findViewById(R.id.join_tvId);
        TextView tvPw1 = (TextView)findViewById(R.id.join_tvPw1);

        etId = (TextView)findViewById(R.id.join_etId);
        etIdCheck = (TextView)findViewById(R.id.join_etIdCheck);
        etPw1 = (TextView)findViewById(R.id.join_etPw1);
        etPw1Check = (TextView)findViewById(R.id.join_etPw1Check);
        etPw2 = (TextView)findViewById(R.id.join_etPw2);
        etPw2Check = (TextView)findViewById(R.id.join_etPw2Check);
        etEmail = (TextView)findViewById(R.id.join_etEmail);
        etEmailCheck = (TextView)findViewById(R.id.join_etEmailCheck);

        etIdCheck.setText("");
        etPw1Check.setText("");
        etPw2Check.setText("");
        etEmailCheck.setText("");

        //keyup 이벤트 추가
        etId.addTextChangedListener(textWatcherEtId);
        etPw1.addTextChangedListener(textWatcherEtPw1);
        etPw2.addTextChangedListener(textWatcherEtPw2);
        etEmail.addTextChangedListener(textWatcherEtEmail);



        //sns 회원가입시
        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();
        Log.i("wtKim","sns_email==>>"+sns_email);
        if(sns_email.equals("") || sns_email.equals("null")){
            Log.i("wtkim","이메일 disabled!");
            etEmail.setText("");
            etEmail.setClickable(true);
            etEmail.setEnabled(true);
            etEmail.setFocusable(true);
            etEmail.setFocusableInTouchMode(true);
        }else{
            etEmail.setText(sns_email);
            etEmail.setClickable(false);
            etEmail.setEnabled(false);
            etEmail.setFocusable(false);
            etEmail.setFocusableInTouchMode(false);
        }


        Log.i("wtKim","sns_type==>"+sns_type);

        if(!sns_type.equals(String.valueOf(CHANNEL_TYPE_MPLAT))){
            etIdCheck.setVisibility(View.GONE);
            etPw1Check.setVisibility(View.GONE);
            etPw2Check.setVisibility(View.GONE);
            tvId.setVisibility(View.GONE);
            etId.setVisibility(View.GONE);
            tvPw1.setVisibility(View.GONE);
            etPw1.setVisibility(View.GONE);
            etPw2.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
    private int textWatcherType=0;
    //아이디 체크
    TextWatcher textWatcherEtId = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("onTextChanged",charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("beforeTextChanged",charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 1;
            etIdCheck.setText("");
            id = editable.toString();

            //id 정규식체크후, 맞을경우 db에서 한번더 체크
            boolean regIdCheck1 = Pattern.matches("[a-z]{6,20}", id);
            boolean regIdCheck2 = Pattern.matches("^(?=.*[a-z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,20}$", id);

            if(regIdCheck1||regIdCheck2){
                idCheckOK = true;
                Object[][] params = {
                        {"ID", id}
                };

                common.loadData(callType_validate, getString(R.string.url_idDupCheck), params);
            }else{
                idCheckOK = false;
                etIdCheck.setTextColor(Color.parseColor(red));
                etIdCheck.setText("아이디는 6~20 영소문자 또는 숫자 조합으로 입력해주세요.");
            }


        }
    };
    //비밀번호 체크1
    TextWatcher textWatcherEtPw1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("onTextChanged",charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("beforeTextChanged",charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            textWatcherType = 2;
            String pw1 = editable.toString();
            String pw2 = etPw2.getText().toString();

            if(!pw1.equals(pw2)) {
                pw2CheckOK = false;
                etPw2Check.setTextColor(Color.parseColor(red));
                etPw2Check.setText("비밀번호를 재확인하여 주세요.");
            }
            //pw 정규식체크후, 맞을경우 db에서 한번더 체크

            boolean regPwCheck1 = Common.validatePassword(pw1);
            int regPwCheck2 = pw1.length();

            if(regPwCheck1 && regPwCheck2>=6){
                pw1CheckOK = true;
                etPw1Check.setTextColor(Color.parseColor(green));
                etPw1Check.setText("비밀번호 형식이 올바릅니다.");
//                Object[][] params = {
//                        {"MOBILE", mobile}
//                        ,{"JOIN_QUEUE_SEQ", joinQueueSeq}
//                        ,{"ID", id}
//                        ,{"PW", pw1}
//                        ,{"EMAIL",email}
//                };
//                common.loadData(callType_validate, getString(R.string.url_join), params);
            }else{
                pw1CheckOK = false;
                etPw1Check.setTextColor(Color.parseColor(red));
                etPw1Check.setText("비밀번호는 6~16자 영문, 숫자, 특수문자 조합으로 입력해주세요.");
            }
        }
    };
    //비밀번호 체크2
    TextWatcher textWatcherEtPw2 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("onTextChanged",charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("beforeTextChanged",charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            textWatcherType = 3;

            if((etPw1.getText().toString()).equals(etPw2.getText().toString()) && !(etPw1.getText().toString().equals(""))){
                pw2CheckOK = true;
                etPw2Check.setTextColor(Color.parseColor(green));
                etPw2Check.setText("비밀번호가 일치합니다.");
            }else{
                pw2CheckOK = false;
                etPw2Check.setTextColor(Color.parseColor(red));
                etPw2Check.setText("비밀번호를 재확인하여 주세요.");
            }


        }
    };
    //이메일 주소 체크
    TextWatcher textWatcherEtEmail = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("onTextChanged",charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("beforeTextChanged",charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 4;
            String email = editable.toString();

            boolean regEmailCheck = Common.validateEmail(email);
            if(regEmailCheck){
                emailCheckOK = true;
                Object[][] params = {
                        {"EMAIL",email}
                };
                common.loadData(callType_validate, getString(R.string.url_emailDupCheck), params);
            }else{
                emailCheckOK = false;
                etEmailCheck.setTextColor(Color.parseColor(red));
                etEmailCheck.setText("이메일을 올바르게 입력했는지 확인하여 주세요.");
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //회원정보 입력 -> 회원가입 수신동의 페이지로 이동
            case R.id.join_btnNext:
                if(sns_type.equals(String.valueOf(CHANNEL_TYPE_MPLAT))){
                    Log.i("wtkim","sns_type 없음!");
                    if((etId.getText().toString()).equals("")){
                        etIdCheck.setTextColor(Color.parseColor(red));
                        etIdCheck.setText("아이디를 입력하여 주세요.");
                    }
                    if((etPw1.getText().toString()).equals("")) {
                        etPw1Check.setTextColor(Color.parseColor(red));
                        etPw1Check.setText("비밀번호를 입력하여 주세요.");
                    }
                    if((etPw2.getText().toString()).equals("")) {
                        etPw2Check.setTextColor(Color.parseColor(red));
                        etPw2Check.setText("비밀번호를 재확인하여 주세요.");
                    }
                    if((etEmail.getText().toString()).equals("")) {
                        etEmailCheck.setTextColor(Color.parseColor(red));
                        etEmailCheck.setText("이메일을 입력하여 주세요.");
                    }

                    if((idCheckOK && pw1CheckOK && pw2CheckOK && emailCheckOK ) && (etPw1.getText().toString().equals(etPw2.getText().toString()))){
                        Object[][] params = {
                                {"MOBILE",mobile}
                                ,{"JOIN_QUEUE_SEQ",joinQueueSeq}
                                ,{"ID",etId.getText().toString()}
                                ,{"PW",etPw1.getText().toString()}
                                ,{"EMAIL",etEmail.getText().toString()}
                        };
                        common.loadData(CHANNEL_TYPE_MPLAT, getString(R.string.url_join), params);
                    }
                    break;
                }else if(sns_type.equals(String.valueOf(CHANNEL_TYPE_KAKAO))){
                    Log.i("wtkim","kakao 로그인!");
                    if((etEmail.getText().toString()).equals("")) {
                        etEmailCheck.setTextColor(Color.parseColor(red));
                        etEmailCheck.setText("이메일을 입력하여 주세요.");
                    }

                    if(emailCheckOK){
                        Object[][] params = {
                                {"MOBILE",mobile}
                                ,{"JOIN_QUEUE_SEQ",joinQueueSeq}
                                ,{"REGIST_JOIN_CHANNEL_TYPE",sns_type}
                                ,{"SNS_ID",sns_id}
                                ,{"EMAIL",etEmail.getText().toString()}
                        };

                        common.loadData(CHANNEL_TYPE_KAKAO, getString(R.string.url_snsJoin), params);
                    }
                    break;
                }else if(sns_type.equals(String.valueOf(CHANNEL_TYPE_NAVER))){
                    Log.i("wtkim","naver 로그인!");
                    if((etEmail.getText().toString()).equals("")) {
                        etEmailCheck.setTextColor(Color.parseColor(red));
                        etEmailCheck.setText("이메일을 입력하여 주세요.");
                    }

                    if(emailCheckOK){
                        Object[][] params = {
                                {"MOBILE",mobile}
                                ,{"JOIN_QUEUE_SEQ",joinQueueSeq}
                                ,{"REGIST_JOIN_CHANNEL_TYPE",sns_type}
                                ,{"SNS_ID",sns_id}
                                ,{"EMAIL",etEmail.getText().toString()}
                        };

                        common.loadData(CHANNEL_TYPE_NAVER, getString(R.string.url_snsJoin), params);
                    }
                    break;
                }else if(sns_type.equals(String.valueOf(CHANNEL_TYPE_GOOGLE))){
                    Log.i("wtkim","GOOGLE 로그인!");
                    if((etEmail.getText().toString()).equals("")) {
                        etEmailCheck.setTextColor(Color.parseColor(red));
                        etEmailCheck.setText("이메일을 입력하여 주세요.");
                    }

                    if(emailCheckOK){
                        Object[][] params = {
                                {"MOBILE",mobile}
                                ,{"JOIN_QUEUE_SEQ",joinQueueSeq}
                                ,{"REGIST_JOIN_CHANNEL_TYPE",sns_type}
                                ,{"SNS_ID",sns_id}
                                ,{"EMAIL",etEmail.getText().toString()}
                        };

                        common.loadData(CHANNEL_TYPE_NAVER, getString(R.string.url_snsJoin), params);
                    }
                    break;
                }else if(sns_type.equals(String.valueOf(CHANNEL_TYPE_FACEBOOK))){
                    Log.i("wtkim","FACEBOOK 로그인!");
                    if((etEmail.getText().toString()).equals("")) {
                        etEmailCheck.setTextColor(Color.parseColor(red));
                        etEmailCheck.setText("이메일을 입력하여 주세요.");
                    }

                    if(emailCheckOK){
                        Object[][] params = {
                                {"MOBILE",mobile}
                                ,{"JOIN_QUEUE_SEQ",joinQueueSeq}
                                ,{"REGIST_JOIN_CHANNEL_TYPE",sns_type}
                                ,{"SNS_ID",sns_id}
                                ,{"EMAIL",etEmail.getText().toString()}
                        };

                        common.loadData(CHANNEL_TYPE_FACEBOOK, getString(R.string.url_snsJoin), params);
                    }
                    break;
                }

                Log.i("wtkim","sns_type==>"+sns_type);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == callType_validate) checkHandler(str);
        else if (calltype == CHANNEL_TYPE_MPLAT) saveHandler(str);
        else if (calltype == CHANNEL_TYPE_KAKAO) saveHandler(str);
        else if (calltype == CHANNEL_TYPE_NAVER) saveHandler(str);
        else if (calltype == CHANNEL_TYPE_GOOGLE) saveHandler(str);
        else if (calltype == CHANNEL_TYPE_FACEBOOK) saveHandler(str);
    }
    //저장 처리
    public void checkHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    if(textWatcherType==1){
                        etIdCheck.setTextColor(Color.parseColor(green));
                        etIdCheck.setText("사용가능한 아이디 입니다");
                    }else if(textWatcherType==2){
                        etPw1Check.setTextColor(Color.parseColor(green));
                        etPw1Check.setText("사용가능한 비밀번호 입니다");
                    }else if(textWatcherType==4) {
                        etEmailCheck.setTextColor(Color.parseColor(green));
                        etEmailCheck.setText("사용가능한 이메일 입니다");
                    }
                }
            } else {
                if(textWatcherType==1){
                    idCheckOK = false;
                    etIdCheck.setTextColor(Color.parseColor(red));
                    etIdCheck.setText(err);
                }else if(textWatcherType==2){
                    pw1CheckOK = false;
                    etPw1Check.setTextColor(Color.parseColor(red));
                    etPw1Check.setText(err);
                }else if(textWatcherType==4) {
                    emailCheckOK = false;
                    etEmailCheck.setTextColor(Color.parseColor(red));
                    etEmailCheck.setText(err);
                }
                //Common.createDialog(this, getString(R.string.txt_authCodeOK).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.d("wtKim", e.toString());
        }
    }

    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    intent = new Intent(JoinActivity.this,AgreeActivity.class);
                    intent.putExtra("MOBILE_TEL",mobile);
                    intent.putExtra("JOIN_QUEUE_SEQ",joinQueueSeq);
                    intent.putExtra("SNS_ID",sns_id);
                    intent.putExtra("SNS_TYPE",sns_type);
                    intent.putExtra("SNS_EMAIL",sns_email);
                    startActivity(intent);
                }
            } else {
                Common.createDialog(this, getString(R.string.dial_title2).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.d("wtKim", e.toString());
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }*/

}
