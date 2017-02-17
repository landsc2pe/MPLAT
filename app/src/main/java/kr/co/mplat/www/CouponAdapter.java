package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class CouponAdapter extends BaseAdapter {
    Handler handler = new Handler();
    Bitmap bm;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CouponListViewItem> listViewItemList = new ArrayList<CouponListViewItem>();

    public CouponAdapter() {

    }

    //Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_coupon_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvCampaignCode = (TextView) convertView.findViewById(R.id.couponItem_tvCampaignCode);
        final ImageView ivImage = (ImageView) convertView.findViewById(R.id.couponItem_ivImage);
        TextView appTitle = (TextView) convertView.findViewById(R.id.couponItem_appTitle);
        TextView reserveYn = (TextView) convertView.findViewById(R.id.couponItem_reserveYn);
        TextView developerName = (TextView) convertView.findViewById(R.id.couponItem_developerName);
        TextView reward = (TextView) convertView.findViewById(R.id.couponItem_reward);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final CouponListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvCampaignCode.setText(listViewItem.getCampaign_code());
        //ivImage.setImageDrawable(listViewItem.getImg_url());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try {
                    URL url = new URL(listViewItem.getImg_url());
                    InputStream is = url.openStream();
                    bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            ivImage.setImageBitmap(bm);
                        }
                    });
                    //mContentView.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch (Exception e) {
                    Log.d("wtkim", "---5");
                }
            }
        });

        t.start();

        appTitle.setText(Html.fromHtml("<font><b>" + listViewItem.getApp_title() + "</b></font>"));
        if ((listViewItem.getReserve_yn()).equals("Y")) {
            //reserveYn.setText(listViewItem.getReserve_yn());
            //reserveYn.setText(Html.fromHtml("<font color='#000000'>사전예약</font>"));
            reserveYn.setVisibility(View.VISIBLE);
        } else {
            reserveYn.setVisibility(View.INVISIBLE);
        }

        developerName.setText(listViewItem.getDeveloper_name());
        reward.setText(Html.fromHtml("<font color='#7161C4'>" + listViewItem.getReward() + "</font>"));

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
    public void addItem(String campaign_code, String img_url, String app_title, String developer_name, String reward, String reserve_yn, String join_yn) {
        CouponListViewItem item = new CouponListViewItem();
        item.setCampaign_code(campaign_code);
        item.setImg_url(img_url);
        item.setApp_title(app_title);
        item.setDeveloper_name(developer_name);
        item.setReward(reward);
        item.setReserve_yn(reserve_yn);
        item.setJoin_yn(join_yn);

        listViewItemList.add(item);
    }
}
