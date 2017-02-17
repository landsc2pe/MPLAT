package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AutoCouponActivity extends MAppCompatActivity implements I_loaddata, I_startFinish, I_dialogdata {
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    Common common = null;
    Intent intent = null;
    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();
    private int lastLoadedCnt = 99999;
    private int rnum = 0;
    String code;
    String category2_name;
    String category_code;
    TextView tv[] = null;
    Boolean answeredChecked = false;
    int dialogType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_coupon);
        common = new Common(this);

        //쿠폰 > 쿠폰신청 선택시, 색변경
        ((LinearLayout) findViewById(R.id.autoCoupon_llCoupon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AutoCouponActivity.this, CouponActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //쿠폰 > 나의 쿠폰함 자동발급 선택시, 색변경
        ((LinearLayout) findViewById(R.id.autoCoupon_llCouponHistory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(AutoCouponActivity.this, CouponHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //help 아이콘 선택시
        ((ImageView) findViewById(R.id.autoCoupon_ivHelp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_auto_coupon_help);
                dialog.show();
                //Common.createDialog(AutoCouponActivity.this, "쿠폰 자동발급 안내",null, "- 쿠폰 자동발급을 신청하시면, 사용 가능한 쿠폰이 자동으로 발급됩니다.\n- 자동 발급된 쿠폰은 나의 쿠폰함에서 확인하실 수 있습니다.\n쿠폰 자동발급은 상ㅇ시적으로 신청 및 해제가 가능합니다.\n쿠폰 자동발급 게임 App 관련 쿠폰만 가능합니다.\n쿠폰 자동발급 희망하는 특정 게임장르 App만 선택하여 발급 받으실 수 있습니다.\n사전예약 쿠폰은 자동 발급이 되지 않습니다. 별도로 신청해주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                ((TextView) dialog.findViewById(R.id.dialog_autoCouponHelp_ok)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //전체 자동발급 신청
        ((Button) findViewById(R.id.autoCoupon_btnPointCoupon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.length > 0) {
                    for (int i = 0; i < tv.length; i++) {
                        ((TextView) tv[i]).setBackgroundResource(R.drawable.border_primary);
                    }
                    Common.createDialog(AutoCouponActivity.this, getString(R.string.app_name).toString(), null, "모든 게임 장르에 대해서 자동발급이 설정되었습니다.", getString(R.string.btn_ok), null, false, false);
                }
            }
        });
        //전체 자동발급 해제
        ((Button) findViewById(R.id.autoCoupon_btnReset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.length > 0) {
                    for (int i = 0; i < tv.length; i++) {
                        ((TextView) tv[i]).setBackgroundResource(R.drawable.border);
                        CategoryInfo ci = (CategoryInfo) tv[i].getTag();
                        ci.selected = false;
                    }
                    Common.createDialog(AutoCouponActivity.this, getString(R.string.app_name).toString(), null, "자동발급 상태가 해제되었습니다. 필요한 경우에는 다시 신청이 가능합니다.", getString(R.string.btn_ok), null, false, false);
                }
            }
        });

        //저장 버튼 선택
        ((Button) findViewById(R.id.autoCoupon_btnSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //하나라도 선택했는가?
                answeredChecked = false;
                int checkedSum = 0;
                if (tv.length > 0) {
                    for (int i = 0; i < tv.length; i++) {
                        CategoryInfo ci = (CategoryInfo) tv[i].getTag();
                        //CategoryInfo ci = (CategoryInfo)view.getTag();
                        if (ci.selected) answeredChecked = true;
                        if (ci.selected) {
                            int aa = Integer.parseInt(ci.strCode);
                            Log.i("wtkim", "aa===>" + String.valueOf(aa));
                            checkedSum += Integer.parseInt(ci.strCode);
                        }
                    }
                }


                if (answeredChecked) {
                    dialogType = 2;
                    Object[][] params = {
                            {"CATEGORY_CODE", String.valueOf(checkedSum)}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_autoCouponSave), params);
                    Log.i("wtkim", "checkedSum==>" + checkedSum);
                } else {
                    dialogType = 9;
                    Object[][] params = {
                            {"CATEGORY_CODE", String.valueOf(checkedSum)}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_autoCouponSave), params);
                    //Common.createDialog(AutoCouponActivity.this, getString(R.string.app_name).toString(),null, "선택된 장르가 하나도 없습니다. 다시 확인해 주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
                }
            }
        });
        //((LinearLayout)findViewById(R.id.couponHistory_llCoupon)).performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        boolean ret = super.onCreateOptionsMenu(menu);
        ChangeNavIcon(1);
        return ret;
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
        //기본정보 호출
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_autoCoupon), null);
    }

    @Override
    public void dialogHandler(String result) {
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveHandler(str);
    }

    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                category_code = json.getString("CATEGORY_CODE");
                int i;
                int category_codeNum = Integer.parseInt(category_code);
                lists = new ArrayList<String>();
                //전체활성화를 위해 배열로 넣어둠
                tv = new TextView[ary_lists.length()];
                for (i = 0; i < ary_lists.length(); i++) {
                    code = ((JSONObject) ary_lists.get(i)).getString("CODE");
                    category2_name = ((JSONObject) ary_lists.get(i)).getString("CATEGORY2_NAME");

                    CategoryInfo ci = new CategoryInfo();
                    ci.strCode = code;
                    ci.strCategory2_name = category2_name;


                    if ((category_codeNum & Integer.parseInt(code)) > 0) {
                        ci.selected = true;
                    } else {
                        ci.selected = false;
                    }
                    //ci.selected = false;

                    TextView name = new TextView(AutoCouponActivity.this);
                    tv[i] = name;

                    name.setLayoutParams(new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT, 1));

                    name.setBackgroundResource(R.drawable.border);
                    name.setWidth(470);

                    name.setTextColor(Color.parseColor("#7D7D7D"));
                    name.setTextSize(16);
                    name.setTag(ci);
                    name.setText(category2_name);
                    if (ci.selected) {
                        name.setBackgroundResource(R.drawable.border_primary);
                    } else {
                        name.setBackgroundResource(R.drawable.border);
                    }
                    ((GridLayout) findViewById(R.id.autoCouponList)).addView(name);

                    name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CategoryInfo ci = (CategoryInfo) view.getTag();
                            if (ci.selected) {
                                ((TextView) view).setBackgroundResource(R.drawable.border);
                            } else {
                                ((TextView) view).setBackgroundResource(R.drawable.border_primary);
                            }
                            ci.selected = !ci.selected;
                        }
                    });
                }
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(), null, e.toString(), getString(R.string.btn_ok), null, false, false);
        }
    }

    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim", json.toString());
            if (err.equals("")) {
                if (dialogType == 2) {
                    Common.createDialog(AutoCouponActivity.this, getString(R.string.app_name).toString(), null, "선택하신 게임 장르에 대해서 자동발급신청이 완료되었습니다.", getString(R.string.btn_ok), null, false, false);
                } else {
                    Common.createDialog(AutoCouponActivity.this, getString(R.string.app_name).toString(), null, "선택된 장르가 하나도 없습니다. 다시 확인해 주시기 바랍니다.", getString(R.string.btn_ok), null, false, false);
                }
            } else {
                Common.createDialog(this, getString(R.string.app_name).toString(), null, err, getString(R.string.btn_ok), null, false, false);
            }
        } catch (Exception e) {
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
    private class CategoryInfo {
        public String strCode;
        public String strCategory2_name;
        public Boolean selected = false;
    }
}
