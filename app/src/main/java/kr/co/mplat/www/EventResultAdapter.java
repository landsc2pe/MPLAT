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

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class EventResultAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<EventResultListViewItem> listViewItemList = new ArrayList<EventResultListViewItem>();
    // ListViewAdapter의 생성자
    public EventResultAdapter() {
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
            convertView = inflater.inflate(R.layout.listview_event_result_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView tvDate = (TextView) convertView.findViewById(R.id.eventResult_tvDate);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.eventResult_tvTitle);

        LinearLayout llParent = (LinearLayout)convertView.findViewById(R.id.eventResult_llParent);
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        EventResultListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        tvDate.setText(listViewItem.getRegistDate());
        tvTitle.setText(listViewItem.getTitle());

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
    public void addItem(String seq,String registDate,String title, String topYn) {
        EventResultListViewItem item = new EventResultListViewItem();
        item.setSeq(seq);
        item.setRegistDate(registDate);
        item.setTitle(title);
        item.setTopYn(topYn);

        listViewItemList.add(item);
    }
}
