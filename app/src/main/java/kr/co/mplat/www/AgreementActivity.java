package kr.co.mplat.www;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.reflect.Array;

public class AgreementActivity extends NAppCompatActivity implements View.OnClickListener,I_dialogdata,I_startFinish,I_loaddata{
    final int CALLTYPE_SAVE = 1;
    Intent intent = null;
    Common common = null;
    CheckBox cbAgreeall,cbAgree_1,cbAgree_2 = null;
    RadioButton rbAgeover14_1,rbAgeover14_2 = null;
    ScrollView svParent,svAgree_1,svAgree_2 = null;
    String sns_type,sns_id,sns_email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        setTvTitle("약관동의");
        common = new Common(this);
        //약관가져오기
        loadAgremenet();

        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("comm-agreementActivity","UID==>"+UID);
        Log.i("comm-agreementActivity","KEY==>"+KEY);

        //SNS 로그인 정보
        sns_id = getIntent().getStringExtra("SNS_ID").toString();
        sns_type = getIntent().getStringExtra("SNS_TYPE").toString();
        sns_email = getIntent().getStringExtra("SNS_EMAIL").toString();





        svParent  =  (ScrollView) findViewById(R.id.agreement_svParent);
        svAgree_1 =  (ScrollView) findViewById(R.id.agreement_svAgree_1);
        svAgree_2 =  (ScrollView) findViewById(R.id.agreement_svAgree_2);

        //동의1을 선택시 부모 스크롤뷰 이벤트 막는다
        svAgree_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    svParent.requestDisallowInterceptTouchEvent(false);
                }else{
                    svParent.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        //동의2를 선택시 부모 스크롤뷰 이벤트 막는다
        svAgree_2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    svParent.requestDisallowInterceptTouchEvent(false);
                }else{
                    svParent.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2){
            //14세 미만으로 선택할 경우, 팝업 후 체크 해제.
            RadioButton rbAgeover14_2 = (RadioButton)findViewById(R.id.agreement_rbAgeover14_2);
            rbAgeover14_2.setChecked(false);
            cbAgreeall.setChecked(false);
        }
    }
    public void loadAgremenet(){
        Object[][] params = {};
        common.loadData(CALLTYPE_SAVE, getString(R.string.url_agreement), params);
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
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SAVE) saveHandler(str);
    }
    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                String agreement1 = json.getString("AGREEMENT1");
                String agreement2 = json.getString("AGREEMENT2");
                TextView tvAgree_1 = (TextView)findViewById(R.id.agreement_tvAgree_1);
                TextView tvAgree_2 = (TextView)findViewById(R.id.agreement_tvAgree_2);

                if (result.equals("OK")) {
                    if(!agreement1.equals("")) tvAgree_1.setText(agreement1);
                    if(!agreement2.equals("")) tvAgree_2.setText(agreement2);
                }
            } else {
                Common.createDialog(this, getString(R.string.dial_title1).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.dial_title1).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    private int dialogType=0;
    @Override
    public void onClick(View view) {
        dialogType=0;
        //이용약관 및 개인정보 취급 이용안내 모두 동의
        cbAgreeall = (CheckBox)findViewById(R.id.agreement_cbAgreeall);
        //14세 이상 확인
        RadioGroup rgAgeover14 = (RadioGroup)findViewById(R.id.agreement_rgAgeover14);
        rbAgeover14_1 = (RadioButton)findViewById(R.id.agreement_rbAgeover14_1);
        rbAgeover14_2 = (RadioButton)findViewById(R.id.agreement_rbAgeover14_2);
        //서비스 이용약관에 동의
        cbAgree_1 = (CheckBox)findViewById(R.id.agreement_cbAgree_1);
        //개인정보 수집 및 이용에 동의
        cbAgree_2 = (CheckBox)findViewById(R.id.agreement_cbAgree_2);
        switch (view.getId()){
            //약관동의 다음버튼 선택시
            case  R.id.agreement_btnNext:
                if(rbAgeover14_1.isChecked()==false||cbAgree_1.isChecked()==false||cbAgree_2.isChecked()==false){
                    Common.createDialog(this, getString(R.string.dial_title1).toString(),null, getString(R.string.dial_msg4).toString(), getString(R.string.btn_ok),null, false, false);
                }else{
                    intent = new Intent(AgreementActivity.this,JoinMobileActivity.class);
                    //sns_id
                    intent.putExtra("SNS_ID",sns_id);
                    intent.putExtra("SNS_TYPE",sns_type);
                    intent.putExtra("SNS_EMAIL",sns_email);
                    startActivity(intent);
                }
                break;
            //약관 모두 동의 선택시
            case R.id.agreement_cbAgreeall:
                if(cbAgreeall.isChecked()){
                    rbAgeover14_1.setChecked(true);
                    cbAgree_1.setChecked(true);
                    cbAgree_2.setChecked(true);
                }else{
                    rgAgeover14.clearCheck();
                    cbAgree_1.setChecked(false);
                    cbAgree_2.setChecked(false);
                }
                break;
            case R.id.agreement_rbAgeover14_2:
                dialogType=2;
                Common.createDialog(this, getString(R.string.dial_title1).toString(),null, getString(R.string.dial_msg1).toString(), getString(R.string.btn_ok),null, false, false);
                break;
        }
    }
}
