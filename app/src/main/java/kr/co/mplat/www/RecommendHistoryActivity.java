package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecommendHistoryActivity extends MAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private String recommendTypeCode = "";
    private String yearmonth = "";
    Spinner spinRecommendTypeCodes = null;
    Spinner spinYearmonths = null;
    private boolean filterLoaded1 =false;
    private boolean filterLoaded2 =false;

    JSONArray ary_recommendTypeCodes = new JSONArray();
    JSONArray ary_yearmonths = new JSONArray();

    ArrayList<String> recommendTypeCodes = new ArrayList<String>();
    ArrayList<String> yearmonths = new ArrayList<String>();

    String strRecommendTypeCode = "";
    String strYearmonth = "";

    //불러오기 index 저장
    int recommendTypeCode_selectedIdx = 0;
    int yearmonth_selectedIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_history);
        common = new Common(this);


        spinRecommendTypeCodes = (Spinner)findViewById(R.id.recommendHistory_spRecommendTypeCodes);
        spinYearmonths = (Spinner)findViewById(R.id.recommendHistory_spYearmonths);
        //spineer 이벤트 등록
        spinRecommendTypeCodes.setOnItemSelectedListener(this);
        spinYearmonths.setOnItemSelectedListener(this);


       //추천하기
        ((LinearLayout)findViewById(R.id.recommendHistory_llRecommend)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendHistoryActivity.this,RecommendActivity.class);
                startActivity(intent);
            }
        });
        //나의 추천등급 클릭 이벤트
        ((LinearLayout)findViewById(R.id.recommendHistory_llRecommendGrade)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendHistoryActivity.this,RecommendGradeActivity.class);
                startActivity(intent);
            }
        });

        dataload();
    }
    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
                ,{"RECOMMEND_TYPE_CODE",strRecommendTypeCode}
                ,{"YEARMONTH",strYearmonth}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_recommendHistory), params);
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
                ary_recommendTypeCodes = json.getJSONArray("RECOMMEND_TYPE_CODES");
                ary_yearmonths = json.getJSONArray("YEARMONTHS");
                lastseq =  json.getString("LAST_SEQ");
                String totalCnt =  json.getString("TOTAL_CNT");
                String yearmonthCnt =  json.getString("YEARMONTH_CNT");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;
                int i;

                ((TextView)findViewById(R.id.recommendHistory_tvTotalCnt)).setText(Html.fromHtml("총 추천 인원 : <font color='#7161C4'>"+totalCnt+"명</font>"));
                ((TextView)findViewById(R.id.recommendHistory_tvYearmonthCnt)).setText(Html.fromHtml("이번 달 추천인원 : <font color='#7161C4'>"+yearmonthCnt+"명</font>"));
                //spiner1
                if(!filterLoaded1) {
                    recommendTypeCodes.add("전체");
                    for (i = 0; i < ary_recommendTypeCodes.length(); i++) {
                        recommendTypeCodes.add(((JSONObject) ary_recommendTypeCodes.get(i)).getString("RECOMMEND_NAME"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, recommendTypeCodes);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spinRecommendTypeCodes.setAdapter(aa);
                    }
                }

                //spiner2
                if(!filterLoaded2) {
                    for (i = 0; i < ary_yearmonths.length(); i++) {
                        yearmonths.add(((JSONObject) ary_yearmonths.get(i)).getString("YEARMONTH_NAME"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, yearmonths);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spinYearmonths.setAdapter(aa);
                    }
                }

                //리스트뷰 로드
                ListView listview;
                RecommendHistoryAdapter adapter;
                //Adapter 생성
                adapter = new RecommendHistoryAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.recommendHistory_lvList);
                listview.setAdapter(adapter);

                String joinDate = "";
                String recommendName = "";
                String email = "";
                String joinDatetime = "";


                for(i=0;i<ary_lists.length();i++){
                    joinDate = ((JSONObject)ary_lists.get(i)).getString("JOIN_DATE");
                    recommendName = ((JSONObject)ary_lists.get(i)).getString("RECOMMEND_NAME");
                    email = ((JSONObject)ary_lists.get(i)).getString("EMAIL");
                    joinDatetime = ((JSONObject)ary_lists.get(i)).getString("JOIN_DATETIME");

                    adapter.addItem(joinDate,recommendName,email);
                }

                //String imageIcon,String appTitle,String mission,String point
                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Log.i("wtkim","리스트클릭!");
                        // get item
                        RecommendHistoryListViewItem item = (RecommendHistoryListViewItem) parent.getItemAtPosition(position) ;
                        String joinDate = item.getJoinDate();
                        String email = item.getEmail();
                        String recommendName = item.getRecommendName();


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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Common.Logg("onItemSelected");
        JSONObject object;
        int addNum = 0;
        try{
            switch (adapterView.getId()){
                case R.id.recommendHistory_spRecommendTypeCodes:
                    //filterLoaded1=true;
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_recommendTypeCodes.get(adapterView.getSelectedItemPosition()+addNum);
                    //Log.i("wtKim","object==>"+object.toString());
                    if(addNum==0){ strRecommendTypeCode = "";}else{ strRecommendTypeCode = object.get("CODE").toString(); }
                    Log.i("wtkim","strRecommendTypeCode11111==>"+strRecommendTypeCode);
                    if(filterLoaded1)dataload();
                    filterLoaded1=true;
                    break;
                case R.id.recommendHistory_spYearmonths:
                    //if(filterLoaded2)return;
                    //filterLoaded2=true;
                    //if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_yearmonths.get(adapterView.getSelectedItemPosition());
                    //if(addNum==0){ strYearmonth = "";}else{ strYearmonth = object.get("YEARMONTH_NAME").toString(); }
                    strYearmonth = object.get("YEARMONTH").toString();
                    Log.i("wtkim","strYearmonth22222==>"+strYearmonth);
                    if(filterLoaded2)dataload();
                    filterLoaded2=true;
                    break;
            }
        }catch(Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
            //Log.i("wtKim","err:"+e.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
