package kr.co.mplat.www;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class CouponDetailActivity extends NAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private AlertDialog dialog = null;
    Handler handler = new Handler();
    Bitmap bm;
    Bitmap bm2;
    Bitmap bm3;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_RESERVE = 2;

    Common common = null;
    String campaign_code = "";
    JSONArray ary_images = new JSONArray();
    ArrayList<String> images = new ArrayList<String>();
    private int dialogType = 0;

    String img_icon = "";
    String app_title = "";
    String developer_name = "";
    String reward = "";
    String open_date = "";
    String reserve_yn = "";
    String join_yn = "";
    String app_open_yn = "";
    String android_url = "";
    String ios_url = "";
    String usable_yn = "";
    String coupon_num = "";
    String used_yn = "";
    String coupon_desc = "";
    String app_desc = "";
    String youtube = "";
    String share_url = "";

    String image1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        setTvTitle("상세 정보");
        common = new Common(this);
        campaign_code = getIntent().getStringExtra("CAMPAIGN_CODE").toString();

        //페이스북 초기화
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        //사전예약신청 이벤트 등록
        ((Button) findViewById(R.id.couponDetail_btnBooking)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object[][] params = {
                        {"CAMPAIGN_CODE", campaign_code}
                };
                common.loadData(CALLTYPE_RESERVE, getString(R.string.url_couponRequest), params);
            }
        });
        //게임 다운로드 버튼 선택시
        ((TextView) findViewById(R.id.couponDetail_tvDownload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!android_url.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(android_url));
                    startActivity(intent);
                } else {
                    Common.createDialog(CouponDetailActivity.this, getString(R.string.app_name).toString(), null, "다운로드링크가 없습니다.", getString(R.string.btn_ok), null, false, false);
                }


            }
        });
        //게임 공유하기 버튼 선택시
        ((TextView) findViewById(R.id.couponDetail_tvShare)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_coupondetail_share);
                dialog.show();
                ((TextView) dialog.findViewById(R.id.dialog_couponDetail_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //SNS 공유하기
                //카카오
                ((ImageView) dialog.findViewById(R.id.couponDetail_ivKakao)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(CouponDetailActivity.this);
                            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                            //메세지 추가
                            kakaoBuilder.addText("MPLAT");
                            String url = img_icon;
                            kakaoBuilder.addImage(url, 81, 81);
                            //앱 실행버튼 추가
                            //kakaoBuilder.addAppButton("앱 실행");
                            kakaoBuilder.addWebLink("링크열기", share_url);
                            //메세지 발송
                            kakaoLink.sendMessage(kakaoBuilder, CouponDetailActivity.this);

                        } catch (KakaoParameterException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //페이스북
                ((ImageView) dialog.findViewById(R.id.couponDetail_ivFacebook)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareFacebook();
                    }
                });
                //트위터
                ((ImageView) dialog.findViewById(R.id.couponDetail_ivTwitter)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareTwitter();
                    }
                });
                //카카오스토리
                ((ImageView) dialog.findViewById(R.id.couponDetail_ivKakaostroy)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String shareBody = share_url;
                        String appId = getPackageName();
                        String appName = app_title;
                        String urlInfo = "{title:'MPLAT',desc:'" + reward + "',imageurl:['" + img_icon + "']}";

                        String url = String.format(
                                "storylink://posting?post=%s&appid=%s&appver=1.0&apiver=1.0&appname=%s&urlinfo=%s",
                                shareBody, appId, appName, urlInfo);


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
        });

        //쿠폰안내 선택시
        ((TextView) findViewById(R.id.couponDetail_tvTab1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.couponDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_primary);
                ((TextView) findViewById(R.id.couponDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_default);
                ((ScrollView) findViewById(R.id.couponDetail_svTab1)).setVisibility(View.VISIBLE);
                ((ScrollView) findViewById(R.id.couponDetail_svTab2)).setVisibility(View.GONE);

            }
        });
        //게임소개 선택시
        ((TextView) findViewById(R.id.couponDetail_tvTab2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.couponDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_default);
                ((TextView) findViewById(R.id.couponDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_primary);
                ((ScrollView) findViewById(R.id.couponDetail_svTab1)).setVisibility(View.GONE);
                ((ScrollView) findViewById(R.id.couponDetail_svTab2)).setVisibility(View.VISIBLE);
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
        Object[][] params = {
                {"CAMPAIGN_CODE", campaign_code}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_couponDetail), params);
    }

    @Override
    public void dialogHandler(String result) {
        if (dialogType == 2 && result.equals("ok")) {
            //색변경
            ((Button) findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primaryDisabled);
            if (reserve_yn.equals("Y")) {
                ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("사전예약 신청완료");
            } else {
                ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("쿠폰 신청완료");
            }
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_RESERVE) reserveHandler(str);
    }

    public void reserveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                dialogType = 2;
                if (reserve_yn.equals("Y")) {
                    Common.createDialog(this, getString(R.string.app_name).toString(), null, "사전예약을 신청하였습니다.", getString(R.string.btn_ok), null, false, false);
                } else {
                    Common.createDialog(this, getString(R.string.app_name).toString(), null, "쿠폰을 신청하였습니다.", getString(R.string.btn_ok), null, false, false);
                }

            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err.toString(), getString(R.string.btn_ok), null, false, false);
            }

        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }


    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                img_icon = json.getString("IMG_ICON");
                app_title = json.getString("APP_TITLE");
                developer_name = json.getString("DEVELOPER_NAME");
                reward = json.getString("REWARD");
                open_date = json.getString("OPEN_DATE");
                reserve_yn = json.getString("RESERVE_YN");
                join_yn = json.getString("JOIN_YN");
                app_open_yn = json.getString("APP_OPEN_YN");
                android_url = json.getString("ANDROID_URL");
                ios_url = json.getString("IOS_URL");
                usable_yn = json.getString("USABLE_YN");
                coupon_num = json.getString("COUPON_NUM");
                used_yn = json.getString("USED_YN");
                coupon_desc = json.getString("COUPON_DESC");
                app_desc = json.getString("APP_DESC");
                youtube = json.getString("YOUTUBE");
                if (youtube.equals("")) {
                    ((WebView) findViewById(R.id.couponDetail_wvYoutube)).setVisibility(View.GONE);
                } else {
                    ((WebView) findViewById(R.id.couponDetail_wvYoutube)).setVisibility(View.VISIBLE);
                }

                share_url = json.getString("SHARE_URL");

                ary_images = json.getJSONArray("IMAGES");
                Log.i("wtkim", "ary_images==>" + ary_images);
                if (ary_images.length() > 0) {
                    ((ImageView) findViewById(R.id.couponDetail_ivTab2Image)).setVisibility(View.VISIBLE);
                } else {
                    ((ImageView) findViewById(R.id.couponDetail_ivTab2Image)).setVisibility(View.GONE);
                }
                int i;
                for (i = 0; i < ary_images.length(); i++) {
                    //우선 0번째것만 보이도록함
                    image1 = (String) ary_images.get(0);
                    Log.i("wtkim", image1);
                    // ((JSONObject)ary_images.get(i)).getString("JOIN_YN");
                }

                //사전예약신청 변경
                if (reserve_yn.equals("Y")) {//예약을 한경우는 disabled, 사전예약 신청 완료로 변경
                    if (join_yn.equals("N")) {
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("사전예약 신청");
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primary);
                    } else if (join_yn.equals("Y")) {
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("사전예약 신청완료");
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primaryDisabled);
                    }
                } else if (reserve_yn.equals("N")) {
                    if (join_yn.equals("N")) {
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("쿠폰 신청");
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primary);
                    } else if (join_yn.equals("Y")) {
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setText("쿠폰 신청완료");
                        ((Button) findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primaryDisabled);
                    }

                }

                //join_yn : 사전예약신청을 한경우
                // ((ImageView)findViewById(R.id.couponDetail_ivImageIcon)).setImageBitmap(bm);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {    // 오래 거릴 작업을 구현한다
                        // TODO Auto-generated method stub
                        try {
                            URL url = new URL(img_icon);
                            URL url2 = new URL(android_url);
                            URL url3 = new URL(image1);//tab2 > 슬라이드 이미지
                            InputStream is = url.openStream();
                            InputStream is2 = url.openStream();
                            InputStream is3 = url.openStream();
                            bm = BitmapFactory.decodeStream(is);
                            bm2 = BitmapFactory.decodeStream(is2);
                            bm3 = BitmapFactory.decodeStream(is3);
                            handler.post(new Runnable() {

                                @Override
                                public void run() {  // 화면에 그려줄 작업
                                    ((ImageView) findViewById(R.id.couponDetail_ivImageIcon)).setImageBitmap(bm);
                                    ((ImageView) findViewById(R.id.couponDetail_ivTab1Image)).setImageBitmap(bm2);
                                    ((ImageView) findViewById(R.id.couponDetail_ivTab2Image)).setImageBitmap(bm3);
                                }
                            });
                            //mContentView.setImageBitmap(bm); //비트맵 객체로 보여주기
                        } catch (Exception e) {
                            Log.d("wtkim", "---5");
                        }
                    }

                });
                t.start();

                ((TextView) findViewById(R.id.couponDetail_ivTitle)).setText(Html.fromHtml("<b>" + app_title + "</b>"));
                ((TextView) findViewById(R.id.couponDetail_ivDeveloperName)).setText(developer_name);
                ((TextView) findViewById(R.id.couponDetail_tvReward)).setText(reward);
                if (!open_date.equals("")) {
                    ((TextView) findViewById(R.id.couponDetail_openDate)).setText(Html.fromHtml("<font color='#D57A76'>출시예정일 : " + open_date + "</font>"));
                } else {
                    ((TextView) findViewById(R.id.couponDetail_openDate)).setText(Html.fromHtml("<font color='#D57A76'>출시예정일 : 미정</font>"));
                }
                //couponDetail_tvTab1Desc
                ((TextView) findViewById(R.id.couponDetail_tvTab1Desc)).setText(Html.fromHtml(coupon_desc));
                ((TextView) findViewById(R.id.couponDetail_tvTab2Desc)).setText(Html.fromHtml(app_desc));
                Log.i("wtkim", "youtube==>" + youtube);
                //유투브
                if (!youtube.equals("")) {
                    WebView wv_auth = (WebView) findViewById(R.id.couponDetail_wvYoutube);
                    wv_auth.getSettings().setPluginState(WebSettings.PluginState.ON);
                    wv_auth.getSettings().setSupportMultipleWindows(true);
                    WebSettings webSettings = wv_auth.getSettings();
                    webSettings.setJavaScriptEnabled(true); //자바스크립트 허용
                    wv_auth.getSettings().setDomStorageEnabled(true);
                    wv_auth.setWebViewClient(new WebViewClient());    //웹뷰 안에서 다른 페이지로 이동할 경우 웹뷰 안에서 이동하도록 함
                    wv_auth.loadUrl("http://youtube.com/embed/" + youtube);
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
                .setImageUrl(Uri.parse(img_icon))

                //공유될 링크
                .setContentUrl(Uri.parse(share_url))

                //게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
                .setContentDescription(app_title)
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.FEED);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.
    }

    public void shareTwitter() {
        String strLink = null;
        try {
            strLink = String.format("http://twitter.com/intent/tweet?text=%s", URLEncoder.encode(app_title, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);
    }

}
