package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GiftProductActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private String brandCode = "";
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_LOAD2 = 3;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    ListView lv_giftProduct;
    ArrayList<String> lists = new ArrayList<String>();

    private int lastLoadedCnt = 99999;
    private int rnum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_product);
        //setContentView(R.layout.listview_gift_product_item);
        setTvTitle("상품권 선택");
        common = new Common(this);
        Intent intent = new Intent(this.getIntent());
        brandCode = intent.getStringExtra("BRAND_CODE").toString();
        lv_giftProduct = (ListView)findViewById(R.id.giftproduct_lv);
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

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
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_LOAD2) load2Handler(str);
    }

    @Override
    public void dialogHandler(String result) {

    }

    public void dataload(){
        common.loadData(CALLTYPE_LOAD2, getString(R.string.url_basicinfo), null);


    }
    ListView listview ;
    GiftProductListViewItem adapter;
    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                int i;
                lists = new ArrayList<String>();

                ListView listview;
                GiftProductAdapter adapter;
                adapter = new GiftProductAdapter();

                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.giftproduct_lv);
                listview.setAdapter(adapter);
                String productCode = "";
                String point = "";
                String goodsImg = "";
                String product = "";

                for(i=0;i<ary_lists.length();i++){
                    productCode = ((JSONObject)ary_lists.get(i)).getString("product_code");
                    point = ((JSONObject)ary_lists.get(i)).getString("point");
                    goodsImg = ((JSONObject)ary_lists.get(i)).getString("goods_img");
                    product = ((JSONObject)ary_lists.get(i)).getString("product");

                    adapter.addItem( productCode,  point,  goodsImg,  product);
                }

                //이벤트 등록
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        GiftProductListViewItem item = (GiftProductListViewItem) parent.getItemAtPosition(position);
                        String goodsImg = item.getGoodsImg();
                        String point = item.getPoint();
                        String product = item.getProduct();
                        String productCode = item.getProductCode();

                        if(!productCode.equals("")){
                            intent = new Intent(GiftProductActivity.this,GiftDetailActivity.class);
                            intent.putExtra("PRODUCT_CODE",productCode);
                            startActivity(intent);
                        }
                    }
                });
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
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String point = json.getString("POINT");
                ((TextView)findViewById(R.id.giftproduct_tvPoint)).setText(Common.getTvComma(point));
                Object[][] params = {
                        {"BRAND_CODE", brandCode}
                };
                common.loadData(CALLTYPE_LOAD, getString(R.string.url_giftProduct), params);
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
