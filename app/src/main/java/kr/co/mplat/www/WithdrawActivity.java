package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONObject;

public class WithdrawActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_WITHDRAW = 9;

    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    Boolean flag_cbAgree = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        setTvTitle("회원탈퇴 신청");
        common = new Common(this);

        ((TextView)findViewById(R.id.withraw_tvTitle11)).setText(Html.fromHtml("<b>1. </b>"));
        ((TextView)findViewById(R.id.withraw_tvTitle12)).setText(Html.fromHtml("<b>탈퇴 후 회원정보 삭제</b>"));
        ((TextView)findViewById(R.id.withraw_tvdesc11)).setText(Html.fromHtml("탈퇴 신청후 <font color='#D57A76'>7일 후 모든 회원정보가 삭제</font> 됩니다. (유예기간7일)"));
        ((TextView)findViewById(R.id.withraw_tvdesc12)).setText(Html.fromHtml("탈퇴 신청후 7일 이전에는 탈퇴를 취소 할 수 있으며, <font color='#D57A76'>7일 경과 후 삭제된 회원정보는 복구되지 않습니다.</font>"));
        ((TextView)findViewById(R.id.withraw_tvTitle21)).setText(Html.fromHtml("<b>2. </b>"));
        ((TextView)findViewById(R.id.withraw_tvTitle22)).setText(Html.fromHtml("<b>탈퇴 및 재가입 제한, 아이디 재사용 불가</b>"));
        ((TextView)findViewById(R.id.withraw_tvdesc21)).setText(Html.fromHtml("서비스 부정이용의 사유로 본인이 탈퇴하거나, 회사에서 탈퇴처리한 경우에는 재가입이 불가능 할 수 있습니다."));
        ((TextView)findViewById(R.id.withraw_tvdesc22)).setText(Html.fromHtml("<font color='#D57A76'>탈퇴한 아이디는 본인 및 타인 모두 재사용이 불가능</font>합니다."));
        ((TextView)findViewById(R.id.withraw_tvTitle31)).setText(Html.fromHtml("<b>3. </b>"));
        ((TextView)findViewById(R.id.withraw_tvTitle32)).setText(Html.fromHtml("<b>탈퇴 후 보유하는 정보</b>"));
        ((TextView)findViewById(R.id.withraw_tvdesc31)).setText(Html.fromHtml("부정 이용자의 재가입을 방지하기 위한 정보"));
        ((TextView)findViewById(R.id.withraw_tvdesc32)).setText(Html.fromHtml("쿠폰, 리뷰, 미션, 설문조사 등 서비스 이용내역"));
        ((TextView)findViewById(R.id.withraw_tvdesc33)).setText(Html.fromHtml("포인트 적립 및 사용내역"));
        ((TextView)findViewById(R.id.withraw_tvdesc34)).setText(Html.fromHtml("고객센터 문의내역"));

        ((CheckBox)findViewById(R.id.withraw_cbAgree)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.getId()==R.id.withraw_cbAgree){
                    if(b){
                        Log.i("wtkim","눌림!");
                        flag_cbAgree = true;

                    }else{
                        Log.i("wtkim","안눌림!");
                        flag_cbAgree = false;
                    }
                }
            }
        });

        //회원탈퇴 버튼 선택시
        ((Button)findViewById(R.id.withraw_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(flag_cbAgree==false){
                    Common.createDialog(WithdrawActivity.this, getString(R.string.app_name).toString(),null, "내용확인에 동의하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else{
                    dialogType = 9;
                    Common.createDialog(WithdrawActivity.this, "탈퇴확인",null, "정말로 회원탈퇴를 진행하시겠습니까? 신청 후 7일 이후에는 아이디가 완전히 삭제되며, 재가입은 불가능합니다.", getString(R.string.btn_ok),getString(R.string.btn_cancel), false, false);

                }
            }
        });
        //뒤로가기 선택시
        ((Button)findViewById(R.id.withraw_btnCancel)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(WithdrawActivity.this,MypageActivity.class);
                startActivity(intent);
                finish();
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
        Object[][] params = {};
        //common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), params);
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 9){
            //intent = new Intent(WithdrawActivity.this,LoginActivity.class);
            //startActivity(intent);
            common.loadData(CALLTYPE_WITHDRAW, getString(R.string.url_withdraw), null);
        }

        if(result.equals("ok") && dialogType == 1){
            intent = new Intent(WithdrawActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_WITHDRAW) withdrawHandler(str);

    }
    public void withdrawHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                dialogType = 1;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "회원탈퇴 신청이 완료되었습니다. 탈퇴 신청 후 7일 이후에 완전히 회원정보가 삭제되며 복구가 불가능합니다.(7일이내 복구 가능)", getString(R.string.btn_ok),null, false, false);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        return ret;
    }
}
