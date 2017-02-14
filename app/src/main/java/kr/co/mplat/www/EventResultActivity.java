package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventResultActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
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
        setContentView(R.layout.activity_event_result);
        common = new Common(this);
        //진행중인 이벤트
        ((LinearLayout)findViewById(R.id.eventResult_llEvent)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(EventResultActivity.this,EventActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dataload();
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
    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_eventResult), params);
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
                EventResultAdapter adapter;
                //Adapter 생성
                adapter = new EventResultAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.eventResult_lvList);
                listview.setAdapter(adapter);

                String seq = "";
                String registDate = "";
                String title = "";
                String topYn = "";

                int i;
                for(i=0;i<ary_lists.length();i++){
                    seq = ((JSONObject)ary_lists.get(i)).getString("SEQ");
                    registDate = ((JSONObject)ary_lists.get(i)).getString("REGIST_DATE");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    topYn = ((JSONObject)ary_lists.get(i)).getString("TOP_YN");

                    adapter.addItem(seq,registDate,title,topYn);
                }

                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        EventResultListViewItem item = (EventResultListViewItem) parent.getItemAtPosition(position) ;
                        String seq = item.getSeq();

                        intent = new Intent(EventResultActivity.this,EventResultDetailActivity.class);
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
