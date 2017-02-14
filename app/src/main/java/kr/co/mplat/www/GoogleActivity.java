package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class GoogleActivity extends NAppCompatActivity implements View.OnClickListener,I_startFinish,I_dialogdata,I_loaddata{
    final int CALLTYPE_LOAD = 1;
    final int CALLTYPE_SAVE = 2;
    Intent intent = null;
    Common common = null;
    private String pre_activity = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        setTvTitle("Google Play 아이디 등록 및 변경");

        common = new Common(this);
        pre_activity = getIntent().getStringExtra("PRE_ACTIVITY").toString();
        //돌아가기 버튼 선택시 이벤트 등록
        ((Button)findViewById(R.id.google_btnBack)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(pre_activity.equals("MissionActivity")){
                    intent = new Intent(GoogleActivity.this,MissionActivity.class);
                    startActivity(intent);
                    finish();
                }else if(pre_activity.equals("MypageActivity")){
                    intent = new Intent(GoogleActivity.this,MypageActivity.class);
                    startActivity(intent);
                    finish();
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
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }
    //기본정보 로드
    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String  googleplay_id = json.getString("GOOGLEPLAY_ID");
                    //구글플레이 아이디가 있는경우
                    if(!googleplay_id.equals("")){
                        ((TextView)findViewById(R.id.google_tvLabel11)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.google_tvGoogleplayID)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.google_tvGoogleplayID)).setText(Html.fromHtml("<b>"+googleplay_id+"</b>"));
                        ((TextView)findViewById(R.id.google_tvRegYN)).setText(Html.fromHtml("<font color='#7161C4'>등록완료</font>"));
                        ((TextView)findViewById(R.id.google_tvInfo1)).setText(Html.fromHtml("<b>고객님은 <font color='#7161C4'>구글플레이 아이디가 등록된 회원</font>입니다.<br/><font color='#7161C4'>별점/댓글 미션 참여가 가능</font>합니다.</b>"));
                        ((TextView)findViewById(R.id.google_tvInfo2)).setText(Html.fromHtml("<b><font color='#D57A76'>구글 플레이 아이디 등록된 PC Web에서만 가능 합니다.</font></b><br/>(www.mplat.co.kr 접속 후 미션 > 구글플레이 아이디 등록)"));

                    }else{//구글플레이 아이디가 없는경우
                        ((TextView)findViewById(R.id.google_tvLabel11)).setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.google_tvGoogleplayID)).setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.google_tvRegYN)).setText(Html.fromHtml("<font color='#D57A76'>미등록</font>"));
                        ((TextView)findViewById(R.id.google_tvInfo1)).setText(Html.fromHtml("<b><font color='#D57A76'>별점/댓글 미션참여</font>를 위해서는<br/><font color='#D57A76'>먼저 구글플레이아이디를 등록</font> 하셔야 합니다.</b>"));
                        ((TextView)findViewById(R.id.google_tvInfo2)).setText(Html.fromHtml("<b><font color='#D57A76'>구글 플레이 아이디 등록된 PC Web에서만 가능 합니다.</font></b><br/>(www.mplat.co.kr 접속 후 미션 > 구글플레이 아이디 등록)"));
                    }

                }
            } else {
                Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_checkMobile).toString(), null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
