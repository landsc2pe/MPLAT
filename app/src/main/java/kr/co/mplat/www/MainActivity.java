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

public class MainActivity extends MAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Common common = null;
    private final int CALLTYPE_LOAD = 1;
    private Intent intent = null;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private int dialogType = 0;

    TextView tvEmail,tvPoint = null;
    ViewFlipper flipper = null;
    URL url = null;
    final static int SLIDE_INTERAVAL = 3000;//슬라이드 변경 시간
    Animation slideInLeft ;
    Animation slideInRight;
    Animation slideOutLeft ;
    Animation slideOutRight;
    JSONArray ary_banner = new JSONArray();
    ArrayList<String> banner = new ArrayList<>();


    //swife
    GestureDetector mDetector;
    final static int DISTANCE = 10;
    final static int VELOCITY = 10;

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

        //메인베너 슬라이드
        flipper = (ViewFlipper) findViewById(R.id.main_vf);
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

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
        //라운지
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
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    //제스쳐 이벤트 등록
    OnGestureListener mGestureListener = new OnGestureListener() {
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > VELOCITY) {
                if (e1.getX() - e2.getX() > DISTANCE) {
                    flipper.setInAnimation(slideInRight);
                    flipper.setOutAnimation(slideOutLeft);
                    flipper.showNext();
                }
                if (e2.getX() - e1.getX() > DISTANCE) {
                    flipper.setInAnimation(slideInLeft);
                    flipper.setOutAnimation(slideOutRight);
                    flipper.showPrevious();
                }
            }

            flipper.stopFlipping();
            flipper.startFlipping();
            return true;
        }

        public boolean onDown(MotionEvent e) {
            try{

                String campaignTypeCode = ((JSONObject) ary_banner.get(flipper.getDisplayedChild())).getString("CAMPAIGN_TYPE_CODE");
                String campaignCode = ((JSONObject) ary_banner.get(flipper.getDisplayedChild())).getString("CAMPAIGN_CODE");
                Log.i("wtKim","campaignTypeCode==>"+campaignTypeCode);
                Log.i("wtKim","campaignCode==>"+campaignCode);
            }catch(Exception e1){
                Log.i("wtKim",e1.toString());
            }
            Log.i("wtKim",String.valueOf(flipper.getDisplayedChild()));
            return false;
        }

        public void onLongPress(MotionEvent e) {

        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onShowPress(MotionEvent e) {

        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

    };


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
        Log.i("wtKim","onClick111 클릭!!");
        switch(view.getId()){
            case  R.id.menubar_ibNav4:
                Log.i("wtKim","menubar_ibNav4 클릭!!");
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
                        for (int i = 0; i < ary_banner.length(); i++) {
                            final ImageView img = new ImageView(this);
                            final int idx=i;

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              try {
                                                  url = new URL(((JSONObject) ary_banner.get(idx)).getString("IMG_URL"));
                                                  URLConnection conn = url.openConnection();
                                                  conn.connect();
                                                  BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                                                  Bitmap bm = BitmapFactory.decodeStream(bis);
                                                  bis.close();
                                                  img.setImageBitmap(bm);
                                                  img.setScaleType(ImageView.ScaleType.FIT_XY);
                                                  flipper.addView(img);
                                              } catch (Exception e) {
                                                  Log.i("wtKim", e.toString());
                                              }
                                          }
                                      }
                                    );
                                }
                            }).start();
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
                    flipper.setInAnimation(slideInLeft);
                    flipper.setOutAnimation(slideOutRight);
                    flipper.setFlipInterval(SLIDE_INTERAVAL);
                    flipper.startFlipping();

                    //swife
                    mDetector = new GestureDetector(this, mGestureListener);
                    mDetector.setIsLongpressEnabled(false);

                    //회원정보로드
                    tvEmail.setText(email);
                    tvPoint.setText(Common.getTvComma(point));

                }else{
                    if(result.equals("NOT OK")){
                        Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                        Log.i("wtkim","Not ok");
                    }
                }
            } else {
                String result = json.getString("RESULT");
                if(result.equals("NOT OK")){
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
                    Log.i("wtkim","1111");
                }
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
            Log.i("wtkim","2222");
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


