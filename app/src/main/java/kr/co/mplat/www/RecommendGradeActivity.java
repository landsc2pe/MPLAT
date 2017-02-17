package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

public class RecommendGradeActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    Common common = null;
    Intent intent = null;
    private AlertDialog dialog = null;
    private int dialogType = 0;
    View ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_grade);

        common = new Common(this);
        //추천하기 클릭 이벤트
        ((LinearLayout)findViewById(R.id.recommendGrade_llRecommend)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendGradeActivity.this,RecommendActivity.class);
                startActivity(intent);
            }
        });
        //추천/가입내역 클릭 이벤트
        ((LinearLayout)findViewById(R.id.recommendGrade_llRecommendHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendGradeActivity.this,RecommendHistoryActivity.class);
                startActivity(intent);
            }
        });

        //추천 등급별 혜택안내 클릭이벤트
        ((Button)findViewById(R.id.recommendGrade_btnInfo)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendGradeActivity.this,RecommendGradeInfoActivity.class);
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
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_recommendGrade), null);
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
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
                String email =  json.getString("EMAIL");
                String grade =  json.getString("GRADE");
                String today =  json.getString("TODAY");
                String extraPoint =  json.getString("EXTRA_POINT");
                String recommendCnt =  json.getString("RECOMMEND_CNT");
                String gradePeriod =  json.getString("GRADE_PERIOD");
                String nextGradeDate =  json.getString("NEXT_GRADE_DATE");

                String gradeLabel =  json.getString("GRADE_LABEL");

            //문구변경
            ((TextView)findViewById(R.id.recommendGrade_tvEmail)).setText(email);
            ((TextView)findViewById(R.id.recommendGrade_tvGrade)).setText(Html.fromHtml("<font color='#7161C4'>"+gradeLabel+"</font>"));
            ((TextView)findViewById(R.id.recommendGrade_tvDesc)).setText(Html.fromHtml(today+" 기준 추천 등급은 <font color='#7161C4'>"+grade+"등급</font> 입니다."));
            //추가 포인트
            ((TextView)findViewById(R.id.recommendGrade_tvInfo12)).setText(extraPoint+" P");
            //추가인원
            ((TextView)findViewById(R.id.recommendGrade_tvInfo22)).setText(recommendCnt+" 명");
            //등급 적용기간
            ((TextView)findViewById(R.id.recommendGrade_tvInfo32)).setText(gradePeriod);
            //다음등급 적용일
            ((TextView)findViewById(R.id.recommendGrade_tvInfo42)).setText(nextGradeDate);



            if (err.equals("")) {

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
    }*/
}
