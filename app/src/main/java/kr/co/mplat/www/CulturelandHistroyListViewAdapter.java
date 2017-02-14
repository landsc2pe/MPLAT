package kr.co.mplat.www;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2016-12-26.
 */

public class CulturelandHistroyListViewAdapter extends BaseAdapter {
    private String pincode;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CulturelandHistroyListViewItem> listViewItemList = new ArrayList<CulturelandHistroyListViewItem>() ;

    // CulturelandHistroyListViewAdapter 생성자
    public CulturelandHistroyListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_culturelandhistory_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView textView1 = (TextView) convertView.findViewById(R.id.culturelandHistory_textView1);   //발급일
        TextView textView2 = (TextView) convertView.findViewById(R.id.culturelandHistory_textView2);   //금액
        TextView textView3 = (TextView) convertView.findViewById(R.id.culturelandHistory_textView3);   //유효기간
        TextView textView4 = (TextView) convertView.findViewById(R.id.culturelandHistory_textView4);   //핀코드
        Button button4 = (Button) convertView.findViewById(R.id.culturelandHistory_btnNo);   //유효기간

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        CulturelandHistroyListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        textView1.setText(listViewItem.getDate());
        textView2.setText(listViewItem.getPrice());
        textView3.setText(listViewItem.getValid_date());
        textView4.setText(listViewItem.getPincode());
        pincode = listViewItem.getPincode().toString();

        button4.setText("번호 확인");

        //버튼 이벤트 등록
        button4.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtkim","pinclide==>"+pincode);
            }
        });
        return convertView;
    }

    //지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }



    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String date, String price, String valid_date, String pincode,String btnLabel) {
        CulturelandHistroyListViewItem item = new CulturelandHistroyListViewItem();
        item.setDate(date);
        item.setPrice(price);
        item.setValid_date(valid_date);
        item.setPincode(pincode);
        item.setLabel(btnLabel);

        listViewItemList.add(item);
    }


}
