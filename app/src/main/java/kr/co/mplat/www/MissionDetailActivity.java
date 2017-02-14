package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MissionDetailActivity extends NAppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
    Common common = null;
    String campaign_code = "";
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_CHECK = 2;
    private int dialogType = 0;
    //슬라이더
    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

    JSONArray ary_images = new JSONArray();
    ArrayList<String> images = new ArrayList<String>();
    String imgIcon_str = "";
    String appTitle_str = "";
    String developerName_str = "";
    String point_str = "";
    String pointRealtimeYn_str = "";
    String pointDate_str = "";
    String installUrl_str = "";
    String joinYn_str = "";
    String appDesc_str = "";
    String youtube_str = "";
    String mission_str = "";
    String missionDesc_str = "";
    String shareUrl_str = "";
    String authYn_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);
        setTvTitle("상세 정보");
        common = new Common(this);
        campaign_code = getIntent().getStringExtra("CAMPAIGN_CODE").toString();


        //참여하기
        ((TextView)findViewById(R.id.missionDetail_tvDownload)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!installUrl_str.equals("")){
                    Object[][] params = {
                            {"CAMPAIGN_CODE",campaign_code}
                    };
                    common.loadData(CALLTYPE_CHECK, getString(R.string.url_missionDetail), params);
                }else{
                    Common.createDialog(MissionDetailActivity.this, getString(R.string.app_name).toString(),null, "다운로드링크가 없습니다.", getString(R.string.btn_ok),null, false, false);
                }
            }
        });
        //공유하기
        ((TextView)findViewById(R.id.missionDetail_tvShare)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_coupondetail_share);
                dialog.show();
                ((TextView)dialog.findViewById(R.id.dialog_couponDetail_ok)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //SNS 공유하기
                //카카오
                ((ImageView)dialog.findViewById(R.id.couponDetail_ivKakao)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        try{
                            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(MissionDetailActivity.this);
                            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                            //메세지 추가
                            kakaoBuilder.addText("MPLAT");
                            String url = imgIcon_str;
                            kakaoBuilder.addImage(url,81,81);
                            //앱 실행버튼 추가
                            //kakaoBuilder.addAppButton("앱 실행");
                            kakaoBuilder.addWebLink("링크열기",shareUrl_str);
                            //메세지 발송
                            kakaoLink.sendMessage(kakaoBuilder,MissionDetailActivity.this);

                        }catch(KakaoParameterException e){
                            e.printStackTrace();
                        }
                    }
                });

                //페이스북
                ((ImageView)dialog.findViewById(R.id.couponDetail_ivFacebook)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        shareFacebook();
                    }
                });
                //트위터
                ((ImageView)dialog.findViewById(R.id.couponDetail_ivTwitter)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        shareTwitter();
                    }
                });
                //카카오스토리
                ((ImageView)dialog.findViewById(R.id.couponDetail_ivKakaostroy)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String shareBody = shareUrl_str;
                        String appId = getPackageName();
                        String appName = appTitle_str;
                        String urlInfo = "{title:'MPLAT',desc:'"+missionDesc_str+"',imageurl:['"+imgIcon_str+"']}";

                        String url = String.format(
                                "storylink://posting?post=%s&appid=%s&appver=1.0&apiver=1.0&appname=%s&urlinfo=%s",
                                shareBody, appId, appName, urlInfo);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });
            }
        });
        //미션참여 방법
        ((TextView)findViewById(R.id.missionDetail_tvTab1)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((ScrollView)findViewById(R.id.missionDetail_svTab1)).setVisibility(View.VISIBLE);
                ((ScrollView)findViewById(R.id.missionDetail_svTab2)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.missionDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_primary);
                ((TextView)findViewById(R.id.missionDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_default);
            }
        });
        //미션 참여방법 > 포인트 적립안내 클릭이벤트
        ((TextView)findViewById(R.id.missionDetail_tvTab1PointDesc)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_point_info);
                dialog.show();

                //포인트 적립안내 dialog 문구변경
                if(pointRealtimeYn_str.equals("Y")){
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo11)).setText(Html.fromHtml("해당 미션은 포인트가 <font color='#7161C4'>즉시 적립</font>되는 미션으로 미션 완료 즉시 아이디로 포인트가 적립됩니다."));
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo21)).setText(Html.fromHtml("미션 참여 내역 및 포인트 적립 내역은 <font color='#7161C4'>[미션 참여 내역]</font> 메뉴를 참고해주시기 바랍니다."));


                }else{
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo11)).setText(Html.fromHtml("해당 미션은 포인트가 추후에 지급되는 미션으로 미션완료 후 <font color='#7161C4'>예정 적립일("+pointDate_str+")</font>에 모든 회원님들에게 일괄적으로 포인트가 적립됩니다."));
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo21)).setText(Html.fromHtml("미션 참여 내역 및 포인트 적립 내역은 <font color='#7161C4'>[미션 참여 내역]</font> 메뉴를 참고해주시기 바랍니다."));

                }

                if(authYn_str.equals("Y")){
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo31)).setText(Html.fromHtml("휴대폰 본인인증이 되어 있지 않으면 미션에 참여하실 수 없습니다. <font color='#7161C4'>[회원님께서는 휴대폰 본인인증이 완료 되었습니다.]</font> "));
                }else{
                    ((TextView)dialog.findViewById(R.id.dialog_pointInfo31)).setText(Html.fromHtml("휴대폰 본인인증이 되어 있지 않으면 미션에 참여하실 수 없습니다. "));
                }
                ((TextView)dialog.findViewById(R.id.dialog_point_info_ok)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //앱정보
        ((TextView)findViewById(R.id.missionDetail_tvTab2)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((ScrollView)findViewById(R.id.missionDetail_svTab1)).setVisibility(View.GONE);
                ((ScrollView)findViewById(R.id.missionDetail_svTab2)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.missionDetail_tvTab1)).setBackgroundResource(R.drawable.border_n_default);
                ((TextView)findViewById(R.id.missionDetail_tvTab2)).setBackgroundResource(R.drawable.border_n_primary);

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
        Object[][] params = {
                {"CAMPAIGN_CODE",campaign_code}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_missionDetail), params);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_CHECK) checkHandler(str);

    }

    public void checkHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if(err.equals("")){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(installUrl_str));
                startActivity(intent);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_images = json.getJSONArray("IMAGES");
                imgIcon_str = json.getString("IMG_ICON");
                appTitle_str = json.getString("APP_TITLE");
                developerName_str = json.getString("DEVELOPER_NAME");
                point_str = json.getString("POINT");
                pointRealtimeYn_str = json.getString("POINT_REALTIME_YN");
                pointDate_str = json.getString("POINT_DATE");
                installUrl_str = json.getString("ANDROID_INSTALL_URL");
                joinYn_str = json.getString("JOIN_YN");
                appDesc_str = json.getString("APP_DESC");
                youtube_str = json.getString("YOUTUBE");
                mission_str = json.getString("MISSION");
                missionDesc_str = json.getString("MISSION_DESC");
                shareUrl_str = json.getString("SHARE_URL");
                authYn_str = json.getString("AUTH_YN");

                if(youtube_str.equals("")){
                    ((WebView)findViewById(R.id.missionDetail_wvYoutube)).setVisibility(View.GONE);
                }else{
                    ((WebView)findViewById(R.id.missionDetail_wvYoutube)).setVisibility(View.VISIBLE);
                }

                //이미지 슬라이더
                //상세보기 슬라이더 ##############################################################################################
                Hash_file_maps = new HashMap<String, String>();
                sliderLayout = (SliderLayout)findViewById(R.id.missionDetail_ivTab2Image);
                int i;
                for(i=0;i<ary_images.length();i++){
                    String strImgUrl = (String)ary_images.get(i);
                    if(!strImgUrl.equals("")){
                        Hash_file_maps.put(String.valueOf(i+1),strImgUrl);
                    }
                }

                for(String name : Hash_file_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(MissionDetailActivity.this);
                    textSliderView
                            //.description(name)
                            .image(Hash_file_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);
                    sliderLayout.addSlider(textSliderView);
                }

                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.setDuration(3000);
                sliderLayout.addOnPageChangeListener(this);

                //상세보기 슬라이더 END ##############################################################################################


                ImageView ivImageIcon = (ImageView)findViewById(R.id.missionDetail_ivImageIcon);
                Picasso.with(getApplicationContext()).load(imgIcon_str).resize(75, 65).into(ivImageIcon);
                //문구변경
                ((TextView)findViewById(R.id.missionDetail_ivTitle)).setText(appTitle_str); //타이틀
                ((TextView)findViewById(R.id.missionDetail_ivPoint)).setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(point_str)+"P</font>"));  //포인트
                ((TextView)findViewById(R.id.missionDetail_ivDeveloperName)).setText(developerName_str);    //개발사
                if(!pointDate_str.equals("")){
                    ((TextView)findViewById(R.id.missionDetail_pointDate)).setText(mission_str+"("+pointDate_str+"지급)");    //미션,포인트지급일
                }else{
                    ((TextView)findViewById(R.id.missionDetail_pointDate)).setText(mission_str);    //미션,포인트지급일
                }
                //참여하기 완료인경우, disabled
                if(joinYn_str.equals("Y")){
                    ((TextView)findViewById(R.id.missionDetail_tvDownload)).setBackgroundResource(R.color.gray);
                }else{
                    ((TextView)findViewById(R.id.missionDetail_tvDownload)).setBackgroundResource(R.color.primary);
                }
                //미션 참여방법
                if(!missionDesc_str.equals("")){
                    ((TextView)findViewById(R.id.missionDetail_tvTab1Desc)).setText(Html.fromHtml(missionDesc_str));
                }else{
                    ((TextView)findViewById(R.id.missionDetail_tvTab1Desc)).setText(Html.fromHtml("내용이 없습니다."));
                }
                //앱정보
            }else{
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {
        /*if(dialogType == 2 && result.equals("ok")){
            //색변경
            ((Button)findViewById(R.id.couponDetail_btnBooking)).setBackgroundResource(R.color.primaryDisabled);
            if(reserve_yn.equals("Y")){
                ((Button)findViewById(R.id.couponDetail_btnBooking)).setText("사전예약 신청완료");
            }else{
                ((Button)findViewById(R.id.couponDetail_btnBooking)).setText("쿠폰 신청완료");
            }
        }*/
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
                .setImageUrl(Uri.parse(imgIcon_str))

                //공유될 링크
                .setContentUrl(Uri.parse(shareUrl_str))

                //게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
                .setContentDescription(appTitle_str)
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.FEED);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.
    }

    public void shareTwitter() {
        String strLink = null;
        try {
            strLink = String.format("http://twitter.com/intent/tweet?text=%s", URLEncoder.encode(appTitle_str, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }


}
