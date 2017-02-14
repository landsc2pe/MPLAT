package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by gdfwo on 2016-11-21.
 */

public class MAppCompatActivity extends AppCompatActivity{
    private int selectedMenuType = 0;
    private ImageButton ibNav1,ibNav2,ibNav3,ibNav4;
    Intent intent = null;
    Common common = null;

    public int getSelectedMenuType() {
        return selectedMenuType;
    }

    public void setSelectedMenuType(int selectedMenuType) {
        this.selectedMenuType = selectedMenuType;
    }

    View ab;

    public void setBackBtnVisible(boolean isVisible){
        if(ab!=null && !isVisible) {
            ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
            ib_back.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //메뉴바
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);

        common = new Common(this);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        ab = inflater.inflate(R.layout.custom_menubar, null);
        actionBar.setCustomView(ab,layoutParams);

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)ab.getParent();
        parent.setContentInsetsAbsolute(0,0);

        ibNav1 = (ImageButton)ab.findViewById(R.id.menubar_ibNav1);
        ibNav2 = (ImageButton)ab.findViewById(R.id.menubar_ibNav2);
        ibNav3 = (ImageButton)ab.findViewById(R.id.menubar_ibNav3);
        ibNav4 = (ImageButton)ab.findViewById(R.id.menubar_ibNav4);

        ibNav1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Log.i("wtkim","getBaseContext()==>"+this.getClass().getName());
                intent = new Intent(getBaseContext(),MainActivity.class);
                intent.putExtra("selectedMenuType","1");
                startActivity(intent);
                finish();
            }
        });

        ibNav2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Log.i("wtkim","getBaseContext()==>"+getBaseContext());
                intent = new Intent(getBaseContext(),PointActivity.class);
                intent.putExtra("selectedMenuType","2");
                startActivity(intent);
                finish();
            }
        });

        ibNav3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(getBaseContext(),MypageActivity.class);
                intent.putExtra("selectedMenuType","3");
                startActivity(intent);
                finish();
            }
        });

        ibNav4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("wtKim","M--setOnClickListener!");
               // selectedMenuType = 4;
               // ChangeNavIcon(selectedMenuType);
              /* UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        //로그아웃 성공 후 하고싶은 내용 코딩 ~
                        Log.i("wtKim","테스트 로그아웃");
                    }
                });
                Common.setPreference(getApplicationContext(), "UID", "");
                Common.setPreference(getApplicationContext(), "KEY", "");
                //임시로 로그인 페이지로 이동하도록 함
               intent = new Intent(MAppCompatActivity.this,LoginActivity.class);
                startActivity(intent);*/
                intent = new Intent(getBaseContext(),ConfigActivity.class);
                intent.putExtra("selectedMenuType","4");
                startActivity(intent);
                finish();

            }
        });

        return true;
    }

    //이미지 변경
    public void ChangeNavIcon(int MenuType){

        Log.i("wtkim","MenuType==>"+MenuType);
        switch (MenuType){
            case 1:
                ibNav1.setImageResource(R.drawable.nav1_on);
                ibNav2.setImageResource(R.drawable.nav2_off);
                ibNav3.setImageResource(R.drawable.nav3_off);
                ibNav4.setImageResource(R.drawable.nav4_off);
                break;
            case 2:
                ibNav1.setImageResource(R.drawable.nav1_off);
                ibNav2.setImageResource(R.drawable.nav2_on);
                ibNav3.setImageResource(R.drawable.nav3_off);
                ibNav4.setImageResource(R.drawable.nav4_off);
                break;
            case 3:
                ibNav1.setImageResource(R.drawable.nav1_off);
                ibNav2.setImageResource(R.drawable.nav2_off);
                ibNav3.setImageResource(R.drawable.nav3_on);
                ibNav4.setImageResource(R.drawable.nav4_off);
                break;
            case 4:
                ibNav1.setImageResource(R.drawable.nav1_off);
                ibNav2.setImageResource(R.drawable.nav2_off);
                ibNav3.setImageResource(R.drawable.nav3_off);
                ibNav4.setImageResource(R.drawable.nav4_on);
                break;
            default:
                ibNav1.setImageResource(R.drawable.nav1_on);
                ibNav2.setImageResource(R.drawable.nav2_off);
                ibNav3.setImageResource(R.drawable.nav3_off);
                ibNav4.setImageResource(R.drawable.nav4_off);
                break;

        }
    }

}
