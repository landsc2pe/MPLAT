package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class PwdActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    String strPwd = "";
    String pre_activity = "";
    Boolean pwdCheckOK = false;
    private String authdate = "";
    private String regist_join_channel_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        setTvTitle("비밀번호 확인");
        common = new Common(this);

        //이전 activity 저장
        pre_activity = getIntent().getStringExtra("PRE_ACTIVITY").toString();
        //keyup 이벤트 추가
        ((EditText)findViewById(R.id.pwd_pwd)).addTextChangedListener(textWatcherEtPwd);
        //다음버튼 선택시
        ((Button)findViewById(R.id.pwd_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!pwdCheckOK){
                    Common.createDialog(PwdActivity.this, getString(R.string.app_name).toString(),null, "비밀번호를 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else{
                    switch (pre_activity){
                        //이전페이지가 회원정보 변경인경우
                        case "1":
                            Object[][] params1 = {
                               {"PW",strPwd}
                            };
                            common.loadData(CALLTYPE_PWDCHECK, getString(R.string.url_pwd), params1);
                            break;
                        //이전페이지가 비밀번호 변경인경우
                        case "2":
                            Object[][] params2 = {
                               {"PW",strPwd}
                            };
                            common.loadData(CALLTYPE_PWDCHECK, getString(R.string.url_pwd), params2);
                            break;
                    }
                }
            }
        });
    }

    //회원정보변경 > 비밀번호 확인
    private int textWatcherType=0;
    TextWatcher textWatcherEtPwd = new TextWatcher() {
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
            strPwd = editable.toString();
            if(strPwd.length()>0){
                pwdCheckOK = true;
            }else{
                pwdCheckOK = false;
            }
            //모두 입력,선택하였을 경우, 등록버튼 활성화
            if(pwdCheckOK){
                ((Button)findViewById(R.id.pwd_btnNext)).setBackgroundResource(R.color.primary);
            }else{
                ((Button)findViewById(R.id.pwd_btnNext)).setBackgroundResource(R.color.primaryDisabled);
            }
        }
    };
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
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_PWDCHECK) pwdCheckHandler(str);
        else if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                /*
                REGIST_JOIN_CHANNEL_TYPE	1	엠플렛
                REGIST_JOIN_CHANNEL_TYPE	2	네이버
                REGIST_JOIN_CHANNEL_TYPE	3	페이스북
                REGIST_JOIN_CHANNEL_TYPE	4	카카오
                REGIST_JOIN_CHANNEL_TYPE	5	구글
                */
                authdate = json.getString("AUTH_DATE");
                regist_join_channel_type = json.getString("REGIST_JOIN_CHANNEL_TYPE");
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void pwdCheckHandler(String str){
        Log.i("wtkim","pwdCheckHandler()호출!");
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if(pre_activity.equals("1")){
                    if(authdate.equals("") && (regist_join_channel_type.equals("1")||regist_join_channel_type.equals("2")||regist_join_channel_type.equals("4"))){//미인증회원(infoChangeEmail)
                        Log.i("wtkim","미인증회원(InfoChangeEmailActivity)시작!");
                        intent = new Intent(PwdActivity.this,InfoChangeEmailActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(authdate.equals("") && (regist_join_channel_type.equals("3")||regist_join_channel_type.equals("5"))){//미인증회원정보(infoChange)
                        Log.i("wtkim","미인증회원정보(InfoChangeActivity)시작!");
                        intent = new Intent(PwdActivity.this,InfoChangeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(!authdate.equals("") && (regist_join_channel_type.equals("1")||regist_join_channel_type.equals("2")||regist_join_channel_type.equals("4"))) {//인증회원(infoChangeAuth)
                        Log.i("wtkim","인증회원(InfoChangeAuthActivity)시작!");
                        intent = new Intent(PwdActivity.this,InfoChangeAuthActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(!authdate.equals("") && (regist_join_channel_type.equals("3")||regist_join_channel_type.equals("5"))) {//인증회원(infoChangeAuth)
                        Log.i("wtkim","인증회원(InfoChangeAuthEmailActivity)시작!");
                        intent = new Intent(PwdActivity.this,InfoChangeAuthEmailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    //인증회원정보(infoChangeAuthEmail)

                    //intent.putExtra("PW",strPwd);

                }else if(pre_activity.equals("2")){
                    intent = new Intent(PwdActivity.this,PwdChangeActivity.class);
                    intent.putExtra("PW",strPwd);
                    startActivity(intent);
                    finish();
                }

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }


}
