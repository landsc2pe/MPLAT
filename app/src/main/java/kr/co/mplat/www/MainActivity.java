package kr.co.mplat.www;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.*;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import kr.co.marketlink.jsyang.ImageViewPager;

public class MainActivity extends MAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Common common = null;
    private final int CALLTYPE_LOAD = 1;
    private Intent intent = null;
    private long backPressedTime = 0;
    private int dialogType = 0;
    private final long FINISH_INTERVAL_TIME = 2000;
    boolean bannerLoad=false;

    TextView tvEmail,tvPoint = null;
    JSONArray ary_banner = new JSONArray();
    ArrayList<String> banner = new ArrayList<>();





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("메인");
        boolean ret = super.onCreateOptionsMenu(menu);
        ChangeNavIcon(1);
        return ret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        common = new Common(this);


        tvEmail = (TextView) findViewById(R.id.main_tvEmail);   //이메일
        tvPoint = (TextView) findViewById(R.id.main_tvPoint);   //포인트


        //등급
        ((LinearLayout)findViewById(R.id.main_llGrade)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,GradeActivity.class);
                intent.putExtra("EMAIL",tvEmail.getText().toString());
                startActivity(intent);
            }
        });
        //포인트
        ((LinearLayout)findViewById(R.id.main_llPoint)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,PointHistoryActivity.class);
                //intent.putExtra("EMAIL",tvEmail.getText().toString());
                startActivity(intent);
            }
        });
        //쿠폰
        ((LinearLayout)findViewById(R.id.main_llCoupon)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,CouponActivity.class);
                //intent.putExtra("EMAIL",tvEmail.getText().toString());
                startActivity(intent);
            }
        });

        //쿠폰 > 나의쿠폰함 선택 이벤트 등록
        //쿠폰 > 쿠폰 자동발급 선택 이벤트 등록

        //미션
        ((LinearLayout)findViewById(R.id.main_llMission)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,MissionActivity.class);
                startActivity(intent);
            }
        });

        //리뷰
        ((LinearLayout)findViewById(R.id.main_llReview)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
        });
        //설문조사
        ((LinearLayout)findViewById(R.id.main_llSurvey)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,SurveyActivity.class);
                startActivity(intent);
            }
        });
        //라운지
        ((LinearLayout)findViewById(R.id.main_llLounge)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,EventActivity.class);
                startActivity(intent);
            }
        });
        //회원추천
        ((LinearLayout)findViewById(R.id.main_llRecommend)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,RecommendActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }




    @Override
    public void onBackPressed() {
        dialogType = 9;//종료타입
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            //super.onBackPressed();
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }else {
            backPressedTime = tempTime;
            Toast.makeText(this, "한번 더 누르면 MPLAT을 종료합니다", Toast.LENGTH_SHORT).show();
           /* common.setPreference(getApplicationContext(), "UID", "");
            common.setPreference(getApplicationContext(), "KEY", "");*/

        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case  R.id.menubar_ibNav4:
                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void monClick(View view) {
        Log.i("wtKim","onClick222 클릭!!");
        switch(view.getId()){
            case  R.id.menubar_ibNav4:
                Log.i("wtKim","menubar_ibNav4 클릭!!");
                intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);

    }
    public void loadHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String email = json.getString("EMAIL");
                    String point = json.getString("POINT");
                    ary_banner = json.getJSONArray("BANNER");

                    if(android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }

                    try {
                        List<String> urls=new ArrayList<>();
                        for (int i = 0; i < ary_banner.length(); i++) {
                            JSONObject object=ary_banner.getJSONObject(i);
                            urls.add(object.getString("IMG_URL"));
                        }
                        if(!bannerLoad) {
                            ImageViewPager imageViewPager = (ImageViewPager) findViewById(R.id.ivp);
                            ImageViewPager.ImageViewPagerClickListener imageViewPagerClickListener=new ImageViewPager.ImageViewPagerClickListener() {
                                @Override
                                public void onClick(int position) {
                                    Log.d("MYLOG",Integer.toString(position));
                                }
                            };
                            imageViewPager.start(this, urls,imageViewPagerClickListener, 1000, 3000);
                            bannerLoad=true;
                        }
                    }catch(Exception e){
                        Log.i("wtkim",e.toString());
                    }

                   /*for(int i=0;i<5;i++){
                        String ImageName = "sample"+i;
                        ImageView img = new ImageView(this);
                        img.setImageResource(R.drawable.sample1+i);
                        img.setScaleType(ImageView.ScaleType.FIT_XY);
                        flipper.addView(img);
                    }*/

                    //회원정보로드
                    tvEmail.setText(email);
                    tvPoint.setText(Common.getTvComma(point));

                }else{
                    if(result.equals("NOT OK")){
                        Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                    }
                }
            } else {
                String result = json.getString("RESULT");
                if(result.equals("NOT OK")){
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                }
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            dialogType = -1;
            common.showCheckNetworkDialog();
            return;
        }

        String UID = Common.getPreference(getApplicationContext(), "UID");
        String KEY = Common.getPreference(getApplicationContext(), "KEY");

        //기본정보 호출
        Object[][] params = {};
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_main), params);
    }

    public Bitmap ImageDownload(String imageURL){
        try{
            ImageView img = new ImageView(this);
            URL url = new URL(imageURL);
            URLConnection conn = url.openConnection();
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();

            return bm;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}


