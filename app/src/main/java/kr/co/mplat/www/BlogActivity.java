package kr.co.mplat.www;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/*
* http://recipes4dev.tistory.com/43
* */
public class BlogActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private int blogMaxcount = 1;
    private int snsMaxcount = 1;

    ArrayList<String> blogs = new ArrayList<String>(); //블로그 spiner
    ArrayList<String> snss = new ArrayList<String>();   //sns spiner
    JSONArray ary_bloglist = new JSONArray(); //블로그
    JSONArray ary_snslist = new JSONArray();   //sns
    JSONArray ary_blog = new JSONArray();   //블로그 로드정보
    JSONArray ary_sns = new JSONArray();   //sns 로드정보

    String strBlog1 = "";
    String strBlog2 = "";
    String strBlog3 = "";
    String strBlog4 = "";
    String strBlog5 = "";
    String strSns1 = "";
    String strSns2 = "";
    String strSns3 = "";
    String strSns4 = "";
    String strSns5 = "";

    LinearLayout blog_llParent1 = null;
    LinearLayout blog_llParent2 = null;
    LinearLayout blog_llParent3 = null;
    LinearLayout blog_llParent4 = null;
    LinearLayout blog_llParent5 = null;
    LinearLayout sns_llParent1  = null;
    LinearLayout sns_llParent2  = null;
    LinearLayout sns_llParent3  = null;
    LinearLayout sns_llParent4  = null;
    LinearLayout sns_llParent5  = null;

    Spinner blog_spBlog1 = null;
    Spinner blog_spBlog2 = null;
    Spinner blog_spBlog3 = null;
    Spinner blog_spBlog4 = null;
    Spinner blog_spBlog5 = null;
    Spinner sns_spSns1   = null;
    Spinner sns_spSns2   = null;
    Spinner sns_spSns3   = null;
    Spinner sns_spSns4   = null;
    Spinner sns_spSns5   = null;

    String blog_etTitle1 = "";
    String blog_etTitle2 = "";
    String blog_etTitle3 = "";
    String blog_etTitle4 = "";
    String blog_etTitle5 = "";

    String blog_etUrl1 = "";
    String blog_etUrl2 = "";
    String blog_etUrl3 = "";
    String blog_etUrl4 = "";
    String blog_etUrl5 = "";

    String sns_etUrl1 = "";
    String sns_etUrl2 = "";
    String sns_etUrl3 = "";
    String sns_etUrl4 = "";
    String sns_etUrl5 = "";


    Button btnBlogAdd = null;
    Button btnSnsAdd = null;
    Button btnSave = null;

    Button btnDelete11 = null;
    Button btnDelete12 = null;
    Button btnDelete13 = null;
    Button btnDelete14 = null;
    Button btnDelete15 = null;

    Button btnDelete21 = null;
    Button btnDelete22 = null;
    Button btnDelete23 = null;
    Button btnDelete24 = null;
    Button btnDelete25 = null;

    //불러오기 spiner index 저장
    int blog1_selectedIndex = 0;
    int blog2_selectedIndex = 0;
    int blog3_selectedIndex = 0;
    int blog4_selectedIndex = 0;
    int blog5_selectedIndex = 0;
    int sns1_selectedIndex = 0;
    int sns2_selectedIndex = 0;
    int sns3_selectedIndex = 0;
    int sns4_selectedIndex = 0;
    int sns5_selectedIndex = 0;

    //answered 체크
    Boolean nextOk = true;
    Boolean answeredBlog1 = true;
    Boolean answeredBlog2 = true;
    Boolean answeredBlog3 = true;
    Boolean answeredBlog4 = true;
    Boolean answeredBlog5 = true;
    Boolean answeredSns1 = true;
    Boolean answeredSns2 = true;
    Boolean answeredSns3 = true;
    Boolean answeredSns4 = true;
    Boolean answeredSns5 = true;
    private String pre_activity = "";
    private String pre_campaignCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        setTvTitle("블로그/SNS 매체 등록 및 변경");
        common = new Common(this);
        intent = getIntent();
        pre_activity = intent.getStringExtra("PRE_ACTIVITY");
        pre_campaignCode = intent.getStringExtra("CAMPAIGN_CODE");

        //문구변경
        ((TextView)findViewById(R.id.blog_tvInfo1)).setText(Html.fromHtml("리뷰(홍보글)를 게재하실<br/><font color='#7161C4'>본인 소유의 블로그 또는 SNS</font>를 등록해주시기 바랍니다."));
        ((TextView)findViewById(R.id.blog_tvInfo2)).setText(Html.fromHtml("블로그 등록 <font color='#D57A76'>(*필수 1개 이상)</font>"));
        //저장버튼
        btnSave = ((Button)findViewById(R.id.blog_btnSave));
        //
        blog_llParent1 = (LinearLayout)findViewById(R.id.blog_llParent1);
        blog_llParent2 = (LinearLayout)findViewById(R.id.blog_llParent2);
        blog_llParent3 = (LinearLayout)findViewById(R.id.blog_llParent3);
        blog_llParent4 = (LinearLayout)findViewById(R.id.blog_llParent4);
        blog_llParent5 = (LinearLayout)findViewById(R.id.blog_llParent5);
        sns_llParent1 = (LinearLayout)findViewById(R.id.sns_llParent1);
        sns_llParent2 = (LinearLayout)findViewById(R.id.sns_llParent2);
        sns_llParent3 = (LinearLayout)findViewById(R.id.sns_llParent3);
        sns_llParent4 = (LinearLayout)findViewById(R.id.sns_llParent4);
        sns_llParent5 = (LinearLayout)findViewById(R.id.sns_llParent5);

        //우선 시작시 1번을 제외한 것은 안보이도록
        blog_llParent2.setVisibility(View.GONE);
        blog_llParent3.setVisibility(View.GONE);
        blog_llParent4.setVisibility(View.GONE);
        blog_llParent5.setVisibility(View.GONE);

        sns_llParent2.setVisibility(View.GONE);
        sns_llParent3.setVisibility(View.GONE);
        sns_llParent4.setVisibility(View.GONE);
        sns_llParent5.setVisibility(View.GONE);

        if(blog_llParent1.getVisibility()==View.GONE) {
            Log.i("wtkim", "1번보이지않음");
        }else{
            Log.i("wtkim", "1번보임");
        }
        if(blog_llParent2.getVisibility()==View.GONE) {
            Log.i("wtkim", "2번보이지않음");
        }else{
            Log.i("wtkim", "2번보임");
        }

        blog_spBlog1 = (Spinner)findViewById(R.id.blog_spBlog1);
        blog_spBlog2 = (Spinner)findViewById(R.id.blog_spBlog2);
        blog_spBlog3 = (Spinner)findViewById(R.id.blog_spBlog3);
        blog_spBlog4 = (Spinner)findViewById(R.id.blog_spBlog4);
        blog_spBlog5 = (Spinner)findViewById(R.id.blog_spBlog5);
        sns_spSns1  = (Spinner)findViewById(R.id.sns_spSns1);
        sns_spSns2  = (Spinner)findViewById(R.id.sns_spSns2);
        sns_spSns3  = (Spinner)findViewById(R.id.sns_spSns3);
        sns_spSns4  = (Spinner)findViewById(R.id.sns_spSns4);
        sns_spSns5  = (Spinner)findViewById(R.id.sns_spSns5);

        //spiner 이벤트 등록
        blog_spBlog1.setOnItemSelectedListener(this);
        blog_spBlog2.setOnItemSelectedListener(this);
        blog_spBlog3.setOnItemSelectedListener(this);
        blog_spBlog4.setOnItemSelectedListener(this);
        blog_spBlog5.setOnItemSelectedListener(this);
        sns_spSns1.setOnItemSelectedListener(this);
        sns_spSns2.setOnItemSelectedListener(this);
        sns_spSns3.setOnItemSelectedListener(this);
        sns_spSns4.setOnItemSelectedListener(this);
        sns_spSns5.setOnItemSelectedListener(this);



        btnBlogAdd   = (Button)findViewById(R.id.blog_btnBlogAdd);
        btnSnsAdd    = (Button)findViewById(R.id.blog_btnSnsAdd);
        //삭제버튼
        btnDelete11 = (Button)findViewById(R.id.blog_btnDelete1);
        btnDelete12 = (Button)findViewById(R.id.blog_btnDelete2);
        btnDelete13 = (Button)findViewById(R.id.blog_btnDelete3);
        btnDelete14 = (Button)findViewById(R.id.blog_btnDelete4);
        btnDelete15 = (Button)findViewById(R.id.blog_btnDelete5);
        btnDelete21 = (Button)findViewById(R.id.sns_btnDelete1);
        btnDelete22 = (Button)findViewById(R.id.sns_btnDelete2);
        btnDelete23 = (Button)findViewById(R.id.sns_btnDelete3);
        btnDelete24 = (Button)findViewById(R.id.sns_btnDelete4);
        btnDelete25 = (Button)findViewById(R.id.sns_btnDelete5);

        //삭제버튼 클릭시
        btnDelete12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 12;//blog2 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 13;//blog3 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete14.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 14;//blog4 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete15.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 15;//blog5 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });

        btnDelete22.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 22;//sns2 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete23.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 23;//sns3 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete24.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 24;//sns4 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        btnDelete25.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 25;//sns5 삭제
                Common.createDialog(BlogActivity.this,"삭제 확인",null, "정말로 해당 항목을 삭제하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });

        //블로그 추가 버튼 클릭시
        btnBlogAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(blogMaxcount==1){
                    blog_llParent2.setVisibility(View.VISIBLE);
                    blogMaxcount = 2;
                    ((EditText)findViewById(R.id.blog_etTitle2)).requestFocus();
                }else if(blogMaxcount==2) {
                    blog_llParent3.setVisibility(View.VISIBLE);
                    btnDelete12.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete12.setEnabled(false);
                    blogMaxcount = 3;
                    ((EditText)findViewById(R.id.blog_etTitle3)).requestFocus();
                }else if(blogMaxcount==3) {
                    blog_llParent4.setVisibility(View.VISIBLE);
                    btnDelete13.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete13.setEnabled(false);
                    blogMaxcount = 4;
                    ((EditText)findViewById(R.id.blog_etTitle4)).requestFocus();
                }else if(blogMaxcount==4) {
                    blog_llParent5.setVisibility(View.VISIBLE);
                    btnDelete14.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete14.setEnabled(false);
                    blogMaxcount = 5;
                    ((EditText)findViewById(R.id.blog_etTitle5)).requestFocus();
                }
            }
        });
        //sns 추가 버튼 클릭시
        btnSnsAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(snsMaxcount==1){
                    sns_llParent2.setVisibility(View.VISIBLE);
                    snsMaxcount = 2;
                    ((EditText)findViewById(R.id.sns_etUrl2)).requestFocus();
                }else if(snsMaxcount==2) {
                    sns_llParent3.setVisibility(View.VISIBLE);
                    btnDelete22.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete22.setEnabled(false);
                    snsMaxcount = 3;
                    ((EditText)findViewById(R.id.sns_etUrl3)).requestFocus();
                }else if(snsMaxcount==3) {
                    sns_llParent4.setVisibility(View.VISIBLE);
                    btnDelete23.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete23.setEnabled(false);
                    snsMaxcount = 4;
                    ((EditText)findViewById(R.id.sns_etUrl4)).requestFocus();
                }else if(snsMaxcount==4) {
                    sns_llParent5.setVisibility(View.VISIBLE);
                    btnDelete24.setBackgroundResource(R.color.primaryDisabled);
                    btnDelete24.setEnabled(false);
                    snsMaxcount = 5;
                    ((EditText)findViewById(R.id.sns_etUrl5)).requestFocus();
                }
            }
        });

        //저장버튼 클릭시
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","저장버튼클릭!");

                blog_etTitle1 = ((EditText)findViewById(R.id.blog_etTitle1)).getText().toString();
                blog_etTitle2 = ((EditText)findViewById(R.id.blog_etTitle2)).getText().toString();
                blog_etTitle3 = ((EditText)findViewById(R.id.blog_etTitle3)).getText().toString();
                blog_etTitle4 = ((EditText)findViewById(R.id.blog_etTitle4)).getText().toString();
                blog_etTitle5 = ((EditText)findViewById(R.id.blog_etTitle5)).getText().toString();
                blog_etUrl1 = ((EditText)findViewById(R.id.blog_etUrl1)).getText().toString();
                blog_etUrl2 = ((EditText)findViewById(R.id.blog_etUrl2)).getText().toString();
                blog_etUrl3 = ((EditText)findViewById(R.id.blog_etUrl3)).getText().toString();
                blog_etUrl4 = ((EditText)findViewById(R.id.blog_etUrl4)).getText().toString();
                blog_etUrl5 = ((EditText)findViewById(R.id.blog_etUrl5)).getText().toString();
                sns_etUrl1 = ((EditText)findViewById(R.id.sns_etUrl1)).getText().toString();
                sns_etUrl2 = ((EditText)findViewById(R.id.sns_etUrl2)).getText().toString();
                sns_etUrl3 = ((EditText)findViewById(R.id.sns_etUrl3)).getText().toString();
                sns_etUrl4 = ((EditText)findViewById(R.id.sns_etUrl4)).getText().toString();
                sns_etUrl5 = ((EditText)findViewById(R.id.sns_etUrl5)).getText().toString();

                if(strBlog1.equals("")||blog_etTitle1.equals("")||blog_etUrl1.equals("")){ answeredBlog1 = false; }else{ answeredBlog1 = true; }
                if(strBlog2.equals("")||blog_etTitle2.equals("")||blog_etUrl2.equals("")){ answeredBlog2 = false; }else{ answeredBlog2 = true; }
                if(strBlog3.equals("")||blog_etTitle3.equals("")||blog_etUrl3.equals("")){ answeredBlog3 = false; }else{ answeredBlog3 = true; }
                if(strBlog4.equals("")||blog_etTitle4.equals("")||blog_etUrl4.equals("")){ answeredBlog4 = false; }else{ answeredBlog4 = true; }
                if(strBlog5.equals("")||blog_etTitle5.equals("")||blog_etUrl5.equals("")){ answeredBlog5 = false; }else{ answeredBlog5 = true; }

                if(!strSns1.equals("")||!sns_etUrl1.equals("")){
                    if(strSns1.equals("")||sns_etUrl1.equals("")){ answeredSns1 = false; }else{ answeredSns1 = true; }
                }
                if(!strSns2.equals("")||!sns_etUrl2.equals("")){
                    if(strSns2.equals("")||sns_etUrl2.equals("")){ answeredSns2 = false; }else{ answeredSns2 = true; }
                }
                if(!strSns3.equals("")||!sns_etUrl3.equals("")){
                    if(strSns3.equals("")||sns_etUrl3.equals("")){ answeredSns3 = false; }else{ answeredSns3 = true; }
                }
                if(!strSns4.equals("")||!sns_etUrl4.equals("")){
                    if(strSns4.equals("")||sns_etUrl4.equals("")){ answeredSns4 = false; }else{ answeredSns4 = true; }
                }
                if(!strSns5.equals("")||!sns_etUrl5.equals("")){
                    if(strSns5.equals("")||sns_etUrl5.equals("")){ answeredSns5 = false; }else{ answeredSns5 = true; }
                }

                if(blogMaxcount==1){
                    if(!answeredBlog1){
                        if(strBlog1.equals("") && blog_etTitle1.equals("") && blog_etUrl1.equals("")){
                            Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(),null, "최소한 블로그 1개 이상을 등록해주셔야 합니다.", getString(R.string.btn_ok),null, false, false);
                            nextOk = false;
                        }else{
                            Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(),null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                            nextOk = false;
                        }
                    }
                }else if(blogMaxcount==2){
                    if(!answeredBlog1||!answeredBlog2){
                        Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(),null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                        nextOk = false;
                    }
                }else if(blogMaxcount==3){
                    if(!answeredBlog1||!answeredBlog2||!answeredBlog3){
                        Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(),null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                        nextOk = false;
                    }
                }else if(blogMaxcount==4){
                    if(!answeredBlog1||!answeredBlog2||!answeredBlog3||!answeredBlog4){
                        Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(),null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                        nextOk = false;
                    }
                }else if(blogMaxcount==5) {
                    if (!answeredBlog1||!answeredBlog2||!answeredBlog3||!answeredBlog4||!answeredBlog5) {
                        Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(), null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok), null, false, false);
                        nextOk = false;
                    }
                }
                //SNS
                if(!answeredSns1||!answeredSns2||!answeredSns3||!answeredSns4||!answeredSns5){
                    Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(), null, "올바르게 입력하지 않았거나 누락된 항목이 있습니다. 다시 한번 확인해 주시기 바랍니다.", getString(R.string.btn_ok), null, false, false);
                    nextOk = false;
                }

                if(nextOk){
                    Object[][] params = {
                             {"CHANNEL_CODES", strBlog1+"|||"+strBlog2+"|||"+strBlog3+"|||"+strBlog4+"|||"+strBlog5+"|||"+strSns1+"|||"+strSns2+"|||"+strSns3+"|||"+strSns4+"|||"+strSns5}
                            ,{"TITLES", blog_etTitle1+"|||"+blog_etTitle2+"|||"+blog_etTitle3+"|||"+blog_etTitle4+"|||"+blog_etTitle5+"|||"+"|||"+"|||"+"|||"+"|||"}
                            ,{"URLS",blog_etUrl1+"|||"+blog_etUrl2+"|||"+blog_etUrl3+"|||"+blog_etUrl4+"|||"+blog_etUrl5+"|||"+sns_etUrl1+"|||"+sns_etUrl2+"|||"+sns_etUrl3+"|||"+sns_etUrl4+"|||"+sns_etUrl5}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_blogChange), params);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try{
            switch (adapterView.getId()){
                case R.id.blog_spBlog1:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_bloglist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strBlog1 = "";}else{strBlog1 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strBlog1==>"+strBlog1);
                    break;
                case R.id.blog_spBlog2:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_bloglist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strBlog2 = "";}else{strBlog2 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strBlog2==>"+strBlog2);
                    break;
                case R.id.blog_spBlog3:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_bloglist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strBlog3 = "";}else{strBlog3 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strBlog3==>"+strBlog3);
                    break;
                case R.id.blog_spBlog4:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_bloglist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strBlog4 = "";}else{strBlog4 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strBlog4==>"+strBlog4);
                    break;
                case R.id.blog_spBlog5:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_bloglist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strBlog5 = "";}else{strBlog5 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strBlog5==>"+strBlog5);
                    break;
                case R.id.sns_spSns1:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_snslist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strSns1 = "";}else{strSns1 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strSns1==>"+strSns1);
                    break;
                case R.id.sns_spSns2:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_snslist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strSns2 = "";}else{strSns2 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strSns2==>"+strSns2);
                    break;
                case R.id.sns_spSns3:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_snslist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strSns3 = "";}else{strSns3 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strSns3==>"+strSns3);
                    break;
                case R.id.sns_spSns4:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_snslist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strSns4 = "";}else{strSns4 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strSns4==>"+strSns4);
                    break;
                case R.id.sns_spSns5:
                    if(adapterView.getSelectedItemPosition()>0){addNum = -1;}else{ addNum=0;}
                    object = (JSONObject) ary_snslist.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){strSns5 = "";}else{strSns5 = object.get("CHANNEL_CODE").toString();}
                    //Log.i("wtkim","strSns5==>"+strSns5);
                    break;
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_blog), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 12){
            strBlog2 = "";
            answeredBlog2 = true;
            blog_llParent2.setVisibility(View.GONE);
            blogMaxcount = 1;
            //값 초기화
            ((Spinner)findViewById(R.id.blog_spBlog2)).setSelection(0);
            ((EditText)findViewById(R.id.blog_etTitle2)).setText("");
            ((EditText)findViewById(R.id.blog_etUrl2)).setText("");
        }else if(result.equals("ok") && dialogType == 13){
            strBlog3 = "";
            answeredBlog3 = true;
            blog_llParent3.setVisibility(View.GONE);
            btnDelete12.setBackgroundResource(R.color.darkblack);
            btnDelete12.setEnabled(true);
            blogMaxcount = 2;
            //값 초기화
            ((Spinner)findViewById(R.id.blog_spBlog3)).setSelection(0);
            ((EditText)findViewById(R.id.blog_etTitle3)).setText("");
            ((EditText)findViewById(R.id.blog_etUrl3)).setText("");
        }else if(result.equals("ok") && dialogType == 14){
            strBlog4 = "";
            answeredBlog4 = true;
            blog_llParent4.setVisibility(View.GONE);
            btnDelete13.setBackgroundResource(R.color.darkblack);
            btnDelete13.setEnabled(true);
            blogMaxcount = 3;
            //값 초기화
            ((Spinner)findViewById(R.id.blog_spBlog4)).setSelection(0);
            ((EditText)findViewById(R.id.blog_etTitle4)).setText("");
            ((EditText)findViewById(R.id.blog_etUrl4)).setText("");
        }else if(result.equals("ok") && dialogType == 15){
            strBlog5 = "";
            answeredBlog5 = true;
            blog_llParent5.setVisibility(View.GONE);
            btnDelete14.setBackgroundResource(R.color.darkblack);
            btnDelete14.setEnabled(true);
            blogMaxcount = 4;
            //값 초기화
            ((Spinner)findViewById(R.id.blog_spBlog5)).setSelection(0);
            ((EditText)findViewById(R.id.blog_etTitle5)).setText("");
            ((EditText)findViewById(R.id.blog_etUrl5)).setText("");
        }else if(result.equals("ok") && dialogType == 22){
            strSns2 = "";
            answeredSns2 = true;
            sns_llParent2.setVisibility(View.GONE);
            snsMaxcount = 1;
            //값 초기화
            ((Spinner)findViewById(R.id.sns_spSns2)).setSelection(0);
            ((EditText)findViewById(R.id.sns_etUrl2)).setText("");
        }else if(result.equals("ok") && dialogType == 23){
            strSns3 = "";
            answeredSns3 = true;
            sns_llParent3.setVisibility(View.GONE);
            btnDelete22.setBackgroundResource(R.color.darkblack);
            btnDelete22.setEnabled(true);
            snsMaxcount = 2;
            //값 초기화
            ((Spinner)findViewById(R.id.sns_spSns3)).setSelection(0);
            ((EditText)findViewById(R.id.sns_etUrl3)).setText("");
        }else if(result.equals("ok") && dialogType == 24){
            strSns4 = "";
            answeredSns4 = true;
            sns_llParent4.setVisibility(View.GONE);
            btnDelete23.setBackgroundResource(R.color.darkblack);
            btnDelete23.setEnabled(true);
            snsMaxcount = 3;
            //값 초기화
            ((Spinner)findViewById(R.id.sns_spSns4)).setSelection(0);
            ((EditText)findViewById(R.id.sns_etUrl4)).setText("");
        }else if(result.equals("ok") && dialogType == 25){
            strSns5 = "";
            answeredSns5 = true;
            sns_llParent5.setVisibility(View.GONE);
            btnDelete24.setBackgroundResource(R.color.darkblack);
            btnDelete24.setEnabled(true);
            snsMaxcount = 4;
            //값 초기화
            ((Spinner)findViewById(R.id.sns_spSns5)).setSelection(0);
            ((EditText)findViewById(R.id.sns_etUrl5)).setText("");
        }


        if(pre_campaignCode!=null && !pre_campaignCode.equals("") && pre_activity.equals("ReviewDetailActivity")){
            intent = new Intent(BlogActivity.this,ReviewDetailActivity.class);
            intent.putExtra("PRE_ACTIVITY","ReviewDetailActivity");
            intent.putExtra("CAMPAIGN_CODE",pre_campaignCode);
            startActivity(intent);
            finish();
        }else if(result.equals("ok") && dialogType == 2){//저장시 마이페이지 메인으로
            intent = new Intent(BlogActivity.this,MypageActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveHandler(str);

    }
    public void loadHandler(String str){
       try{
           JSONObject json = new JSONObject(str);
           ary_bloglist = json.getJSONArray("BLOG_LIST");
           ary_snslist = json.getJSONArray("SNS_LIST");
           ary_blog = json.getJSONArray("BLOG");
           ary_sns = json.getJSONArray("SNS");
           int i,j;
           blogs = new ArrayList<String>();
           snss  = new ArrayList<String>();

           blogs.add("선택");
           for(i=0;i<ary_bloglist.length();i++){
               blogs.add(((JSONObject)ary_bloglist.get(i)).getString("CATEGORY2_NAME"));
               ArrayAdapter<String> aa =new ArrayAdapter<String>(this,R.layout.spinner_item_blog,blogs);
               aa.setDropDownViewResource(R.layout.spinner_item_blog);
               blog_spBlog1.setAdapter(aa);
               blog_spBlog2.setAdapter(aa);
               blog_spBlog3.setAdapter(aa);
               blog_spBlog4.setAdapter(aa);
               blog_spBlog5.setAdapter(aa);
           }

           snss.add("선택");
           for(i=0;i<ary_snslist.length();i++){
               snss.add(((JSONObject)ary_snslist.get(i)).getString("CATEGORY2_NAME"));
               ArrayAdapter<String> bb =new ArrayAdapter<String>(this,R.layout.spinner_item_blog,snss);
               bb.setDropDownViewResource(R.layout.spinner_item_blog);
               sns_spSns1.setAdapter(bb);
               sns_spSns2.setAdapter(bb);
               sns_spSns3.setAdapter(bb);
               sns_spSns4.setAdapter(bb);
               sns_spSns5.setAdapter(bb);
           }
           //로드정보 셋팅
           //블로그1
          /* String code = ((JSONObject)ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
           String title = ((JSONObject)ary_blog.get(0)).getString("BLOG_TITLE");
           String url = ((JSONObject)ary_blog.get(0)).getString("BLOG_URL");*/
           String blog_code1 = "";
           String blog_code2 = "";
           String blog_code3 = "";
           String blog_code4 = "";
           String blog_code5 = "";
           String sns_code1 = "";
           String sns_code2 = "";
           String sns_code3 = "";
           String sns_code4 = "";
           String sns_code5 = "";

           String blog_title1 = "";
           String blog_title2 = "";
           String blog_title3 = "";
           String blog_title4 = "";
           String blog_title5 = "";

           String blog_url1 = "";
           String blog_url2 = "";
           String blog_url3 = "";
           String blog_url4 = "";
           String blog_url5 = "";

           String sns_url1 = "";
           String sns_url2 = "";
           String sns_url3 = "";
           String sns_url4 = "";
           String sns_url5 = "";

           try{
               if(ary_blog.length()==1){
                   blog_code1 = ((JSONObject) ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
                   blog_title1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_TITLE");
                   blog_url1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_URL");
               }else if(ary_blog.length()==2){
                   blog_llParent1.setVisibility(View.VISIBLE);
                   blog_llParent2.setVisibility(View.VISIBLE);
                   blog_code1 = ((JSONObject) ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
                   blog_title1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_TITLE");
                   blog_url1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_URL");
                   blog_code2 = ((JSONObject) ary_blog.get(1)).getString("REVIEW_BLOG_CODE");
                   blog_title2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_TITLE");
                   blog_url2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_URL");

               }else if(ary_blog.length()==3){
                   blog_llParent1.setVisibility(View.VISIBLE);
                   blog_llParent2.setVisibility(View.VISIBLE);
                   blog_llParent3.setVisibility(View.VISIBLE);
                   blog_code1 = ((JSONObject) ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
                   blog_title1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_TITLE");
                   blog_url1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_URL");
                   blog_code2 = ((JSONObject) ary_blog.get(1)).getString("REVIEW_BLOG_CODE");
                   blog_title2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_TITLE");
                   blog_url2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_URL");
                   blog_code3 = ((JSONObject) ary_blog.get(2)).getString("REVIEW_BLOG_CODE");
                   blog_title3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_TITLE");
                   blog_url3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_URL");
               }else if(ary_blog.length()==4){
                   blog_llParent1.setVisibility(View.VISIBLE);
                   blog_llParent2.setVisibility(View.VISIBLE);
                   blog_llParent3.setVisibility(View.VISIBLE);
                   blog_llParent4.setVisibility(View.VISIBLE);
                   blog_code1 = ((JSONObject) ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
                   blog_title1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_TITLE");
                   blog_url1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_URL");
                   blog_code2 = ((JSONObject) ary_blog.get(1)).getString("REVIEW_BLOG_CODE");
                   blog_title2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_TITLE");
                   blog_url2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_URL");
                   blog_code3 = ((JSONObject) ary_blog.get(2)).getString("REVIEW_BLOG_CODE");
                   blog_title3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_TITLE");
                   blog_url3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_URL");
                   blog_code4 = ((JSONObject) ary_blog.get(3)).getString("REVIEW_BLOG_CODE");
                   blog_title4 = ((JSONObject) ary_blog.get(3)).getString("BLOG_TITLE");
                   blog_url4 = ((JSONObject) ary_blog.get(3)).getString("BLOG_URL");
               }else if(ary_blog.length()==5){
                   blog_llParent1.setVisibility(View.VISIBLE);
                   blog_llParent2.setVisibility(View.VISIBLE);
                   blog_llParent3.setVisibility(View.VISIBLE);
                   blog_llParent4.setVisibility(View.VISIBLE);
                   blog_llParent5.setVisibility(View.VISIBLE);
                   blog_code1 = ((JSONObject) ary_blog.get(0)).getString("REVIEW_BLOG_CODE");
                   blog_title1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_TITLE");
                   blog_url1 = ((JSONObject) ary_blog.get(0)).getString("BLOG_URL");
                   blog_code2 = ((JSONObject) ary_blog.get(1)).getString("REVIEW_BLOG_CODE");
                   blog_title2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_TITLE");
                   blog_url2 = ((JSONObject) ary_blog.get(1)).getString("BLOG_URL");
                   blog_code3 = ((JSONObject) ary_blog.get(2)).getString("REVIEW_BLOG_CODE");
                   blog_title3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_TITLE");
                   blog_url3 = ((JSONObject) ary_blog.get(2)).getString("BLOG_URL");
                   blog_code4 = ((JSONObject) ary_blog.get(3)).getString("REVIEW_BLOG_CODE");
                   blog_title4 = ((JSONObject) ary_blog.get(3)).getString("BLOG_TITLE");
                   blog_url4 = ((JSONObject) ary_blog.get(3)).getString("BLOG_URL");
                   blog_code5 = ((JSONObject) ary_blog.get(4)).getString("REVIEW_BLOG_CODE");
                   blog_title5 = ((JSONObject) ary_blog.get(4)).getString("BLOG_TITLE");
                   blog_url5 = ((JSONObject) ary_blog.get(4)).getString("BLOG_URL");
               }

               for(i=0;i<ary_bloglist.length();i++){
                   String channel_code = ((JSONObject) ary_bloglist.get(i)).getString("CHANNEL_CODE");
                   if(channel_code.equals(blog_code1)) blog1_selectedIndex = i+1;
                   if(channel_code.equals(blog_code2)) blog2_selectedIndex = i+1;
                   if(channel_code.equals(blog_code3)) blog3_selectedIndex = i+1;
                   if(channel_code.equals(blog_code4)) blog4_selectedIndex = i+1;
                   if(channel_code.equals(blog_code5)) blog5_selectedIndex = i+1;

               }

               //sns
               if(ary_sns.length()==1){
                   sns_llParent1.setVisibility(View.VISIBLE);
                   sns_code1 = ((JSONObject) ary_sns.get(0)).getString("REVIEW_SNS_CODE");
                   sns_url1 = ((JSONObject) ary_sns.get(0)).getString("SNS_URL");
               }else if(ary_sns.length()==2){
                   sns_llParent1.setVisibility(View.VISIBLE);
                   sns_llParent2.setVisibility(View.VISIBLE);
                   sns_code1 = ((JSONObject) ary_sns.get(0)).getString("REVIEW_SNS_CODE");
                   sns_url1 = ((JSONObject) ary_sns.get(0)).getString("SNS_URL");
                   sns_code2 = ((JSONObject) ary_sns.get(1)).getString("REVIEW_SNS_CODE");
                   sns_url2 = ((JSONObject) ary_sns.get(1)).getString("SNS_URL");
               }else if(ary_sns.length()==3){
                   sns_llParent1.setVisibility(View.VISIBLE);
                   sns_llParent2.setVisibility(View.VISIBLE);
                   sns_llParent3.setVisibility(View.VISIBLE);
                   sns_code1 = ((JSONObject) ary_sns.get(0)).getString("REVIEW_SNS_CODE");
                   sns_url1 = ((JSONObject) ary_sns.get(0)).getString("SNS_URL");
                   sns_code2 = ((JSONObject) ary_sns.get(1)).getString("REVIEW_SNS_CODE");
                   sns_url2 = ((JSONObject) ary_sns.get(1)).getString("SNS_URL");
                   sns_code3 = ((JSONObject) ary_sns.get(2)).getString("REVIEW_SNS_CODE");
                   sns_url3 = ((JSONObject) ary_sns.get(2)).getString("SNS_URL");
               }else if(ary_sns.length()==4){
                   sns_llParent1.setVisibility(View.VISIBLE);
                   sns_llParent2.setVisibility(View.VISIBLE);
                   sns_llParent3.setVisibility(View.VISIBLE);
                   sns_llParent4.setVisibility(View.VISIBLE);
                   sns_code1 = ((JSONObject) ary_sns.get(0)).getString("REVIEW_SNS_CODE");
                   sns_url1 = ((JSONObject) ary_sns.get(0)).getString("SNS_URL");
                   sns_code2 = ((JSONObject) ary_sns.get(1)).getString("REVIEW_SNS_CODE");
                   sns_url2 = ((JSONObject) ary_sns.get(1)).getString("SNS_URL");
                   sns_code3 = ((JSONObject) ary_sns.get(2)).getString("REVIEW_SNS_CODE");
                   sns_url3 = ((JSONObject) ary_sns.get(2)).getString("SNS_URL");
                   sns_code4 = ((JSONObject) ary_sns.get(3)).getString("REVIEW_SNS_CODE");
                   sns_url4 = ((JSONObject) ary_sns.get(3)).getString("SNS_URL");
               }else if(ary_sns.length()==5){
                   sns_llParent1.setVisibility(View.VISIBLE);
                   sns_llParent2.setVisibility(View.VISIBLE);
                   sns_llParent3.setVisibility(View.VISIBLE);
                   sns_llParent4.setVisibility(View.VISIBLE);
                   sns_llParent5.setVisibility(View.VISIBLE);
                   sns_code1 = ((JSONObject) ary_sns.get(0)).getString("REVIEW_SNS_CODE");
                   sns_url1 = ((JSONObject) ary_sns.get(0)).getString("SNS_URL");
                   sns_code2 = ((JSONObject) ary_sns.get(1)).getString("REVIEW_SNS_CODE");
                   sns_url2 = ((JSONObject) ary_sns.get(1)).getString("SNS_URL");
                   sns_code3 = ((JSONObject) ary_sns.get(2)).getString("REVIEW_SNS_CODE");
                   sns_url3 = ((JSONObject) ary_sns.get(2)).getString("SNS_URL");
                   sns_code4 = ((JSONObject) ary_sns.get(3)).getString("REVIEW_SNS_CODE");
                   sns_url4 = ((JSONObject) ary_sns.get(3)).getString("SNS_URL");
                   sns_code5 = ((JSONObject) ary_sns.get(4)).getString("REVIEW_SNS_CODE");
                   sns_url5 = ((JSONObject) ary_sns.get(4)).getString("SNS_URL");
               }

               for(i=0;i<ary_snslist.length();i++){
                   String channel_code = ((JSONObject) ary_snslist.get(i)).getString("CHANNEL_CODE");
                   if(channel_code.equals(sns_code1)) sns1_selectedIndex = i+1;
                   if(channel_code.equals(sns_code2)) sns2_selectedIndex = i+1;
                   if(channel_code.equals(sns_code3)) sns3_selectedIndex = i+1;
                   if(channel_code.equals(sns_code4)) sns4_selectedIndex = i+1;
                   if(channel_code.equals(sns_code5)) sns5_selectedIndex = i+1;
               }
               //blog - spiner 셋팅
               blog_spBlog1.setSelection(blog1_selectedIndex);
               blog_spBlog2.setSelection(blog2_selectedIndex);
               blog_spBlog3.setSelection(blog3_selectedIndex);
               blog_spBlog4.setSelection(blog4_selectedIndex);
               blog_spBlog5.setSelection(blog5_selectedIndex);

               //blog - title 셋팅
               ((EditText)findViewById(R.id.blog_etTitle1)).setText(blog_title1);
               ((EditText)findViewById(R.id.blog_etTitle2)).setText(blog_title2);
               ((EditText)findViewById(R.id.blog_etTitle3)).setText(blog_title3);
               ((EditText)findViewById(R.id.blog_etTitle4)).setText(blog_title4);
               ((EditText)findViewById(R.id.blog_etTitle5)).setText(blog_title5);
               //blog - url 셋팅
               ((EditText)findViewById(R.id.blog_etUrl1)).setText(blog_url1);
               ((EditText)findViewById(R.id.blog_etUrl2)).setText(blog_url2);
               ((EditText)findViewById(R.id.blog_etUrl3)).setText(blog_url3);
               ((EditText)findViewById(R.id.blog_etUrl4)).setText(blog_url4);
               ((EditText)findViewById(R.id.blog_etUrl5)).setText(blog_url5);

               //sns - spiner 셋팅
               sns_spSns1.setSelection(sns1_selectedIndex);
               sns_spSns2.setSelection(sns2_selectedIndex);
               sns_spSns3.setSelection(sns3_selectedIndex);
               sns_spSns4.setSelection(sns4_selectedIndex);
               sns_spSns5.setSelection(sns5_selectedIndex);

               //sns - url 셋팅
               ((EditText)findViewById(R.id.sns_etUrl1)).setText(sns_url1);
               ((EditText)findViewById(R.id.sns_etUrl2)).setText(sns_url2);
               ((EditText)findViewById(R.id.sns_etUrl3)).setText(sns_url3);
               ((EditText)findViewById(R.id.sns_etUrl4)).setText(sns_url4);
               ((EditText)findViewById(R.id.sns_etUrl5)).setText(sns_url5);
           }catch (Exception e){
               Log.i("wtkim",e.toString());
           }

       }catch (Exception e){

       }
    }
    public void saveHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if(err.equals("")){
                String result = json.getString("RESULT");
                if(result.equals("OK")){
                    dialogType = 2;
                    Common.createDialog(BlogActivity.this, getString(R.string.app_name).toString(), null, "매체 등록(변경)이 정상적으로 완료되었습니다.", getString(R.string.btn_ok), null, false, false);
                }
            }
        }catch (Exception e){

        }
    }

}
