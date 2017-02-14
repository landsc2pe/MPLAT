package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GradeInfoActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_info);
        setTvTitle("회원등급별 혜택안내");
        common = new Common(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_gradeInfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        /*if(dialogType == 9 && result.equals("ok")){
            Common.setPreference(getApplicationContext(), "UID", "");
            Common.setPreference(getApplicationContext(), "KEY", "");

            intent = new Intent(MypageActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }*/
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
            if (err.equals("")) {
                JSONArray ary_grades = json.getJSONArray("GRADES");
                int i;
                for(i=0;i<ary_grades.length();i++){
                    String dummySeq = ((JSONObject)ary_grades.get(i)).getString("DUMMY_SEQ");
                    String userGradeCode = ((JSONObject)ary_grades.get(i)).getString("USER_GRADE_CODE");
                    String label = ((JSONObject)ary_grades.get(i)).getString("LABEL");
                    String range = ((JSONObject)ary_grades.get(i)).getString("RANGE");
                    String extraRate = ((JSONObject)ary_grades.get(i)).getString("EXTRA_RATE");

                    if(i==0){//vip
                        ((ImageView)findViewById(R.id.gradeInfo_ivVip1)).setImageResource(R.drawable.grade_vip);
                        ((TextView)findViewById(R.id.gradeInfo_tvVip2)).setText(label);
                        ((TextView)findViewById(R.id.gradeInfo_tvVip3)).setText(range);
                        ((TextView)findViewById(R.id.gradeInfo_tvVip4)).setText(Html.fromHtml("<font color='#7161C4'>"+extraRate+"</font>"));

                    }else if(i==1){//gold
                        ((ImageView)findViewById(R.id.gradeInfo_ivGold1)).setImageResource(R.drawable.grade_gold);
                        ((TextView)findViewById(R.id.gradeInfo_tvGold2)).setText(label);
                        ((TextView)findViewById(R.id.gradeInfo_tvGold3)).setText(range);
                        ((TextView)findViewById(R.id.gradeInfo_tvGold4)).setText(Html.fromHtml("<font color='#7161C4'>"+extraRate+"</font>"));
                    }else if(i==2){//silver
                        ((ImageView)findViewById(R.id.gradeInfo_ivSilver1)).setImageResource(R.drawable.grade_silver);
                        ((TextView)findViewById(R.id.gradeInfo_tvSilver2)).setText(label);
                        ((TextView)findViewById(R.id.gradeInfo_tvSilver3)).setText(range);
                        ((TextView)findViewById(R.id.gradeInfo_tvSilver4)).setText(Html.fromHtml("<font color='#7161C4'>"+extraRate+"</font>"));
                    }else if(i==3) {//bronze
                        ((ImageView) findViewById(R.id.gradeInfo_ivBronze1)).setImageResource(R.drawable.grade_bronze);
                        ((TextView) findViewById(R.id.gradeInfo_tvBronze2)).setText(label);
                        ((TextView) findViewById(R.id.gradeInfo_tvBronze3)).setText(range);
                        ((TextView) findViewById(R.id.gradeInfo_tvBronze4)).setText(Html.fromHtml("<font color='#7161C4'>" + extraRate + "</font>"));
                    }
                }

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
