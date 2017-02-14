package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class AuthDescActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String pre_activity = "";
    private String pre_campaignCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_desc);
        setTvTitle("본인인증");
        common = new Common(this);

        intent = getIntent();
        pre_activity = intent.getStringExtra("PRE_ACTIVITY").toString();
        pre_campaignCode = intent.getStringExtra("CAMPAIGN_CODE").toString();

        Log.i("wtkim", "pre_activity==>" + pre_activity);
        //리뷰에서 넘어온 경우, 문구변경
        if (pre_activity.equals("ReviewDetailActivity")) {
            ((TextView) findViewById(R.id.authDesc_tvInfo1)).setText(Html.fromHtml("'블로그 및 SNS 매체 등록'을 하기 위해서는<br/>휴대폰 본인인증 절차가 반드시 필요합니다. <br/><font color='#7161C4'>본인인증 후에는 인증된 정보로 회원정보가 변경</font>됩니다.</font>"));
        } else {
            //문구변경
            ((TextView) findViewById(R.id.authDesc_tvInfo1)).setText(Html.fromHtml("<b>고객님의 휴대폰을 사용하여 본인인증 절차를 진행합니다. <font color='#7161C4'>인증 후에는 인증된 정보로 회원정보가 변경</font> 됩니다.</b>"));
            ((TextView) findViewById(R.id.authDesc_cbAgree)).setText(Html.fromHtml("<b>휴대폰 본인인증 완료 시, 인증된 정보로 회원정보가 변경됨을 확인 합니다.</b>"));
        }

        //인증확인체크시 이벤트
        final CheckBox auth_cbAgree = (CheckBox) findViewById(R.id.authDesc_cbAgree);
        auth_cbAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth_cbAgree.isChecked()) {
                    findViewById(R.id.authDesc_btnNext).setBackgroundResource(R.color.primary);
                    auth_cbAgree.setTextColor(getResources().getColor(R.color.primary));
                } else {
                    auth_cbAgree.setTextColor(getResources().getColor(R.color.primaryFont));
                    findViewById(R.id.authDesc_btnNext).setBackgroundResource(R.color.primaryDisabled);
                }
            }
        });
        //다음 버튼 클릭 이벤트
        findViewById(R.id.authDesc_btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth_cbAgree.isChecked()) {
                    finish();
                    intent = new Intent(AuthDescActivity.this, AuthWvActivity.class);
                    intent.putExtra("URL", getString(R.string.url_authwv));
                    if(pre_activity.equals("ReviewDetailActivity")){
                        intent.putExtra("PRE_ACTIVITY", "ReviewDetailActivity");
                        intent.putExtra("PRE_CAMPAIGN_CODE", pre_campaignCode);
                    }else{
                        intent.putExtra("PRE_ACTIVITY", "AuthDescActivity");
                        intent.putExtra("PRE_CAMPAIGN_CODE", pre_campaignCode);
                    }
                    startActivity(intent);
                } else {
                    Common.createDialog(AuthDescActivity.this, getString(R.string.app_name).toString(), null, "휴대폰 본인인증 안내확인에 체크해 주세요.", getString(R.string.btn_ok), null, false, false);
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
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {

    }
}
