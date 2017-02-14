package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QnaWriteActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    String email = "";
    JSONArray ary_qna_codes = new JSONArray(); //qna code
    ArrayList<String> qna_codes = new ArrayList<String>();
    String strQnacode = "";
    Spinner spin_qnacode = null;
    EditText etEmail = null;
    EditText etTitle = null;
    EditText etContent = null;
    //불러오기 index 저장
    int qnacode_selectedIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_write);
        setTvTitle("문의하기");
        common = new Common(this);

        spin_qnacode = (Spinner)findViewById(R.id.qnaWrite_spQnaCode);
        etEmail = (EditText)findViewById(R.id.qnaWrite_etEmail);
        etTitle = (EditText)findViewById(R.id.qnaWrite_etTitle);
        etContent = (EditText) findViewById(R.id.qnaWrite_etContent);

        //문구변경
        ((TextView)findViewById(R.id.qnaWrite_tvInfo)).setText(Html.fromHtml("문의내용은 <font color='#7161C4'>문의내역 조회 메뉴에서 확인</font>하실 수 있습니다.</font>"));
        //스피너 이벤트 등록
        spin_qnacode.setOnItemSelectedListener(this);
        //다음버튼 선택 이벤트 등록
        ((Button)findViewById(R.id.qnaWrite_btnNext)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strEmail = etEmail.getText().toString();
                String strTitle = etTitle.getText().toString();
                String strContent = etContent.getText().toString();

                if(strQnacode.equals("")){
                    Common.createDialog(QnaWriteActivity.this, getString(R.string.app_name).toString(),null, "문의분야를 선택하여 주세요.", getString(R.string.btn_ok),null, false, false);
                }else if(strEmail.equals("")){
                    Common.createDialog(QnaWriteActivity.this, getString(R.string.app_name).toString(),null, "이메일을 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                    etEmail.requestFocus();
                }else if(strTitle.equals("")){
                    Common.createDialog(QnaWriteActivity.this, getString(R.string.app_name).toString(),null, "제목을 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                    etTitle.requestFocus();
                }else if(strContent.equals("")){
                    Common.createDialog(QnaWriteActivity.this, getString(R.string.app_name).toString(),null, "내용을 입력하여 주세요.", getString(R.string.btn_ok),null, false, false);
                    etContent.requestFocus();
                }else{
                    Object[][] params = {
                            {"QNA_CODE",strQnacode}
                            ,{"TITLE",strTitle}
                            ,{"QUESTION",strContent}
                            ,{"EMAIL",strEmail}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_qnaWrite), params);
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_qnaType), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveHandler(str);

    }
    //joinAddinfo
    public void loadHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    //이메일 주소 입력
                    email = json.getString("EMAIL");
                    //spiner 정보로드
                    ary_qna_codes =  json.getJSONArray("QNA_CODES");
                    int i;
                    qna_codes = new ArrayList<String>();

                    qna_codes.add("전체");
                    for(i=0;i<ary_qna_codes.length();i++){
                        qna_codes.add(((JSONObject)ary_qna_codes.get(i)).getString("LABEL"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.spinner_item,qna_codes);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spin_qnacode.setAdapter(aa);
                    }
                    //이메일 주소 입력
                    if(!email.equals("")){
                        etEmail.setText(email);
                        etEmail.setSelection(email.length());
                    }

                   //
                    //Common.createDialog(this, getString(R.string.app_name).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
                }else if(result.equals("LOGOUT")){
                    intent = new Intent(QnaWriteActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void saveHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    dialogType = 2;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "정상적으로 1:1문의가 접수되었습니다.\n빠른 시일 내에 답변 드릴 수 있도록 하겠습니다. 감사합니다.", getString(R.string.btn_ok),null, true, false);
                }
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum = 0;
        try{
            switch (adapterView.getId()){
                case R.id.qnaWrite_spQnaCode:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_qna_codes.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strQnacode = "";}else{ strQnacode = object.get("CODE").toString(); }
                    break;
            }
        }catch(Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2) {
            intent = new Intent(QnaWriteActivity.this, QnaActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
