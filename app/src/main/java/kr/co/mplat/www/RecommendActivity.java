package kr.co.mplat.www;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RecommendActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    Common common = null;
    Intent intent = null;
    private AlertDialog dialog = null;
    private int dialogType = 0;
    private final int CALLTYPE_SEND = 1;
    private final int CALLTYPE_KAKAO = 2;
    private final int CALLTYPE_FACEBOOK = 3;
    private final int CALLTYPE_URL = 4;

    private String dial_name = "";
    private String dial_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        common = new Common(this);

        //추천/가입내역 클릭 이벤트
        ((LinearLayout)findViewById(R.id.recommend_llRecommendHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendActivity.this,RecommendHistoryActivity.class);
                startActivity(intent);
            }
        });
        //나의 추천등급 클릭 이벤트
        ((LinearLayout)findViewById(R.id.recommend_llRecommendGrade)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendActivity.this,RecommendGradeActivity.class);
                startActivity(intent);
            }
        });
        //회원추천 안내
        ((Button)findViewById(R.id.btnRecommendInfo)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(RecommendActivity.this,RecommendInfoActivity.class);
                startActivity(intent);
            }
        });

        //카카오톡
        ((ImageView)findViewById(R.id.recommend_ivKakao)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                common.loadData(CALLTYPE_KAKAO, "http://mplat.co.kr/recommend/recommendLinkApp?UID="+Common.getPreference(getApplicationContext(), "UID"), null);

            }
        });
        //페이스북
        ((ImageView)findViewById(R.id.recommend_ivFacebook)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                common.loadData(CALLTYPE_FACEBOOK, "http://mplat.co.kr/recommend/recommendLinkApp?UID="+Common.getPreference(getApplicationContext(), "UID"), null);
            }
        });

        //URL 링크
        ((ImageView)findViewById(R.id.recommend_ivUrl)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                common.loadData(CALLTYPE_URL, "http://mplat.co.kr/recommend/recommendLinkApp?UID="+Common.getPreference(getApplicationContext(), "UID"), null);


            }
        });
        //이메일
        ((ImageView)findViewById(R.id.recommend_ivEmail)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_recommend_email);
                dialog.show();
                //문구변경
                //((TextView)dialog.findViewById(R.id.recommend_popMsg)).setText(Html.fromHtml("<font color='#D57A76'>해당 회원은 이미 엠플랫에 가입된 회원입니다.</font>"));

                //취소
                ((TextView)dialog.findViewById(R.id.dialog_recommendEmail_cancel)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                //보내기
                ((TextView)dialog.findViewById(R.id.dialog_recommendEmail_send)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dial_name = "";
                        dial_email = "";
                        dial_name = ((TextView)dialog.findViewById(R.id.recommend_popName)).getText().toString();
                        dial_email = ((TextView)dialog.findViewById(R.id.recommend_popEmail)).getText().toString();
                        if(dial_name.equals("")||dial_email.equals("")){
                            ((TextView)dialog.findViewById(R.id.recommend_popMsg)).setText(Html.fromHtml("<font color='#D57A76'>입력하신 내용을 다시 한번 확인해 주세요.</font>"));
                        }else{
                            Boolean emailCheck = Common.validateEmail(dial_email);
                            if(!emailCheck){
                                ((TextView)dialog.findViewById(R.id.recommend_popMsg)).setText(Html.fromHtml("<font color='#D57A76'>이메일 형식이 맞지 않습니다.</font>"));
                            }else{
                                sendEmail();
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
    }

    public void sendEmail(){
        Object[][] params = {
                 {"TO_NAME",dial_name}
                ,{"TO_EMAIL",dial_email}
        };
        common.loadData(CALLTYPE_SEND, getString(R.string.url_recommendSendmail), params);
    }
    //SNS 공유하기
    public void shareFacebook() {
      /*  ShareLinkContent content = new ShareLinkContent.Builder()
                //링크의 콘텐츠 제목
                .setContentTitle("문화상품권,기프티콘,현금까지 100%주는 리워드앱! 엠플랫 가입하기")
                //게시물에 표시될 썸네일 이미지의 URL
                //.setImageUrl(Uri.parse(imgIcon_str))
                //공유될 링크
                .setContentUrl(Uri.parse("http://www.naver.com"))
                //게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
                .setContentDescription("엠플랫 다운받기")
                .build();

        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.FEED);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.*/
    }

    public void shareTwitter() {
       /* String strLink = null;
        try {
            strLink = String.format("http://twitter.com/intent/tweet?text=%s", URLEncoder.encode(appTitle_str, "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);*/
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


    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SEND) sendHandler(str);
        else if (calltype == CALLTYPE_KAKAO) kakaoHandler(str);
        else if (calltype == CALLTYPE_FACEBOOK) facebookHandler(str);
        else if (calltype == CALLTYPE_URL) urlHandler(str);

    }
    public void kakaoHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                String kakaoUrl = json.getString("KAKAO_URL");
                String facebookUrl = json.getString("FACEBOOK_URL");
                String directUrl = json.getString("DIRECT_URL");

                 try{
                    final KakaoLink kakaoLink = KakaoLink.getKakaoLink(RecommendActivity.this);
                    final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                    //메세지 추가
                    kakaoBuilder.addText("문화상품권,기프티콘,현금까지 100% 주늘 리워드앱! 엠플랫 가입하기");
                    String url = "http://mplat.co.kr/images/main/mplat_sample.png";
                    kakaoBuilder.addImage(url,81,81);
                    //앱 실행버튼 추가
                    //kakaoBuilder.addAppButton("앱 실행");
                  String uid = Common.getPreference(getApplicationContext(), "UID");
                    kakaoBuilder.addWebLink("링크열기",kakaoUrl);
                    //메세지 발송
                    kakaoLink.sendMessage(kakaoBuilder,RecommendActivity.this);

                }catch(KakaoParameterException e){
                    e.printStackTrace();
                }
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void facebookHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                String kakaoUrl = json.getString("KAKAO_URL");
                String facebookUrl = json.getString("FACEBOOK_URL");
                String directUrl = json.getString("DIRECT_URL");

                try{
                 ShareLinkContent content = new ShareLinkContent.Builder()
                //링크의 콘텐츠 제목
                .setContentTitle("문화상품권,기프티콘,현금까지 100%주는 리워드앱! 엠플랫 가입하기")
                //게시물에 표시될 썸네일 이미지의 URL
                .setImageUrl(Uri.parse("http://mplat.co.kr/images/main/mplat_sample.png"))
                //공유될 링크
                .setContentUrl(Uri.parse(facebookUrl))
                //게일반적으로 2~4개의 문장으로 구성된 콘텐츠 설명
                .setContentDescription("엠플랫 다운받기")
                .build();

                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show(content, ShareDialog.Mode.FEED);   //AUTOMATIC, FEED, NATIVE, WEB 등이 있으며 이는 다이얼로그 형식을 말합니다.

                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void urlHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                String kakaoUrl = json.getString("KAKAO_URL");
                String facebookUrl = json.getString("FACEBOOK_URL");
                String directUrl = json.getString("DIRECT_URL");

                try{
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label",directUrl);
                    clipboard.setPrimaryClip(clip);

                    //이미지 띄우기
                    Toast toastView = new Toast(getApplicationContext());
                    ImageView img = new ImageView(getApplicationContext());
                    img.setImageResource(R.drawable.clipboard);
                    toastView.setView(img);

                    toastView.setDuration(Toast.LENGTH_SHORT);
                    toastView.setGravity(Gravity.CENTER, 0, 0);
                    toastView.show();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void sendHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                Toast toast = Toast.makeText(getApplicationContext(), "추천 이메일이 정상적으로 발송되었습니다.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), err.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
           //Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
    }
}
