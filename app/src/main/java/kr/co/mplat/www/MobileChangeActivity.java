package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class MobileChangeActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    String mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_change);
        setTvTitle("휴대폰 번호 변경");
        common = new Common(this);
        ((Button)findViewById(R.id.mobileChange_btnSendAuthCode)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mobile = ((EditText)findViewById(R.id.mobileChange_etMobile)).getText().toString();
                if(mobile.length()<10||mobile.length()>11){
                    dialogType = 0;
                    Common.createDialog(MobileChangeActivity.this, getString(R.string.app_name).toString(),null, "휴대폰 번호를 올바르게 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else{
                    dialogType = 1;
                    Common.createDialog(MobileChangeActivity.this, getString(R.string.app_name).toString(),mobile,"이 번호로 코드를 보내드릴까요?", getString(R.string.btn_ok),getString(R.string.btn_no), false, false);
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
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 1){
            Object[][] params = {
                    {"MOBILE",mobile}
            };
            common.loadData(CALLTYPE_LOAD, getString(R.string.url_mobileChange), params);
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) authcodeHandler(str);
    }

    public void authcodeHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                intent = new Intent(MobileChangeActivity.this,MobileChangeRequestActivity.class);
                intent.putExtra("MOBILE_TEL",mobile);
                startActivity(intent);
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
