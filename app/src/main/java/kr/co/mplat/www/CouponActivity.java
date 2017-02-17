package kr.co.mplat.www;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CouponActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();
    private int lastLoadedCnt = 99999;
    private int rnum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        common = new Common(this);
        dataload();


        //쿠폰 > 나의 쿠폰함 선택시, 색변경
        ((LinearLayout)findViewById(R.id.coupon_llCouponHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(CouponActivity.this,CouponHistoryActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //쿠폰 > 쿠폰 자동발급 선택시, 색변경
        ((LinearLayout)findViewById(R.id.coupon_llAutoCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(CouponActivity.this,AutoCouponActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                ,{"DEVICE","ANDROID"}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_coupon), params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        boolean ret = super.onCreateOptionsMenu(menu);
        ChangeNavIcon(1);
        return ret;
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

                int i;
                lists = new ArrayList<String>();

                //리스트뷰 로드
                ListView listview;
                CouponAdapter adapter;
                //Adapter 생성
                adapter = new CouponAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.coupon_lvList);
                listview.setAdapter(adapter);
                String campaign_code;
                String img_url;
                String app_title;
                String developer_name;
                String reward;
                String reserve_yn;
                String join_yn;

                for(i=0;i<ary_lists.length();i++){
                    campaign_code = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    img_url = ((JSONObject)ary_lists.get(i)).getString("IMG_ICON");
                    app_title = ((JSONObject)ary_lists.get(i)).getString("APP_TITLE");
                    developer_name = ((JSONObject)ary_lists.get(i)).getString("DEVELOPER_NAME");
                    reward = ((JSONObject)ary_lists.get(i)).getString("REWARD");
                    reserve_yn = ((JSONObject)ary_lists.get(i)).getString("RESERVE_YN");
                    join_yn = ((JSONObject)ary_lists.get(i)).getString("JOIN_YN");
                    //String campaign_code, Drawable img_icon, String app_title, String developer_name, String reward,String reserve_yn,String join_yn
                    adapter.addItem(campaign_code, img_url,app_title,developer_name,reward,reserve_yn,join_yn);
                }

                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        CouponListViewItem item = (CouponListViewItem) parent.getItemAtPosition(position) ;
                        String campaigncode = item.getCampaign_code();
                        String reward = item.getReward();
                        intent = new Intent(CouponActivity.this,CouponDetailActivity.class);
                        intent.putExtra("CAMPAIGN_CODE",campaigncode);
                        intent.putExtra("REWARD",reward);
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
