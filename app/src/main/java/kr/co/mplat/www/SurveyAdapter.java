package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Color;
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

public class SurveyAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<SurveyListViewItem> listViewItemList = new ArrayList<SurveyListViewItem>();
    // ListViewAdapter의 생성자
    public SurveyAdapter() {
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
            convertView = inflater.inflate(R.layout.listview_survey_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //ImageView ivImageIcon = (ImageView) convertView.findViewById(R.id.missionItem_ivImageIcon);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.survey_tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.survey_tvDate);
        TextView tvChildPoint = (TextView) convertView.findViewById(R.id.survey_tvChildPoint);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        SurveyListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //Picasso.with(context).load(listViewItem.getImgIcon()).resize(75, 75).into(ivImageIcon);
        String sd = listViewItem.getStartDate();
        String ed = listViewItem.getEndDate();
        String sd1 = sd.substring(0,4);
        String sd2 = sd.substring(4,6);
        String sd3 = sd.substring(6,8);
        String ed1 = ed.substring(0,4);
        String ed2 = ed.substring(4,6);
        String ed3 = ed.substring(6,8);

        tvTitle.setText(listViewItem.getTitle());
        tvDate.setText(sd1+"."+sd2+"."+sd3+"~"+ed1+"."+ed2+"."+ed3);
        tvChildPoint.setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(listViewItem.getPoint())+" P</font>"));
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
    public void addItem(String campaign_code, String title, String startDate, String endDate, String point, String surveyTime, String surveyUrl, String pointRealtimeYn) {
        SurveyListViewItem item = new SurveyListViewItem();
        item.setCampaign_code(campaign_code);
        item.setTitle(title);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setPoint(point);
        item.setSurveyTime(surveyTime);
        item.setSurveyUrl(surveyUrl);
        item.setPointRealtimeYn(pointRealtimeYn);
        listViewItemList.add(item);
    }
}
