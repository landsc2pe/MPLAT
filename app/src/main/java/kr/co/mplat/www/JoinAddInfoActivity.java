package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  회원가입 안내 페이지
 */

public class JoinAddInfoActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,RadioGroup.OnCheckedChangeListener,I_loaddata,I_startFinish,I_dialogdata {

    private Common common = null;
    Intent intent = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private final int CALLTYPE_INFOCHECK = 3;
    RadioButton rbPhoneType_1,rbPhoneType_2,rbPhoneType_3,rbWedding_1,rbWedding_2 = null;
    RadioGroup rgMobile,rgWedding = null;
    Spinner spin_telecom, spin_os, spin_area, spin_job = null;
    private int dialogType=0;


    ArrayList<String> telecoms = new ArrayList<>(); //이동통신사
    ArrayList<String> oss = new ArrayList<>();      //os
    ArrayList<String> areas = new ArrayList<>();    //AREA
    ArrayList<String> jobs = new ArrayList<>();     //JOB

    JSONArray ary_telecoms = new JSONArray();      //이동통신사
    JSONArray ary_oss = new JSONArray();            //os
    JSONArray ary_areas = new JSONArray();          //AREA
    JSONArray ary_jobs = new JSONArray();           //JOB

    String strTelecom = "";
    String strPhoneType = "";
    String strOs = "";
    String strWedding = "";
    String strArea = "";
    String strJob = "";

    String pre_activity = "";

    //불러오기 index 저장
    int telecoms_selectedIdx = 0;
    int areas_selectedIdx = 0;
    int jobs_selectedIdx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_add_info);

        setTvTitle("부가정보 입력");
        common = new Common(this);
        //저장
        strPhoneType = "1";
        strOs = "1";
        //
        spin_telecom = (Spinner)findViewById(R.id.joinaddinfo_spTelecom);
        spin_os = (Spinner)findViewById(R.id.joinaddinfo_spOS);
        spin_area = (Spinner)findViewById(R.id.joinaddinfo_spAREA);
        spin_job = (Spinner)findViewById(R.id.joinaddinfo_spJOB);

        rgMobile = (RadioGroup) findViewById(R.id.joinaddinfo_rgMobile);
        rgWedding = (RadioGroup) findViewById(R.id.joinaddinfo_rgWedding);

        //라디오 그룹 이벤트 등록
        rgMobile.setOnCheckedChangeListener(this);
        rgWedding.setOnCheckedChangeListener(this);


        rbPhoneType_1 = (RadioButton)findViewById(R.id.joinaddinfo_rbPhoneType_1);
        rbPhoneType_2 = (RadioButton)findViewById(R.id.joinaddinfo_rbPhoneType_2);
        rbPhoneType_3 = (RadioButton)findViewById(R.id.joinaddinfo_rbPhoneType_3);
        rbWedding_1 = (RadioButton) findViewById(R.id.joinaddinfo_rbWedding_1);
        rbWedding_2 = (RadioButton) findViewById(R.id.joinaddinfo_rbWedding_2);

        //spiner 이벤트 등록
        spin_telecom.setOnItemSelectedListener(this);
        spin_os.setOnItemSelectedListener(this);
        spin_area.setOnItemSelectedListener(this);
        spin_job.setOnItemSelectedListener(this);

        //mypage에서 넘어온 경우, 버튼변경
        intent = getIntent();
        pre_activity = intent.getStringExtra("PRE_ACTIVITY").toString();
        if(pre_activity.equals("3")){
            //버튼변경
            ((Button)findViewById(R.id.jojnaddinfo_btnNext)).setText("변경완료");
            ((Button)findViewById(R.id.jojnaddinfo_btnNext)).setBackgroundResource(R.color.primary);
            //설명문구 안보이게
            ((LinearLayout)findViewById(R.id.joinaddinfo_ll_info)).setVisibility(View.GONE);
            ((View)findViewById(R.id.joinaddinfo_v1)).setVisibility(View.GONE);
            setTvTitle("부가정보 변경");

        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup.getCheckedRadioButtonId()==R.id.joinaddinfo_rbPhoneType_1){
            strPhoneType = "1";
        }else if(radioGroup.getCheckedRadioButtonId()==R.id.joinaddinfo_rbPhoneType_2) {
            strPhoneType = "2";
        }else if(radioGroup.getCheckedRadioButtonId()==R.id.joinaddinfo_rbPhoneType_3) {
            strPhoneType = "9";
        }

        if(radioGroup.getCheckedRadioButtonId()==R.id.joinaddinfo_rbWedding_1) {
            strWedding = "0";
        }else if(radioGroup.getCheckedRadioButtonId()==R.id.joinaddinfo_rbWedding_2) {
            strWedding = "1";
        }

        ChangeNextButton();
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
        dialogType=1;
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_addinfoList), null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jojnaddinfo_btnNext:
                if(strTelecom.equals("")||strPhoneType.equals("")||strOs.equals("")||strWedding.equals("")||strArea.equals("")||strJob.equals("")){
                    Common.createDialog(this, getString(R.string.dial_title4).toString(),null, "부가정보를 모두 입력해야 합니다.", getString(R.string.btn_ok),null, false, false);
                }else{
                    dialogType=2;
                    //String UID = Common.getPreference(getApplicationContext(), "UID");
                    //String KEY = Common.getPreference(getApplicationContext(), "KEY");
                    //휴대폰 종류와 스마트폰 os 는 가리고 저장
                    Object[][] params = {
                             {"TELECOM",strTelecom}
                            ,{"PHONE_TYPE",strPhoneType}
                            ,{"OS",strOs}
                            ,{"WEDDING",strWedding}
                            ,{"AREA",strArea}
                            ,{"JOB",strJob}
                    };

                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_joinAddinfo), params);
                }
                break;
        }

    }

    public void ChangeNextButton(){
        Button btnNext = (Button)findViewById(R.id.jojnaddinfo_btnNext);
        if(strTelecom.equals("")||strPhoneType.equals("")||strOs.equals("")||strWedding.equals("")||strArea.equals("")||strJob.equals("")){
            btnNext.setBackgroundResource(R.color.primaryDisabled);
        }else{
            btnNext.setBackgroundResource(R.color.primary);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try {
            switch (adapterView.getId()) {
                case R.id.joinaddinfo_spTelecom:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_telecoms.get(adapterView.getSelectedItemPosition()+addNum);
                    //Log.i("wtKim","object==>"+object.toString());
                    if(addNum==0){ strTelecom = "";}else{ strTelecom = object.get("CODE").toString(); }
                    break;
                case R.id.joinaddinfo_spOS:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_oss.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strOs = "";}else{ strOs = object.get("CODE").toString(); }
                    break;
                case R.id.joinaddinfo_spAREA:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_areas.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strArea = "";}else{ strArea = object.get("CODE").toString(); }
                    break;
                case R.id.joinaddinfo_spJOB:
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_jobs.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strJob = "";}else{ strJob = object.get("CODE").toString(); }
                    break;
            }
        } catch(Exception e){
            Common.createDialog(this, getString(R.string.dial_title4).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
            //Log.i("wtKim","err:"+e.toString());
        }

        ChangeNextButton();
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_INFOCHECK) infocheckHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }
    //joinAddinfo
    public void saveDataHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    Common.createDialog(this, getString(R.string.dial_title4).toString(),null, getString(R.string.dial_msg6), getString(R.string.btn_ok),null, true, false);
                }
            } else {
                Common.createDialog(this, getString(R.string.dial_title4).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.dial_title4).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 2 && !pre_activity.equals("3")){
            intent = new Intent(JoinAddInfoActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(result.equals("ok") && dialogType == 2 && pre_activity.equals("3")){
            intent = new Intent(JoinAddInfoActivity.this,MypageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //기본 불러오기
    public void startHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            ary_telecoms = json.getJSONArray("TELECOM");
            ary_oss = json.getJSONArray("OS");
            ary_areas = json.getJSONArray("AREA");
            ary_jobs = json.getJSONArray("JOB");

            int i;
            telecoms = new ArrayList<>();
            oss = new ArrayList<>();
            areas = new ArrayList<>();
            jobs = new ArrayList<>();

            telecoms.add("선택");
            for (i = 0; i < ary_telecoms.length(); i++){
                telecoms.add(((JSONObject) ary_telecoms.get(i)).getString("LABEL"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, telecoms);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spin_telecom.setAdapter(aa);
            }
            oss.add("선택");
            for (i = 0; i < ary_oss.length(); i++){
                oss.add(((JSONObject) ary_oss.get(i)).getString("LABEL"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, oss);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spin_os.setAdapter(aa);
            }
            areas.add("선택");
            for (i = 0; i < ary_areas.length(); i++){
                areas.add(((JSONObject) ary_areas.get(i)).getString("LABEL"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, areas);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spin_area.setAdapter(aa);
            }
            jobs.add("선택");
            for (i = 0; i < ary_jobs.length(); i++){
                jobs.add(((JSONObject) ary_jobs.get(i)).getString("CATEGORY2_NAME"));
                ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, jobs);
                aa.setDropDownViewResource(R.layout.spinner_item);
                spin_job.setAdapter(aa);
            }


            //기존정보가 있는지 로드
            common.loadData(CALLTYPE_INFOCHECK, getString(R.string.url_addinfo), null);
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.dial_title4).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //기존정보체크
    public void infocheckHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim","json==>"+json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String telecom = json.getString("TELECOM");
                    String phone_type = json.getString("PHONE_TYPE");
                    String os = json.getString("OS");
                    String wedding = json.getString("WEDDING");
                    String area = json.getString("AREA");
                    String job = json.getString("JOB");

                    if(!telecom.equals("")) CheckedTelecom(telecom);
                    if(!phone_type.equals("")) CheckedPhone_type(phone_type);
                    if(!os.equals("")) CheckedOs(os);
                    if(!wedding.equals("")) CheckedWedding(wedding);
                    if(!area.equals("")) CheckedArea(area);
                    if(!job.equals("")) CheckedJob(job);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this,"MPLAT",null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, "MPLAT",null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //텔레콤 정보로드
    public void CheckedTelecom(String str){
        try{
            for(int i=0;i<ary_telecoms.length();i++){
                //ary_jobs.get(i)).getString("CATEGORY2_NAME"));
                String code = ((JSONObject) ary_telecoms.get(i)).getString("CODE");
                //인덱스 저장
                if(code.equals(str)){telecoms_selectedIdx = i+1;}
            }
        }catch (Exception e){

        }
        //joinaddinfo_spTelecom
        ((Spinner)findViewById(R.id.joinaddinfo_spTelecom)).setSelection(telecoms_selectedIdx);
    }
    //폰 정보로드
    public void CheckedPhone_type(String str){
        Log.i("wtkim","CheckedPhone_type==>"+str);
    }

    //os 정보로드
    public void CheckedOs(String str){
        Log.i("wtkim","CheckedOs==>"+str);
    }

    //결혼여부 정보로드
    public void CheckedWedding(String str){
        if(str.equals("0")){
            ((RadioButton)findViewById(R.id.joinaddinfo_rbWedding_1)).setChecked(true);
        }else if(str.equals("1")){
            ((RadioButton)findViewById(R.id.joinaddinfo_rbWedding_2)).setChecked(true);
        }
    }
    //지역정보로드
    public void CheckedArea(String str){
        try{
            for(int i=0;i<ary_areas.length();i++){
                //ary_jobs.get(i)).getString("CATEGORY2_NAME"));
                String code = ((JSONObject) ary_areas.get(i)).getString("CODE");
                //인덱스 저장
                if(code.equals(str)){areas_selectedIdx = i+1;}
            }
        }catch (Exception e){

        }
        ((Spinner)findViewById(R.id.joinaddinfo_spAREA)).setSelection(areas_selectedIdx);
    }
    //직업 정보로드
    public void CheckedJob(String str){
        try{
            for(int i=0;i<ary_jobs.length();i++){
                //ary_jobs.get(i)).getString("CATEGORY2_NAME"));
                String code = ((JSONObject) ary_jobs.get(i)).getString("CODE");
                //인덱스 저장
                if(code.equals(str)){jobs_selectedIdx = i+1;}
            }
        }catch (Exception e){

        }
        ((Spinner)findViewById(R.id.joinaddinfo_spJOB)).setSelection(jobs_selectedIdx);
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);

        if(pre_activity.equals("")){
            setBackBtnVisible(false);
        }else{
            setBackBtnVisible(true);
        }

        return ret;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
