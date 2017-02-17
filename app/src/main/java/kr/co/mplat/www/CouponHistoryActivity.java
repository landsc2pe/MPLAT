package kr.co.mplat.www;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CouponHistoryActivity extends MAppCompatActivity implements CouponHistoryAdapter.ListBtnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_history);
        common = new Common(this);
        dataload();



        //쿠폰 > 쿠폰신청 선택시, 색변경
        ((LinearLayout)findViewById(R.id.couponHistory_llCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               intent = new Intent(CouponHistoryActivity.this,CouponActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //쿠폰 > 쿠폰 자동발급 선택시, 색변경
        ((LinearLayout)findViewById(R.id.couponHistory_llAutoCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(CouponHistoryActivity.this,AutoCouponActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    //게임 다운로드
    @Override
    public void onListBtnClick(CouponHistoryAdapter.TagInfo info) {
        Log.i("wtkim","info.clickTarget==>"+info.clickTarget);
        String couponNum = info.couponNum;
        String androidUrl = info.android_url;
        String clickTarget = info.clickTarget;
        if(clickTarget.equals("download")){
            if(!androidUrl.equals("")){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(androidUrl));
                startActivity(intent);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "다운경로가 없습니다. 관리자에게 문의하세요.", getString(R.string.btn_ok),null, false, false);
            }
        }else if(clickTarget.equals("couponnum")){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label",couponNum.toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "주소가 클립보드에 복사되었습니다!", Toast.LENGTH_SHORT).show();
        }



        //Toast.makeText(this, Integer.toString(position+1) + "번 아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show() ;
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        boolean ret = super.onCreateOptionsMenu(menu);
        ChangeNavIcon(1);
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
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
                {"DEVICE","ANDROID"}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_couponHistory), params);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                int i;
                lists = new ArrayList<String>();

                //리스트뷰 로드
                ListView listview;
                CouponHistoryAdapter adapter;
                ArrayList<CouponHistoryListViewItem> items = new ArrayList<CouponHistoryListViewItem>() ;
                //Adapter 생성
                //adapter = new CouponHistoryAdapter();
                //adapter = new CouponHistoryAdapter(this, R.layout.listview_couponhistory_item, items,this,this) ;
                adapter = new CouponHistoryAdapter(this, R.layout.listview_couponhistory_item, items,this);
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.couponHistory_lvList);
                listview.setAdapter(adapter);
                String campaign_code;
                String img_url;
                String app_title;
                String developer_name;
                String reward;
                String app_open_yn;
                String open_date;
                String coupon_num;
                String android_url;
                String ios_url;
                String usable_yn;
                String used_yn;
                String use_end_date;

                for(i=0;i<ary_lists.length();i++){
                    campaign_code = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    img_url = ((JSONObject)ary_lists.get(i)).getString("IMG_ICON");
                    app_title = ((JSONObject)ary_lists.get(i)).getString("APP_TITLE");
                    developer_name = ((JSONObject)ary_lists.get(i)).getString("DEVELOPER_NAME");
                    reward = ((JSONObject)ary_lists.get(i)).getString("REWARD");
                    app_open_yn = ((JSONObject)ary_lists.get(i)).getString("APP_OPEN_YN");
                    open_date = ((JSONObject)ary_lists.get(i)).getString("OPEN_DATE");
                    coupon_num = ((JSONObject)ary_lists.get(i)).getString("COUPON_NUM");
                    android_url = ((JSONObject)ary_lists.get(i)).getString("ANDROID_URL");
                    ios_url = ((JSONObject)ary_lists.get(i)).getString("IOS_URL");
                    usable_yn = ((JSONObject)ary_lists.get(i)).getString("USABLE_YN");
                    used_yn = ((JSONObject)ary_lists.get(i)).getString("USED_YN");
                    use_end_date = ((JSONObject)ary_lists.get(i)).getString("USE_END_DATE");


                    //String campaign_code, Drawable img_icon, String app_title, String developer_name, String reward,String reserve_yn,String join_yn
                    adapter.addItem(campaign_code, img_url,app_title,developer_name,reward,app_open_yn,open_date,coupon_num,android_url,ios_url,usable_yn,used_yn,use_end_date);
                }

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }
}
