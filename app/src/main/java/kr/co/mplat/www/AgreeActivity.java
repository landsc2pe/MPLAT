package kr.co.mplat.www;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class AgreeActivity extends NAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    CheckBox agree_cbAgreeEmail,agree_cbAgreeSMS = null;
    final int CALLTYPE_AGREE = 1;
    Common common = null;
    String mobile = "";
    String joinQueueSeq = "";
    String sns_id = "";
    String sns_type = "";
    String sns_email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
        setTvTitle("수신동의 확인");
        common = new Common(this);
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-agreeActivity","UID==>"+UID);
        Log.i("comm-agreeActivity","KEY==>"+KEY);

        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();


        //문구 변경
        TextView tvAddInfo = (TextView)findViewById(R.id.agree_tvTitle);
        String str = "&nbsp;이메일, SMS 문자 수신에 동의하시면 엠플랫에서 제공하는 각종 <font color='#7261C5'>프로모션과 이벤트 정보</font>를 받아보실 수 있습니다.";
        if(Build.VERSION.SDK_INT>=24) {
            tvAddInfo.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvAddInfo.setText(Html.fromHtml(str));
        }

        agree_cbAgreeEmail = (CheckBox) findViewById(R.id.agree_cbAgreeEmail);
        agree_cbAgreeSMS = (CheckBox) findViewById(R.id.agree_cbAgreeSMS);
        mobile = getIntent().getStringExtra("MOBILE_TEL").toString();
        joinQueueSeq = getIntent().getStringExtra("JOIN_QUEUE_SEQ").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //수신동의 선택 -> 수신동의 확인
            case R.id.agree_btnNext:
                String cbAgreeEmail = "N";
                String cbAgreeSMS = "N";
                if(agree_cbAgreeEmail.isChecked()) cbAgreeEmail = "Y";
                if(agree_cbAgreeSMS.isChecked()) cbAgreeSMS = "Y";
                Log.i("wtkim","cbAgreeEmail==>"+cbAgreeEmail);
                Log.i("wtkim","cbAgreeSMS==>"+cbAgreeSMS);
                Object[][] params = {
                         {"MOBILE", mobile}
                        ,{"JOIN_QUEUE_SEQ", joinQueueSeq}
                        ,{"AGREE_EMAIL_YN", cbAgreeEmail}
                        ,{"AGREE_SMS_YN", cbAgreeSMS}
                };
                common.loadData(CALLTYPE_AGREE, getString(R.string.url_agree), params);
            break;
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_AGREE) saveHandler(str);
    }
    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                String uid = json.getString("UID");
                String key = json.getString("KEY");
                String id = json.getString("ID");
                String agree_email_yn = json.getString("AGREE_EMAIL_YN");
                String agree_sms_yn = json.getString("AGREE_SMS_YN");
                String agree_date = json.getString("AGREE_DATE");

                if (result.equals("OK")) {
                    Common.setPreference(getApplicationContext(), "UID", uid);
                    Common.setPreference(getApplicationContext(), "KEY", key);

                    intent = new Intent(AgreeActivity.this, JoinCompleteActivity.class);
                    intent.putExtra("UID",uid);
                    intent.putExtra("KEY",key);
                    intent.putExtra("ID",id);
                    intent.putExtra("AGREE_EMAIL_YN",agree_email_yn);
                    intent.putExtra("AGREE_SMS_YN",agree_sms_yn);
                    intent.putExtra("AGREE_DATE",agree_date);
                    intent.putExtra("SNS_ID",sns_id);
                    intent.putExtra("SNS_TYPE",sns_type);
                    intent.putExtra("SNS_EMAIL",sns_email);


                    startActivity(intent);
                }
            } else {
                Common.createDialog(this, getString(R.string.dial_title3).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.dial_title3).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
            //Log.d("wtKim", e.toString());
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
    }

    @Override
    public void dialogHandler(String result) {

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
}
