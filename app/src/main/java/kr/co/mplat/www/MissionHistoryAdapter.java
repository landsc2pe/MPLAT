package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Context;
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

public class MissionHistoryAdapter extends BaseExpandableListAdapter{
    private AlertDialog dialog = null;
    private Context mContext;
    private ArrayList<MissionHistoryItemParent> parent;
    private LayoutInflater inflater;
    private String reason = "";

    public MissionHistoryAdapter(Context mContext, ArrayList<MissionHistoryItemParent> parent) {
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
            convertView = inflater.inflate(R.layout.listview_mission_history_parent_item,null);
        }
        MissionHistoryItemParent itemParent = (MissionHistoryItemParent) getGroup(groupPosition);
        String type = itemParent.getType();
        String point = itemParent.getPoint();

        TextView tvType = (TextView) convertView.findViewById(R.id.missionHistoryPanrentItem_tvType);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.missionHistoryPanrentItem_tvPoint);
        tvType.setText(Html.fromHtml(type));
        if(type.equals("적립 완료")){
            tvPoint.setText(Html.fromHtml("<font color='#62BDB5'>"+Common.getTvComma(point)+" P</font>"));
        }else if(type.equals("적립 예정")){
            tvPoint.setText(Html.fromHtml(Common.getTvComma(point)+" P"));
        }else if(type.equals("적립 불가")){
            tvPoint.setText(Html.fromHtml("<font color='#D57A76'>"+Common.getTvComma(point)+" P</font>"));
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
        MissionHistoryItemParent itemParent = (MissionHistoryItemParent) getGroup(groupPosition);
        String type = itemParent.getType();

        //inflate the layout
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_mission_history_child_item, null);
        }

        MissionHistoryItemChild child = (MissionHistoryItemChild)getChild(groupPosition, childPosition);
        String mission = child.getMission();
        String title = child.getAppTitle();
        String pointDate = child.getPointDate();
        String point = child.getPoint();
        reason = child.getReason();

        MissionTag mtag = new MissionTag();
        mtag.mission = mission;
        mtag.title = title;
        mtag.reason = reason;

        convertView.setTag(mtag);

        Log.i("wtkim",convertView.toString());

        //set the child name
        TextView tvMission = (TextView) convertView.findViewById(R.id.missionHistory_tvMission);
        TextView tvPoint = (TextView)convertView.findViewById(R.id.missionHistoryPanrentItem_tvPoint);
        ImageView ivHelp = (ImageView) convertView.findViewById(R.id.missionHistory_ivHelp);
        //get the imageView

        if(type.equals("적립 완료")){
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

    static public class MissionTag{
        public String mission = "";
        public String title = "";
        public String reason = "";
    }
}
