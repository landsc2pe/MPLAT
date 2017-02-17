package kr.co.mplat.www;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class CouponHistoryAdapter extends BaseAdapter implements View.OnClickListener{
    Handler handler = new Handler();
    Bitmap bm;
    ImageView ivImgUrl;
    TextView tvCampaignCode;
    TextView tvAppTitle;
    TextView tvDeveloperName;
    TextView tvReward;
    TextView tvAppOpenYn;
    TextView tvOpenDate;
    TextView tvCouponNum;
    TextView tvAndroidUrl;
    TextView tvUsableYn;
    TextView tvUsedYn;
    TextView tvUseEndDate;

    // 버튼 클릭 이벤트를 위한 Listener 인터페이스 정의.
    public interface ListBtnClickListener {
        void onListBtnClick(TagInfo info) ;
    }
   /* public interface ListCouponClickListener{
        void onListCouponClick(String couponNum) ;
    }*/
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener;
    //생성자로부터 전달된 ListCouponClickListener 저장.
    //private ListCouponClickListener listCouponClickListener;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CouponHistoryListViewItem> listViewItemList = new ArrayList<CouponHistoryListViewItem>();

    /*public CouponHistoryAdapter(){

    }*/
    CouponHistoryAdapter(Context context,int resource,ArrayList<CouponHistoryListViewItem> list, ListBtnClickListener clickListener){
        //super(context, resource, list,clickListener);
        super();
        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;
        this.listBtnClickListener = clickListener ;
        //this.listCouponClickListener = couponClickListener;
    }


    //Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public void onClick(View view) {
        TagInfo ti=(TagInfo)view.getTag();
        if(view.getId()==R.id.couponHistoryItem_btnDown){
            ti.clickTarget="download";
        } else if(view.getId()==R.id.couponHistoryItem_tvCouponNum){
            ti.clickTarget="couponnum";
        }
        view.setTag(ti);
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((TagInfo) view.getTag()) ;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_couponhistory_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        tvCampaignCode = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvCampaignCode);
        ivImgUrl = (ImageView)convertView.findViewById(R.id.couponHistoryItem_ivImgUrl);
        tvAppTitle = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvAppTitle);
        tvDeveloperName = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvDeveloperName);
        tvReward = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvReward);
        tvAppOpenYn = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvAppOpenYn);
        tvOpenDate = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvOpenDate);
        tvCouponNum = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvCouponNum);
        tvAndroidUrl = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvAndroidUrl);
        tvUsableYn = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvUsableYn);
        tvUsedYn = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvUsedYn);
        tvUseEndDate = (TextView)convertView.findViewById(R.id.couponHistoryItem_tvUseEndDate);
        //
        //couponHistoryItem_btnDown

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final CouponHistoryListViewItem listViewItem = listViewItemList.get(position);

        //출시일 이전에는 다운로드버튼 비활성화
        String strCurdate = Common.getDateString().substring(0,8);
        String strOpenDate = listViewItem.getOpen_date().replace("-","");
        String strCouponUseEndDate = listViewItem.getUse_end_date().replace("-","");
        //Log.i("wtkim","strOpenDate==>"+strOpenDate);

        if(!strOpenDate.equals("") && (Integer.parseInt(strCurdate)>=Integer.parseInt(strOpenDate))){
            ((Button)convertView.findViewById(R.id.couponHistoryItem_btnDown)).setBackgroundResource(R.color.primary);
        }else{
            ((Button)convertView.findViewById(R.id.couponHistoryItem_btnDown)).setBackgroundResource(R.color.primaryDisabled);
        }

        // 아이템 내 각 위젯에 데이터 반영
        tvCampaignCode.setText(listViewItem.getCampaign_code());
        tvAppTitle.setText(Html.fromHtml("<b>"+listViewItem.getApp_title()+"</b>"));
        tvDeveloperName.setText(listViewItem.getDeveloper_name());
        tvReward.setText(Html.fromHtml("<font color='#7161C4'>"+listViewItem.getReward()+"</font>"));
        tvAppOpenYn.setText(listViewItem.getApp_open_yn());
        if(!(listViewItem.getOpen_date().equals(""))){
            tvOpenDate.setText(Html.fromHtml("<font color='#D57A76'>출시 예정일 : "+listViewItem.getOpen_date()+"</font>"));
        }else{
            tvOpenDate.setText(Html.fromHtml("<font color='#D57A76'>출시 예정일 : 미정</font>"));
        }

        String usedYn = listViewItem.getUsed_yn().toString();
        if(!(listViewItem.getCoupon_num().equals(""))){//쿠폰번호가 있는경우
            tvOpenDate.setText(Html.fromHtml("<font color='#D57A76'>쿠폰 유효기간 : "+listViewItem.getUse_end_date()+"</font>"));
            tvCouponNum.setText(Html.fromHtml("<font color='#D57A76'>"+listViewItem.getCoupon_num()+"</font>"));
            if(usedYn.equals("Y")){
                tvCouponNum.setPaintFlags(tvCouponNum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if(!strOpenDate.equals("") && (Integer.parseInt(strCurdate)<Integer.parseInt(strOpenDate))){
                tvCouponNum.setText(Html.fromHtml("<font color='#D57A76'>출시 이후 쿠폰이 발급됩니다.</font>"));
            }

            //쿠폰사용기간이 만료되었을 경우 [유효기간 만료] 문구 추가

            if(!strCouponUseEndDate.equals("") && (Integer.parseInt(strCurdate)>Integer.parseInt(strCouponUseEndDate))){
                tvCouponNum.setText(Html.fromHtml(listViewItem.getCoupon_num()+" <font color='#D57A76'>[유효기간 만료]</font>"));
            }


        }else{//쿠폰번호가 없는경우
            tvCouponNum.setText(listViewItem.getCoupon_num());
        }

        tvAndroidUrl.setText(listViewItem.getAndroid_url());
        tvUsableYn.setText(listViewItem.getUsable_yn());
        tvUsedYn.setText(listViewItem.getUsed_yn());
        tvUseEndDate.setText(listViewItem.getUse_end_date());



        if(!listViewItem.getImg_url().equals("")){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {    // 오래 거릴 작업을 구현한다
                    // TODO Auto-generated method stub
                    try{
                        URL url = new URL(listViewItem.getImg_url());
                        InputStream is = url.openStream();
                        bm = BitmapFactory.decodeStream(is);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {  // 화면에 그려줄 작업
                                ivImgUrl.setImageBitmap(bm);
                            }
                        });
                        //mContentView.setImageBitmap(bm); //비트맵 객체로 보여주기
                    } catch(Exception e){
                        Log.d("wtkim","---5");
                    }
                }
            });

            t.start();
        }

        //게임다운로드 버튼 선택시
        Button down = (Button)convertView.findViewById(R.id.couponHistoryItem_btnDown);
        TagInfo tf = new TagInfo();
        tf.android_url = listViewItem.getAndroid_url();
        tf.couponNum = listViewItem.getCoupon_num();
        down.setTag(tf);
        down.setOnClickListener(this);
        //text선택시
        TextView tv = (TextView) convertView.findViewById(R.id.couponHistoryItem_tvCouponNum);
        tv.setTag(tf);
        tv.setOnClickListener(this);


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String campaign_code,String img_url,String app_title,String developer_name,String reward,String app_open_yn,String open_date,String coupon_num,String android_url,String ios_url,String usable_yn,String used_yn,String use_end_date) {
        CouponHistoryListViewItem item = new CouponHistoryListViewItem();
        item.setCampaign_code(campaign_code);
        item.setImg_url(img_url);
        item.setApp_title(app_title);
        item.setDeveloper_name(developer_name);
        item.setReward(reward);
        item.setApp_open_yn(app_open_yn);
        item.setOpen_date(open_date);
        item.setCoupon_num(coupon_num);
        item.setAndroid_url(android_url);
        item.setIos_url(ios_url);
        item.setUsable_yn(usable_yn);
        item.setUsable_yn(used_yn);
        item.setUse_end_date(use_end_date);

        listViewItemList.add(item);
    }


    //쿠폰표시 영역 선택시 클립보드 복사
    //클립보드 복사

    class TagInfo{
        public String android_url ="";
        public String couponNum ="";
        public String clickTarget="";
    }
}
