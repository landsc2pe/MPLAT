package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class JoinCompleteActivity extends NAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    TextView tvDate, tvResult,tvWelcome = null;
    Common common = null;
    String uid = "";
    String key = "";
    String id = "";
    String agree_email_yn = "";
    String agree_sms_yn = "";
    String agree_date = "";
    String sns_id = "";
    String sns_type = "";
    String sns_email = "";
    final int CHANNEL_TYPE_MPLAT = 1;
    final int CHANNEL_TYPE_NAVER = 2;
    final int CHANNEL_TYPE_FACEBOOK = 3;
    final int CHANNEL_TYPE_KAKAO = 4;
    final int CHANNEL_TYPE_GOOGLE = 5;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_complete);

        setTvTitle("수신동의 확인");
        common = new Common(this);

        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();

        //문구 변경
        TextView tvAddInfo = (TextView)findViewById(R.id.joincomplete_tvAddInfo);
        String str = "&nbsp;추가로, <font color='#7261C5'>부가정보를 입력</font>하시면 <font color='#7261C5'>100P</font>가 적립됩니다.";
        if(Build.VERSION.SDK_INT>=24) {
            tvAddInfo.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvAddInfo.setText(Html.fromHtml(str));
        }

        uid = getIntent().getStringExtra("UID").toString();
        key = getIntent().getStringExtra("KEY").toString();
        id = getIntent().getStringExtra("ID").toString();
        agree_email_yn = getIntent().getStringExtra("AGREE_EMAIL_YN").toString();
        agree_sms_yn = getIntent().getStringExtra("AGREE_SMS_YN").toString();
        agree_date = getIntent().getStringExtra("AGREE_DATE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();


        //환영메시지
        tvWelcome = (TextView) findViewById(R.id.joincomplete_tvWelcome);
        if(!sns_type.equals(String.valueOf(CHANNEL_TYPE_MPLAT))){
            tvWelcome.setText(Html.fromHtml("<b>회원가입을 진심으로 환영합니다."));
        }else{
            tvWelcome.setText(Html.fromHtml("<b><u>"+id+"님</u></b>의 회원가입을 진심으로 환영합니다."));
        }


        TextView tvDate = (TextView)findViewById(R.id.joincomplete_tvDate);
        TextView tvResult = (TextView)findViewById(R.id.joincomplete_tvResult);
        //처리일자
        String agree_datestr = agree_date.substring(0,4)+'년'+agree_date.substring(4,agree_date.length()-2)+'월'+agree_date.substring(agree_date.length()-2)+'일';
        tvDate.setText(agree_datestr);
        //처리내용
        String agree_email_ynstr = "";
        String agree_sms_ynstr = "";
        String result = "";

        if(agree_email_yn.equals("Y")){
            agree_email_ynstr = "<font color='#7161C4'>이메일(수신동의)</font>";
        }else{
            agree_email_ynstr = "<font color='#D57A76'>이메일(수신거부)</font>";
        }

        if(agree_sms_yn.equals("Y")){
            agree_sms_ynstr = "<font color='#7161C4'>SMS(수신동의)</font>";
        }else{
            agree_sms_ynstr = "<font color='#D57A76'>SMS(수신거부)</font>";
        }
        Log.i("wtKim","agree : "+agree_email_ynstr+" "+agree_sms_ynstr);
        tvResult.setText(Html.fromHtml(agree_email_ynstr+" "+agree_sms_ynstr));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //부가정보 입력
            case R.id.joincomplete_btnAddInfo:
                intent = new Intent(JoinCompleteActivity.this,JoinAddInfoActivity.class);
                intent.putExtra("PRE_ACTIVITY","");
                startActivity(intent);
                break;
            //메인화면
            case R.id.joincomplete_btnGoMain:
                intent = new Intent(JoinCompleteActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

    }

    @Override
    public void loaddataHandler(int calltype, String str) {

    }
}
