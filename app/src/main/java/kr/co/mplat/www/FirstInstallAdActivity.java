package kr.co.mplat.www;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class FirstInstallAdActivity extends FragmentActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    FirstInstallAdAdapter adapter;
    ViewPager pager;
    Context context = this;
    Common common = null;
    //Sqlite sqlite = new Sqlite(context);
    JSONArray ary_banners = new JSONArray();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_install_ad);
        common = new Common(this);

        ((TextView)findViewById(R.id.firstInstall_tvSkip)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstInstallAdActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        ((ImageView)findViewById(R.id.firstInstall_ivSkip)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstInstallAdActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

       // sqlite.getWritableDatabase();
        //sqlite.InsertData();

       /* String[] arrData = new String[3];
        arrData[0] = "AA";
        arrData[1] = "BB";
        arrData[2] = "CC";*/

        /*String[] arrData = sqlite.SelectData("1");
        if (arrData[0].length() > 0) {
            String Flag = arrData[1];
            if (Flag.equals("1")) {
                Intent intent = new Intent(context, App.class);
                startActivity(intent);
            }
        }*/

        adapter = new FirstInstallAdAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_main), null);
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
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                ary_banners = json.getJSONArray("BANNER");
                int i;
                for(i=0;i<ary_banners.length();i++){
                    String campaignCode = ((JSONObject)ary_banners.get(i)).getString("CAMPAIGN_CODE");
                    String campaignTypeCode = ((JSONObject)ary_banners.get(i)).getString("CAMPAIGN_TYPE_CODE");
                    String imgUrl = ((JSONObject)ary_banners.get(i)).getString("IMG_URL");
                }
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
