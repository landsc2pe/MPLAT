package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class EventDetailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
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
        setContentView(R.layout.activity_event_detail);
        common = new Common(this);

        setTvTitle("이벤트 상세내용");
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_eventDetail), params);
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

            String title = "";
            String contents = "";
            String contentsImg = "";
            String startDate = "";
            String endDate = "";
            if (err.equals("")) {
                title = json.getString("TITLE");
                contents = json.getString("CONTENTS");
                contentsImg = json.getString("CONTENTS_IMG");
                startDate = json.getString("START_DATE");
                endDate = json.getString("END_DATE");
                //문구변경
                ((TextView)findViewById(R.id.eventDetail_tvDate)).setText(startDate+"~"+endDate);
                ((TextView)findViewById(R.id.eventDetail_tvTitle)).setText(title);
                WebView wv_auth = (WebView) findViewById(R.id.authWv_wvContents);
                WebSettings webSettings = wv_auth.getSettings();
                webSettings.setJavaScriptEnabled(true); //자바스크립트 허용

                wv_auth.setWebViewClient(new WebViewClient());    //웹뷰 안에서 다른 페이지로 이동할 경우 웹뷰 안에서 이동하도록 함
                wv_auth.loadData(contents,  "text/html; charset=UTF-8", null);
                wv_auth.setInitialScale(1);
                wv_auth.getSettings().setUseWideViewPort(true);
                wv_auth.getSettings().setMinimumFontSize(40);

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
