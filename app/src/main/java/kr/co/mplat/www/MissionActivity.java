package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MissionActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private String googleplayId = "";
    private String totalPoint = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        //setContentView(R.layout.listview_mission_item);
        common = new Common(this);
        dataload();

        //하단 포인트 textview 클릭시 포인트 내역으로 이동
        ((TextView)findViewById(R.id.mission_tvPoint)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MissionActivity.this,PointHistoryActivity.class);
                startActivity(intent);
            }
        });

        //미션 참여내역 선택시 이벤트 등록
        ((LinearLayout)findViewById(R.id.mission_llMissionHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MissionActivity.this,MissionHistoryActivity.class);
                startActivity(intent);
                /*((LinearLayout)findViewById(R.id.mission_llMissionHistory)).setBackgroundResource(R.color.primary);
                ((TextView)findViewById(R.id.mission_tvMissionHistory)).setTextColor(view.getResources().getColor(R.color.white));

                ((LinearLayout)findViewById(R.id.mission_llMission)).setBackgroundResource(R.color.white);
                ((TextView)findViewById(R.id.mission_tvMission)).setTextColor(view.getResources().getColor(R.color.primaryFont));*/
            }
        });

    }

    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
                ,{"DEVICE","ANDROID"}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_mission), params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start(null);
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

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                googleplayId =  json.getString("GOOGLEPLAY_ID");
                totalPoint =  json.getString("POINT");
                lastseq =  json.getString("LAST_SEQ");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                //리스트뷰 로드
                ListView listview;
                MissionAdapter adapter;
                //Adapter 생성
                adapter = new MissionAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.mission_lvList);
                listview.setAdapter(adapter);

                String campaignCode;
                String imgIcon;
                String appTitle;
                String developerName;
                String mission;
                String point;
                String pointRealtimeYn;
                String pointDate;
                String joinYn;
                int i;
                for(i=0;i<ary_lists.length();i++){
                    campaignCode = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    imgIcon = ((JSONObject)ary_lists.get(i)).getString("IMG_ICON");
                    appTitle = ((JSONObject)ary_lists.get(i)).getString("APP_TITLE");
                    developerName = ((JSONObject)ary_lists.get(i)).getString("DEVELOPER_NAME");
                    mission = ((JSONObject)ary_lists.get(i)).getString("MISSION");
                    point = ((JSONObject)ary_lists.get(i)).getString("POINT");
                    pointRealtimeYn = ((JSONObject)ary_lists.get(i)).getString("POINT_REALTIME_YN");
                    pointDate = ((JSONObject)ary_lists.get(i)).getString("POINT_DATE");
                    joinYn = ((JSONObject)ary_lists.get(i)).getString("JOIN_YN");

                    adapter.addItem(campaignCode, imgIcon,appTitle,developerName,mission,point,pointRealtimeYn,pointDate,joinYn);
                }

                //보유포인트 문구변경
                ((TextView)findViewById(R.id.mission_tvPoint)).setText("보유포인트 "+Common.getTvComma(totalPoint)+"P");

                //String imageIcon,String appTitle,String mission,String point
                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Log.i("wtkim","리스트클릭!");
                        // get item
                        MissionListViewItem item = (MissionListViewItem) parent.getItemAtPosition(position) ;
                        String joinYn = item.getJoinYn();
                        String campaigncode = item.getCampaignCode();
                        String mission = item.getMission();
                        if(joinYn.equals("N")){
                            intent = new Intent(MissionActivity.this,MissionDetailActivity.class);
                            intent.putExtra("CAMPAIGN_CODE",campaigncode);
                            startActivity(intent);
                        }
                        //구글플레이 미션이 삭제됨. 일단 주석처리
                        /*if(joinYn.equals("N")){
                            intent = new Intent(MissionActivity.this,GoogleActivity.class);
                            intent.putExtra("PRE_ACTIVITY","MissionActivity");
                            startActivity(intent);
                        }*/

                        /*String campaigncode = item.getCampaign_code();
                        String reward = item.getReward();
                        intent = new Intent(MissionActivity.this,CouponDetailActivity.class);
                        intent.putExtra("CAMPAIGN_CODE",campaigncode);
                        intent.putExtra("REWARD",reward);
                        startActivity(intent);*/
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
