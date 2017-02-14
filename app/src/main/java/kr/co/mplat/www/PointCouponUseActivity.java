package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class PointCouponUseActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_USE = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;

    private String title = "";
    private String expiry_date = "";
    private String point = "";
    private String coupon_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_coupon_use);
        setTvTitle("포인트 쿠폰 등록");
        common = new Common(this);
        intent = getIntent();

        title = intent.getExtras().getString("TITLE");
        expiry_date = intent.getExtras().getString("EXPIRY_DATE");
        point = intent.getExtras().getString("POINT");
        coupon_number = intent.getExtras().getString("COUPON_NUMBER");

        ((TextView)findViewById(R.id.pointCouponUse_tvCouponName)).setText(Html.fromHtml("<font color='#7161C4'>"+title+"</font>"));
        ((TextView)findViewById(R.id.pointCouponUse_tvCouponNumber)).setText(Html.fromHtml("<font color='#7161C4'>"+coupon_number+"</font>"));
        ((TextView)findViewById(R.id.pointCouponUse_tvCouponPoint)).setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(point)+"P</font>"));
        ((TextView)findViewById(R.id.pointCouponUse_tvCouponValidity)).setText(Html.fromHtml("<font color='#7161C4'>"+expiry_date+"</font>"));

        //쿠폰등록 버튼 클릭시 이벤트 추가
        ((Button)findViewById(R.id.pointCouponUse_btnRegCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Object[][] params = {
                        {"COUPON_NUM", coupon_number}
                        ,{"TYPE", "USE"}
                };
                //쿠폰조회
                common.loadData(CALLTYPE_USE, getString(R.string.url_pointCoupon), params);
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
        //기본정보 호출
        //common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(dialogType == 1 && result.equals("ok")){
            intent = new Intent(PointCouponUseActivity.this,MypageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_USE) useHandler(str);

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


            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void useHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                dialogType = 1;
                String result = json.getString("RESULT");
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "쿠폰번호 등록이 정상적으로 완료되었습니다.", getString(R.string.btn_ok),null, false, false);
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
