package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONObject;

public class SurveyAuthActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    Common common = null;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_auth);
        setTvTitle("본인인증");
        common = new Common(this);

        //문구변경
        ((TextView)findViewById(R.id.surveyAuth_info2)).setText(Html.fromHtml("<font color='#7161C4'>휴대폰 본인인증 후에는 인증된 정보로 회원정보가 변경</font>됩니다."));
        //인증확인체크시 이벤트
        final CheckBox cbAgree_1 = (CheckBox)findViewById(R.id.survey_cbAgree_1);
        cbAgree_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(cbAgree_1.isChecked()){
                    findViewById(R.id.survey_btnNext).setBackgroundResource(R.color.primary);
                    cbAgree_1.setTextColor(getResources().getColor(R.color.primary));
                }else{
                    cbAgree_1.setTextColor(getResources().getColor(R.color.primaryFont));
                    findViewById(R.id.survey_btnNext).setBackgroundResource(R.color.primaryDisabled);
                }
            }
        });
        //다음 버튼 클릭 이벤트
        findViewById(R.id.survey_btnNext).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(cbAgree_1.isChecked()){
                    finish();
                    intent = new Intent(SurveyAuthActivity.this,AuthWvActivity.class);
                    intent.putExtra("URL",getString(R.string.url_authwv));
                    intent.putExtra("PRE_ACTIVITY","SurveyAuthActivity");
                    startActivity(intent);
                }else{
                    Common.createDialog(SurveyAuthActivity.this, getString(R.string.app_name).toString(),null, "회원정보변경 안내확인에 체크해 주세요.", getString(R.string.btn_ok),null, false, false);
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
    }
    @Override
    public void loaddataHandler(int calltype, String str) {
    }

    @Override
    public void dialogHandler(String result) {

    }
}
