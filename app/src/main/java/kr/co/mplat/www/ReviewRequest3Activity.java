package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ReviewRequest3Activity extends NAppCompatActivity implements I_startFinish,I_loaddata,I_dialogdata{
    final int CALLTYPE_LOAD = 1;
    final int CALLTYPE_SAVE = 2;
    private Common common = null;
    private int dialogType = 0;
    //Intent intent = null;
    String basicUsername = "";
    String basicZipcode = "";
    String basicAddr = "";
    String basicPhone = "";
    EditText etName = null;
    EditText etZipCode = null;
    EditText etAddress = null;
    EditText etAddressDetail = null;
    EditText etMobile = null;
    EditText etMsg = null;
    Button btnAddressHistory = null;
    Boolean answeredCheck = false;

    String campaignCode = "";
    String blogSnsCode = "";
    String apply = "";
    String essential = "";
    String username = "";
    String zipcode = "";
    String address = "";
    String phone = "";


    String zipNo = "";
    String jibunAddr = "";
    String roadAddr = "";
    String roadAddrPart1 = "";
    String roadAddrPart2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_request3);
        setTvTitle("리뷰신청 (3/3)");
        common = new Common(this);
        Intent intent = getIntent();

        if(intent.hasExtra("BLOG_SNS_CODE")){ blogSnsCode = intent.getStringExtra("BLOG_SNS_CODE").toString(); }
        if(intent.hasExtra("APPLY")){ apply = intent.getStringExtra("APPLY").toString(); }
        if(intent.hasExtra("ESSENTIAL")){ essential = intent.getStringExtra("ESSENTIAL").toString(); }
        if(intent.hasExtra("CAMPAIGN_CODE")){ campaignCode = intent.getStringExtra("CAMPAIGN_CODE").toString(); }
        if(intent.hasExtra("BASIC_USERNAME")){ basicUsername = intent.getStringExtra("BASIC_USERNAME").toString(); }
        if(intent.hasExtra("BASIC_ZIPCODE")){ basicZipcode = intent.getStringExtra("BASIC_ZIPCODE").toString(); }
        if(intent.hasExtra("BASIC_ADDR")){ basicAddr = intent.getStringExtra("BASIC_ADDR").toString(); }
        if(intent.hasExtra("BASIC_PHONE")){ basicPhone = intent.getStringExtra("BASIC_PHONE").toString(); }

        btnAddressHistory = (Button)findViewById(R.id.reviewRequest3_btnAddressHistory);
        etName = (EditText) findViewById(R.id.reviewRequest3_etName);
        etZipCode = (EditText) findViewById(R.id.reviewRequest3_etZipCode);
        etAddress = (EditText) findViewById(R.id.reviewRequest3_etAddress);
        etAddressDetail = (EditText) findViewById(R.id.reviewRequest3_etAddressDetail);
        etMobile = (EditText) findViewById(R.id.reviewRequest3_etMobile);

        //우편번호찾기로 받아오는 경우
        if(intent.hasExtra("zipNo")){ zipNo = intent.getStringExtra("zipNo").toString(); }
        if(intent.hasExtra("jibunAddr")){ jibunAddr = intent.getStringExtra("jibunAddr").toString(); }
        if(intent.hasExtra("roadAddr")){ roadAddr = intent.getStringExtra("roadAddr").toString(); }
        if(intent.hasExtra("roadAddrPart1")){ roadAddrPart1 = intent.getStringExtra("roadAddrPart1").toString(); }
        if(intent.hasExtra("roadAddrPart2")){ roadAddrPart2 = intent.getStringExtra("roadAddrPart2").toString(); }


        if(!zipNo.equals("")){ etZipCode.setText(zipNo);}
        if(!roadAddrPart1.equals("")){ etAddress.setText(roadAddrPart1);}
        if(!roadAddrPart2.equals("")){ etAddressDetail.setText(roadAddrPart2);}


        //우편번호찾기로 받아오는 경우 END

        //editText 이벤트 등록
        etName.addTextChangedListener(textWatcherEtName);
        etAddress.addTextChangedListener(textWatcherEtAddress);
        etMobile.addTextChangedListener(textWatcherEtMobile);


        //이전 신청주소 불러오기
        btnAddressHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!basicUsername.equals("")||!basicZipcode.equals("")||!basicAddr.equals("")||!basicPhone.equals("")){
                    etName.setText(basicUsername);
                    etZipCode.setText(basicZipcode);
                    etAddress.setText(basicAddr);
                    etMobile.setText(basicPhone);
                    Toast.makeText(ReviewRequest3Activity.this,"마지막으로 신청한 주소 정보를 가져왔습니다.",Toast.LENGTH_LONG);
                }else{
                    Toast.makeText(ReviewRequest3Activity.this,"이전에 신청한 주소 정보가 없습니다.",Toast.LENGTH_LONG);
                }
                answeredCheck();
            }
        });

        //신청완료 버튼 선택 이벤트
        ((Button)findViewById(R.id.reviewRequest3_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(answeredCheck){
                    String strName = ((EditText)findViewById(R.id.reviewRequest3_etName)).getText().toString();
                    String strZipCode = ((EditText)findViewById(R.id.reviewRequest3_etZipCode)).getText().toString();
                    String strAddress = ((EditText)findViewById(R.id.reviewRequest3_etAddress)).getText().toString();
                    String strAddressDetail = ((EditText)findViewById(R.id.reviewRequest3_etAddressDetail)).getText().toString();
                    String strMobile = ((EditText)findViewById(R.id.reviewRequest3_etMobile)).getText().toString();

                    Object[][] params = {
                             {"CAMPAIGN_CODE",campaignCode}
                            ,{"BLOG_SNS_CODE",blogSnsCode}
                            ,{"APPLY",apply}
                            ,{"ESSENTIAL",essential}
                            ,{"USERNAME",strName}
                            ,{"ZIPCODE",strZipCode}
                            ,{"ADDRESS",strAddress+" "+strAddressDetail}
                            ,{"PHONE",strMobile}
                    };
                    /*Log.i("wtkim","==============================");
                    Log.i("wtkim","CAMPAIGN_CODE==>"+campaignCode);
                    Log.i("wtkim","BLOG_SNS_CODE==>"+blogSnsCode);
                    Log.i("wtkim","APPLY==>"+apply);
                    Log.i("wtkim","ESSENTIAL==>"+essential);
                    Log.i("wtkim","USERNAME==>"+strName);
                    Log.i("wtkim","ZIPCODE==>"+strZipCode);
                    Log.i("wtkim","ADDRESS==>"+strAddress+" "+strAddressDetail);
                    Log.i("wtkim","PHONE==>"+strMobile);
                    Log.i("wtkim","==============================");*/
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_reviewRequest), params);
                }
            }
        });

        //우편번호 찾기 선택시 이벤트
        ((Button)findViewById(R.id.reviewRequest3_btnSearchZipcode)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewRequest3Activity.this,ReviewSearchZipCodeActivity.class);
                intent.putExtra("CAMPAIGN_CODE",campaignCode);
                intent.putExtra("BLOG_SNS_CODE",blogSnsCode);
                intent.putExtra("APPLY",apply);
                intent.putExtra("ESSENTIAL",essential);

                startActivity(intent);
            }
        });

    }

    TextWatcher textWatcherEtName = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { answeredCheck();}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    TextWatcher textWatcherEtAddress = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { answeredCheck();}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };

    TextWatcher textWatcherEtMobile = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { answeredCheck();}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void afterTextChanged(Editable s) {}
    };


    public void answeredCheck(){
        String strName = etName.getText().toString();
        String strZipCode = etZipCode.getText().toString();
        String strAddress = etAddress.getText().toString();
        String strAddressDetail = etAddressDetail.getText().toString();
        String strMobile = etMobile.getText().toString();

        if(strName.equals("")||strZipCode.equals("")||strAddress.equals("")||strMobile.equals("")){
            ((Button)findViewById(R.id.reviewRequest3_btnNext)).setBackgroundResource(R.color.primaryDisabled);
            answeredCheck = false;
        }else{
            ((Button)findViewById(R.id.reviewRequest3_btnNext)).setBackgroundResource(R.color.primary);
            answeredCheck = true;
        }
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
        //common.loadData(CALLTYPE_LOAD, getString(R.string.url_address), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveHandler(str);

    }
    //저장 처리
    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {

               /* String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    //인증코드 입력화면으로 이동
                    String mobile = etMobile.getText().toString();
                    intent = new Intent(SearchIdActivity.this,SearchIdAuthActivity.class);
                    intent.putExtra("MOBILE_TEL",mobile);
                    startActivity(intent);
                }*/
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                Intent intent = new Intent(ReviewRequest3Activity.this,ReviewActivity.class);
                startActivity(intent);

            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }
}
