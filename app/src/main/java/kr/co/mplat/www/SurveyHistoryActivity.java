package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurveyHistoryActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_PWDCHECK = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();
    private int lastLoadedCnt = 99999;
    private int rnum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_history);
        common = new Common(this);

        //문구변경
        ((TextView)findViewById(R.id.surveyHistory_descinfo)).setText(Html.fromHtml("오늘을 기준으로 <font color='#7161C4'>최근 3달간의</font> 미션 참여내역만 표시됩니다."));
        //설문조사 참여내역 선택시 이동
        ((LinearLayout)findViewById(R.id.surveyHistory_llMission)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(SurveyHistoryActivity.this,SurveyActivity.class);
                startActivity(intent);
            }
        });
        dataload();
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
    public void dataload(){
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_surveyHistory), null);
    }
    @Override
    public void dialogHandler(String result) {

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
                ary_lists = json.getJSONArray("LIST");

                int i;
                String pointParent = json.getString("POINT");
                String pointComplete = json.getString("POINT_COMPLETE");
                String pointExpected = json.getString("POINT_EXPECTED");

                String type = "";
                String title = "";
                String point = "";
                String pointGiveYn = "";

                ExpandableListView elv = (ExpandableListView)findViewById(R.id.surveyHistory_elvList);
                ArrayList<SurveyHistoryItemChild> comp_c = new ArrayList<SurveyHistoryItemChild>();//완료
                SurveyHistoryItemParent comp = new SurveyHistoryItemParent();
                SurveyHistoryItemChild tmp_c = new SurveyHistoryItemChild();

                String strType = "";
                for(i=0;i<ary_lists.length();i++){
                    type = ((JSONObject)ary_lists.get(i)).getString("TYPE");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    point = ((JSONObject)ary_lists.get(i)).getString("POINT");
                    pointGiveYn = ((JSONObject)ary_lists.get(i)).getString("POINT_GIVE_YN");

                    tmp_c = new SurveyHistoryItemChild(type,title,point,pointGiveYn);
                    if(type.equals("완료")){
                        comp_c.add(tmp_c);
                    }
                }

                if(comp_c != null) comp = new SurveyHistoryItemParent("적립 완료",pointComplete,comp_c);
                ArrayList<SurveyHistoryItemParent> lists = new ArrayList<SurveyHistoryItemParent>();
                if(comp != null) lists.add(comp);

                //확장 리스트 뷰 ##########################################################################################3

                //create and bind to adatper
                SurveyHistoryAdapter adapter = new SurveyHistoryAdapter(this, lists);
                elv.setAdapter(adapter);

                //set onclick listener
                elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        //Toast.makeText(getApplicationContext(), lists.get(groupPosition).players.get(childPosition), Toast.LENGTH_LONG).show();

                        Log.i("wtkim","하하하하");
                        Log.i("wtkim","하하하하");
                        return false;
                    }
                });


                //확장 리스트 뷰 END ##########################################################################################3


            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err, getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
}
