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

public class ReviewListAdapter extends BaseExpandableListAdapter{
    private AlertDialog dialog = null;
    private Context mContext;
    private ArrayList<ReviewListItemParent> parent;
    private LayoutInflater inflater;
    private String type ="";
    private String campaignCode = "";
    private String reviewReason = "";

    public ReviewListAdapter(Context mContext, ArrayList<ReviewListItemParent> parent) {
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
            convertView = inflater.inflate(R.layout.listview_review_list_parent_item,null);
        }
        ReviewListItemParent itemParent = (ReviewListItemParent) getGroup(groupPosition);
        type = itemParent.getType();
        String pCount = itemParent.getCount();

        TextView tvType = (TextView) convertView.findViewById(R.id.reviewListPanrentItem_tvType);
        TextView tvCount = (TextView) convertView.findViewById(R.id.reviewListPanrentItem_tvCount);
        tvType.setText(Html.fromHtml(type));
        if(type.equals("미등록")||type.equals("수정요청")){
            tvCount.setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(pCount)+"</font>"));
        }else if(type.equals("검수완료")){
            tvCount.setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(pCount)+"</font>"));
        }else{
            tvCount.setText(Html.fromHtml("<font>"+Common.getTvComma(pCount)+"</font>"));
        }

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
        ReviewListItemParent itemParent = (ReviewListItemParent) getGroup(groupPosition);
        final String type = itemParent.getType();

        //inflate the layout
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_review_list_child_item, null);
        }

        ReviewListItemChild child = (ReviewListItemChild)getChild(groupPosition, childPosition);
        campaignCode = child.getCampaign_code();
        String reviewUrl = child.getReviewUrl();
        String reviewStartDate = child.getReviewStartDate();
        String reviewEndDate = child.getReviewEndDate();
        String joinEndDate = child.getJoinEndDate();
        String campaign_code = child.getCampaign_code();
        String choiceDate = child.getChoiceDate();
        reviewReason = child.getReviewReason();
        String title = child.getTitle();
        Log.i("wtkim","reviewReason==>"+reviewReason);

        /*MissionTag mtag = new MissionTag();
        mtag.mission = mission;
        mtag.title = title;
        mtag.reason = reason;

        convertView.setTag(mtag);*/

        //Log.i("wtkim",convertView.toString());

        //set the child name
        TextView tvTitle = (TextView) convertView.findViewById(R.id.reviewlist_tvTitle);
        TextView tvReviewEndDate = (TextView)convertView.findViewById(R.id.reviewlist_tvReviewEndDate);
        TextView btnRegist = (TextView) convertView.findViewById(R.id.reviewlist_btnRegist);
        //get the imageView
        tvTitle.setText(title);
        tvReviewEndDate.setText("등록(수정) 마감일 : "+joinEndDate);

        btnRegist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(type.equals("미등록")) {
                    Intent intent=new Intent(mContext,ReviewUpdateActivity.class);
                    intent.putExtra("CAMPAIGN_CODE",campaignCode);
                    mContext.startActivity(intent);
                }else if(type.equals("기간만료 / 중단")) {
                    Log.i("wtkim","reviewReason==>"+reviewReason);
                }else{
                    //테스트용 추후삭제
                    Intent intent=new Intent(mContext,ReviewUpdateActivity.class);
                    intent.putExtra("CAMPAIGN_CODE",campaignCode);
                    mContext.startActivity(intent);
                }
            }
        });
        if(type.equals("미등록")){
            btnRegist.setText("리뷰 등록");
        }else if(type.equals("등록(수정)완료 / 검수 대기")){
            btnRegist.setVisibility(View.GONE);
        }else if(type.equals("수정요청")){
            btnRegist.setText("수정 등록");
        }else if(type.equals("검수완료")){
            btnRegist.setVisibility(View.GONE);
        }else if(type.equals("자격박탈")){
            btnRegist.setText("사유보기");
            btnRegist.setBackgroundResource(R.color.red);
        }else if(type.equals("기간만료 / 중단")){
            btnRegist.setText("중단사유");
            btnRegist.setBackgroundResource(R.color.red);
        }

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


}
