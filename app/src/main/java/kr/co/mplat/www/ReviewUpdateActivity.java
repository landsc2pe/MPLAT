package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class ReviewUpdateActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private Common common = null;
    Intent intent = null;
    private int dialogType=0;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_UPDATE = 2;
    EditText etUrl = null;
    Button btnWrite = null;
    Boolean answeredCheck = false;
    String campaignCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_update);
        setTvTitle("리뷰등록");
        common = new Common(this);

        etUrl = (EditText)findViewById(R.id.reviewUpdate_etUrl);
        btnWrite = (Button)findViewById(R.id.reviewUpdate_btnWrite);
        etUrl.addTextChangedListener(textWatcherEtUrl);
        intent = getIntent();
        campaignCode = intent.getStringExtra("CAMPAIGN_CODE").toString();
        //등록완료 버튼 선택시
        btnWrite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strUrl = etUrl.getText().toString();
                Object[][] params = {
                         {"CAMPAIGN_CODE",strUrl}
                        ,{"REVIEW_URL",strUrl}
                };
                common.loadData(CALLTYPE_UPDATE, getString(R.string.url_reviewWrite), params);
            }
        });
    }

    TextWatcher textWatcherEtUrl = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ChangeNextButton();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

    public void ChangeNextButton(){
        String strUrl = etUrl.getText().toString();
        if(strUrl.equals("")){
            answeredCheck = false;
            btnWrite.setBackgroundResource(R.color.primaryDisabled);
        }else{
            answeredCheck = true;
            btnWrite.setBackgroundResource(R.color.primary);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_UPDATE) updateHandler(str);
    }

    @Override
    public void start(View view) {

    }

    public void updateHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {

            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
