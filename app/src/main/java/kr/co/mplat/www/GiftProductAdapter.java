package kr.co.mplat.www;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-02-01.
 */

public class GiftProductAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<GiftProductListViewItem> listViewItemList = new ArrayList<GiftProductListViewItem>() ;

    public GiftProductAdapter() {
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_gift_product_item, parent, false);

            //
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivGoods = (ImageView) convertView.findViewById(R.id.giftproduct_ivGoods);
        TextView tvProduct = (TextView) convertView.findViewById(R.id.giftproduct_tvProduct);
        TextView tvPoint = (TextView) convertView.findViewById(R.id.giftproduct_tvPoint);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        GiftProductListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //iconImageView.setImageDrawable(listViewItem.getIcon());
        Picasso.with(context).load(listViewItem.getGoodsImg()).resize(75, 75).into(ivGoods);
        tvProduct.setText(listViewItem.getProduct());
        tvPoint.setText(listViewItem.getPoint()+"P");



        return convertView;
    }

    public void addItem(String productCode, String point, String goodsImg, String product) {
        GiftProductListViewItem item = new GiftProductListViewItem();

        item.setProductCode(productCode);
        item.setPoint(point);
        item.setGoodsImg(goodsImg);
        item.setProduct(product);

        listViewItemList.add(item);
    }
}
