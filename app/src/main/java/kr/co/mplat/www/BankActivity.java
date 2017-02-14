package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class BankActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,TextView.OnEditorActionListener, I_loaddata,I_startFinish,I_dialogdata {
    private Common common = null;
    Intent intent = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    Spinner spin_bank = null;
    private int dialogType=0;

    ArrayList<String> banks = new ArrayList<>();    //은행
    JSONArray ary_banks = new JSONArray();          //은행
    String strBanks = "";
    String strName = "";        //예금주
    String strBankCode = "";    //은행코드
    String strAccountNum = "";  //계좌번호

    Boolean bankCheckOK = false;
    Boolean nameCheckOK = false;
    Boolean accountNumOK = false;

    private String pre_activity = "";
    //불러오기 index 저장
    int banks_selectedIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        setTvTitle("계좌번호 등록");
        common = new Common(this);

        intent=new Intent(this.getIntent());
        if(intent.hasExtra("PRE_ACTIVITY")){
            pre_activity = intent.getExtras().getString("PRE_ACTIVITY");
        }

        spin_bank = (Spinner) findViewById(R.id.bank_spBank);
        //spin 이벤트 등록
        spin_bank.setOnItemSelectedListener(this);

        //다음버튼 선택시
        findViewById(R.id.bank_btnNext).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(bankCheckOK && nameCheckOK && accountNumOK){
                    Object[][] params = {
                             {"NAME",strName}
                            ,{"BANK_CODE",strBanks}
                            ,{"ACCOUNT_NUM",strAccountNum}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_bankAccount), params);

                }else{
                    Common.createDialog(BankActivity.this, getString(R.string.app_name).toString(),null, "계좌등록 정보를 올바르게 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }
            }
        });

        //keyup 이벤트 추가
        ((EditText)findViewById(R.id.bank_etName)).addTextChangedListener(textWatcherEtName);
        ((EditText)findViewById(R.id.bank_etAccountNum)).addTextChangedListener(textWatcherEtAccountNum);
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
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try{
            switch (adapterView.getId()){
                case R.id.bank_spBank:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_banks.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){
                        strBanks = "";
                        bankCheckOK=false;
                    }else{
                        strBanks = object.get("CODE").toString();
                        bankCheckOK=true;
                    }
                    //모두 입력,선택하였을 경우, 등록버튼 활성화
                    if(bankCheckOK && nameCheckOK && accountNumOK){
                        ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primary);
                    }else{
                        ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primaryDisabled);
                    }
                    Log.i("wtkim","bankCheckOK==>"+bankCheckOK);
                    break;
            }
        }catch (Exception e){
            Log.i("wtkim","error :"+e.toString());
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    //예금주 이름
    private int textWatcherType=0;
    TextWatcher textWatcherEtName = new TextWatcher() {
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
            strName = editable.toString();
            if(strName.length()>0){
                nameCheckOK = true;
            }
            //모두 입력,선택하였을 경우, 등록버튼 활성화
            if(bankCheckOK && nameCheckOK && accountNumOK){
                ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primary);
            }else{
                ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primaryDisabled);
            }

            Log.i("wtkim","strName==>"+strName);

        }
    };
    //계좌번호
    TextWatcher textWatcherEtAccountNum = new TextWatcher() {
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
            textWatcherType = 2;
            strAccountNum = editable.toString();
            accountNumOK = Common.validateAccountNum(strAccountNum);

            //모두 입력,선택하였을 경우, 등록버튼 활성화
            if(bankCheckOK && nameCheckOK && accountNumOK){
                ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primary);
            }else{
                ((Button)findViewById(R.id.bank_btnNext)).setBackgroundResource(R.color.primaryDisabled);
            }
        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void dialogHandler(String result) {
        Log.i("wtkim","pre_activity==>"+pre_activity);
        if(result.equals("ok") && dialogType==1 && pre_activity.equals("CashAuthDescActivity")){
            intent = new Intent(BankActivity.this,MypageActivity.class);
            startActivity(intent);
            finish();
        }else if(result.equals("ok") && dialogType==1 && pre_activity.equals("")) {
            intent = new Intent(BankActivity.this, PointActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }

    public void saveDataHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    dialogType = 1;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
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

    public void startHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            ary_banks = json.getJSONArray("BANK");
            String name = json.getString("NAME");
            String bank_code = json.getString("BANK_CODE");
            String account_num = json.getString("ACCOUNT_NUM");

            Log.i("wtkim","name==>"+name);
            Log.i("wtkim","bank_code==>"+bank_code);
            Log.i("wtkim","account_num==>"+account_num);

            int i;
            banks = new ArrayList<>();
            banks.add("선택");
            for (i = 0; i < ary_banks.length(); i++){
                banks.add(((JSONObject) ary_banks.get(i)).getString("LABEL"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, banks);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spin_bank.setAdapter(aa);
            }

            //기존값이 있을경우 값 불러옴
            if(!name.equals("") || !bank_code.equals("") || !account_num.equals("")){
                Log.i("wktim","문구변경하는곳");
                BankActivity.this.setTvTitle("계좌번호 변경");
                ((TextView)findViewById(R.id.bank_tvInfo)).setText("등록된 이체 계좌번호를 변경할 수 있습니다.");
            }
            ((EditText)findViewById(R.id.bank_etName)).setText(name);
            ((EditText)findViewById(R.id.bank_etAccountNum)).setText(account_num);


            for(int j=0;j<ary_banks.length();j++){
                String code = ((JSONObject) ary_banks.get(j)).getString("CODE");
                //인덱스 저장
                if(code.equals(bank_code)){banks_selectedIdx = j+1;}
            }

            ((Spinner)findViewById(R.id.bank_spBank)).setSelection(banks_selectedIdx);

        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }*/
}
