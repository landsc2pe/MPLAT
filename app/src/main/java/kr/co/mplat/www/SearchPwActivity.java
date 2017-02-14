package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class SearchPwActivity extends NAppCompatActivity implements View.OnClickListener,I_startFinish,I_dialogdata,I_loaddata{
    EditText searchpw_etId = null;
    String id = null;
    Intent intent = null;
    Common common = null;
    final int CALLTYPE_SAVE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common=new Common(this);
        setContentView(R.layout.activity_search_pw);
        setTvTitle("비밀번호 찾기");
        searchpw_etId = (EditText)findViewById(R.id.searchpw_etId);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchpw_btnSearchPw :
                id = searchpw_etId.getText().toString();

                if(searchpw_etId.getText().toString().equals("")){
                    Common.createDialog(this, getString(R.string.txt_searchPw).toString(), searchpw_etId.getText().toString(), getString(R.string.dial_msg7), getString(R.string.btn_ok),null, false, false);
                }else{
                    Object[][] params = {
                            {"ID", id}
                    };
                    common.loadData(CALLTYPE_SAVE, getString(R.string.url_searchPw), params);
                }
                break;
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

    @Override
    public void dialogHandler(String result) {

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_SAVE) saveHandler(str);
    }
    //저장 처리
    public void saveHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            if (err.equals("")) {
                String result = json.getString("RESULT");
                Log.i("wtKim","result=>"+result);
                if (result.equals("OK")) {
                    intent = new Intent(SearchPwActivity.this,SearchPwMobileActivity.class);
                    intent.putExtra("ID",id);
                    startActivity(intent);
                }
            } else {
                Common.createDialog(this, getString(R.string.txt_searchPw).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.txt_searchPw).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
