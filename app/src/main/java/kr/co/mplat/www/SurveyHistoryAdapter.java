package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Html;
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

public class SurveyHistoryAdapter extends BaseExpandableListAdapter{
    private AlertDialog dialog = null;
    private Context mContext;
    private ArrayList<SurveyHistoryItemParent> parent;
    private LayoutInflater inflater;
    private String reason = "";

    public SurveyHistoryAdapter(Context mContext, ArrayList<SurveyHistoryItemParent> parent) {
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
            convertView = inflater.inflate(R.layout.listview_survey_history_parent_item,null);
        }
        SurveyHistoryItemParent itemParent = (SurveyHistoryItemParent) getGroup(groupPosition);
        String type = itemParent.getType();
        String point = itemParent.getPoint();

        TextView tvType = (TextView) convertView.findViewById(R.id.surveyHistoryPanrentItem_tvType);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.surveyHistoryPanrentItem_tvPoint);
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
        SurveyHistoryItemParent itemParent = (SurveyHistoryItemParent) getGroup(groupPosition);
        String type = itemParent.getType();

        //inflate the layout
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_survey_history_child_item, null);
        }

        SurveyHistoryItemChild child = (SurveyHistoryItemChild)getChild(groupPosition, childPosition);
        String type_c = child.getType();
        String title = child.getTitle();
        String point = child.getPoint();

        //set the child name
        TextView tvTitle = (TextView)convertView.findViewById(R.id.surveyHistory_tvTitle);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.surveyHistoryParentItem_tvPoint);
        //get the imageView

        if(type_c.equals("완료")){
            tvTitle.setText(Html.fromHtml("["+type_c+"]<br/>"+title));
            tvPoint.setText(Html.fromHtml("<font color='#62BDB5'>"+Common.getTvComma(point)+" P</font>"));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
