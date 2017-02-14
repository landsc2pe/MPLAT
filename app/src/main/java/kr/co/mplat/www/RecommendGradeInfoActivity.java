package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class RecommendGradeInfoActivity extends NAppCompatActivity {
    Intent intent = null;
    Common common = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_grade_info);
        setTvTitle("추천등급별 혜택 안내");

        common = new Common(this);
        WebView wv_auth = (WebView) findViewById(R.id.recommendGradeInfo_wv);
        WebSettings webSettings = wv_auth.getSettings();
        webSettings.setJavaScriptEnabled(true); //자바스크립트 허용
        wv_auth.setWebViewClient(new WebViewClient());    //웹뷰 안에서 다른 페이지로 이동할 경우 웹뷰 안에서 이동하도록 함

        wv_auth.getSettings().setDefaultTextEncodingName("UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<style type='text/css'>");
        sb.append("	*{color:#7d7d7d;}");
        sb.append("	.primary{color:#7161C4;}");
        sb.append("	table{border-collapse:collapse;width:100%;margin-top:10px;}");
        sb.append("	th{border:1px solid #DEDEDE;background:#eee;padding:10px;text-align:center}");
        sb.append("	td{border:1px solid #DEDEDE;padding:10px;text-align:center;}");

        sb.append("</style>");
        sb.append("<br/><span><b>추천등급 조건표</b></span>");

        sb.append("<table>");
        sb.append("	<thead>");
        sb.append("		<tr>");
        sb.append("			<th>추천 등급</th>");
        sb.append("			<th>3개월 평균 추천인원</th>");
        sb.append("			<th>추가포인트</th>");
        sb.append("		</tr>");
        sb.append("	</thead>");
        sb.append("	<tbody>");
        sb.append("		<tr>");
        sb.append("			<td>1등급</th>");
        sb.append("			<td>50명이상</th>");
        sb.append("			<td class='primary'>100P</th>");
        sb.append("		</tr>");
        sb.append("		<tr>");
        sb.append("			<td>2등급</th>");
        sb.append("			<td>30~49명</th>");
        sb.append("			<td class='primary'>50P</th>");
        sb.append("		</tr>");
        sb.append("		<tr>");
        sb.append("			<td>3등급</th>");
        sb.append("			<td>15~29명</th>");
        sb.append("			<td class='primary'>30P</th>");
        sb.append("		</tr>");
        sb.append("		<tr>");
        sb.append("			<td>4등급</th>");
        sb.append("			<td>0~14명</th>");
        sb.append("			<td class='primary'>0P</th>");
        sb.append("		</tr>");
        sb.append("	</tbody>");
        sb.append("</table>");

        sb.append("<p>- 회원추천 시 추천등급에 따라 기준 포인트에 추가 포인트가 적립됩니다.</p>");
        sb.append("<p>- 추가 포인트는 추천 포인트 지급 시 자동으로 계산되어 적립됩니다.</p>");
        sb.append("</body></html>");
        wv_auth.loadData(sb.toString(),  "text/html; charset=UTF-8", null);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        ImageButton ib_back = (ImageButton) ab.findViewById(R.id.ibBack);
        ib_back.setImageResource(R.drawable.ic_action_cancel);
        return ret;
    }
}
