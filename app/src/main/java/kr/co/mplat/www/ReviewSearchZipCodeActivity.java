package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReviewSearchZipCodeActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SEARCH = 2;
    private int dialogType = 0;
    Common common = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private String campaignCode = "";
    private String blogSnsCode = "";
    private String apply = "";
    private String essential = "";

    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_search_zip_code);
        setTvTitle("우편번호 찾기");
        common = new Common(this);

        intent = getIntent();
        if(intent.hasExtra("CAMPAIGN_CODE")){ campaignCode = intent.getStringExtra("CAMPAIGN_CODE").toString(); }
        if(intent.hasExtra("BLOG_SNS_CODE")){ blogSnsCode = intent.getStringExtra("BLOG_SNS_CODE").toString(); }
        if(intent.hasExtra("APPLY")){ apply = intent.getStringExtra("APPLY").toString(); }
        if(intent.hasExtra("ESSENTIAL")){ essential = intent.getStringExtra("ESSENTIAL").toString(); }

        ((Button)findViewById(R.id.reviewsearchzipcode_btnsearch)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strSearch = ((EditText)findViewById(R.id.reviewsearchzipcode_etSearch)).getText().toString();
                searchAdress(strSearch);
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
    public void searchAdress(String str){
        if(!str.equals("")){
            Object[][] params = {
                {"KEYWORD", str}
            };
            common.loadData(CALLTYPE_SEARCH, getString(R.string.url_address), params);
        }else{
            Common.createDialog(this, getString(R.string.app_name).toString(),null,"검색어를 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
        }

    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SEARCH) searchHandler(str);
    }

    public void searchHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                ((LinearLayout)findViewById(R.id.reviewsearchzipcode_llDesc2)).setVisibility(View.GONE);
                ary_lists = json.getJSONArray("LIST");
                //리스트뷰 로드
                ListView listview;
                AddressAdapter adapter;

                //Adapter 생성
                adapter = new AddressAdapter();
                //리스트뷰 참조 및 Adapter 달기
                listview = (ListView) findViewById(R.id.reviewsearchzipcode_lvList);
                listview.setAdapter(adapter);
                String detBdNmList;
                String engAddr;
                String zipNo;
                String roadAddrPart2;
                String jibunAddr;
                String roadAddrPart1;
                String rnMgtSn;
                String admCd;
                String bdMgtSn;
                String roadAddr;
                int i;
                for(i=0;i<ary_lists.length();i++){
                    detBdNmList = ((JSONObject)ary_lists.get(i)).getString("detBdNmList");
                    engAddr = ((JSONObject)ary_lists.get(i)).getString("engAddr");
                    zipNo = ((JSONObject)ary_lists.get(i)).getString("zipNo");
                    roadAddrPart2 = ((JSONObject)ary_lists.get(i)).getString("roadAddrPart2");
                    jibunAddr = ((JSONObject)ary_lists.get(i)).getString("jibunAddr");
                    roadAddrPart1 = ((JSONObject)ary_lists.get(i)).getString("roadAddrPart1");
                    rnMgtSn = ((JSONObject)ary_lists.get(i)).getString("rnMgtSn");
                    admCd = ((JSONObject)ary_lists.get(i)).getString("admCd");
                    bdMgtSn = ((JSONObject)ary_lists.get(i)).getString("bdMgtSn");
                    roadAddr = ((JSONObject)ary_lists.get(i)).getString("roadAddr");

                    adapter.addItem( detBdNmList,  engAddr,  zipNo,  roadAddrPart2,  jibunAddr,  roadAddrPart1,  rnMgtSn,  admCd,  bdMgtSn,  roadAddr);

                }

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        //get item
                        AddressListViewItem item = (AddressListViewItem) parent.getItemAtPosition(position);
                        String detBdNmList = item.getDetBdNmList();
                        String engAddr = item.getEngAddr();
                        String zipNo = item.getZipNo();
                        String roadAddrPart2 = item.getRoadAddrPart2();
                        String jibunAddr = item.getJibunAddr();
                        String roadAddrPart1 = item.getRoadAddrPart1();
                        String rnMgtSn = item.getRnMgtSn();
                        String admCd = item.getAdmCd();
                        String bdMgtSn = item.getBdMgtSn();
                        String roadAddr = item.getRoadAddr();
                        Log.i("wtkim","zipNo==>"+zipNo);

                        intent = new Intent(ReviewSearchZipCodeActivity.this,ReviewRequest3Activity.class);
                        intent.putExtra("CAMPAIGN_CODE",campaignCode);
                        intent.putExtra("BLOG_SNS_CODE",blogSnsCode);
                        intent.putExtra("APPLY",apply);
                        intent.putExtra("ESSENTIAL",essential);
                        intent.putExtra("zipNo",zipNo);
                        intent.putExtra("jibunAddr",jibunAddr);
                        intent.putExtra("roadAddr",roadAddr);
                        intent.putExtra("roadAddrPart1",roadAddrPart1);
                        intent.putExtra("roadAddrPart2",roadAddrPart2);

                        startActivity(intent);

                        //우편번호
                        //주소입력
                        //세부 주소입력
                        //받는사람 휴대전화 번호
                    }
                });

                //Common.createDialog(this, getString(R.string.app_name).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
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
