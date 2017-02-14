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

import org.json.JSONObject;

public class PointCouponActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_COUPONCHECK = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String email = "";
    private String authdate = "";
    private String regist_join_channel_type = "";
    private EditText etCouponNumber;
    private int textWatcherType=0;
    private String couponNumber = "";

    private String title = "";
    private String expiry_date = "";
    private String point = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_coupon);
        setTvTitle("포인트 쿠폰 등록");
        common = new Common(this);

        etCouponNumber = (EditText)findViewById(R.id.pointCoupon_etCouponNumber);
        ((Button)findViewById(R.id.cashAuthDesc_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(couponNumber.length()<16){
                    Common.createDialog(PointCouponActivity.this, getString(R.string.app_name), null, "16자리 쿠폰번호를 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else{
                    Object[][] params = {
                             {"COUPON_NUM", couponNumber}
                            ,{"TYPE", "RETRIEVE"}
                    };
                    //쿠폰조회
                    common.loadData(CALLTYPE_COUPONCHECK, getString(R.string.url_pointCoupon), params);
                }
            }
        });
        //keyup 이벤트 추가
        etCouponNumber.addTextChangedListener(textWatcherEtCouponNumber);
        //다음 버튼 선택시 이벤트 추가


    }
    //쿠폰넘버 체크
    TextWatcher textWatcherEtCouponNumber = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 1;
            couponNumber = editable.toString();
            if(editable.length()==16){
                ((Button)findViewById(R.id.cashAuthDesc_btnNext)).setBackgroundResource(R.color.primary);
            }else{
                ((Button)findViewById(R.id.cashAuthDesc_btnNext)).setBackgroundResource(R.color.primaryDisabled);
            }
        }
    };
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
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 1){
            intent = new Intent(PointCouponActivity.this,PointCouponUseActivity.class);
            intent.putExtra("TITLE",title);
            intent.putExtra("COUPON_NUMBER",couponNumber);
            intent.putExtra("EXPIRY_DATE",expiry_date);
            intent.putExtra("POINT",point);
            startActivity(intent);
            finish();
        };
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_COUPONCHECK) couponCheckHandler(str);

    }
    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                String email = json.getString("EMAIL");
                String point = json.getString("POINT");
                String grade = json.getString("GRADE");
                String grade_label = json.getString("GRADE_LABEL");
                String recommend_grade = json.getString("RECOMMEND_GRADE");
                String recommend_grade_label = json.getString("RECOMMEND_GRADE_LABEL");
                String  googleplay_id = json.getString("GOOGLEPLAY_ID");

                String email_changable = json.getString("EMAIL_CHANGABLE");
                authdate = json.getString("AUTH_DATE");
                regist_join_channel_type = json.getString("REGIST_JOIN_CHANNEL_TYPE");
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void couponCheckHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                dialogType = 1;
                String result = json.getString("RESULT");
                title = json.getString("TITLE");
                expiry_date = json.getString("EXPIRY_DATE");
                point = json.getString("POINT");
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "쿠폰이 정상적으로 확인되었습니다.", getString(R.string.btn_ok),null, false, false);
            }else{
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
