package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class MissionAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<MissionListViewItem> listViewItemList = new ArrayList<MissionListViewItem>();
    // ListViewAdapter의 생성자
    public MissionAdapter() {
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
            convertView = inflater.inflate(R.layout.listview_mission_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivImageIcon = (ImageView) convertView.findViewById(R.id.missionItem_ivImageIcon);
        TextView tvAppTitle = (TextView) convertView.findViewById(R.id.missionItem_tvAppTitle);
        TextView tvMission = (TextView) convertView.findViewById(R.id.missionItem_tvMission);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.missionItem_tvPoint);
        LinearLayout llParent = (LinearLayout)convertView.findViewById(R.id.missionItem_llParent);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MissionListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        Picasso.with(context).load(listViewItem.getImgIcon()).resize(75, 75).into(ivImageIcon);
        tvAppTitle.setText(listViewItem.getAppTitle());
        if(listViewItem.getPointRealtimeYn().equals("Y")){
            tvMission.setText(listViewItem.getMission()+" (즉시지급)");
        }else{
            tvMission.setText(listViewItem.getMission()+" ("+listViewItem.getPointDate()+"지급)");
        }
        tvPoint.setText(Html.fromHtml("<font color='#7161C4'>"+Common.getTvComma(listViewItem.getPoint())+"P</font>"));

        String strColor = "#D3D3D3";
        if(listViewItem.getJoinYn().equals("Y")){
            tvAppTitle.setTextColor(Color.parseColor(strColor));
            tvMission.setTextColor(Color.parseColor(strColor));
            tvPoint.setTextColor(Color.parseColor(strColor));

        }

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
    public void addItem(String campaignCode,String imgIcon ,String appTitle,String developerName,String mission,String point,String pointRealtimeYn,String pointDate,String joinYn) {
        MissionListViewItem item = new MissionListViewItem();
        item.setCampaignCode(campaignCode);
        item.setImgIcon(imgIcon);
        item.setAppTitle(appTitle);
        item.setDeveloperName(developerName);
        item.setMission(mission);
        item.setPoint(point);
        item.setPointRealtimeYn(pointRealtimeYn);
        item.setPointDate(pointDate);
        item.setJoinYn(joinYn);

        listViewItemList.add(item);
    }
}
