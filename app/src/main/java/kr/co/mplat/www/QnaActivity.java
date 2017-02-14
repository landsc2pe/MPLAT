package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class QnaActivity extends NAppCompatActivity {
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);
        setTvTitle("1:1 문의");

        //문의하기 클릭이벤트 등록
        ((TextView)findViewById(R.id.qna_tvQnaWrite)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(QnaActivity.this,QnaWriteActivity.class);
                startActivity(intent);
            }
        });
        //문의내역 조회 클릭이벤트 등록
        ((TextView)findViewById(R.id.qna_tvQnaHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(QnaActivity.this,QnaHistoryActivity.class);
                startActivity(intent);
            }
        });

    }
}
