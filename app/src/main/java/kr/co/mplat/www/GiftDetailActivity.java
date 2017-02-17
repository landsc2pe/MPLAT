package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftDetailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private String productCode = "";
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_LOAD2 = 2;
    private final int CALLTYPE_REQUEST = 3;
    private int dialogType = 0;
    Common common = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    ArrayList<String> lists = new ArrayList<String>();
    private String myPoint = "";
    private String myMobile = "";
    private String product = "";
    private String goodsPoint = "";
    private String brand_description = "";
    private Boolean flag_buyOk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        setTvTitle("상품권 구매");
        common = new Common(this);
        Intent intent = new Intent(this.getIntent());
        productCode = intent.getStringExtra("PRODUCT_CODE").toString();
        dataload();
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
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 3) {
            Intent intent = new Intent(GiftDetailActivity.this,GiftActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void dataload(){
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_LOAD2) load2Handler(str);
        else if (calltype == CALLTYPE_REQUEST) requestHandler(str);

    }

    public void requestHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                dialogType = 3;

                Common.createDialog(this, getString(R.string.app_name).toString(),null, "모바일 상품권 구매가 완료되었습니다.\n고객님의 휴대전화 문자메시지를 확인해주시기 바랍니다.", getString(R.string.btn_ok),null, false, false);
            }else{
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");

            if (err.equals("")) {
                 myPoint = json.getString("POINT");
                 myMobile = json.getString("MOBILE");
                Object[][] params = {
                        {"PRODUCT_CODE", productCode}
                };
                common.loadData(CALLTYPE_LOAD2, getString(R.string.url_giftDetail), params);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    public void load2Handler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                String yn = ((JSONObject)ary_lists.get(0)).getString("YN");
                product = ((JSONObject)ary_lists.get(0)).getString("product");
                goodsPoint = ((JSONObject)ary_lists.get(0)).getString("point");
                brand_description = ((JSONObject)ary_lists.get(0)).getString("brand_description");
                String goods_img = ((JSONObject)ary_lists.get(0)).getString("goods_img");

                //문구변경
                ImageView ivGoodsImg = (ImageView)findViewById(R.id.giftDetail_ivGoodsImg);
                Picasso.with(getApplicationContext()).load(goods_img).resize(500,500).into(ivGoodsImg);
                ((TextView)findViewById(R.id.giftDetail_tvGoodsProduct)).setText(product);
                ((TextView)findViewById(R.id.giftDetail_tvGoodsPoint)).setText(Common.getTvComma(goodsPoint)+" P");
                //보유포인트
                ((TextView)findViewById(R.id.giftDetail_info12)).setText(Common.getTvComma(myPoint)+" P");
                //차감포인트

                //구매후포인트
                int remainPoint = (Integer.parseInt(myPoint)-Integer.parseInt(goodsPoint));
                if(remainPoint>0){
                    flag_buyOk = true;
                    ((TextView)findViewById(R.id.giftDetail_tvNotice)).setVisibility(View.GONE);
                    ((TextView)findViewById(R.id.giftDetail_info21)).setTextColor(getResources().getColor(R.color.primary));
                    ((TextView)findViewById(R.id.giftDetail_info22)).setText(Html.fromHtml("<font color='#7161C4'>-"+Common.getTvComma(goodsPoint)+" P</font>"));
                    ((TextView)findViewById(R.id.giftDetail_info32)).setText(Common.getTvComma(String.valueOf(remainPoint))+" P");
                    ((Button)findViewById(R.id.giftDetail_btnNext)).setBackgroundResource(R.color.primary);
                }else{
                    flag_buyOk = false;
                    ((TextView)findViewById(R.id.giftDetail_tvNotice)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.giftDetail_info21)).setTextColor(getResources().getColor(R.color.primary));
                    ((TextView)findViewById(R.id.giftDetail_info22)).setText(Html.fromHtml("<font color='#7161C4'>- "+Common.getTvComma(goodsPoint)+" P</font>"));
                    ((TextView)findViewById(R.id.giftDetail_info32)).setText(Common.getTvComma(String.valueOf(remainPoint))+" P");
                    ((TextView)findViewById(R.id.giftDetail_info31)).setText(Html.fromHtml("<font color='#D57A76'>필요 포인트</font>"));
                    ((TextView)findViewById(R.id.giftDetail_info32)).setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(String.valueOf(-remainPoint))+" P</font>"));
                    ((Button)findViewById(R.id.giftDetail_btnNext)).setBackgroundResource(R.color.primaryDisabled);
                }
                //상품권 유효기간은 발급일을 포함하여 30일 이내 입니다.
                ((TextView)findViewById(R.id.giftDetail_info41)).setText(Html.fromHtml("상품권 유효기간은 <font color='#D57A76'>발급일을 포함하여 30일 이내</font> 입니다."));

                //상품권 구매하기 클릭 이벤트
                ((Button)findViewById(R.id.giftDetail_btnNext)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(flag_buyOk){
                            dialog = createDialog(R.layout.custom_dialog_gift_detail_buy);
                            dialog.show();
                            ((TextView)dialog.findViewById(R.id.dialog_giftDetailbuy_product)).setText(Html.fromHtml("<font>"+product+"</font>"));
                            ((TextView)dialog.findViewById(R.id.dialog_giftDetailbuy_goodsPoint)).setText(Html.fromHtml("<font>-"+Common.getTvComma(goodsPoint)+" P</font>"));
                            ((TextView)dialog.findViewById(R.id.dialog_giftDetailbuy_mobile)).setText(Html.fromHtml("<font color='#7161C4'>(문자 수신번호 : "+myMobile+")</font>"));

                            //확인선택시
                            ((TextView)dialog.findViewById(R.id.dialog_giftDetailbuy_ok)).setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    String jobNum = Common.getDateString()+""+Common.getRandomString(5);
                                    Log.i("wtkim","rndCode==>"+jobNum);
                                    Object[][] params = {
                                            {"JOB_NUM", jobNum}
                                            ,{"PRODUCT_CODE", productCode}
                                    };
                                    common.loadData(CALLTYPE_REQUEST, getString(R.string.url_giftRequest), params);
                                }
                            });
                            //취소 선택시
                            ((TextView)dialog.findViewById(R.id.dialog_giftDetailbuy_cancel)).setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }



                        //((TextView)dialog.findViewById(R.id.dialog_giftDetailDesc)).setText(Html.fromHtml("<font>"+brand_description+"</font>"));



                    }
                });

                //상품권 안내 클릭 이벤트
                ((Button)findViewById(R.id.giftDetail_btnInfo)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog = createDialog(R.layout.custom_dialog_gift_detail_check);
                        dialog.show();
                        ((TextView)dialog.findViewById(R.id.dialog_giftDetailDesc)).setText(Html.fromHtml("<font>"+brand_description+"</font>"));
                        ((TextView)dialog.findViewById(R.id.dialog_giftDetail_ok)).setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });

                    }
                });

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
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
}
