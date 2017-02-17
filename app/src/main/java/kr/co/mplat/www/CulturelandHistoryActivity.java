package kr.co.mplat.www;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class CulturelandHistoryActivity extends NAppCompatActivity implements AbsListView.OnScrollListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private int dialogType = 0;
    private int lastSeq = 0;
    JSONArray ary_list = new JSONArray();
    Common common = null;
    private AlertDialog commonDialog = null;
    private ArrayList<String> rnums = new ArrayList<String>();
    private JSONArray giftcard_list = new JSONArray();
    Intent intent = null;
    private int nextPage = 1;
    private int endPage = 999999;
    private boolean loading = false;
    ListView listview ;
    private AlertDialog dialog = null;
    private ListView lvList;
    private ListView list_giftcard;
    //CulturelandHistroyListViewAdapter adapter;
    private GiftcardList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultureland_history);
        setTvTitle("문화상품권 보관함");
        common = new Common(this);

        //adapter.addItem("test1","test2","test3","test4") ;
        list_giftcard =(ListView)findViewById(R.id.culturelandHistory_lvList);

        //리스트 클릭
        list_giftcard.setOnItemClickListener(new ListViewItemClickListener());
        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



       //안내문구
        ((TextView)findViewById(R.id.culturelandHistory_tvDesc1)).setText(Html.fromHtml("문화상품권 사용기한은 <font color=\"#D57A76\">발급일로부터 60일</font>입니다."));
        ((TextView)findViewById(R.id.culturelandHistory_tvDesc2)).setText(Html.fromHtml("※ 사용기한이 만료된 상품권은 <font color=\"#D57A76\">환불/보상이 되지 않습니다.</font>"));
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Log.i("wtkim","onScroll호출!");
    }

    @Override
    protected void onResume() {

        super.onResume();
        if(nextPage ==1){
            Log.i("wtkim","start11111111111!");
            start(null);
            //더 불러오기 처리
           /*list_giftcard.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    try {
                        if (rnums.size() > 0 && rnums.size() < (firstVisibleItem + visibleItemCount + 10))
                            Log.i("wtkim","start@222222222222!");
                        *//*
                        I/wtkim: size()==>6
                        I/wtkim: firstVisibleItem==>0
                        I/wtkim: visibleItemCount==>6
                        I/wtkim: rnums==>[87, 86, 85, 84, 77, 76]
                        *//*
                            Log.i("wtkim","size()==>"+rnums.size());
                        Log.i("wtkim","firstVisibleItem==>"+firstVisibleItem);
                        Log.i("wtkim","visibleItemCount==>"+visibleItemCount);
                        Log.i("wtkim","rnums==>"+rnums.toString());
                            start(null);
                    } catch (Exception e) {
                    }
                }
            });*/
        }
    }

    @Override
    public void start(View view) {
        if(loading)return;
        if(nextPage >=endPage)return;
        loading=true;
        if(nextPage==1){
            list_giftcard.setVisibility(View.GONE);
        }
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            //if(nextPage==1)txt_nogiftcard.setVisibility(View.VISIBLE);
            loading=false;
            return;
        }
        //상품권 보관 정보로드


        Object[][] params = {
                {"LAST_SEQ", ""}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_culturelandHistory), params);
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) startHandler(str);
        else if (calltype == CALLTYPE_SAVE) saveDataHandler(str);
    }
    public void startHandler(String str){
        loading=false;
        try {
            JSONObject json = new JSONObject(str);
            JSONArray currentList = new JSONArray();
            JSONObject item;
            String seq;
            String date;
            String valid_date;
            String price;
            String pincode;

            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    String last_seq = json.getString("LAST_SEQ");
                    String point = json.getString("POINT");
                    currentList = json.getJSONArray("LIST");
                    if (nextPage == 1) rnums = new ArrayList<>();
                    for (int i = 0; i < currentList.length(); i++) {
                        item = currentList.getJSONObject(i);
                        seq = item.getString("SEQ");
                        date = item.getString("DATE");
                        valid_date = item.getString("VALID_DATE");
                        price = item.getString("PRICE");
                        pincode = item.getString("PINCODE");
                        rnums.add(seq);
                    }
                    giftcard_list = Common.concatJSONArray(giftcard_list, currentList);
                    //리스트 추가
                    if (nextPage == 1) {
                        adapter = new GiftcardList(CulturelandHistoryActivity.this);
                        giftcard_list = currentList;
                        list_giftcard.setAdapter(adapter);
                        list_giftcard.setVisibility(View.VISIBLE);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    //다음 페이지
                    if(last_seq.equals("")){
                        lastSeq = 0;
                    }
                    nextPage = lastSeq + 1;
                    //set 포인트
                    ((TextView)findViewById(R.id.culturelandHistory_tvPoint)).setText(Common.getTvComma(point));

                }
            } else {
                Log.i("wtkim","11111111");
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err.toString(), getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtkim","222222222");
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void saveDataHandler(String str){
        Log.i("wtkim","saveDataHandler호출!");
        try {
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");

                if (result.equals("OK")) {
                    dialogType = 1;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "", getString(R.string.btn_ok),null, false, false);
                   /* intent = new Intent(CulturelandRequestActivity.this,PointActivity.class);
                    startActivity(intent);*/
                }
            } else {
                Log.i("wtkim","aaaaaaaaaa");
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Log.i("wtkim","bbbbbbbbbbbb");
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    //상품클릭 처리
    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //dialog=createDialog(R.layout.dialog_giftcard);
            //dialog.show();
            /*try {
                JSONObject point = (JSONObject) view.getTag();

                //브랜드
                TextView giftcard_brand = (TextView)dialog.findViewById(R.id.giftcard_brand);
                giftcard_brand.setText(point.getString("BRAND"));
                //상품명
                TextView giftcard_product = (TextView)dialog.findViewById(R.id.giftcard_product);
                giftcard_product.setText(point.getString("PRODUCT"));
                //포인트
                TextView giftcard_price = (TextView) dialog.findViewById(R.id.giftcard_price);
                giftcard_price.setText(Common.numberFormat(point.getInt("PRICE"))+" P");
                //버튼에 상품코드 넣기
                TextView btn_giftcard_ok = (TextView)dialog.findViewById(R.id.btn_giftcard_ok);
                btn_giftcard_ok.setTag(point.getString("PRODUCT_CODE"));
            }catch(Exception e){
                //Log.d("MYLOG", e.toString());
            }*/
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Listitem @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public class GiftcardList extends ArrayAdapter<String> {
        private final Activity context;
        public GiftcardList(Activity context){
            super(context,R.layout.listview_culturelandhistory_item,rnums);
            this.context = context;
        }
        @Override
        public View getView(final int position, View view, ViewGroup parent){
            ViewHolder holder = null;
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.listview_culturelandhistory_item, null, true);

            try {
                JSONObject row = giftcard_list.getJSONObject(position);
                rowView.setTag(row);
                //마지막 페이지를 저장해 두었다가 더이상 스크롤로 못 불러오게 함
                //endPage = row.getInt("END_PAGE");
                endPage = 10;
                //발급일
                TextView textView1 = (TextView)rowView.findViewById(R.id.culturelandHistory_textView1);
                textView1.setText(row.getString("DATE"));
                //금액
                TextView textView2 = (TextView)rowView.findViewById(R.id.culturelandHistory_textView2);
                textView2.setText(Common.getTvComma(row.getString("PRICE"))+"원");
                //유효기간
                TextView textView3 = (TextView) rowView.findViewById(R.id.culturelandHistory_textView3);
                textView3.setText(row.getString("VALID_DATE"));
                //유효기간
                TextView textView4 = (TextView) rowView.findViewById(R.id.culturelandHistory_textView4);
                textView4.setText(row.getString("PINCODE"));

                //번호확인 버튼 클릭
                /*Object obj = new Object();
                obj.pinCode="test";*/

                Button btn = (Button)rowView.findViewById(R.id.culturelandHistory_btnNo);

//                if(view==null){
                    holder = new ViewHolder();
                    holder.setTvDate(row.getString("DATE"));
                    holder.setTvPrice(Common.getTvComma(row.getString("PRICE"))+"원");
                    holder.setTvValidDate(row.getString("VALID_DATE"));
                    holder.setTvPincode(row.getString("PINCODE"));

                    btn.setTag(holder);


                btn.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String tvDate = ((ViewHolder)view.getTag()).getTvDate();
                        String tvPrice = ((ViewHolder)view.getTag()).getTvPrice();
                        String tvValidDate = ((ViewHolder)view.getTag()).getTvValidDate();
                        String tvPincode = ((ViewHolder)view.getTag()).getTvPincode();

                        // LayoutInflater를 통해 위의 custom layout을 AlertDialog에 반영. 이 외에는 거의 동일하다.
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View row_v = inflater.inflate(R.layout.custom_dialog_cultureland, null);
                        TextView tvPriceContent = (TextView)row_v.findViewById(R.id.cultureland_dial_tvPriceContent);
                        TextView tvValidateDateContent = (TextView)row_v.findViewById(R.id.cultureland_dial_tvValidateDateContent);
                        TextView tvPinCodeContent = (TextView)row_v.findViewById(R.id.cultureland_dial_tvPinCodeContent);
                        TextView tvClose = (TextView)row_v.findViewById(R.id.cultureland_dial_tvClose);

                        tvPriceContent.setText(tvPrice);
                        tvValidateDateContent.setText(tvValidDate);
                        tvPinCodeContent.setText(tvPincode);
                        tvClose.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });



                        AlertDialog.Builder builder = new AlertDialog.Builder(CulturelandHistoryActivity.this);
                        builder.setView(row_v);

                      /* builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });*/

                        /*builder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }

                        });*/
                        AlertDialog dlog = builder.create();
                        dialog=dlog;
                        dlog.show();

                    }
                });

            } catch(Exception e){
                //Log.d("MYLOG", e.toString());
            }
            return rowView;
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Listitem @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private AlertDialog createDialog(int layoutResource){
        if(dialog!=null && dialog.isShowing())return dialog;
        final View innerView = getLayoutInflater().inflate(layoutResource,null);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(layoutResource);
        return ab.create();
    }
    public void setDismiss(Dialog dialog){
        if(dialog!=null && dialog.isShowing())dialog.dismiss();
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    private static class ViewHolder{
        private String tvDate = null;
        private String tvPrice = null;
        private String tvValidDate = null;
        private String tvPincode = null;

        public String getTvDate() {
            return tvDate;
        }

        public void setTvDate(String tvDate) {
            this.tvDate = tvDate;
        }

        public String getTvPrice() {
            return tvPrice;
        }

        public void setTvPrice(String tvPrice) {
            this.tvPrice = tvPrice;
        }

        public String getTvValidDate() {
            return tvValidDate;
        }

        public void setTvValidDate(String tvValidDate) {
            this.tvValidDate = tvValidDate;
        }

        public String getTvPincode() {
            return tvPincode;
        }

        public void setTvPincode(String tvPincode) {
            this.tvPincode = tvPincode;
        }
    }
}
