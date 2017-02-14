package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CulturelandRequestActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private int selectedPrice = 0;
    private int pointNum = 0;
    String etMobile = "";
    RadioButton rb1000 = null;
    RadioButton rb2000 = null;
    RadioButton rb3000 = null;
    RadioButton rb4000 = null;
    RadioButton rb5000 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultureland_request);
        setTvTitle("온라인 문화상품권 교환");
        common = new Common(this);

        ((TextView)findViewById(R.id.culturelandRequest_tvInfo1)).setText(Html.fromHtml("온라인문화상품권 교환은 실시간으로 처리가 됩니다. <font color=\"#D57A76\">교환 후 취소가 불가능</font> 합니다."));
        ((TextView)findViewById(R.id.culturelandRequest_tvInfo2)).setText(Html.fromHtml("온라인문화상품권의 <font color=\"#D57A76\">유효기간은 발급일로부터 60일</font>입니다. 유효기간 경과 후 취소 되지 않습니다."));
        ((TextView)findViewById(R.id.culturelandRequest_tvInfo3)).setText(Html.fromHtml("온라인문화상품권 교환 시, <font color=\"#D57A76\">액면가의 10%의 교환수수료가 추가로 차감</font>됩니다."));
        //setculturelandRequest_etMobile
        etMobile = ((EditText)findViewById(R.id.culturelandRequest_etMobile)).getText().toString();

        //라디오 그룹 값변경 이벤트 등록
        rb1000 = ((RadioButton) findViewById(R.id.cultureland_rb1000));
        rb2000 = ((RadioButton) findViewById(R.id.cultureland_rb2000));
        rb3000 = ((RadioButton) findViewById(R.id.cultureland_rb3000));
        rb4000 = ((RadioButton) findViewById(R.id.cultureland_rb4000));
        rb5000 = ((RadioButton) findViewById(R.id.cultureland_rb5000));

        //교환하기 버튼선택 이벤트 등록
        ((Button)findViewById(R.id.culturelandRequest_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(selectedPrice==0){
                    Common.createDialog(CulturelandRequestActivity.this, getString(R.string.app_name).toString(),null, "상품권을 선택하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else{
                    if(pointNum<selectedPrice) {
                        Log.i("wtkim","point==>"+pointNum);
                        Log.i("wtkim","selectedPrice==>"+selectedPrice);
                        findViewById(R.id.cultureland_tvInfo0).setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.cultureland_tvInfo0)).setText("회원님의 보유포인트가 선택하신 상품권 금액보다 부족하여 교환이 불가능합니다.");
                    }else{
                        dialogType =2;
                        Common.createDialog(CulturelandRequestActivity.this, "휴대폰 번호 확인",etMobile, "이 번호로 문화상품권을 보내드릴까요?", getString(R.string.btn_cancel),getString(R.string.btn_yes), false, false);

                    }
                }
            }
        });

        rb1000.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rb1000.setChecked(true);
                rb2000.setChecked(false);
                rb3000.setChecked(false);
                rb4000.setChecked(false);
                rb5000.setChecked(false);
                selectedPrice = 1000;
                findViewById(R.id.culturelandRequest_btnNext).setBackgroundResource(R.color.primary);
            }
        });
        rb2000.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rb1000.setChecked(false);
                rb2000.setChecked(true);
                rb3000.setChecked(false);
                rb4000.setChecked(false);
                rb5000.setChecked(false);
                selectedPrice = 2000;
                findViewById(R.id.culturelandRequest_btnNext).setBackgroundResource(R.color.primary);
            }
        });
        rb3000.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rb1000.setChecked(false);
                rb2000.setChecked(false);
                rb3000.setChecked(true);
                rb4000.setChecked(false);
                rb5000.setChecked(false);
                selectedPrice = 3000;
                findViewById(R.id.culturelandRequest_btnNext).setBackgroundResource(R.color.primary);
            }
        });
        rb4000.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rb1000.setChecked(false);
                rb2000.setChecked(false);
                rb3000.setChecked(false);
                rb4000.setChecked(true);
                rb5000.setChecked(false);
                selectedPrice = 4000;
                findViewById(R.id.culturelandRequest_btnNext).setBackgroundResource(R.color.primary);
            }
        });
        rb5000.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                rb1000.setChecked(false);
                rb2000.setChecked(false);
                rb3000.setChecked(false);
                rb4000.setChecked(false);
                rb5000.setChecked(true);
                selectedPrice = 5000;
                findViewById(R.id.culturelandRequest_btnNext).setBackgroundResource(R.color.primary);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rb1000.setChecked(false);
        rb2000.setChecked(false);
        rb3000.setChecked(false);
        rb4000.setChecked(false);
        rb5000.setChecked(false);
        start(null);
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //이체 계좌 정보로드
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(dialogType==1 && result.equals("ok")){
            finish();
            intent = new Intent(CulturelandRequestActivity.this,PointActivity.class);
            startActivity(intent);
        }else if(dialogType==2 && result.equals("cancel")){
            String strRnd = Common.getDateString()+Common.getRandomString(5);
            Object[][] params = {
                    {"JOB_NUM", String.valueOf(strRnd)}
                    ,{"PRICE",Integer.toString(selectedPrice)}
                    ,{"MOBILE",String.valueOf(etMobile)}

            };
            Log.i("wtkim","JOB_NUM==>"+ String.valueOf(strRnd));
            Log.i("wtkim","PRICE==>"+selectedPrice);
            Log.i("wtkim","etMobile==>"+String.valueOf(etMobile));
            common.loadData(CALLTYPE_SAVE, getString(R.string.url_culturelandRequest), params);
        }
    }
    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }
    public void startHandler(String str){
        Log.i("wtkim","cashResultActivity==>startHandler()호출!");
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String UID = Common.getPreference(getApplicationContext(), "UID");
                    String KEY = Common.getPreference(getApplicationContext(), "KEY");
                    String email = json.getString("EMAIL");
                    String point = json.getString("POINT");
                    String mobile = json.getString("MOBILE");

                    //((TextView)findViewById(R.id.culturelandRequest_tvEmail)).setText(email);
                    ((TextView)findViewById(R.id.culturelandRequest_tvPoint)).setText(Common.getTvComma(point));
                    pointNum = Integer.parseInt(point);
                    etMobile = mobile;

                    ((TextView)findViewById(R.id.culturelandRequest_etMobile)).setText(mobile);
                }
            } else {
                Log.i("wtkim","11111111");
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err.toString(), getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtkim","222222222");
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void saveDataHandler(String str){
        Log.i("wtkim","saveDataHandler호출!");
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");

                if (result.equals("OK")) {
                    dialogType = 1;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "온라인 문화상품권 "+Common.getTvComma(String.valueOf(selectedPrice))+"원이 고객님 휴대폰 문자로 전송되었습니다.", getString(R.string.btn_ok),null, false, false);
                   /* intent = new Intent(CulturelandRequestActivity.this,PointActivity.class);
                    startActivity(intent);*/
                }
            } else {
                Log.i("wtkim","aaaaaaaaaa");
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtkim","bbbbbbbbbbbb");
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}

