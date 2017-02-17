package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class GradeActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    String email = "";
    String extraRate = "";
    String activePoint = "";
    String gradePeriod = "";
    String nextGradeDate = "";
    String grade = "";
    String gradeLabel = "";
    TextView tvEmail = null;
    ImageView ivGradeImage = null;
    TextView tvGradeLabel = null;
    TextView tvExtraRate = null;
    TextView tvActivePoint = null;
    TextView tvGradePeriod = null;
    TextView tvNextGradeDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        setTvTitle("회원등급");
        common = new Common(this);
        //String email = getIntent().getStringExtra("EMAIL");

        tvEmail = (TextView) findViewById(R.id.grade_tvEmail);
        ivGradeImage = (ImageView) findViewById(R.id.grade_ivGradeImage);
        tvGradeLabel = (TextView) findViewById(R.id.grade_tvGradeLabel);
        tvExtraRate = (TextView) findViewById(R.id.grade_tvExtraRate);
        tvActivePoint = (TextView) findViewById(R.id.grade_tvActivePoint);
        tvGradePeriod = (TextView) findViewById(R.id.grade_tvGradePeriod);
        tvNextGradeDate = (TextView) findViewById(R.id.grade_tvNextGradeDate);


        //활동지수 상세내역 클릭이벤트 등록
        ((Button) findViewById(R.id.grade_btnDetail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GradeActivity.this, ActivepointActivity.class);
                startActivity(intent);
            }
        });
        //회원등급별 혜택안내 클릭이벤트 등록
        ((Button) findViewById(R.id.grade_btnGradeInfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GradeActivity.this, GradeInfoActivity.class);
                startActivity(intent);
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
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_grade), null);
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String today = json.getString("TODAY");
                email = json.getString("EMAIL");
                extraRate = json.getString("EXTRA_RATE");
                activePoint = json.getString("ACTIVE_POINT");
                gradePeriod = json.getString("GRADE_PERIOD");
                nextGradeDate = json.getString("NEXT_GRADE_DATE");
                grade = json.getString("GRADE");
                gradeLabel = json.getString("GRADE_LABEL");

                //문구변경
                tvEmail.setText(email);
                switch (grade) {
                    case "0":
                        ivGradeImage.setImageResource(R.drawable.grade_bronze);
                        break;
                    case "1":
                        ivGradeImage.setImageResource(R.drawable.grade_silver);
                        break;
                    case "2":
                        ivGradeImage.setImageResource(R.drawable.grade_gold);
                        break;
                    case "3":
                        ivGradeImage.setImageResource(R.drawable.grade_vip);
                        break;
                }

                tvGradeLabel.setText(Html.fromHtml(today + " 기준 회원님의 등급은 <font color='#F07907'>" + gradeLabel + "</font> 입니다"));
                tvExtraRate.setText(Html.fromHtml("<font color='#7161C4'>" + extraRate + "</font>"));
                tvActivePoint.setText(Html.fromHtml("<font color='#7161C4'>" + activePoint + "</font>"));
                tvGradePeriod.setText(Html.fromHtml("<font color='#7161C4'>" + gradePeriod + "</font>"));
                tvNextGradeDate.setText(Html.fromHtml("<font color='#7161C4'>" + nextGradeDate + "</font>"));
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

}
