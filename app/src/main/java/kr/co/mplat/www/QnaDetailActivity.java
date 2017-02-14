package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class QnaDetailActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata {
    private Common common = null;
    Intent intent = null;
    private final int CALLTYPE_DEL = 1;
    private int dialogType=0;
    String seq = "";
    String qna_type = "";
    String title = "";
    String question = "";
    String question_date = "";
    String answer = "";
    String answer_date = "";
    String gubun = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_detail);
        setTvTitle("문의내역 상세 내용");
        common = new Common(this);

        intent = getIntent();
        seq = intent.getExtras().getString("SEQ");
        qna_type = intent.getExtras().getString("QNA_TYPE");
        title = intent.getExtras().getString("TITLE");
        question = intent.getExtras().getString("QUESTION");
        question_date = intent.getExtras().getString("QUESTION_DATE");
        answer = intent.getExtras().getString("ANSWER");
        answer_date = intent.getExtras().getString("ANSWER_DATE");

        if(!answer.equals("")){
            gubun = "답변완료";
        }else{
            gubun = "확인중";
            ((View)findViewById(R.id.line21)).setVisibility(View.GONE);
            ((View)findViewById(R.id.line22)).setVisibility(View.GONE);
        }

        ((TextView)findViewById(R.id.qnaDetail_tvQuestionDate)).setText(Html.fromHtml("<b>문의내용</b> (문의일:"+question_date+")"));
        ((TextView)findViewById(R.id.qnaDetail_tvGubun)).setText(Html.fromHtml("<b><font color='#7161C4'>"+gubun+"</font></b>"));
        ((TextView)findViewById(R.id.qnaDetail_tvType)).setText(Html.fromHtml("[ "+qna_type+" ]"));
        ((TextView)findViewById(R.id.qnaDetail_tvTitle)).setText(Html.fromHtml("<b>"+title+"</b>"));
        ((TextView)findViewById(R.id.qnaDetail_tvQuestion)).setText(Html.fromHtml(question));
        ((TextView)findViewById(R.id.qnaDetail_tvAnswerDate)).setText(Html.fromHtml(answer_date));
        ((TextView)findViewById(R.id.qnaDetail_tvAnswer)).setText(Html.fromHtml(answer));

        ((Button)findViewById(R.id.qnaDetail_btnDelete)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialogType = 1;
                Common.createDialog(QnaDetailActivity.this, "삭제 확인",null, "문의내용을 삭제하시겠습니까?\n문의내용 복구는 불가능 합니다.", getString(R.string.btn_ok),getString(R.string.btn_cancel), false, false);
            }
        });
    }

    @Override
    public void dialogHandler(String result) {
        if(result.equals("ok") && dialogType == 1){
            Object[][] params = {
                {"SEQ",seq}
            };
            common.loadData(CALLTYPE_DEL, getString(R.string.url_qnaDelete), params);
        }else if(result.equals("ok") && dialogType == 2){
            intent = new Intent(QnaDetailActivity.this,QnaHistoryActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_DEL) delHandler(str);
    }

    public void delHandler(String str){
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                if (result.equals("OK")) {
                    dialogType = 2;
                    Common.createDialog(this, getString(R.string.app_name).toString(),null, "문의내용이 정상적으로 삭제되었습니다.", getString(R.string.btn_ok),null, true, false);
                }
            } else {
                dialogType = 9;
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            dialogType = 9;
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        start(null);
    }
    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }

    }
}
