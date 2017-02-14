package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

public class SurveyResultActivity extends NAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    Common common = null;
    private String campaignCode = "";
    private String resultCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result);
        common = new Common(this);
        setTvTitle("설문종료 안내");

        Intent intent = getIntent();
        String resultCode = intent.getExtras().getString("RESULT_CODE").toString();
        String point = intent.getExtras().getString("POINT").toString();
        String totalPoint = intent.getExtras().getString("TOTAL_POINT").toString();

        TextView tvCompleteMsg = ((TextView)findViewById(R.id.surveyResult_tvCompleteMsg));
        TextView tvCompleteDesc = ((TextView)findViewById(R.id.surveyResult_tvCompleteDesc));
        TextView tvPoint = ((TextView)findViewById(R.id.surveyResult_tvPoint));
        TextView tvTotalPoint = ((TextView)findViewById(R.id.surveyResult_tvTotalPoint));

        //설문완료경우 메세지
        if(resultCode.equals("1")){
            tvCompleteMsg.setText(Html.fromHtml("조사에 참여해 주셔서 감사합니다."));
            tvCompleteDesc.setVisibility(View.GONE);
        }else{
            tvCompleteMsg.setText(Html.fromHtml("죄송합니다. <font color='#D57A76'>설문이 중단</font>되었습니다."));
            tvCompleteDesc.setText(Html.fromHtml("귀하께서는 조사 대상이 아니셔서 더 이상 조사에 참여하실 수 없습니다.\n\n엠플랫은 조사 대상이 아니셔서 조사에 참여하지 못한 경우, 일정포인트를 적립해 드립니다.\n\n엠플랫은 참여하실 수 있는 조사만을 안내해 드리려고 최대한 노력하고 있습니다.\n\n다만, 회원님의 통계정보/전문패널 정보를 통해 조사 대상자 여부를 사전에 확인하기 어려운 경우 조사 진행에 앞서 몇 가지 설문을 통해 대상자 여부를 확인하고 있고, 대상자 조건에 맞지 않는 경우 해당조사에 참여하실 수 없는 점을 이해해 주시기 바랍니다."));
        }
        tvPoint.setText(Html.fromHtml(Common.getTvComma(point)+"P"));
        tvTotalPoint.setText(Html.fromHtml(Common.getTvComma(totalPoint)+"P"));


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
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {

            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void dialogHandler(String result) {

    }
}
