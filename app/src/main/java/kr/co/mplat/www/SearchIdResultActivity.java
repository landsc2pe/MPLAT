package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class SearchIdResultActivity extends NAppCompatActivity implements View.OnClickListener,I_loaddata,I_startFinish,I_dialogdata{
    Intent intent = null;
    Common common = null;
    private String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id_result);
        setTvTitle("아이디 찾기");
        common = new Common(this);

        id = getIntent().getStringExtra("ID");
        TextView tvId = (TextView)findViewById(R.id.searchidresult_tvId);
        tvId.setText(id);
    }
    @Override
    public void onClick(View view) {
        intent = new Intent(SearchIdResultActivity.this,LoginActivity.class);
        startActivity(intent);
        intent.putExtra("ID",id);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        setBackBtnVisible(false);
        return ret;
    }
    @Override
    public void onBackPressed() {
    }

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {

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
