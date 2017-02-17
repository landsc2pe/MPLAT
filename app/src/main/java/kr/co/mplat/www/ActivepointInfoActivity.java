package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivepointInfoActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_categories = new JSONArray();
    ArrayList<String> categories = new ArrayList<String>();
    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();

    private int lastLoadedCnt = 99999;
    private int rnum = 0;

    Button btnAll = null;
    Button btnCoupon = null;
    Button btnBetaTest = null;
    Button btnMission = null;
    Button btnSurvey = null;
    Button btnMypage = null;
    Button btnCommon = null;
    int btnCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activepoint_info);
        setTvTitle("활동지수가 부여되는 회원활동");
        common = new Common(this);

        btnAll = (Button) findViewById(R.id.activepointInfo_btnAll);
        btnCoupon = (Button) findViewById(R.id.activepointInfo_btnCoupon);
        btnBetaTest = (Button) findViewById(R.id.activepointInfo_btnBetaTest);
        btnMission = (Button) findViewById(R.id.activepointInfo_btnMission);
        btnSurvey = (Button) findViewById(R.id.activepointInfo_btnSurvey);
        btnMypage = (Button) findViewById(R.id.activepointInfo_btnMypage);
        btnCommon = (Button) findViewById(R.id.activepointInfo_btnCommon);

        btnAll.setBackgroundResource(R.color.primary);
        btnAll.setTextColor(this.getResources().getColor(R.color.white));

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAll.setBackgroundResource(R.color.primary);
                btnAll.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));

                dataload("btnAll");
            }
        });

        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCoupon.setBackgroundResource(R.color.primary);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.white));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnCoupon");
            }
        });

        btnBetaTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBetaTest.setBackgroundResource(R.color.primary);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnBetaTest");
            }
        });

        btnMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMission.setBackgroundResource(R.color.primary);
                btnMission.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnMission");
            }
        });

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSurvey.setBackgroundResource(R.color.primary);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnSurvey");
            }
        });

        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMypage.setBackgroundResource(R.color.primary);
                btnMypage.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnCommon.setBackgroundResource(R.drawable.border);
                btnCommon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnMypage");
            }
        });

        btnCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCommon.setBackgroundResource(R.color.primary);
                btnCommon.setTextColor(view.getResources().getColor(R.color.white));
                btnCoupon.setBackgroundResource(R.drawable.border);
                btnCoupon.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnBetaTest.setBackgroundResource(R.drawable.border);
                btnBetaTest.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMission.setBackgroundResource(R.drawable.border);
                btnMission.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnSurvey.setBackgroundResource(R.drawable.border);
                btnSurvey.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnAll.setBackgroundResource(R.drawable.border);
                btnAll.setTextColor(view.getResources().getColor(R.color.primaryFont));
                btnMypage.setBackgroundResource(R.drawable.border);
                btnMypage.setTextColor(view.getResources().getColor(R.color.primaryFont));
                dataload("btnCommon");
            }
        });

        dataload("btnAll");
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

    public void dataload(String t) {
        String cate = "";
        //기본정보 호출
        switch (t) {
            case "btnAll":
                cate = "";
                break;
            case "btnCoupon":
                cate = "01";
                break;
            case "btnBetaTest":
                cate = "03";
                break;
            case "btnMission":
                cate = "04";
                break;
            case "btnSurvey":
                cate = "05";
                break;
            case "btnMypage":
                cate = "08";
                break;
            case "btnCommon":
                cate = "10";
                break;
        }
        lastseq = "";
        Object[][] params = {
                {"LAST_SEQ", lastseq}
                , {"CATEGORY1", cate}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_activepointInfo), params);
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
                ary_categories = json.getJSONArray("CATEGORIES");
                ary_lists = json.getJSONArray("LIST");
                lastseq = json.getString("LAST_SEQ");
                lastLoadedCnt = ary_categories.length();
                rnum += lastLoadedCnt;

                int i;
                categories = new ArrayList<String>();
                String code = "";
                String category1Name = "";
                Button tmpBtn[] = new Button[ary_categories.length()];
                //카테고리 버튼생성
                LinearLayout ll_1 = (LinearLayout) findViewById(R.id.activepointInfo_ll_1);
                LinearLayout ll_2 = (LinearLayout) findViewById(R.id.activepointInfo_ll_2);

                //리스트뷰 로드
                ListView listView;
                ActivepointInfoAdapter adapter;
                //adapter 새성
                adapter = new ActivepointInfoAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listView = (ListView) findViewById(R.id.activepointInfo_lvList);
                listView.setAdapter(adapter);

                String activePointCode;
                String name;
                String activePoint;
                String limitDesc;

                for (i = 0; i < ary_lists.length(); i++) {
                    activePointCode = ((JSONObject) ary_lists.get(i)).getString("ACTIVE_POINT_CODE");
                    name = ((JSONObject) ary_lists.get(i)).getString("NAME");
                    activePoint = ((JSONObject) ary_lists.get(i)).getString("ACTIVE_POINT");
                    limitDesc = ((JSONObject) ary_lists.get(i)).getString("LIMIT_DESC");

                    adapter.addItem(activePointCode, name, activePoint, limitDesc);
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
