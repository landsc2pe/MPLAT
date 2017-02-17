package kr.co.mplat.www;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-09.
 */

public class ReviewHistoryAdapter extends BaseExpandableListAdapter{
    private AlertDialog dialog = null;
    private Context mContext;
    private ArrayList<ReviewHistoryItemParent> parent;
    private LayoutInflater inflater;
    private String type ="";

    public ReviewHistoryAdapter(Context mContext, ArrayList<ReviewHistoryItemParent> parent) {
        this.mContext = mContext;
        this.parent = parent;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return parent.get(groupPosition).childGroups.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parent.get(groupPosition).childGroups.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.listview_review_history_parent_item,null);
        }
        ReviewHistoryItemParent itemParent = (ReviewHistoryItemParent) getGroup(groupPosition);
        type = itemParent.getType();
        String pCount = itemParent.getCount();

        TextView tvType = (TextView) convertView.findViewById(R.id.reviewHistoryPanrentItem_tvType);
        TextView tvCount = (TextView) convertView.findViewById(R.id.reviewHistoryPanrentItem_tvCount);
        tvType.setText(Html.fromHtml(type));
        if(type.equals("신청 완료(선정 대기)")||type.equals("리뷰 종료")){
            tvCount.setText(Html.fromHtml("<font>"+Common.getTvComma(pCount)+"</font>"));
        }else if(type.equals("대상자 선정")){
            tvCount.setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(pCount)+"</font>"));
        }else if(type.equals("대상자 미선정")){
            tvCount.setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(pCount)+"</font>"));
        }




        /*if(type.equals("적립 완료")){
            tvPoint.setText(Html.fromHtml("<font color='#62BDB5'>"+Common.getTvComma(point)+" P</font>"));
        }else if(type.equals("적립 예정")){
            tvPoint.setText(Html.fromHtml(Common.getTvComma(point)+" P"));
        }else if(type.equals("적립 불가")){
            tvPoint.setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(point)+" P</font>"));
        }*/

        ImageView imageView = (ImageView) convertView.findViewById(R.id.indicator);
        if(isExpanded){
            imageView.setImageResource(R.drawable.ic_action_collapse);
        } else {
            imageView.setImageResource(R.drawable.ic_action_expand);
        }
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ReviewHistoryItemParent itemParent = (ReviewHistoryItemParent) getGroup(groupPosition);
        final String type = itemParent.getType();

        //inflate the layout
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_review_history_child_item, null);
        }

        ReviewHistoryItemChild child = (ReviewHistoryItemChild)getChild(groupPosition, childPosition);
        String campaignCode = child.getCampaign_code();
        String title = child.getTitle();
        String choiceDate = child.getChoiceDate();
        String reviewStartDate = child.getReviewStartDate();
        String reviewEndDate = child.getReviewEndDate();
        String joinEndDate = child.getJoinEndDate();
        String reviewUrl = child.getReviewUrl();

        //set the child name
        TextView tvTitle = (TextView) convertView.findViewById(R.id.reviewHistory_tvTitle);
        TextView tvChoiceDate = (TextView)convertView.findViewById(R.id.reviewHistory_tvChoiceDate);
        TextView btnCancel = (TextView) convertView.findViewById(R.id.reviewHistory_btnCancel);
        //get the imageView
        tvTitle.setText(title);
        tvChoiceDate.setText(choiceDate);

        ReviewTag mtag = new ReviewTag();
        mtag.setType(type);
        mtag.setCampaignCode(campaignCode);
        btnCancel.setTag(mtag);
        //btnCancel.setTag("aaa");

        btnCancel.setOnClickListener((View.OnClickListener)mContext);

        if(type.equals("대상자 선정")){
            btnCancel.setText("리뷰 등록");
            btnCancel.setTextColor(convertView.getResources().getColor(R.color.white));
            btnCancel.setBackgroundResource(R.color.primary);

        }else if(type.equals("신청 완료(선정 대기)")){
            btnCancel.setText("취소 신청");
            btnCancel.setTextColor(convertView.getResources().getColor(R.color.white));
            btnCancel.setBackgroundResource(R.color.red);
        }else{
            btnCancel.setVisibility(View.GONE);
        }
        /*if(type.equals("적립 완료")){
            if(!pointDate.equals("")){
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title+"("+pointDate+")"));
            }else{
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title));
            }
            tvPoint.setText(Html.fromHtml("<font color='#62BDB5'>"+Common.getTvComma(point)+" P</font>"));
            ivHelp.setVisibility(View.INVISIBLE);
        }else if(type.equals("적립 예정")){
            if(!pointDate.equals("")){
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title+"("+pointDate+")"));
            }else{
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title));
            }
            tvPoint.setText(Html.fromHtml(Common.getTvComma(point)+" P"));
            ivHelp.setVisibility(View.INVISIBLE);
        }else if(type.equals("적립 불가")){
            if(!pointDate.equals("")){
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title+"("+pointDate+")"));
            }else{
                tvMission.setText(Html.fromHtml("["+mission+"]<br/>"+title));
            }
            tvPoint.setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(point)+" P</font>"));
            ivHelp.setVisibility(View.VISIBLE);
        }*/
/*
        ivHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","물음표");
            }
        });
        */
        //String positionName = (String) getGroup(groupPosition).toString();
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static public class ReviewTag{
        public String type = "";
        public String campaignCode = "";

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCampaignCode() {
            return campaignCode;
        }

        public void setCampaignCode(String campaignCode) {
            this.campaignCode = campaignCode;
        }
    }
}
