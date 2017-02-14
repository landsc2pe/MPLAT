package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class PointActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    //dialogType :0 - 기본, 1 - 로그인, 9 - 종료
    private int dialogType = 0;
    Common common = null;
    private String email = "";
    private String point = "";
    private String authMobileYn = "";
    private String bankAccountYn = "";
    /*
    Log.i("wtkim","email==>"+email);
    Log.i("wtkim","point==>"+point);
    Log.i("wtkim","authMobileYn==>"+authMobileYn);
    Log.i("wtkim","bankAccountYn==>"+bankAccountYn);
    Log.i("wtkim","result==>"+result);
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        common = new Common(this);

        // 현금이체 선택시
        ((ImageView)findViewById(R.id.point_ibTranspermoney)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","현금이체 선택!");
                //본인인증하지 않고 현금이체를 진행할 경우
                if(authMobileYn.equals("N")){
                    intent = new Intent(PointActivity.this,AuthActivity.class);
                    startActivity(intent);
                //계좌등록하지 않고 현금이체를 진행할 경우
                }else if(bankAccountYn.equals("N")) {
                    intent = new Intent(PointActivity.this, BankActivity.class);
                    startActivity(intent);
                }else{
                //본인인증을 한경우
                    intent = new Intent(PointActivity.this,CashActivity.class);
                    startActivity(intent);
                }
            }
        });

        // 문화상품권 선택시
        ((ImageView)findViewById(R.id.point_ibCulturegift)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(PointActivity.this,CulturelandActivity.class);
                startActivity(intent);
            }
        });
        // 모바일상품권 선택시
        ((ImageView)findViewById(R.id.point_ibMobilegift)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","모바일 상품권 선택!");
                intent = new Intent(PointActivity.this,GiftActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("상점");
        boolean ret = super.onCreateOptionsMenu(menu);
        //메뉴 선택
        ChangeNavIcon(2);
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void dialogHandler(String result) {
        //dialog 확인버튼 누를시, 로그인페이지로 보냄
        if(result.equals("ok") && dialogType == 1){
            intent = new Intent(PointActivity.this,LoginActivity.class);
            startActivity(intent);
        };
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
                email = json.getString("EMAIL");
                point = json.getString("POINT");
                authMobileYn = json.getString("AUTH_MOBILE_YN");
                bankAccountYn = json.getString("BANK_ACCOUNT_YN");
                String result = json.getString("RESULT");

                //email ,포인트 표시
                //((TextView)findViewById(R.id.main_tvEmail)).setText(email);
                ((TextView)findViewById(R.id.main_tvPoint)).setText(Common.getTvComma(point));
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
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
        //정보 로드
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");

        if(UID.equals("")||KEY.equals("")){
            dialogType = 1;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, "다시 로그인해주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
        }else {
            //기본정보 호출
            Object[][] params = {};
            common.loadData(CALLTYPE_LOAD, getString(R.string.url_point), params);
        }
    }
}
