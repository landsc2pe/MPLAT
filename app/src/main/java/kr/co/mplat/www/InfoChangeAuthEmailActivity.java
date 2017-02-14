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

public class InfoChangeAuthEmailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
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
        setContentView(R.layout.activity_info_change_auth_email);
        setTvTitle("회원정보 변경");
        common = new Common(this);
        //문구변경
        ((TextView)findViewById(R.id.infoChangeAuthEmail_tvInfo1)).setText(Html.fromHtml("<b><font color=\"#D57A76\">이름, 생년월일, 성별은 수정이 불가능하며</font>,<br/>Facebook ID 또는 Google ID로 가입한 회원은<br/><font color='#D57A76'>이메일주소 변경이 불가능</font> 합니다.</b>"));

        etEmail = (EditText)findViewById(R.id.infoChangeAuthEmail_etEmail);
        ((Button)findViewById(R.id.infoChangeAuthEmail_btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(InfoChangeAuthEmailActivity.this,MypageActivity.class);
                startActivity(intent);
                finish();
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
            intent = new Intent(InfoChangeAuthEmailActivity.this,MypageActivity.class);
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
            String name = "";
            String gender = "";
            String birth_date = "";
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
                name = json.getString("NAME");
                gender = json.getString("GENDER");
                birth_date = json.getString("BIRTH_DATE");

                ((EditText)findViewById(R.id.infoChangeAuthEmail_etEmail)).setText(email);
                ((TextView)findViewById(R.id.infoChangeAuthEmail_tvAuthYN)).setText(Html.fromHtml("<b><font color=\"#7161C4\">인증회원(인증일 "+auth_date+")</font></b>"));
                ((TextView)findViewById(R.id.infoChangeAuthEmail_tvName)).setText(name);
                ((TextView)findViewById(R.id.infoChangeAuthEmail_tvSex)).setText(gender);
                ((TextView)findViewById(R.id.infoChangeAuthEmail_tvBirth)).setText(birth_date);
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
