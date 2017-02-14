package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class ActivepointActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activepoint);
        setTvTitle("활동지수 상세내역");
        common = new Common(this);

        //활동지수가 부여되는 회원활동 클릭이벤트 등록
        ((Button)findViewById(R.id.activepoint_btnInfo)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ActivepointActivity.this,ActivepointInfoActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
    }*/

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_activepoint), null);
    }

    @Override
    public void dialogHandler(String result) {
        /*if(dialogType == 9 && result.equals("ok")){


            intent = new Intent(ActivepointActivity.this,ActivepointHistoryActivity.class);
            startActivity(intent);
        }*/
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                final String month1 = json.getString("MONTH1");
                final String month2 = json.getString("MONTH2");
                final String month3 = json.getString("MONTH3");
                String sum = json.getString("SUM");
                String avg = json.getString("AVG");
                String monthname_1 = json.getString("MONTHNAME_1");
                String monthname_2 = json.getString("MONTHNAME_2");
                String monthname_3 = json.getString("MONTHNAME_3");
                final String monthcode_1 = json.getString("MONTHCODE_1");
                final String monthcode_2 = json.getString("MONTHCODE_2");
                final String monthcode_3 = json.getString("MONTHCODE_3");
                String sumname = json.getString("SUMNAME");

                //최근 3개월 평균 활동 지수
                ((TextView)findViewById(R.id.activepoint_tvAvg)).setText(Html.fromHtml("<b>최근 3개월 평균 활동 지수&nbsp;&nbsp;&nbsp; <font color='#7161C4'>"+Common.getTvComma(avg)+"</font></b>"));
                //1/3개월
                ((TextView)findViewById(R.id.activepoint_tvMonthname_1)).setText(monthname_1);
                ((TextView)findViewById(R.id.activepoint_tvMonthname_2)).setText(monthname_2);
                ((TextView)findViewById(R.id.activepoint_tvMonthname_3)).setText(monthname_3);

                ((TextView)findViewById(R.id.activepoint_tvMonth_1)).setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(month1)+"</font>"));
                ((TextView)findViewById(R.id.activepoint_tvMonth_2)).setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(month2)+"</font>"));
                ((TextView)findViewById(R.id.activepoint_tvMonth_3)).setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(month3)+"</font>"));
                //합계
                ((TextView)findViewById(R.id.activepoint_tvSumname)).setText(Html.fromHtml("<b>합계("+sumname+")</b>"));
                ((TextView)findViewById(R.id.activepoint_tvSum)).setText(Html.fromHtml("<font color='#7161C4'>"+sum+"</font>"));

                //평균
                ((TextView)findViewById(R.id.activepoint_tvAvg2)).setText(Html.fromHtml(avg));


                //이벤트 추가
                //월 클릭 이벤트 추가 1/3
                ((LinearLayout)findViewById(R.id.activepoint_llMonth1)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ActivepointActivity.this,ActivepointHistoryActivity.class);
                        intent.putExtra("MONTHCODE",monthcode_1);
                        intent.putExtra("MONTH",month1);
                        startActivity(intent);
                    }
                });
                //월 클릭 이벤트 추가 2/3
                ((LinearLayout)findViewById(R.id.activepoint_llMonth2)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ActivepointActivity.this,ActivepointHistoryActivity.class);
                        intent.putExtra("MONTHCODE",monthcode_2);
                        intent.putExtra("MONTH",month2);
                        startActivity(intent);

                    }
                });
                //월 클릭 이벤트 추가 3/3
                ((LinearLayout)findViewById(R.id.activepoint_llMonth3)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ActivepointActivity.this,ActivepointHistoryActivity.class);
                        intent.putExtra("MONTHCODE",monthcode_3);
                        intent.putExtra("MONTH",month3);
                        startActivity(intent);
                    }
                });

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
