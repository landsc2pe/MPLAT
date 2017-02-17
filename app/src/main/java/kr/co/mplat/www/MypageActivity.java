package kr.co.mplat.www;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class MypageActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String email = "";
    private String authdate = "";
    private String regist_join_channel_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        common = new Common(this);

        //회원등급 선택시 이벤트 등록
        ((TextView)findViewById(R.id.mypage_tvUserGrade)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MypageActivity.this,GradeActivity.class);
                startActivity(i);
            }
        });
        //추천등급 선택시 이벤트 등록
        ((TextView)findViewById(R.id.mypage_tvRecommendGrade)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MypageActivity.this,RecommendGradeActivity.class);
                startActivity(i);
            }
        });
        //보유포인트 선택시 이벤트 등록
        ((TextView)findViewById(R.id.mypage_tvPoint)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MypageActivity.this,PointHistoryActivity.class);
                startActivity(i);
            }
        });
        //리스트 메뉴*********************************************************************************
        //회원정보 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangeUserInfo)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(authdate.equals("") && (regist_join_channel_type.equals("3")||regist_join_channel_type.equals("5"))) {//미인증회원정보(infoChange)
                    Log.i("wtkim", "미인증회원정보(InfoChangeActivity)시작!");
                    intent = new Intent(MypageActivity.this, InfoChangeActivity.class);
                    startActivity(intent);
                    finish();
                }else if(!authdate.equals("") && (regist_join_channel_type.equals("3")||regist_join_channel_type.equals("5"))) {//인증회원(infoChangeAuth)
                    Log.i("wtkim","인증회원(InfoChangeAuthEmailActivity)시작!");
                    intent = new Intent(MypageActivity.this,InfoChangeAuthEmailActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(MypageActivity.this,PwdActivity.class);
                    intent.putExtra("PRE_ACTIVITY","1");
                    startActivity(intent);
                }

            }
        });
        //비밀번호 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangePwd)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,PwdActivity.class);
                intent.putExtra("PRE_ACTIVITY","2");
                startActivity(intent);
            }
        });
        //부가정보 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangeAddInfo)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,JoinAddInfoActivity.class);
                intent.putExtra("PRE_ACTIVITY","3");
                startActivity(intent);
            }
        });
        //휴대전화번호 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangeMobile)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,MobileChangeActivity.class);

                startActivity(intent);
            }
        });
        //본인인증
        ((LinearLayout)findViewById(R.id.mypage_llAuth)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(authdate.equals("")){
                    intent = new Intent(MypageActivity.this,AuthDescActivity.class);

                    startActivity(intent);
                }else{
                    Common.createDialog(MypageActivity.this, getString(R.string.app_name).toString(),null, "회원님은 이미 휴대폰 본인인증 절차를 마치셨습니다.", getString(R.string.btn_ok),null, false, false);
                }

            }
        });
        //블로그/SNS 매체 등록 및 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangeSNS)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,BlogActivity.class);
                startActivity(intent);
            }
        });
        //Google Play 아이디 등록 및 변경 GoogleplayID 등록 삭제되었음 - 일단 주석처리
        /*((LinearLayout)findViewById(R.id.mypage_llChangeGooglePlay)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,GoogleActivity.class);
                intent.putExtra("PRE_ACTIVITY","MypageActivity");
                startActivity(intent);
            }
        });*/
        //은행 이체계좌 등록 및 변경
        ((LinearLayout)findViewById(R.id.mypage_llChangeAccount)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(authdate.equals("")){
                    intent = new Intent(MypageActivity.this,CashAuthDescActivity.class);

                    startActivity(intent);
                }else{
                    intent = new Intent(MypageActivity.this,BankActivity.class);
                    intent.putExtra("PRE_ACTIVITY","CashAuthDescActivity");
                    startActivity(intent);
                }


            }
        });

        //회원탈퇴 버튼 선택시
        ((Button)findViewById(R.id.mypage_btnWithraw)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,WithdrawActivity.class);
                startActivity(intent);
            }
        });
        //로그아웃
        ((Button)findViewById(R.id.mypage_btnLogout)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 9;
                Common.createDialog(MypageActivity.this,"로그아웃",null, "정말로 로그아웃 하시겠습니까?", getString(R.string.btn_yes),getString(R.string.btn_no), false, false);
            }
        });
        //쿠폰등록
        ((Button)findViewById(R.id.mypage_btnPointCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MypageActivity.this,PointCouponActivity.class);
                startActivity(intent);
            }
        });
        /*//메뉴 리스트뷰 생성
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU);
        ListView listView = (ListView) findViewById(R.id.mypage_lvMemu);
        listView.setAdapter(adapter);

        //메뉴 리스트뷰 클릭이벤트 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                String strText = (String) parent.getItemAtPosition(position) ;

                // TODO : use strText
            }
        }) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String strText = (String)adapterView.getItemAtPosition(i);
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        //메뉴 선택
        ChangeNavIcon(3);
        return ret;
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void dialogHandler(String result) {
        if(dialogType == 9 && result.equals("ok")){
            Common.setPreference(getApplicationContext(), "UID", "");
            Common.setPreference(getApplicationContext(), "KEY", "");

            intent = new Intent(MypageActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                /*
                REGIST_JOIN_CHANNEL_TYPE	1	엠플렛
                REGIST_JOIN_CHANNEL_TYPE	2	네이버
                REGIST_JOIN_CHANNEL_TYPE	3	페이스북
                REGIST_JOIN_CHANNEL_TYPE	4	카카오
                REGIST_JOIN_CHANNEL_TYPE	5	구글
                */
                String result = json.getString("RESULT");
                String email = json.getString("EMAIL");
                String point = json.getString("POINT");
                String grade = json.getString("GRADE");
                String grade_label = json.getString("GRADE_LABEL");
                String recommend_grade = json.getString("RECOMMEND_GRADE");
                String recommend_grade_label = json.getString("RECOMMEND_GRADE_LABEL");
                String  googleplay_id = json.getString("GOOGLEPLAY_ID");

                String email_changable = json.getString("EMAIL_CHANGABLE");
                authdate = json.getString("AUTH_DATE");
                regist_join_channel_type = json.getString("REGIST_JOIN_CHANNEL_TYPE");
                //배너 회원등급, 추천등급, 보유포인트
                ((TextView)findViewById(R.id.mypage_tvEmail)).setText(Html.fromHtml(email));
                ((TextView)findViewById(R.id.mypage_tvUserGrade)).setText(Html.fromHtml(grade_label+"<br/>회원등급"));
                ((TextView)findViewById(R.id.mypage_tvRecommendGrade)).setText(Html.fromHtml(recommend_grade_label+"<br/>추천등급"));
                ((TextView)findViewById(R.id.mypage_tvPoint)).setText(Html.fromHtml(Common.getTvComma(point)+"P<br/>보유포인트"));
                /* Mplat 아이디로 가입했을 경우 회원정보 변경 보임 비밀번호 변경 메뉴 보임
                카카오톡, 네이버로 가입했을 경우, 회원정보 변경메뉴 유지, 비밀번호 입력 불필요, 비밀번호 변경 메뉴 나타나지 않음
                페이스북 구글 아이디로 가입했을 경우, 회원정보 변경 문구 > 회원정보 보기로 명칭변경, 비밀번호 변경 메뉴 나타나지 않음
                 */
                Log.i("wtkim","regist_join_channel_type==>"+regist_join_channel_type);
                switch(regist_join_channel_type){
                    case "1":
                        break;
                    case "2":case "4":
                        ((LinearLayout)findViewById(R.id.mypage_llChangePwd)).setVisibility(View.GONE);
                        ((View)findViewById(R.id.mypage_llChangePwd_nextline)).setVisibility(View.GONE);
                        break;
                    case "3":case "5":
                        ((LinearLayout)findViewById(R.id.mypage_llChangePwd)).setVisibility(View.GONE);
                        ((View)findViewById(R.id.mypage_llChangePwd_nextline)).setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.mypage_tvChangeInfo)).setText("회원정보 보기");
                        break;
                }



            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
