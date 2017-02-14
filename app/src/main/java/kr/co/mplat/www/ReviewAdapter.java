package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class ReviewAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ReviewListViewItem> listViewItemList = new ArrayList<ReviewListViewItem>();
    // ListViewAdapter의 생성자
    public ReviewAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int pos = position;
        Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_review_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivImageIcon = (ImageView) convertView.findViewById(R.id.reviewItem_ivImageIcon);
        TextView tvAppTitle = (TextView) convertView.findViewById(R.id.reviewItem_tvAppTitle);
        TextView tvRemainDays = (TextView) convertView.findViewById(R.id.reviewItem_tvRemainDays);
        TextView tvReward = (TextView) convertView.findViewById(R.id.reviewItem_tvReward);
        TextView tvReviewMethod = (TextView) convertView.findViewById(R.id.reviewItem_tvReviewMethod);
        TextView tvJoinInfo = (TextView) convertView.findViewById(R.id.reviewItem_tvJoinInfo);
        LinearLayout llParent = (LinearLayout)convertView.findViewById(R.id.reviewItem_llParent);

        TextView tvJoinImage = (TextView)convertView.findViewById(R.id.reviewItem_tvJoinImage);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ReviewListViewItem listViewItem = listViewItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        //if(!listViewItem.getImgIcon().equals("")){
        Picasso.with(context).load(listViewItem.getImgIcon()).resize(75, 75).into(ivImageIcon);
        //}


        if(listViewItem.getJoinYn().equals("Y")){
            tvAppTitle.setTextColor(ContextCompat.getColor(context, R.color.primaryDisabled));
            tvRemainDays.setBackgroundResource(R.color.primaryDisabled);
            tvReward.setTextColor(ContextCompat.getColor(context, R.color.primaryDisabled));
            tvReviewMethod.setTextColor(ContextCompat.getColor(context, R.color.primaryDisabled));
            tvJoinInfo.setTextColor(ContextCompat.getColor(context, R.color.primaryDisabled));
            tvJoinImage.setVisibility(View.VISIBLE);
        }else{
            tvAppTitle.setTextColor(ContextCompat.getColor(context, R.color.primaryFont));
            tvRemainDays.setBackgroundResource(R.color.primary);
            tvReward.setTextColor(ContextCompat.getColor(context, R.color.primary));
            tvReviewMethod.setTextColor(ContextCompat.getColor(context, R.color.primaryFont));
            tvJoinInfo.setTextColor(ContextCompat.getColor(context, R.color.primaryFont));
            tvJoinImage.setVisibility(View.GONE);
        }


        tvAppTitle.setText(listViewItem.getTitle());
        tvRemainDays.setText(listViewItem.getRemainDays());
        tvReward.setText(listViewItem.getReward());
        tvReviewMethod.setText(listViewItem.getReviewMethod());
        tvJoinInfo.setText(listViewItem.getTargetCnt()+"명 선정 (현재"+listViewItem.getJoinCnt()+"명 신청)");

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String campaignCode, String imgIcon, String title, String reward, String reviewMethod, String targetCnt, String joinCnt, String joinYn,String remainDays) {
        ReviewListViewItem item = new ReviewListViewItem();
        item.setCampaignCode(campaignCode);
        item.setImgIcon(imgIcon);
        item.setTitle(title);
        item.setReward(reward);
        item.setReviewMethod(reviewMethod);
        item.setTargetCnt(targetCnt);
        item.setJoinCnt(joinCnt);
        item.setJoinYn(joinYn);
        item.setRemainDays(remainDays);

        listViewItemList.add(item);
    }
}
