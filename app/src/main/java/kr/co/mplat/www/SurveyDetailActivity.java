package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class SurveyDetailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
    Common common = null;
    Intent intent = null;
    String campaign_code = "";
    String title = "";
    String startDate = "";
    String endDate = "";
    String point = "";
    String surveyTime = "";
    String surveyUrl = "";
    String pointRealtimeYn = "";

    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_CHECK = 2;
    private int dialogType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_detail);
        setTvTitle("설문참여 상세내용");
        common = new Common(this);
        campaign_code = getIntent().getStringExtra("CAMPAIGN_CODE");
        title = getIntent().getStringExtra("TITLE");
        startDate = getIntent().getStringExtra("START_DATE");
        endDate = getIntent().getStringExtra("END_DATE");
        point = getIntent().getStringExtra("POINT");
        surveyTime = getIntent().getStringExtra("SURVEY_TIME");
        surveyUrl = getIntent().getStringExtra("SURVEY_URL");
        pointRealtimeYn = getIntent().getStringExtra("POINT_REALTIME_YN");

        //문구변경
        TextView tvTitle = (TextView)findViewById(R.id.surveyDetail_tvTitle);
        TextView tvPoint = (TextView)findViewById(R.id.surveyDetail_tvPoint);
        TextView tvDate = (TextView)findViewById(R.id.missionDetail_tvDate);
        TextView tvPointRealtimeYn = (TextView)findViewById(R.id.missionDetail_tvPointRealtimeYn);
        TextView tvSurveyStart = (TextView)findViewById(R.id.surveyDetail_tvSurveyStart);

        String sd = startDate;
        String ed = endDate;

        String sd1 = sd.substring(0,4);
        String sd2 = sd.substring(4,6);
        String sd3 = sd.substring(6,8);
        String ed1 = ed.substring(0,4);
        String ed2 = ed.substring(4,6);
        String ed3 = ed.substring(6,8);

        tvTitle.setText(title);
        tvPoint.setText(Common.getTvComma(point)+"P");
        tvDate.setText(sd1+"."+sd2+"."+sd3+"~"+ed1+"."+ed2+"."+ed3);
        if(pointRealtimeYn.equals("Y")){
            tvPointRealtimeYn.setText("즉시적립(조사 종료 후 즉시적립");
        }else{
            tvPointRealtimeYn.setText("");
        }
        tvSurveyStart.setText("참여하기 (소요시간:"+surveyTime+"분)");

        ((TextView)findViewById(R.id.surveyDetail_tvInfo2)).setText(Html.fromHtml("<font color='#D57A76'>불성실하게 응답한 경우, 조사는 예고없이 종료되며 포인트로 적립되지 않습니다.</font>"));
        //참여하기 클릭
        tvSurveyStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(SurveyDetailActivity.this,SurveyJoinActivity.class);
                intent.putExtra("URL",surveyUrl);
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
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);

    }

    @Override
    public void dialogHandler(String result) {

    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {

            }else{
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }


}
