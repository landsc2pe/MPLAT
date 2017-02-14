package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

public class CompanyActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_UPDATE= 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        setTvTitle("회사정보");
        common = new Common(this);
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_company), null);
    }
    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }
    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result       = json.getString("RESULT");
                String company_name = json.getString("COMPANY_NAME");
                String name         = json.getString("NAME");
                String num          = json.getString("NUM");
                String phone        = json.getString("PHONE");
                String mail         = json.getString("MAIL");

                //텍스트 정보변경
                ((TextView)findViewById(R.id.company_TvCompanyName)).setText(Html.fromHtml("<font color='#7161C4'>"+company_name+"</font>"));
                ((TextView)findViewById(R.id.company_Tvname)).setText(Html.fromHtml("<font color='#7161C4'>"+name+"</font>"));
                ((TextView)findViewById(R.id.company_tvNum)).setText(Html.fromHtml("<font color='#7161C4'>"+num+"</font>"));
                ((TextView)findViewById(R.id.company_tvPhone)).setText(Html.fromHtml("<font color='#7161C4'>"+phone+"</font>"));
                ((TextView)findViewById(R.id.company_tvMail)).setText(Html.fromHtml("<font color='#7161C4'>"+mail+"</font>"));
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
