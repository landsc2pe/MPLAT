package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class SurveyActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    private int dialogType = 0;
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private String authDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        common = new Common(this);

        //설문조사 참여내역 선택시 이동
        ((LinearLayout)findViewById(R.id.surveyHistory_llMissionHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(SurveyActivity.this,SurveyHistoryActivity.class);
                startActivity(intent);
            }
        });

        //포인트 이벤트 등록
        ((TextView)findViewById(R.id.survey_tvPoint)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(SurveyActivity.this,PointHistoryActivity.class);
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
        Object[][] params = {
                {"LAST_SEQ",""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_survey), params);


    }


    @Override
    public void dialogHandler(String result) {
        /*if(result.equals("ok") && dialogType == 2) {
            Log.i("wktim","go AuthWvActivity!");
            intent = new Intent(SurveyActivity.this,AuthWvActivity.class);
            intent.putExtra("PRE_ACTIVITY","SurveyActivity");
            intent.putExtra("URL",url);
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
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                lastseq =  json.getString("LAST_SEQ");
                authDate = json.getString("AUTH_DATE");

                //리스트뷰 로드
                ListView listView;
                SurveyAdapter adapter;
                //Adapter 생성
                adapter = new SurveyAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listView = (ListView)findViewById(R.id.survey_lvList);
                listView.setAdapter(adapter);

                String campaignCode = "";
                String title = "";
                String startDate = "";
                String endDate = "";
                String point = "";
                String surveyTime = "";
                String surveyUrl = "";
                String pointRealtimeYn = "";
                int i;
                for(i=0;i<ary_lists.length();i++){
                    campaignCode = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    startDate = ((JSONObject)ary_lists.get(i)).getString("START_DATE");
                    endDate = ((JSONObject)ary_lists.get(i)).getString("END_DATE");
                    point = ((JSONObject)ary_lists.get(i)).getString("POINT");
                    surveyTime = ((JSONObject)ary_lists.get(i)).getString("SURVEY_TIME");
                    surveyUrl = ((JSONObject)ary_lists.get(i)).getString("SURVEY_URL");
                    pointRealtimeYn = ((JSONObject)ary_lists.get(i)).getString("POINT_REALTIME_YN");

                    adapter.addItem(campaignCode, title, startDate,endDate,point,surveyTime,surveyUrl,pointRealtimeYn);
                }

                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        SurveyListViewItem item = (SurveyListViewItem) parent.getItemAtPosition(position);
                        String campaignCode = item.getCampaign_code();
                        String title = item.getTitle();
                        String startDate = item.getStartDate();
                        String endDate = item.getEndDate();
                        String point = item.getPoint();
                        String surveyTime = item.getSurveyTime();
                        String surveyUrl = item.getSurveyUrl();
                        String pointRealtimeYn = item.getPointRealtimeYn();
                        if(authDate.equals("")){
                            dialogType = 2;
                            intent = new Intent(SurveyActivity.this,SurveyAuthActivity.class);
                            startActivity(intent);
                        }else{
                            dialogType = 9;
                            intent = new Intent(SurveyActivity.this,SurveyDetailActivity.class);
                            intent.putExtra("CAMPAIGN_CODE",campaignCode);
                            intent.putExtra("TITLE",title);
                            intent.putExtra("START_DATE",startDate);
                            intent.putExtra("END_DATE",endDate);
                            intent.putExtra("POINT",point);
                            intent.putExtra("SURVEY_TIME",surveyTime);
                            intent.putExtra("SURVEY_URL",surveyUrl);
                            intent.putExtra("POINT_REALTIME_YN",pointRealtimeYn);

                            startActivity(intent);
                        }

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
