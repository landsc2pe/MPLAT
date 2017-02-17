package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class CashResultActivity extends NAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    Common common = null;
    private int dialogType = 0;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_result);
        setTvTitle("현금이체 결과 확인");
        common = new Common(this);
        ((Button)findViewById(R.id.cash_btnBack)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(CashResultActivity.this,PointActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void dialogHandler(String result) {

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
        //이체 계좌 정보로드
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }

    public void startHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String UID = Common.getPreference(getApplicationContext(), "UID");
                    String KEY = Common.getPreference(getApplicationContext(), "KEY");
                    dialogType = 1;
                    String email = json.getString("EMAIL");
                    String point = json.getString("POINT");
                    String mobile = json.getString("MOBILE");
                    String price = getIntent().getStringExtra("PRICE").toString();
                    String bank = getIntent().getStringExtra("BANK").toString();
                    String accountNum = getIntent().getStringExtra("ACCOUNT_NUM").toString();
                    String name = getIntent().getStringExtra("NAME").toString();
                    String cashDate = getIntent().getStringExtra("CASH_DATE").toString();

                    //Common.createDialog(this, getString(R.string.app_name).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
                    ((TextView)findViewById(R.id.cashResult_tvEmail)).setText(email);
                    ((TextView)findViewById(R.id.cashResult_tvPoint)).setText(Common.getTvComma(point));
                    ((EditText)findViewById(R.id.cashResult_etPrice)).setText(price);
                    ((EditText)findViewById(R.id.cashResult_etBank)).setText(bank);
                    ((EditText)findViewById(R.id.cashResult_etAccountNum)).setText(accountNum);
                    ((EditText)findViewById(R.id.cashResult_etName)).setText(name);
                    ((TextView)findViewById(R.id.cashResult_tvDate)).setText(cashDate);

                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err.toString(), getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void saveDataHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");

                if (result.equals("OK")) {
                    dialogType = 1;
                    intent = new Intent(CashResultActivity.this,PointActivity.class);
                    startActivity(intent);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}

