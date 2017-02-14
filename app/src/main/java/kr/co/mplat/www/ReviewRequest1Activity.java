package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ReviewRequest1Activity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    CheckBox check1 = null;
    CheckBox check2 = null;
    Boolean cbCheck1;
    Boolean cbCheck2;
    String campaignCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_request1);
        setTvTitle("리뷰신청(1/3)");
        campaignCode = getIntent().getStringExtra("CAMPAIGN_CODE").toString();
        //문구변경
        ((TextView)findViewById(R.id.reviewRequest1_info12)).setText(Html.fromHtml("<font color='#7161C4'>리뷰어 선정결과 및 진행 안내는 개인정보에 입력하신 이메일과 휴대전화번호</font>로 알려드립니다. (리뷰 상품 배송 주소 및 휴대전화번호와 무관합니다)</font>"));
        ((TextView)findViewById(R.id.reviewRequest1_info21)).setText(Html.fromHtml("<font color='#D57A76'>- </font>"));
        ((TextView)findViewById(R.id.reviewRequest1_info22)).setText(Html.fromHtml("<font color='#D57A76'>리뷰 등록 기간에 리뷰 미 등록시 추후 리뷰어 선정에 제한</font>을 받습니다."));
        ((TextView)findViewById(R.id.reviewRequest1_info52)).setText(Html.fromHtml("참여 후 작성되는 리뷰의 <font color='#D57A76'>유지기간은 최소 6개월</font> 입니다."));
        check1 = (CheckBox)findViewById(R.id.reviewRequest1_check1);
        check2 = (CheckBox)findViewById(R.id.reviewRequest1_check2);
        ((Button)findViewById(R.id.reviewRequest1_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(check1.isChecked()==false||check2.isChecked()==false){
                    Common.createDialog(ReviewRequest1Activity.this, "MPLAT",null, "리뷰 신청 약관에 모두 동의하지 않으시면 리뷰를 신청하실 수 없습니다.", getString(R.string.btn_ok),null, false, false);
                }else{
                    intent = new Intent(ReviewRequest1Activity.this,ReviewRequest2Activity.class);
                    intent.putExtra("CAMPAIGN_CODE",campaignCode);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {

    }

    @Override
    public void start(View view) {

    }
}
