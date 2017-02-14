package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class InfoChangeActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_change);
        setTvTitle("회원정보 변경");
        common = new Common(this);

        //문구변경
        ((TextView)findViewById(R.id.infoChange_tvInfo1)).setText(Html.fromHtml("<b><font color=\"#D57A76\">이름, 생년월일, 성별은 수정이 불가능하며,</font><br/>Facebook ID 또는 Google ID로 가입한 회원은<br/><font color='#D57A76'>이메일주소 변경이 불가능</font> 합니다.</b>"));
        ((TextView)findViewById(R.id.infoChange_tvAuthYN)).setText(Html.fromHtml("<b><font color=\"#D57A76\">미인증회원</font></b>"));

        //뒤로 버튼 이벤트 추가
        ((Button)findViewById(R.id.infoChange_btnBack)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(InfoChangeActivity.this,MypageActivity.class);
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

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            String email = "";
            if (err.equals("")) {
                /*
                REGIST_JOIN_CHANNEL_TYPE	1	엠플렛
                REGIST_JOIN_CHANNEL_TYPE	2	네이버
                REGIST_JOIN_CHANNEL_TYPE	3	페이스북
                REGIST_JOIN_CHANNEL_TYPE	4	카카오
                REGIST_JOIN_CHANNEL_TYPE	5	구글
                */
                String result = json.getString("RESULT");
                email = json.getString("EMAIL");
                String point = json.getString("POINT");
                String grade = json.getString("GRADE");
                String grade_label = json.getString("GRADE_LABEL");
                String recommend_grade = json.getString("RECOMMEND_GRADE");
                String recommend_grade_label = json.getString("RECOMMEND_GRADE_LABEL");
                String googleplay_id = json.getString("GOOGLEPLAY_ID");
                String auth_date = json.getString("AUTH_DATE");
                String email_changable = json.getString("EMAIL_CHANGABLE");

                ((EditText)findViewById(R.id.infoChange_etEmail)).setText(email);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
