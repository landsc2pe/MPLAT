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

public class RecommendHistoryAdapter extends BaseAdapter{
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<RecommendHistoryListViewItem> listViewItemList = new ArrayList<RecommendHistoryListViewItem>();
    // ListViewAdapter의 생성자
    public RecommendHistoryAdapter() {
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
            convertView = inflater.inflate(R.layout.listview_recommend_history_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView tvJoinDate = (TextView) convertView.findViewById(R.id.recommendHistory_tvJoinDate);
        TextView tvRecommendName = (TextView) convertView.findViewById(R.id.recommendHistory_tvRecommendName);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.recommendHistory_tvEmail);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        RecommendHistoryListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        tvJoinDate.setText(listViewItem.getJoinDate());
        tvRecommendName.setText(listViewItem.getRecommendName());
        tvEmail.setText(listViewItem.getEmail());

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
    public void addItem(String joinDate,String recommendName,String email) {
        RecommendHistoryListViewItem item = new RecommendHistoryListViewItem();
        item.setJoinDate(joinDate);
        item.setRecommendName(recommendName);
        item.setEmail(email);

        listViewItemList.add(item);
    }
}
