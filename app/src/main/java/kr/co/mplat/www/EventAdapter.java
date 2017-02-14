package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
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

public class EventAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<EventListViewItem> listViewItemList = new ArrayList<EventListViewItem>();
    // ListViewAdapter의 생성자
    public EventAdapter() {
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
            convertView = inflater.inflate(R.layout.listview_event_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout llParent = (LinearLayout)convertView.findViewById(R.id.event_llParent);
        ImageView ivTitleImg = (ImageView) convertView.findViewById(R.id.event_ivTitleImg);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.event_tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.event_tvDate);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        EventListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        Picasso.with(context).load(listViewItem.getTitleImg()).resize(75, 75).into(ivTitleImg);
        tvTitle.setText(listViewItem.getTitle());
        tvDate.setText(listViewItem.getStartDate()+"~"+listViewItem.getEndDate());

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
    public void addItem(String title,String titleImg,String directYn,String directLink,String startDate,String endDate,String seq,String ord) {
        EventListViewItem item = new EventListViewItem();
        item.setTitle(title);
        item.setTitleImg(titleImg);
        item.setDirectYn(directYn);
        item.setDirectLink(directLink);
        item.setStartDate(startDate);
        item.setEndDate(endDate);
        item.setSeq(seq);
        item.setOrd(ord);
        listViewItemList.add(item);
    }
}
