package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class InfoChangeEmailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_UPDATE = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    EditText etEmail = null;
    Button btnNext = null;
    String preEmail = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_change_email);
        setTvTitle("회원정보 변경");

        common = new Common(this);
        //문구변경
        ((TextView)findViewById(R.id.infoChangeEmail_tvInfo1)).setText(Html.fromHtml("<b><font color=\"#D57A76\">이름, 생년월일, 성별은 수정이 불가능하며</font>,<br/>본인인증을 하지 않으신 분은 정보가 나타나지 않습니다.</b>"));
        ((TextView)findViewById(R.id.infoChangeEmail_tvAuthYN)).setText(Html.fromHtml("<b><font color=\"#D57A76\">미인증회원</font></b>"));


        etEmail = (EditText)findViewById(R.id.infoChangeEmail_etEmail);
        btnNext = (Button)findViewById(R.id.infoChangeEmail_btnNext);
        //시작시 변경버튼 안보이도록 함
        btnNext.setVisibility(View.GONE);
        //변경버튼 선택시 이벤트등록
        ((Button)findViewById(R.id.infoChangeEmail_btnChange)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","변경버튼선택!");
                etEmail.setEnabled(true);
                etEmail.setCursorVisible(true);
                etEmail.setSelection(etEmail.length());
                etEmail.setEnabled(true);
                btnNext.setBackgroundResource(R.color.primary);
                btnNext.setEnabled(true);
                ((Button)findViewById(R.id.infoChangeEmail_btnChange)).setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);

            }
        });

        //변경완료 버튼 선택시 이벤트 등록
        ((Button)findViewById(R.id.infoChangeEmail_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","변경완료 버튼선택!");
                boolean regEmailCheck = Common.validateEmail(etEmail.getText().toString());
                Log.i("wtkim","regEmailCheck==>"+regEmailCheck);
                if(regEmailCheck){
                    Object[][] params = {
                        {"NEW_EMAIL", etEmail.getText().toString()}
                    };
                    common.loadData(CALLTYPE_UPDATE, getString(R.string.url_emailChange), params);
                }else{
                    Common.createDialog(InfoChangeEmailActivity.this, getString(R.string.app_name).toString(),null, "이메일 형식이 올바르지 않습니다.", getString(R.string.btn_ok),null, false, false);
                }
            }
        });
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
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2) {
            intent = new Intent(InfoChangeEmailActivity.this,MypageActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_UPDATE) updateHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            String email = "";
            String point = "";
            String grade = "";
            String grade_label = "";
            String recommend_grade = "";
            String recommend_grade_label = "";
            String googleplay_id = "";
            String auth_date = "";
            String email_changable = "";
            if (err.equals("")) {
                String result = json.getString("RESULT");
                email  = json.getString("EMAIL");
                point = json.getString("POINT");
                grade = json.getString("GRADE");
                grade_label = json.getString("GRADE_LABEL");
                recommend_grade = json.getString("RECOMMEND_GRADE");
                recommend_grade_label = json.getString("RECOMMEND_GRADE_LABEL");
                googleplay_id = json.getString("GOOGLEPLAY_ID");
                auth_date = json.getString("AUTH_DATE");
                email_changable = json.getString("EMAIL_CHANGABLE");

                ((EditText)findViewById(R.id.infoChangeEmail_etEmail)).setText(email);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void updateHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                dialogType = 2;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "이메일 주소 변경이 완료되었습니다.", getString(R.string.btn_ok),null, false, false);

                //String result = json.getString("RESULT");
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
