package kr.co.mplat.www;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_GIFT = 2;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    LinearLayout ll = null;
    ImageView iv = null;
    TextView tv = null;

    private JSONArray gift_list = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        setTvTitle("상품권 브랜드 선택");
        common = new Common(this);

        GridLayout glList = (GridLayout)findViewById(R.id.gift_glList);
    }

    @Override
    public void dialogHandler(String result) {

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
        //기본정보 로드
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_basicinfo), null);
        //상품권리스트 로드
       //common.loadData(CALLTYPE_GIFT, getString(R.string.url_gift), null);
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_GIFT) giftHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                //포인트 정보
                String point = json.getString("POINT");
                ((TextView)findViewById(R.id.gift_tvPoint)).setText(Common.getTvComma(point));
                //gift 리스트 정보 로드
                common.loadData(CALLTYPE_GIFT, getString(R.string.url_gift), null);
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //기프트 브랜드 정보로드
    public void giftHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            JSONArray currentList = new JSONArray();
            JSONObject item;
            String brand_code;
            String brand;
            String brand_img;
            String category;

            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if(result.equals("OK")){
                    currentList = json.getJSONArray("LIST");
                    GridLayout gift_glList = (GridLayout)findViewById(R.id.gift_glList);

                    GiftItem giftItem = null;
                    for(int i=0;i<currentList.length();i++){
                        item = currentList.getJSONObject(i);

                        category = item.getString("category");
                        brand_code = item.getString("brand_code");
                        brand = item.getString("brand");
                        brand_img = item.getString("brand_img");

                        //dto
                        giftItem = new GiftItem(category,brand_code,brand,brand_img);
                        //vo
                        ll = new LinearLayout(this);
                        LinearLayout.LayoutParams position;
                        position = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1); // 맨 마지막 부분의 숫자가 가중치 입니
                        ll.setLayoutParams(position);
                        ll.setTag(giftItem);

                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setGravity(Gravity.CENTER_HORIZONTAL);
                        ll.setGravity(Gravity.CENTER);
                        iv = new ImageView(this);
                        Picasso.with(getApplicationContext()).load(brand_img).into(iv);
                        tv = new TextView(this);
                        if(Build.VERSION.SDK_INT>=17)tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        tv.setText(giftItem.getBrand());

                        ll.addView(iv);
                        ll.addView(tv);
                        gift_glList.addView(ll);

                        int parentWidth=gift_glList.getMeasuredWidth();
                        int parentHeight=300;

                        android.view.ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                        layoutParams.width = parentWidth/3;
                        layoutParams.height = parentHeight;

                        iv.setLayoutParams(layoutParams);

                        Log.i("wtkim","brand_img==>"+giftItem.getBrandImg());
                        Log.i("wtkim","brand==>"+giftItem.getBrand());
                        // ImageView 를 추가하기 위한 LinearLayout 생성
                        //Picasso.with(getApplicationContext()).load(imgIcon_str).resize(75, 65).into(ivImageIcon);
                        ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(GiftActivity.this,GiftProductActivity.class);
                                String strBrandCode = ((GiftItem)view.getTag()).getBrandCode();
                                intent.putExtra("BRAND_CODE",strBrandCode);
                                startActivity(intent);
                                //Log.i("wtkim",((GiftItem)view.getTag()).getBrandCode());
                            }
                        });
                    }
                    //gift_list = Common.concatJSONArray(gift_list,currentList);

                }


                //포인트 정보
                /*String point = json.getString("POINT");
                ((TextView)findViewById(R.id.gift_tvPoint)).setText(Common.getTvComma(point));*/
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Listitem @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Listitem @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public class GiftItem{
        private String category = "";
        private String brandCode = "";
        private String brand = "";
        private String brandImg = "";

        public GiftItem() {}

        public GiftItem(String category, String brandCode, String brand, String brandImg) {
            this.category = category;
            this.brandCode = brandCode;
            this.brand = brand;
            this.brandImg = brandImg;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getBrandCode() {
            return brandCode;
        }

        public void setBrandCode(String brandCode) {
            this.brandCode = brandCode;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }
    }
}
