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

public class ReviewActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        common = new Common(this);
        dataload();

        //리뷰 신청결과 선택 이벤트
        ((LinearLayout)findViewById(R.id.review_llHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewActivity.this,ReviewHistoryActivity.class);
                startActivity(intent);
            }
        });
        //리뷰 등록 선택 이벤트
        ((LinearLayout)findViewById(R.id.review_llList)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewActivity.this,ReviewListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_review), params);
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
                Log.i("wtkim","ReviewActivity!!");
                String UID = Common.getPreference(getApplicationContext(), "UID");
                String KEY = Common.getPreference(getApplicationContext(), "KEY");
                Log.i("wtkim","UID==>"+UID);
                Log.i("wtkim","KEY==>"+KEY);
                ary_lists = json.getJSONArray("LIST");
                lastseq =  json.getString("LAST_SEQ");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                //리스트뷰 로드
                ListView listview;
                ReviewAdapter adapter;
                //Adapter 생성
                adapter = new ReviewAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.review_lvList);
                listview.setAdapter(adapter);

                String campaignCode = "";
                String imgIcon = "";
                String title = "";
                String reward = "";
                String reviewMethod = "";
                String targetCnt = "";
                String joinCnt = "";
                String joinYn = "";
                String remainDays = "";

                int i;
                for(i=0;i<ary_lists.length();i++){
                    campaignCode = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    imgIcon = ((JSONObject)ary_lists.get(i)).getString("IMG_ICON");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    reward = ((JSONObject)ary_lists.get(i)).getString("REWARD");
                    reviewMethod = ((JSONObject)ary_lists.get(i)).getString("REVIEW_METHOD");
                    targetCnt = ((JSONObject)ary_lists.get(i)).getString("TARGET_CNT");
                    joinCnt = ((JSONObject)ary_lists.get(i)).getString("JOIN_CNT");
                    joinYn = ((JSONObject)ary_lists.get(i)).getString("JOIN_YN");
                    remainDays = ((JSONObject)ary_lists.get(i)).getString("REMAIN_DAYS");

                    adapter.addItem(campaignCode,imgIcon,title,reward,reviewMethod,targetCnt,joinCnt,joinYn,remainDays);
                }

                //String imageIcon,String appTitle,String mission,String point
                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                   @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Log.i("wtkim","리스트클릭!");
                       ReviewListViewItem item = (ReviewListViewItem) parent.getItemAtPosition(position);
                        // get item
                       String campaigncode = item.getCampaignCode();
                       String joinYn = item.getJoinYn();
                       if(joinYn.equals("N")){
                           intent = new Intent(ReviewActivity.this,ReviewDetailActivity.class);
                           intent.putExtra("CAMPAIGN_CODE",campaigncode);
                           startActivity(intent);
                       }else{
                           Common.createDialog(ReviewActivity.this, getString(R.string.app_name).toString(),null, "해당 리뷰는 이미 신청하셨습니다.", getString(R.string.btn_ok),null, false, false);
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
