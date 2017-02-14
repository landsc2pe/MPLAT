package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class EventResultDetailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
    Common common = null;
    String campaign_code = "";
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_CHECK = 2;
    private int dialogType = 0;
    private String seq = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_result_detail);

        common = new Common(this);
        setTvTitle("이벤트 당첨자 상세내용");
        Intent intent = getIntent();
        seq = intent.getExtras().getString("SEQ");
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
        Object[][] params = {
                {"SEQ",seq}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_eventResultDetail), params);
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
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            String title = "";
            String contents = "";
            String registDate = "";

            if (err.equals("")) {
                title = json.getString("TITLE");
                contents = json.getString("CONTENTS");
                registDate = json.getString("REGIST_DATE");

                //문구변경
                ((TextView)findViewById(R.id.eventResultDetail_tvDate)).setText(registDate);
                ((TextView)findViewById(R.id.eventResultDetail_tvTitle)).setText(title);
                ((TextView)findViewById(R.id.evenResulttDetail_tvContent)).setText(Html.fromHtml(contents));

            }else{
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
