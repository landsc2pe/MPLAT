package kr.co.mplat.www;

import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gdfwo on 2016-11-21.
 */

public class NAppCompatActivity extends AppCompatActivity{
    private String TvTitle = "";
    View ab;

    public String getTvTitle(){
        return TvTitle;
    }

    public void setTvTitle(String tvTitle){
        this.TvTitle = tvTitle;

    }
    public void setBackBtnVisible(boolean isVisible){
        if(ab!=null && !isVisible) {
            ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
            ib_back.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        ab = inflater.inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(ab,layoutParams);

        TextView titleTxtView = (TextView) ab.findViewById(R.id.tvTitle);
        titleTxtView.setText(TvTitle);
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)ab.getParent();
        parent.setContentInsetsAbsolute(0,0);
        //백버튼 이벤트 추가
        ImageView ibBack=(ImageView)ab.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                try {
                    AppCompatActivity a = (AppCompatActivity)view.getContext();
                    a.finish();
                }catch(Exception e){
                    Log.d("wtkim",e.toString());
                }
            }
        });
        return true;
    }

}
