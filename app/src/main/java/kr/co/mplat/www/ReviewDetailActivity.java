package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ReviewDetailActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata, OnMapReadyCallback {
    private AlertDialog dialog = null;
    private GoogleMap mMap;
    Common common = null;
    Intent intent = null;
    String campaign_code = "";
    private final int CALLTYPE_BASICLOAD = 1;
    private final int CALLTYPE_LOAD = 2;
    private final int CALLTYPE_COMMENT = 3;
    private int dialogType = 0;
    private String lastseq = "";
    private Double mapX;
    private Double mapY;


    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();

    private String title = "";
    private String shareUrl = "";
    private String reviewUrl = "";
    private String reviewDesc = "";
    private String guidelineFile = "";
    private String blogSnsYn = "";
    private String authDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);




        setTvTitle("리뷰 상세 정보");
        common = new Common(this);
        intent = getIntent();
        if (intent.hasExtra("CAMPAIGN_CODE")) {
            campaign_code = getIntent().getStringExtra("CAMPAIGN_CODE").toString();
        }

        //공유하기
        ((TextView) findViewById(R.id.reviewDetail_tvShare)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_reviewdetail_share);
                dialog.show();
                ((TextView) dialog.findViewById(R.id.dialog_reviewDetail_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //SNS 공유하기
                //카카오
                ((ImageView) dialog.findViewById(R.id.reviewDetail_ivKakao)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(ReviewDetailActivity.this);
                            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                            //메세지 추가
                            kakaoBuilder.addText(title);
                            //String url = reviewUrl;
                            //kakaoBuilder.addImage(url,81,81);
                            //앱 실행버튼 추가
                            //kakaoBuilder.addAppButton("앱 실행");
                            kakaoBuilder.addWebLink("링크열기", shareUrl);
                            //메세지 발송
                            kakaoLink.sendMessage(kakaoBuilder, ReviewDetailActivity.this);

                        } catch (KakaoParameterException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //페이스북
                ((ImageView) dialog.findViewById(R.id.reviewDetail_ivFacebook)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareFacebook();
                    }
                });
                //트위터
                ((ImageView) dialog.findViewById(R.id.reviewDetail_ivTwitter)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareTwitter();
                    }
                });
                //카카오스토리
                ((ImageView) dialog.findViewById(R.id.reviewDetail_ivKakaostroy)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shareBody = shareUrl;
                        String appId = getPackageName();
                        String appName = title;
                        String urlInfo = "{title:'MPLAT',desc:'" + reviewDesc + "'}";

                        String url = String.format(
                                "storylink://posting?post=%s&appid=%s&appver=1.0&apiver=1.0&appname=%s&urlinfo=%s",
                                shareBody, appId, appName, urlInfo);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
        });
        //리뷰소개

        //미션참여 방법
        ((TextView) findViewById(R.id.reviewDetail_tvTab1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScrollView) findViewById(R.id.reviewDetail_svTab1)).setVisibility(View.VISIBLE);
                ((ScrollView) findViewById(R.id.reviewDetail_svTab2)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.reviewDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_primary);
                ((TextView) findViewById(R.id.reviewDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_default);
            }
        });

        //신청자 명단
        ((TextView) findViewById(R.id.reviewDetail_tvTab2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ScrollView) findViewById(R.id.reviewDetail_svTab1)).setVisibility(View.GONE);
                ((ScrollView) findViewById(R.id.reviewDetail_svTab2)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.reviewDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_default);
                ((TextView) findViewById(R.id.reviewDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_primary);


                Object[][] params = {
                        {"LAST_SEQ", lastseq}
                        , {"CAMPAIGN_CODE", campaign_code}
                };
                common.loadData(CALLTYPE_COMMENT, getString(R.string.url_reviewComment), params);

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
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        common.loadData(CALLTYPE_BASICLOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_BASICLOAD) basicloadHandler(str);
        else if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_COMMENT) commentHandler(str);
    }

    public void commentHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                String cnt = json.getString("CNT");
                String step = json.getString("STEP");
                String last_seq = json.getString("LAST_SEQ");

                ListView listView;
                ReviewDetailAdapter adapter;

                adapter = new ReviewDetailAdapter();
                listView = (ListView) findViewById(R.id.reviewDetail_lvTab2listview);
                listView.setAdapter(adapter);
                int i;
                String email = "";
                String comment = "";
                String registDatetime = "";
                for (i = 0; i < ary_lists.length(); i++) {
                    email = ((JSONObject) ary_lists.get(i)).getString("EMAIL");
                    comment = ((JSONObject) ary_lists.get(i)).getString("COMMENT");
                    registDatetime = ((JSONObject) ary_lists.get(i)).getString("REGIST_DATETIME");
                    adapter.addItem(email, comment, registDatetime);
                }

            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    public void basicloadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                authDate = json.getString("AUTH_DATE");
                //상세정보로드
                Object[][] params = {
                        {"CAMPAIGN_CODE", campaign_code}
                };
                common.loadData(CALLTYPE_LOAD, getString(R.string.url_reviewDetail), params);
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");

            if (err.equals("")) {
                String imgIcon = json.getString("IMG_ICON");
                title = json.getString("TITLE");
                String reward = json.getString("REWARD");
                String reviewMethod = json.getString("REVIEW_METHOD");
                String targetCnt = json.getString("TARGET_CNT");
                String joinCnt = json.getString("JOIN_CNT");
                String joinYn = json.getString("JOIN_YN");
                reviewDesc = json.getString("REVIEW_DESC");
                shareUrl = json.getString("SHARE_URL");
                String step = json.getString("STEP");
                String step1Date = json.getString("STEP1_DATE");
                String step2Date = json.getString("STEP2_DATE");
                String step3Date = json.getString("STEP3_DATE");
                String step4Date = json.getString("STEP4_DATE");
                blogSnsYn = json.getString("BLOG_SNS_YN");
                String mapXy = json.getString("MAP_XY");
                if(!mapXy.equals("")){
                    String[] ary_map = mapXy.split("\\:::");
                    mapY = Double.parseDouble(ary_map[0]);
                    mapX = Double.parseDouble(ary_map[1]);
                    //구글맵
                    SupportMapFragment mapFragment =
                            (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                }

                String guideline = json.getString("GUIDELINE");
                guidelineFile = json.getString("GUIDELINE_FILE");
                final String information = json.getString("INFORMATION");
                String infoUrl = json.getString("INFO_URL");
                String sample = json.getString("SAMPLE");
                String beware = json.getString("BEWARE");
                String joinBeware = json.getString("JOIN_BEWARE");
                String keyword = json.getString("KEYWORD");
                String reviewUrl = json.getString("REVIEW_URL");
                String reviewReason = json.getString("REVIEW_REASON");
                String reviewEndDate = json.getString("REVIEW_END_DATE");

                ImageView ivImageIcon = (ImageView) findViewById(R.id.reviewDetail_ivImageIcon);
                Picasso.with(getApplicationContext()).load(imgIcon).resize(75, 75).into(ivImageIcon);
                ((TextView) findViewById(R.id.reviewDetail_tvTitle)).setText(Html.fromHtml(title));
                ((TextView) findViewById(R.id.reviewDetail_tvReward)).setText(Html.fromHtml("<font color='#7161C4'>" + reward + "</font>"));
                ((TextView) findViewById(R.id.reviewDetail_tvReviewMethod)).setText(Html.fromHtml(reviewMethod));
                ((TextView) findViewById(R.id.reviewDetail_tvJoinInfo)).setText(Html.fromHtml("<font color='#7161C4'>" + targetCnt + "명 선정</font> (현재 " + joinCnt + "명 신청)"));
                ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setText(Html.fromHtml("<font>리뷰 신청<br/>" + step1Date + "</font>"));
                ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setText(Html.fromHtml("<font>리뷰어 선정<br/>" + step2Date + "</font>"));
                ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setText(Html.fromHtml("<font>리뷰 등록<br/>" + step3Date + "</font>"));
                ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setText(Html.fromHtml("<font>리뷰 종료<br/>" + step4Date + "</font>"));


                //리뷰신청
                ((TextView) findViewById(R.id.reviewDetail_tvRegist)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!authDate.equals("")) {//본인인증을 받은경우
                            if (blogSnsYn.equals("Y")) {//블로그/SNS 매체 등록한 경우
                                intent = new Intent(ReviewDetailActivity.this, ReviewRequest1Activity.class);
                                intent.putExtra("CAMPAIGN_CODE", campaign_code);
                                startActivity(intent);
                            } else {//블로그/SNS 매체 등록하지 않은 경우
                                dialogType = 2;
                                Common.createDialog(ReviewDetailActivity.this, getString(R.string.app_name).toString(), null, "리뷰신청은 블로그 및 SNS 매체를 등록하신 후 이용하실 수 있습니다. 지금 바로 등록 하시겠습니까?", getString(R.string.btn_cancel), getString(R.string.btn_ok), false, false);
                            }
                        } else {//본인인증을 받지 않은경우
                            intent = new Intent(ReviewDetailActivity.this, AuthDescActivity.class);
                            intent.putExtra("PRE_ACTIVITY", "ReviewDetailActivity");
                            intent.putExtra("CAMPAIGN_CODE", campaign_code);
                            startActivity(intent);
                        }
                    }
                });

                //리뷰소개
                //제공내역
                if (!reward.equals("")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Reward)).setText(Html.fromHtml("<font>" + reward + "</font>"));
                } else {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Reward)).setText(Html.fromHtml("<font>-</font>"));
                }

                //검색키워드
                if (!keyword.equals("")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Keyword)).setText(Html.fromHtml("<font>" + keyword + "</font>"));
                } else {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Keyword)).setText(Html.fromHtml("<font>-</font>"));
                }

                //리뷰안내
                if (!information.equals("")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvInfomation)).setText(Html.fromHtml("<font>" + information + "</font>"));
                } else {
                    ((TextView) findViewById(R.id.reviewDetail_tvInfomation)).setText(Html.fromHtml("<font>-</font>"));
                }

                //리뷰 작성 가이드 라인
                if (!guideline.equals("")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Guideline)).setText(Html.fromHtml("<font>" + guideline + "</font>"));
                } else {
                    ((TextView) findViewById(R.id.reviewDetail_tvTab1Guideline)).setText(Html.fromHtml("<font>-</font>"));
                }

                //다운로드
                ((Button) findViewById(R.id.reviewDetail_btnDownloadGuide)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!guidelineFile.equals("")) {
                            if (guidelineFile.contains("mplat.co.kr")) {
                                String url = guidelineFile;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            } else {
                                String url = "http://mplat.co.kr" + guidelineFile;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        } else {
                            Common.createDialog(ReviewDetailActivity.this, getString(R.string.app_name).toString(), null, "첨부된 가이드라인이 없습니다.", getString(R.string.btn_ok), null, false, false);
                        }
                    }
                });
                //리뷰 참여방법안내
                ((TextView) findViewById(R.id.reviewDetail_tvTab1Beware)).setText(Html.fromHtml("<font>" + beware + "</font>"));
                ((TextView) findViewById(R.id.reviewDetail_tvTab1JoinBeware)).setText(Html.fromHtml("<font>" + joinBeware + "</font>"));

                ImageView ivTab1Image = (ImageView) findViewById(R.id.reviewDetail_ivTab1Image);
                Picasso.with(getApplicationContext()).load(imgIcon).into(ivTab1Image);

                //((TextView)findViewById(R.id.reviewDetail_tvTab1Desc)).setText(Html.fromHtml(reviewDesc));
                //reviewDetail_tvTab1JoinBeware

                //step 색변경
                if (step.equals("1")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setBackgroundResource(R.color.primary);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                } else if (step.equals("2")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setBackgroundResource(R.color.primary);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                } else if (step.equals("3")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setBackgroundResource(R.color.primary);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                } else if (step.equals("4")) {
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep1)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep2)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setBackgroundResource(R.drawable.border_padding0);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep3)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryFont));
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setBackgroundResource(R.color.primary);
                    ((TextView) findViewById(R.id.reviewDetail_tvStep4)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {
        if (dialogType == 2 && result.equals("cancel")) {//확인인경우
            intent = new Intent(ReviewDetailActivity.this, BlogActivity.class);
            intent.putExtra("PRE_ACTIVITY", "ReviewDetailActivity");
            intent.putExtra("CAMPAIGN_CODE", campaign_code);
            startActivity(intent);
        }
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private AlertDialog createDialog(int layoutResource) {
        if (dialog != null && dialog.isShowing()) return dialog;
        final View innerView = getLayoutInflater().inflate(layoutResource, null);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(layoutResource);
        return ab.create();
    }

    public void setDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void shareFacebook() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                //링크의 콘텐츠 제목
                .setContentTitle("MPLAT")
                //게시물에 표시될 썸네일 이미지의 URL
                //.setImageUrl(Uri.parse(reviewUrl))
                //공유될 링크
                .setContentUrl(Uri.parse(shareUrl))

                //게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
                .setContentDescription(title)
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.FEED);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.
    }

    public void shareTwitter() {
        String strLink = null;
        try {
            strLink = String.format("http://twitter.com/intent/tweet?text=%s", URLEncoder.encode(title, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);
    }

   /* @Override
    public void onMapReady(GoogleMap map) {
        mMap = googleMap;

        LatLng seoul = new LatLng( 37.59,127.149 );
        mMap.addMarker( new MarkerOptions().position(seoul).title( "Marker in Seoul" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLng(seoul) );

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng seoul = new LatLng( mapX,mapY );
        mMap.addMarker( new MarkerOptions().position(seoul).title( "" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLng(seoul) );

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);
    }
}
