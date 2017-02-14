package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

public class CulturelandActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultureland);
        common = new Common(this);


        //상품권 교환 선택시 클릭이벤트 추가
        ((ImageButton)findViewById(R.id.cultureland_ibCultureTransfer)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","상품권교환선택!");
                intent = new Intent(CulturelandActivity.this,CulturelandRequestActivity.class);
                startActivity(intent);
            }
        });
        //상품권 보관함 선택시 클릭이벤트 추가
        ((ImageButton)findViewById(R.id.cultureland_ibCulturelandCart)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","상품권 보관함 선택!");
                intent = new Intent(CulturelandActivity.this,CulturelandHistoryActivity.class);
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
        //기본정보 로드
        Object[][] params = {};
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_point), params);
    }

    @Override
    public void dialogHandler(String result) {

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
                String result = json.getString("RESULT");
                String email = json.getString("EMAIL");
                String point = json.getString("POINT");
                ((TextView)findViewById(R.id.cultureland_tvPoint)).setText(Common.getTvComma(point));
            }else{
                String result = json.getString("RESULT");
                //로그아웃인 경우
                if(result.equals("LOGOUT")){
                    finish();
                    intent = new Intent(CulturelandActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                }
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

}
