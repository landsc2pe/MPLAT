package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONObject;

public class AuthActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    //dialogType :0 - 기본, 1 - 로그인, 9 - 종료
    private int dialogType = 0;
    Common common = null;
    private String email = "";
    private String point = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        //setTvTitle("본인인증");
        common = new Common(this);

        //인증확인체크시 이벤트
        final CheckBox auth_cbAgree_1 = (CheckBox)findViewById(R.id.auth_cbAgree_1);
        auth_cbAgree_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(auth_cbAgree_1.isChecked()){
                    findViewById(R.id.auth_btnNext).setBackgroundResource(R.color.primary);
                    auth_cbAgree_1.setTextColor(getResources().getColor(R.color.primary));
                }else{
                    auth_cbAgree_1.setTextColor(getResources().getColor(R.color.primaryFont));
                    findViewById(R.id.auth_btnNext).setBackgroundResource(R.color.primaryDisabled);
                }
            }
        });
        //다음 버튼 클릭 이벤트
        findViewById(R.id.auth_btnNext).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(auth_cbAgree_1.isChecked()){
                    finish();
                    intent = new Intent(AuthActivity.this,AuthWvActivity.class);
                    intent.putExtra("URL",getString(R.string.url_authwv));
                    startActivity(intent);
                }else{
                    Common.createDialog(AuthActivity.this, getString(R.string.app_name).toString(),null, "회원정보변경 안내확인에 체크해 주세요.", getString(R.string.btn_ok),null, false, false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        //메뉴 선택
        ChangeNavIcon(2);
        return ret;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //정보 로드
        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");
        Log.i("wtkim","UID==>"+UID);
        Log.i("wtkim","KEY==>"+KEY);
        //네트워크 상태 확인
        /*if(!common.isConnected()) {
            Log.i("wtKim","isConnected==>"+common.isConnected());
            //common.showCheckNetworkDialog();
            return;
        }*/
        if(UID.equals("")||KEY.equals("")){
            dialogType = 1;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, "다시 로그인해주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
        }else {
            //기본정보 호출
            Object[][] params = {};
            common.loadData(CALLTYPE_LOAD, getString(R.string.url_point), params);
        }
    }

    @Override
    public void dialogHandler(String result) {
        //dialog 확인버튼 누를시, 로그인페이지로 보냄
        if(result.equals("ok") && dialogType == 1){
            finish();
            intent = new Intent(AuthActivity.this,LoginActivity.class);
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
                String result = json.getString("RESULT");

                //email ,포인트 표시
                //((TextView)findViewById(R.id.main_tvEmail)).setText(email);
                ((TextView)findViewById(R.id.main_tvPoint)).setText(point);
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
    }
}
