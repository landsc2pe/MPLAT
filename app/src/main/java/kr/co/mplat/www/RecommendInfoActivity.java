package kr.co.mplat.www;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecommendInfoActivity extends NAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_info);
        setTvTitle("회원추천 안내");
        //문구변경
        ((TextView)findViewById(R.id.recommend_tvInfo13)).setText(Html.fromHtml("매월 1일부터 말일까지 가입자에 대해, <font color='#7161C4'>다음달 15일 일괄적으로 정산하여 포인트가 지급</font>됩니다."));
        ((TextView)findViewById(R.id.recommend_tvInfo23)).setText(Html.fromHtml("추천등급에 따라 <font color='#7161C4'>1명 가입 시 기본 포인트에 추가 포인트가 지급</font>됩니다."));
        ((TextView)findViewById(R.id.recommend_tvInfo31)).setText(Html.fromHtml("추천자를 확인하기 위한 <font color='#D57A76'>\"추천링크\"를 클릭하지 않고 가입하는 경우</font>"));
        ((TextView)findViewById(R.id.recommend_tvInfo32)).setText(Html.fromHtml("추천을 받아 가입한 \"회원\"이 <font color='#D57A76'>중복가입으로 판정되는 경우</font>"));
        ((TextView)findViewById(R.id.recommend_tvInfo33)).setText(Html.fromHtml("<font color='#D57A76'>부정한 방법으로 \"추천가입\"을 진행한 경우</font>"));
        ((TextView)findViewById(R.id.recommend_tvInfo34)).setText(Html.fromHtml("기타 <font color='#D57A76'>엠플랫이 명시한 \"약관\"에 위배</font>되었을 경우"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
    }
}
