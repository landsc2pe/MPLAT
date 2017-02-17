package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CashActivity extends NAppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener, I_loaddata, I_startFinish, I_dialogdata {
    Intent intent = null;
    Common common = null;
    private int dialogType = 0;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    String etPrice = "";
    String oriPrice = "";
    String bank = "";
    String name = "";
    String account_num = "";
    String cash_date = "";
    Boolean priceCheckOK = false;
    private int callType_validate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        setTvTitle("현금이체 신청");
        common = new Common(this);
        //이체금액에 포커스
        ((EditText) findViewById(R.id.cash_etPrice)).requestFocus();
        //이체안내 문구추가1
        ((TextView) findViewById(R.id.cash_tvTransferInfo1)).setText(Html.fromHtml("<font color=\"#7161C4\">최소 5,000포인트</font> 이상 부터 이체 가능합니다."));
        //이체안내 문구추가2
        //((TextView)findViewById(R.id.cash_tvTransferInfo2)).setText(Html.fromHtml("금일 신청하신 현금이체 신청은 <font color=\"#7161C4\">"+cash_date+"</font>에 등록한 계좌로 이체처리 됩니다."));
        //keyup 이벤트 추가
        ((EditText) findViewById(R.id.cash_etPrice)).addTextChangedListener(textWatcherEtPrice);
        ((EditText) findViewById(R.id.cash_etPrice)).addTextChangedListener(new Common.CustomTextWathcer((EditText) findViewById(R.id.cash_etPrice)));

        //현금이체 신청
        ((Button) findViewById(R.id.cash_btnAcouuntTransfer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(oriPrice) >= 5000) {
                    Object[][] params = {
                            {"PRICE", oriPrice}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_cashRequest), params);
                }

            }
        });
        //계좌정보 변경
        ((Button) findViewById(R.id.cash_btnChangeAccountNum)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("wtkim", "계좌정보 버튼클릭!");

            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    private int textWatcherType = 0;
    //아이디 체크
    TextWatcher textWatcherEtPrice = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("onTextChanged",charSequence.toString());
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Log.i("beforeTextChanged",charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            textWatcherType = 1;
            etPrice = editable.toString();
            if (etPrice.length() > 0) {
                oriPrice = etPrice.replaceAll(",", "");

                //id 정규식체크후, 맞을경우 db에서 한번더 체크
                if (Integer.parseInt(oriPrice) >= 5000) {
                    priceCheckOK = true;
                    ((Button) findViewById(R.id.cash_btnAcouuntTransfer)).setBackgroundResource(R.color.primary);
                    /*Object[][] params = {
                            {"PRICE", etPrice.toString()}
                    };
                    common.loadData(callType_validate, getString(R.string.url_cashRequest), params);*/
                } else {
                    priceCheckOK = false;
                    ((Button) findViewById(R.id.cash_btnAcouuntTransfer)).setBackgroundResource(R.color.primaryDisabled);
                }
            }
        }
    };

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
        Log.i("wtkim", "start! 호출!");
        //네트워크 상태 확인
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //이체 계좌 정보로드.
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_cash), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }

    public void startHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                String email = json.getString("EMAIL");
                String point = json.getString("POINT");
                bank = json.getString("BANK");
                name = json.getString("NAME");
                account_num = json.getString("ACCOUNT_NUM");
                cash_date = json.getString("CASH_DATE");


                if (result.equals("OK")) {
                    dialogType = 1;
                    //Common.createDialog(this, getString(R.string.app_name).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
                    //((TextView)findViewById(R.id.cash_tvEmail)).setText(email);

                    //이체안내 문구추가2
                    ((TextView) findViewById(R.id.cash_tvTransferInfo2)).setText(Html.fromHtml("금일 신청하신 현금이체 신청은 <font color=\"#7161C4\">" + cash_date + "</font>에 등록한 계좌로 이체처리 됩니다."));
                    ((TextView) findViewById(R.id.cash_tvPoint)).setText(Common.getTvComma(point));
                    ((EditText) findViewById(R.id.cash_etBank)).setText(bank);
                    ((EditText) findViewById(R.id.cash_etAccountNum)).setText(account_num);
                    ((EditText) findViewById(R.id.cash_etName)).setText(name);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    public void saveDataHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");

                if (result.equals("OK")) {
                    dialogType = 1;
                    intent = new Intent(CashActivity.this, CashResultActivity.class);
                    intent.putExtra("PRICE", etPrice);
                    intent.putExtra("BANK", bank);
                    intent.putExtra("ACCOUNT_NUM", account_num);
                    intent.putExtra("NAME", name);
                    intent.putExtra("CASH_DATE", cash_date);
                    startActivity(intent);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }
}
