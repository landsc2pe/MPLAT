package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewRequest2Activity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata {
    private Common common = null;
    Intent intent = null;
    private int dialogType=0;
    private final int CALLTYPE_LOAD = 1;
    private String campaignCode = "";
    Spinner spinBlogSns = null;
    String blogSnsCode = "";
    String apply = "";
    String essential = "";
    String username = "";
    String zipcode = "";
    String address = "";
    String phone = "";
    String basicUsername = "";
    String basicZipcode = "";
    String basicAddr = "";
    String basicPhone = "";

    ArrayList<String> blogSnss = new ArrayList<>(); //이동통신사
    JSONArray ary_blogSns = new JSONArray();      //이동통신사
    String strBlogSns = "";
    int blogSnss_selectedIdx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_request2);
        setTvTitle("리뷰신청 (2/3)");
        common = new Common(this);
        //캠페인코드
        intent = getIntent();
        campaignCode = intent.getStringExtra("CAMPAIGN_CODE").toString();
        spinBlogSns = (Spinner)findViewById(R.id.reviewRequest2_spBlogSns);

        //editText 이벤트 등록
        EditText etMsg = (EditText)findViewById(R.id.reviewRequest2_etMsg);
        EditText etRequestInfo = (EditText)findViewById(R.id.reviewRequest2_etRequestInfo);
        Spinner spBlogSns = (Spinner)findViewById(R.id.reviewRequest2_spBlogSns);
        spBlogSns.setOnItemSelectedListener(this);
        etMsg.addTextChangedListener(textWatcherEtMsg);
        etRequestInfo.addTextChangedListener(textWatcherEtRequestInfo);

        //다음 버튼 선택시
        ((Button)findViewById(R.id.reviewRequest2_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String etMsg = ((EditText)findViewById(R.id.reviewRequest2_etMsg)).getText().toString();
                String etRequestInfo = ((EditText)findViewById(R.id.reviewRequest2_etRequestInfo)).getText().toString();


                if(!strBlogSns.equals("") && !etMsg.equals("") && !etRequestInfo.equals("")) {
                    intent = new Intent(ReviewRequest2Activity.this,ReviewRequest3Activity.class);
                    intent.putExtra("BLOG_SNS_CODE",strBlogSns);
                    intent.putExtra("APPLY",etMsg);
                    intent.putExtra("ESSENTIAL",etRequestInfo);
                    intent.putExtra("CAMPAIGN_CODE",campaignCode);
                    intent.putExtra("BASIC_USERNAME",basicUsername);
                    intent.putExtra("BASIC_ZIPCODE",basicZipcode);
                    intent.putExtra("BASIC_ADDR",basicAddr);
                    intent.putExtra("BASIC_PHONE",basicPhone);

                    startActivity(intent);
                }
            }
        });
    }

    TextWatcher textWatcherEtMsg = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            Log.i("onTextChanged", s.toString());
            ChangeNextButton();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

    TextWatcher textWatcherEtRequestInfo = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            Log.i("onTextChanged", s.toString());
            ChangeNextButton();
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    public void dialogHandler(String result) {

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
        dialogType=1;
        Object[][] params = {
                {"CAMPAIGN_CODE",campaignCode}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_reviewInfo), params);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            ary_blogSns = json.getJSONArray("BLOG_SNS_LIST");
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            int i;
            blogSnss.add("선택");
            for (i = 0; i < ary_blogSns.length(); i++){
                blogSnss.add(((JSONObject) ary_blogSns.get(i)).getString("LABEL"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, blogSnss);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spinBlogSns.setAdapter(aa);
            }

            if (err.equals("")) {
                blogSnsCode = json.getString("BLOG_SNS_CODE");
                apply = json.getString("APPLY");
                essential = json.getString("ESSENTIAL");
                username = json.getString("USERNAME");
                zipcode = json.getString("ZIPCODE");
                address = json.getString("ADDRESS");
                phone = json.getString("PHONE");
                basicUsername = json.getString("BASIC_USERNAME");
                basicZipcode = json.getString("BASIC_ZIPCODE");
                basicAddr = json.getString("BASIC_ADDR");
                basicPhone = json.getString("BASIC_PHONE");
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

   @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try {
            switch (adapterView.getId()) {
                case R.id.reviewRequest2_spBlogSns:
                    Log.i("wtkim","changed!");
                    Log.i("wtkim","adapterView.getSelectedItemPosition()==>"+adapterView.getSelectedItemPosition());
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_blogSns.get(adapterView.getSelectedItemPosition()+addNum);
                    Log.i("wtKim","object==>"+object.toString());
                    if(addNum==0){ strBlogSns = "";}else{ strBlogSns = object.get("CODE").toString(); }
                    Log.i("wtKim", "strBlogSns==>"+strBlogSns);
                    break;
            }
        }catch(Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }

        ChangeNextButton();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void ChangeNextButton(){
        Button btnNext = (Button)findViewById(R.id.reviewRequest2_btnNext);
        String etMsg = ((EditText)findViewById(R.id.reviewRequest2_etMsg)).getText().toString();
        String etRequestInfo = ((EditText)findViewById(R.id.reviewRequest2_etRequestInfo)).getText().toString();
        if(strBlogSns.equals("")||etMsg.equals("")||etRequestInfo.equals("")){
            btnNext.setBackgroundResource(R.color.primaryDisabled);
        }else{
            btnNext.setBackgroundResource(R.color.primary);
        }
    }
}
