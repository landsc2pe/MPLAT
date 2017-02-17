package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivepointHistoryActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
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
    private int rnum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activepoint_history);
        setTvTitle("월별 활동지수 상세보기");
        common = new Common(this);
        intent = getIntent();
        monthcode = intent.getStringExtra("MONTHCODE").toString();
        month = intent.getStringExtra("MONTH").toString();

        if (!monthcode.equals("")) {
            ((TextView) findViewById(R.id.activepointHistory_tvTitle)).setText(Html.fromHtml(monthcode.substring(0, 4) + "년 " + monthcode.substring(4, 6) + "월 활동 지수 : <font color='#7161C4'>" + Common.getTvComma(month) + "</font>"));
        }

        dataload();

    }

    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }


    }

    public void dataload() {
        //기본정보 호출
        Object[][] params = {
                {"MONTHCODE", monthcode}
                , {"LAST_SEQ", ""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_activepointHistory), params);

    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                //문구변경
                ary_historys = json.getJSONArray("HISTORY");
                lastseq = json.getString("LAST_SEQ");
                lastLoadedCnt = ary_historys.length();
                rnum += lastLoadedCnt;

                int i;
                historys = new ArrayList<String>();

                //리스트뷰 로드
                ListView listview;
                ActivepointHistoryAdapter adapter;
                //Adapter 생성
                adapter = new ActivepointHistoryAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.activepointHistory_lvList);
                listview.setAdapter(adapter);
                String seq;
                String datetime;
                String label;
                String active_point;

                for (i = 0; i < ary_historys.length(); i++) {
                    seq = ((JSONObject) ary_historys.get(i)).getString("SEQ");
                    datetime = ((JSONObject) ary_historys.get(i)).getString("DATETIME");
                    //datetime = "2016-12-13 09:56:01";
                    label = ((JSONObject) ary_historys.get(i)).getString("LABEL");
                    active_point = ((JSONObject) ary_historys.get(i)).getString("ACTIVE_POINT");
                    Log.i("wtkim", "seq==>" + datetime);

                    adapter.addItem(seq, datetime, label, active_point);
                }

            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
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
