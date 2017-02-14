package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class WithdrawCancelActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_WITHDRAWCANCEL = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
  /*  String uid = "";
    String key = "";*/
    String withdraw_date = "";
    String delete_date = "";
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cancel);
        setTvTitle("회원탈퇴 취소");
        common = new Common(this);
        intent = getIntent();
        /*uid = intent.getStringExtra("UID").toString();
        key = intent.getStringExtra("KEY").toString();*/
        withdraw_date = intent.getStringExtra("WITHDRAW_DATE").toString();
        delete_date = intent.getStringExtra("DELETE_DATE").toString();
        email = intent.getStringExtra("EMAIL").toString();

        //문구변경
        ((TextView)findViewById(R.id.withdrawCancel_tvInfo1)).setText(Html.fromHtml("<font color='#7161C4'>"+email+"</font>는 회원탈퇴를 신청한 아이디이며, 현재는 <font color='#D57A76'>탈퇴 유예기간으로 취소가 가능</font>합니다."));
        ((TextView)findViewById(R.id.withdrawCancel_tvInfo2)).setText(Html.fromHtml("<b>완전 삭제 예정일</b>"));
        ((TextView)findViewById(R.id.withdrawCancel_tvInfo3)).setText(Html.fromHtml("<b><font color='#D57A76'>"+delete_date+" 자정 이후</font></b>"));
        ((TextView)findViewById(R.id.withdrawCancel_tvInfo4)).setText(Html.fromHtml("(탈퇴 신청 및 동의일 :"+withdraw_date+")"));
        ((TextView)findViewById(R.id.withdrawCancel_tvInfo5)).setText(Html.fromHtml("완전삭제 예정일 이후에는 <font color='#D57A76'>데이터가 완전히 삭제되어 복구가 불가능</font>하며, 해당 <font color='#D57A76'>이메일로 재가입하실 수 없습니다.</font>"));

        //회원탈퇴 취소버튼 클릭시 이벤트
        ((Button)findViewById(R.id.withdrawCancel_btnCancel)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","회원탈퇴 취소 이벤트");
                common.loadData(CALLTYPE_WITHDRAWCANCEL, getString(R.string.url_withdrawCancel), null);
            }
        });
        //뒤로가기 이벤트
        ((Button)findViewById(R.id.withdrawCancel_btnBack)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","뒤로가기 이벤트");
                intent = new Intent(WithdrawCancelActivity.this,LoginActivity.class);
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

    }
    @Override
    public void dialogHandler(String result) {
        if(dialogType == 1 && result.equals("ok")){
            intent = new Intent(WithdrawCancelActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
       if (calltype == CALLTYPE_WITHDRAWCANCEL) cancelHandler(str);
    }

    public void cancelHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                String result = json.getString("RESULT");
                dialogType = 1;
                Common.setPreference(getApplicationContext(), "UID", "");
                Common.setPreference(getApplicationContext(), "KEY", "");
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "탈퇴취소가 완료되었으며, 회원님의 아이디가 복구되었습니다. 이전에 사용하시던 이메일로 다시 로그인 해주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
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
