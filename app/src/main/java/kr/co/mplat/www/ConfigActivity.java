package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class ConfigActivity extends MAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    private String alliance_email = "";
    private String android_ver = "";
    private String android_ver_min = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        //setTvTitle("공지사항");
        common = new Common(this);
        //문구변경
        ((TextView) findViewById(R.id.config_tvCustomerCenter)).setText(Html.fromHtml("<b>고객센터</b>"));
        //((TextView)findViewById(R.id.config_tvSearchIdPw)).setText(Html.fromHtml("<b>아이디/비밀번호 찾기</b>"));
        ((TextView) findViewById(R.id.config_tvNoticeAgree)).setText(Html.fromHtml("<b>수신동의 알림</b>"));
        ((TextView) findViewById(R.id.config_tvAgreement)).setText(Html.fromHtml("<b>약관</b>"));
        ((TextView) findViewById(R.id.config_tvServiceInfo)).setText(Html.fromHtml("<b>서비스 정보</b>"));

        //공지사항
        ((TextView) findViewById(R.id.config_tvNotice)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, NoticeActivity.class);
                startActivity(intent);
            }
        });
        //자주묻는 질문
        ((TextView) findViewById(R.id.config_tvFaq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, FaqActivity.class);
                startActivity(intent);
            }
        });
        //1:1 문의
        ((TextView) findViewById(R.id.config_tvQna)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, QnaActivity.class);
                startActivity(intent);
            }
        });
        //수신동의 및 알림상태 변경
        ((TextView) findViewById(R.id.config_tvAgreeChange)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, AgreeChangeActivity.class);
                startActivity(intent);
            }
        });
        //이용약관
        ((TextView) findViewById(R.id.config_tvRule)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, RuleActivity.class);
                startActivity(intent);
            }
        });
        //개인정보 취급 방침
        ((TextView) findViewById(R.id.config_tvPrivacy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });
        //회사정보
        ((TextView) findViewById(R.id.config_tvCompany)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ConfigActivity.this, CompanyActivity.class);
                startActivity(intent);
            }
        });
        //제휴문의
        ((TextView) findViewById(R.id.config_tvPartnership)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:contact@mplat.co.kr"));
                startActivity(intent);
            }
        });
        //앱버전
        ((TextView) findViewById(R.id.config_btnUpdateCheck)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myapp_ver = BuildConfig.VERSION_CODE;
                int last_ver = Integer.parseInt(android_ver);
                dialog = createDialog(R.layout.custom_dialog_app_update_check);
                dialog.show();
                ((TextView) dialog.findViewById(R.id.dialog_Appver_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (myapp_ver == last_ver) {
                    ((TextView) dialog.findViewById(R.id.dialog_lastver)).setText(Html.fromHtml(BuildConfig.VERSION_NAME + "." + android_ver));
                    ((TextView) dialog.findViewById(R.id.dialog_currentver)).setText(Html.fromHtml("<font color='#7161C4'>" + Common.getVer().toString() + "</font>"));
                    ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(Html.fromHtml("<font color='#7161C4'>현재 최신버전의 앱이 설치되어 있습니다.</font>"));
                } else {
                    ((TextView) dialog.findViewById(R.id.dialog_lastver)).setText(Html.fromHtml(BuildConfig.VERSION_NAME + "." + android_ver));
                    ((TextView) dialog.findViewById(R.id.dialog_currentver)).setText(Html.fromHtml("<font color='#D57A76'>" + Common.getVer().toString() + "</font>"));
                    ((TextView) dialog.findViewById(R.id.dialog_msg)).setText(Html.fromHtml("<font color='#D57A76'>최신버전 업데이트가 가능합니다.</font>"));
                    ((TextView) dialog.findViewById(R.id.dialog_Appver_ok)).setText(Html.fromHtml("<font color='#FFFFFF'>업데이트 파일 다운로드</font>"));

                }


                //최신버전이 아닌 경우
                /*if (myapp_ver < last_ver) {
                    TextView txt_config_latest = (TextView) dialog.findViewById(R.id.txt_config_latest);
                    txt_config_latest.setText("최신 버전이 아닙니다. 업데이트 해주세요.");
                    Button btn_config_update = (Button) dialog.findViewById(R.id.btn_config_update);
                    btn_config_update.setVisibility(View.VISIBLE);
                    btn_config_update.setOnClickListener(this);
                }*/
            }
        });
        //dialog button 클릭시
       /* ((TextView)findViewById(R.id.btn_config_appver_ok)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setDismiss(dialog);
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim", json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                alliance_email = json.getString("ALLIANCE_EMAIL");
                android_ver = json.getString("ANDROID_VER");
                android_ver_min = json.getString("ANDROID_VER_MIN");

                //문구변경
                ((TextView) findViewById(R.id.config_appver)).setText("앱 버전 " + Common.getVer());
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if (!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_config), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        //메뉴 선택
        ChangeNavIcon(4);
        return ret;
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
}
