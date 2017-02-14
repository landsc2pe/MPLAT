package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
//http://blog.naver.com/PostView.nhn?blogId=dbwoghks90&logNo=220620764598&parentCategoryNo=&categoryNo=&viewDate=&isShowPopularPosts=false&from=postView
public class MissionHistoryActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
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
        setContentView(R.layout.activity_mission_history);
        common = new Common(this);

        //문구 변경
        ((TextView)findViewById(R.id.missionHistory_descinfo)).setText(Html.fromHtml("오늘을 기준으로 <font color='#7161C4'><b>최근 3달간</b></font>의 미션 참여내역만 표시됩니다."));


        //미션 참여 클릭시
        ((LinearLayout)findViewById(R.id.missionHistory_llMission)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(MissionHistoryActivity.this,MissionActivity.class);
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
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_missionHistory), null);
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
                String pointComplete = json.getString("POINT_COMPLETE");
                String pointExpected = json.getString("POINT_EXPECTED");
                String pointIncomplete = json.getString("POINT_INCOMPLETE");
                String type = "";
                String mission = "";
                String appTitle = "";
                String point = "";
                String pointDate = "";
                String incompleteReason = "";


                ExpandableListView elv = (ExpandableListView)findViewById(R.id.missionHistory_elvList);
                ArrayList<MissionHistoryItemChild> comp_c = new ArrayList<MissionHistoryItemChild>();
                ArrayList<MissionHistoryItemChild> expec_c = new ArrayList<MissionHistoryItemChild>();
                ArrayList<MissionHistoryItemChild> incomp_c = new ArrayList<MissionHistoryItemChild>();
                MissionHistoryItemParent comp = new MissionHistoryItemParent();
                MissionHistoryItemParent expec = new MissionHistoryItemParent();
                MissionHistoryItemParent incomp = new MissionHistoryItemParent();
                MissionHistoryItemChild tmp_c = new MissionHistoryItemChild();

                String strType = "";
                for(i=0;i<ary_lists.length();i++){
                    type = ((JSONObject)ary_lists.get(i)).getString("TYPE");
                    mission = ((JSONObject)ary_lists.get(i)).getString("MISSION");
                    appTitle = ((JSONObject)ary_lists.get(i)).getString("APP_TITLE");
                    point = ((JSONObject)ary_lists.get(i)).getString("POINT");
                    pointDate = ((JSONObject)ary_lists.get(i)).getString("POINT_DATE");
                    incompleteReason = ((JSONObject)ary_lists.get(i)).getString("INCOMPLETE_REASON");
                    tmp_c = new MissionHistoryItemChild(mission,appTitle,pointDate,point,incompleteReason);
                    if(type.equals("COMPLETE")){
                        comp_c.add(tmp_c);
                    }else if(type.equals("EXPECTED")){
                        expec_c.add(tmp_c);
                    }else if(type.equals("INCOMPLETE")){
                        incomp_c.add(tmp_c);
                    }
                }

                if(comp_c != null) comp = new MissionHistoryItemParent("적립 완료",pointComplete,comp_c);
                if(expec_c != null) expec = new MissionHistoryItemParent("적립 예정",pointExpected,expec_c);
                if(incomp_c != null) incomp = new MissionHistoryItemParent("적립 불가",pointIncomplete,incomp_c);

                ArrayList<MissionHistoryItemParent> lists = new ArrayList<MissionHistoryItemParent>();
                if(comp != null) lists.add(comp);
                if(expec != null) lists.add(expec);
                if(incomp != null) lists.add(incomp);

                //확장 리스트 뷰 ##########################################################################################3

                //create and bind to adatper
                MissionHistoryAdapter adapter = new MissionHistoryAdapter(this, lists);
                elv.setAdapter(adapter);

                //set onclick listener
                elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        //Toast.makeText(getApplicationContext(), lists.get(groupPosition).players.get(childPosition), Toast.LENGTH_LONG).show();
                        Log.i("wtkim",v.getTag().toString());
                        Log.i("wtkim","groupPosition==>"+groupPosition);
                        MissionHistoryAdapter.MissionTag missionTag = (MissionHistoryAdapter.MissionTag)v.getTag();

                        if(groupPosition==2){
                            dialog = createDialog(R.layout.custom_dialog_mission_reason_check);
                            dialog.show();
                            ((TextView)dialog.findViewById(R.id.dialog_reasonTitle)).setText(Html.fromHtml("["+missionTag.mission+"]"+missionTag.title));
                            ((TextView)dialog.findViewById(R.id.dialog_reasonMsg)).setText(Html.fromHtml(missionTag.reason));
                            ((TextView)dialog.findViewById(R.id.dialog_reason_ok)).setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }

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

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private AlertDialog createDialog(int layoutResource) {
        if (dialog != null && dialog.isShowing()) return dialog;
        final View innerView = getLayoutInflater().inflate(layoutResource, null);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setView(layoutResource);
        return ab.create();
    }
    public void setDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 안내문 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
}
