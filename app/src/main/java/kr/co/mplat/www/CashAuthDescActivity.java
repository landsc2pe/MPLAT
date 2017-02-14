package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

public class CashAuthDescActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata {
    private Common common = null;
    Intent intent = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_auth_desc);
        setTvTitle("본인인증");
        common = new Common(this);
        //인증확인체크시 이벤트
        final CheckBox auth_cbAgree = (CheckBox)findViewById(R.id.cashAuthDesc_cbAgree);
        auth_cbAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(auth_cbAgree.isChecked()){
                    findViewById(R.id.cashAuthDesc_btnNext).setBackgroundResource(R.color.primary);
                    auth_cbAgree.setTextColor(getResources().getColor(R.color.primary));
                }else{
                    auth_cbAgree.setTextColor(getResources().getColor(R.color.primaryFont));
                    findViewById(R.id.cashAuthDesc_btnNext).setBackgroundResource(R.color.primaryDisabled);
                }
            }
        });
        //다음 버튼 클릭 이벤트
        findViewById(R.id.cashAuthDesc_btnNext).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(auth_cbAgree.isChecked()){
                    finish();
                    intent = new Intent(CashAuthDescActivity.this,AuthWvActivity.class);
                    intent.putExtra("URL",getString(R.string.url_authwv));
                    intent.putExtra("PRE_ACTIVITY","CashAuthDescActivity");
                    startActivity(intent);
                }else{
                    Common.createDialog(CashAuthDescActivity.this, getString(R.string.app_name).toString(),null, "휴대폰 본인인증 안내확인에 체크해 주세요.", getString(R.string.btn_ok),null, false, false);
                }
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_bank), null);
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {

    }
}
