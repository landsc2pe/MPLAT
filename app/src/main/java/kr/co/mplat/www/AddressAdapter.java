package kr.co.mplat.www;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class AddressAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<AddressListViewItem> listViewItemList = new ArrayList<AddressListViewItem>();

    // ListViewAdapter의 생성자
    public AddressAdapter() {
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_address_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        LinearLayout llParent = (LinearLayout) convertView.findViewById(R.id.addressItem_llParent);
        TextView tvZipNo = (TextView) convertView.findViewById(R.id.addressItem_tvZipNo);
        TextView tvRoadAddrPart1 = (TextView) convertView.findViewById(R.id.addressItem_tvRoadAddrPart1);
        //TextView tvRoadAddrPart2 = (TextView) convertView.findViewById(R.id.addressItem_tvRoadAddrPart2);
        TextView tvJibunAddr = (TextView) convertView.findViewById(R.id.addressItem_tvJibunAddr);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        AddressListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        tvZipNo.setText(listViewItem.getZipNo());
        tvRoadAddrPart1.setText(Html.fromHtml("<font color='#7161C4'>" + listViewItem.getRoadAddrPart1() + "" + listViewItem.getRoadAddrPart2() + "</font>"));
        //tvRoadAddrPart2.setText(Html.fromHtml("<font color='#7161C4'>"+listViewItem.getRoadAddrPart2()+"</font>"));
        tvJibunAddr.setText(listViewItem.getJibunAddr());

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
    public void addItem(String detBdNmList, String engAddr, String zipNo, String roadAddrPart2, String jibunAddr, String roadAddrPart1, String rnMgtSn, String admCd, String bdMgtSn, String roadAddr) {
        AddressListViewItem item = new AddressListViewItem();
        item.setDetBdNmList(detBdNmList);
        item.setEngAddr(engAddr);
        item.setZipNo(zipNo);
        item.setRoadAddrPart2(roadAddrPart2);
        item.setJibunAddr(jibunAddr);
        item.setRoadAddrPart1(roadAddrPart1);
        item.setRnMgtSn(rnMgtSn);
        item.setAdmCd(admCd);
        item.setBdMgtSn(bdMgtSn);
        item.setRoadAddr(roadAddr);

        listViewItemList.add(item);
    }


}
