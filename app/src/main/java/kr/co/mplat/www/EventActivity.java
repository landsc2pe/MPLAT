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

public class EventActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
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
        setContentView(R.layout.activity_event);
        common = new Common(this);
        //이벤트 당첨자
        ((LinearLayout)findViewById(R.id.event_llEventResult)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventActivity.this,EventResultActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dataload();
    }

    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
                ,{"DEVICE","ANDROID"}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_event), params);
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

                lastseq =  json.getString("LAST_SEQ");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                //리스트뷰 로드
                ListView listview;
                EventAdapter adapter;
                //Adapter 생성
                adapter = new EventAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.event_lvList);
                listview.setAdapter(adapter);

                String title;
                String titleImg;
                String directYn;
                String directLink;
                String startDate;
                String endDate;
                String seq;
                String ord;

                int i;
                for(i=0;i<ary_lists.length();i++){
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    titleImg = ((JSONObject)ary_lists.get(i)).getString("TITLE_IMG");
                    directYn = ((JSONObject)ary_lists.get(i)).getString("DIRECT_YN");
                    directLink = ((JSONObject)ary_lists.get(i)).getString("DIRECT_LINK");
                    startDate = ((JSONObject)ary_lists.get(i)).getString("START_DATE");
                    endDate = ((JSONObject)ary_lists.get(i)).getString("END_DATE");
                    seq = ((JSONObject)ary_lists.get(i)).getString("SEQ");
                    ord = ((JSONObject)ary_lists.get(i)).getString("ORD");

                    adapter.addItem(title, titleImg,directYn,directLink,startDate,endDate,seq,ord);
                }

                //보유포인트 문구변경
                //((TextView)findViewById(R.id.mission_tvPoint)).setText("보유포인트 "+Common.getTvComma(totalPoint)+"P");

                //String imageIcon,String appTitle,String mission,String point
                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        EventListViewItem item = (EventListViewItem) parent.getItemAtPosition(position) ;
                        String seq = item.getSeq();
                        Log.i("wtkim","seq==>"+seq);
                       intent = new Intent(EventActivity.this,EventDetailActivity.class);
                        intent.putExtra("SEQ",seq);
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
