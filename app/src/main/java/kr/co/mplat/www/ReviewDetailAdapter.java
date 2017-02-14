package kr.co.mplat.www;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-02-06.
 */

public class ReviewDetailAdapter extends BaseAdapter {
    private ArrayList<ReviewDetailListViewItem> listviewItemList = new ArrayList<ReviewDetailListViewItem>();

    public ReviewDetailAdapter() {
    }

    @Override
    public int getCount() {
        return listviewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_reviewdetailtab2_item,parent,false);
        }

        TextView tvEmail = (TextView)convertView.findViewById(R.id.reviewDetailTab2_tvEmail);
        TextView tvComment = (TextView)convertView.findViewById(R.id.reviewDetailTab2_tvComment);

        ReviewDetailListViewItem listViewItem = listviewItemList.get(position);
        tvEmail.setText(listViewItem.getEmail());
        tvComment.setText(listViewItem.getComment());
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listviewItemList.get(position);
    }

    public void addItem(String email, String comment, String registDatetime) {
        ReviewDetailListViewItem item = new ReviewDetailListViewItem();
        item.setEmail(email);
        item.setComment(comment);
        item.setRegistDatetime(registDatetime);

        listviewItemList.add(item);
    }
}
