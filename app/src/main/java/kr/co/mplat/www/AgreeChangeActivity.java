package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

public class AgreeChangeActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_UPDATE= 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String email = "";
    private String authdate = "";
    private String regist_join_channel_type = "";
    boolean isChecked_SwcAgreeEmail;
    boolean isChecked_SwcAgreeSms;
    boolean isChecked_SwcAgreeNotice;
    boolean isChecked_SwcAgreeEvent;
    Switch swcAgreeEmail = null;
    Switch swcAgreeSms = null;
    Switch swcAgreeNotice = null;
    Switch swcAgreeEvent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_change);
        setTvTitle("수신동의 및 알림 상태 변경");
        common = new Common(this);
        //문구변경
        ((TextView)findViewById(R.id.agreeChange_tvTitle1)).setText(Html.fromHtml("<b>수신동의 상태 변경</b>"));
        ((TextView)findViewById(R.id.agreeChange_tvTitle2)).setText(Html.fromHtml("<b>알림 상태 변경</b>"));

        //스위치
        swcAgreeEmail = (Switch)findViewById(R.id.agreechange_swcAgreeEmail);
        swcAgreeSms = (Switch)findViewById(R.id.agreechange_swcAgreeSms);
        swcAgreeNotice = (Switch)findViewById(R.id.agreechange_swcAgreeNotice);
        swcAgreeEvent = (Switch)findViewById(R.id.agreechange_swcAgreeEvent);

        swcAgreeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogType = 1;//이메일
                String value = "";
                if(((Switch) findViewById(R.id.agreechange_swcAgreeEmail)).isChecked()){value = "Y";}else{value = "N";}
                Object[][] params = {
                        {"AGREE_TYPE", "EMAIL"}
                        ,{"VALUE", value}
                };
                Log.i("wtkim","value==>"+value);
                //쿠폰조회
                common.loadData(CALLTYPE_UPDATE, getString(R.string.url_agreeChange), params);

            }
        });

        swcAgreeSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogType = 2;//SMS
                String value = "";
                if(((Switch) findViewById(R.id.agreechange_swcAgreeSms)).isChecked()){value = "Y";}else{value = "N";}
                Object[][] params = {
                        {"AGREE_TYPE", "SMS"}
                        ,{"VALUE", value}
                };
                Log.i("wtkim","value==>"+value);
                //쿠폰조회
                common.loadData(CALLTYPE_UPDATE, getString(R.string.url_agreeChange), params);
            }
        });

        swcAgreeNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogType = 3;//공지사항
                String value = "";
                if(((Switch) findViewById(R.id.agreechange_swcAgreeNotice)).isChecked()){value = "Y";}else{value = "N";}
                Object[][] params = {
                        {"AGREE_TYPE", "NOTICE"}
                        ,{"VALUE", value}
                };
                Log.i("wtkim","value==>"+value);
                //쿠폰조회
                common.loadData(CALLTYPE_UPDATE, getString(R.string.url_agreeChange), params);
            }
        });

        swcAgreeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogType = 4;//이벤트
                String value = "";
                if(((Switch) findViewById(R.id.agreechange_swcAgreeEvent)).isChecked()){value = "Y";}else{value = "N";}
                Object[][] params = {
                        {"AGREE_TYPE", "EVENT"}
                        ,{"VALUE", value}
                };
                Log.i("wtkim","value==>"+value);
                //쿠폰조회
                common.loadData(CALLTYPE_UPDATE, getString(R.string.url_agreeChange), params);
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_agreeInfo), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_UPDATE) updateHandler(str);

    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result           = json.getString("RESULT");
                String agree_email      = json.getString("AGREE_EMAIL");
                String agree_email_date = json.getString("AGREE_EMAIL_DATE");
                String agree_sms        = json.getString("AGREE_SMS");
                String agree_sms_date   = json.getString("AGREE_SMS_DATE");
                String noti_notice      = json.getString("NOTI_NOTICE");
                String noti_event       = json.getString("NOTI_EVENT");

                //문구변경
                if(!agree_email_date.equals("")){
                    ((TextView)findViewById(R.id.agreeChage_tvAgreeEmail)).setText(Html.fromHtml("이메일 수신 동의 <font color='#7161C4'>(최종 변경 : "+agree_email_date.toString()+")"));
                }
                if(!agree_sms_date.equals("")){
                    ((TextView)findViewById(R.id.agreeChage_tvAgreeSms)).setText(Html.fromHtml("SMS 수신동의 <font color='#7161C4'>(최종변경 : "+agree_sms_date+"</font>)"));
                }
                if(agree_email.equals("Y")){ swcAgreeEmail.setChecked(true); }else{ swcAgreeEmail.setChecked(false); }
                if(agree_sms.equals("Y")){ swcAgreeSms.setChecked(true); }else{ swcAgreeSms.setChecked(false); }
                if(noti_notice.equals("Y")){ swcAgreeNotice.setChecked(true); }else{ swcAgreeNotice.setChecked(false); }
                if(noti_event.equals("Y")){ swcAgreeEvent.setChecked(true); }else{ swcAgreeEvent.setChecked(false); }

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void updateHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                if(dialogType==1){
                    if(((Switch) findViewById(R.id.agreechange_swcAgreeEmail)).isChecked()){
                        Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "이메일 수신 상태가 수신동의로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }else{
                        Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "이메일 수신 상태가 수신거부로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }
                }else if(dialogType==2){
                    if(((Switch) findViewById(R.id.agreechange_swcAgreeSms)).isChecked()){
                        Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "SMS 수신 상태가 수신동의로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }else{
                        Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "SMS 수신 상태가 수신거부로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }
                }else if(dialogType==3){
                    if(((Switch) findViewById(R.id.agreechange_swcAgreeNotice)).isChecked()){
                        //Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "공지사항 수신 상태가 수신동의로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }else{
                        //Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "공지사항 수신 상태가 수신거부로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }
                }else if(dialogType==4){
                    if(((Switch) findViewById(R.id.agreechange_swcAgreeEvent)).isChecked()){
                        //Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "이벤트 및 광고 수신 상태가 수신동의로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }else{
                        //Common.createDialog(AgreeChangeActivity.this, getString(R.string.app_name).toString(),null, "이벤트 및 광고 수신 상태가 수신거부로 변경되었습니다.\n\n변경일자:"+Common.getDateString("yyyy-MM-dd"), getString(R.string.btn_ok),null, false, false);
                    }
                }
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    @Override
    public void dialogHandler(String result) {

    }
}
