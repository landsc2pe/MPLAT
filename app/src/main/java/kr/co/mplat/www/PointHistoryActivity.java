package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PointHistoryActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    String monthcode = "";
    String month = "";
    JSONArray ary_historys = new JSONArray();
    ArrayList<String> historys = new ArrayList<String>();
    private int lastLoadedCnt = 99999;
    private int rnum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);
        setTvTitle("포인트 내역");
        common = new Common(this);
        dataload();


        //포인트 안내 이벤트 등록
        ((ImageView)findViewById(R.id.pointHistory_ivHelp)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(PointHistoryActivity.this,PointInfoActivity.class);
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
    }

    public void dataload(){
        Object[][] params = {
                {"LAST_SEQ",""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_pointHistory), params);
    }

    @Override
    public void dialogHandler(String result) {
       /* if(dialogType == 9 && result.equals("ok")){
            Common.setPreference(getApplicationContext(), "UID", "");
            Common.setPreference(getApplicationContext(), "KEY", "");

            intent = new Intent(ActivepointHistoryActivity.this,LoginActivity.class);
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
                //문구변경
                String mypoint = json.getString("POINT");
                ((TextView)findViewById(R.id.pointHistory_tvPoint)).setText(Common.getTvComma(mypoint));

                ary_historys = json.getJSONArray("HISTORY");
                lastseq =  json.getString("LAST_SEQ");
                lastLoadedCnt=ary_historys.length();
                rnum+=lastLoadedCnt;

                int i;
                historys = new ArrayList<String>();

                //리스트뷰 로드
                ListView listview;
                PointHistoryAdapter adapter;
                //Adapter 생성
                adapter = new PointHistoryAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.pointHistory_lvList);
                listview.setAdapter(adapter);
                String seq;
                String datetime;
                String gubun;
                String title;
                String point;

                for(i=0;i<ary_historys.length();i++){
                    seq = ((JSONObject)ary_historys.get(i)).getString("SEQ");
                    datetime = ((JSONObject)ary_historys.get(i)).getString("DATETIME");
                    gubun = ((JSONObject)ary_historys.get(i)).getString("GUBUN");
                    title = ((JSONObject)ary_historys.get(i)).getString("TITLE");
                    point = ((JSONObject)ary_historys.get(i)).getString("POINT");
                    Log.i("wtkim","seq==>"+datetime);

                    adapter.addItem(seq,datetime,gubun,title,point);
                }

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
    }
}
