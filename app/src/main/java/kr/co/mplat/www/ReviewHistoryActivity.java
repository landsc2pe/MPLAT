package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ReviewHistoryActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata,View.OnClickListener{
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_CANCEL = 2;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();

    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private int dialogType = 0;
    private String campaignCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);
        common = new Common(this);

        //문구변경
        ((TextView)findViewById(R.id.reviewHistory_descinfo)).setText(Html.fromHtml("오늘을 기준으로 <font color='#7161C4'>최근 1년간의</font> 리뷰 신청결과만 표시됩니다."));
        //리뷰신청 선택시
        ((LinearLayout)findViewById(R.id.review_llReview)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewHistoryActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
        });
        //리뷰등록 선택시
        ((LinearLayout)findViewById(R.id.review_llList)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewHistoryActivity.this,ReviewListActivity.class);
                startActivity(intent);
            }
        });
        dataload();
    }

    public void dataload(){
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_reviewHistory), null);
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
        if(result.equals("cancel") && dialogType == 2){
            Object[][] params = {
                    {"CAMPAIGN_CODE", campaignCode}
            };
            common.loadData(CALLTYPE_CANCEL, getString(R.string.url_reviewCancel), params);
        }
    }
    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
        else if (calltype == CALLTYPE_CANCEL) cancelHandler(str);

    }

    public void cancelHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                Common.createDialog(this, getString(R.string.app_name).toString(),null, "선택하신 리뷰 신청이 취소되었습니다.",  getString(R.string.btn_ok),null, false, false);
                dataload();
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err,  getString(R.string.btn_ok),null, false, false);
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    public void loadHandler(String str){
        try{
            JSONObject json = new JSONObject(str);
            Log.i("wtkim",json.toString());
            String err = json.getString("ERR");

            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");

                int i;
                String joinCnt = json.getString("JOIN_CNT");
                String completeCnt = json.getString("COMPLETE_CNT");
                String notTargetCnt = json.getString("NOT_TARGET_CNT");
                String targetCnt = json.getString("TARGET_CNT");
                String campaignCode = "";
                String type = "";
                String title = "";
                String joinEndDate = "";
                String choiceDate = "";
                String reviewStartDate = "";
                String reviewEndDate = "";
                String reviewUrl = "";


                ExpandableListView elv = (ExpandableListView)findViewById(R.id.reviewHistory_elvList);
                ArrayList<ReviewHistoryItemChild> join_c = new ArrayList<ReviewHistoryItemChild>();
                ArrayList<ReviewHistoryItemChild> comp_c = new ArrayList<ReviewHistoryItemChild>();
                ArrayList<ReviewHistoryItemChild> target_c = new ArrayList<ReviewHistoryItemChild>();
                ArrayList<ReviewHistoryItemChild> notTarget_c = new ArrayList<ReviewHistoryItemChild>();

                ReviewHistoryItemParent join = new ReviewHistoryItemParent();
                ReviewHistoryItemParent comp = new ReviewHistoryItemParent();
                ReviewHistoryItemParent target = new ReviewHistoryItemParent();
                ReviewHistoryItemParent notTarget = new ReviewHistoryItemParent();
                ReviewHistoryItemChild tmp_c = new ReviewHistoryItemChild();

                String strType = "";
                for(i=0;i<ary_lists.length();i++){
                    campaignCode = ((JSONObject)ary_lists.get(i)).getString("CAMPAIGN_CODE");
                    type = ((JSONObject)ary_lists.get(i)).getString("TYPE");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    joinEndDate = ((JSONObject)ary_lists.get(i)).getString("JOIN_END_DATE");
                    choiceDate = ((JSONObject)ary_lists.get(i)).getString("CHOICE_DATE");
                    reviewStartDate = ((JSONObject)ary_lists.get(i)).getString("REVIEW_START_DATE");
                    reviewEndDate = ((JSONObject)ary_lists.get(i)).getString("REVIEW_END_DATE");
                    reviewUrl = ((JSONObject)ary_lists.get(i)).getString("REVIEW_URL");
                    tmp_c = new ReviewHistoryItemChild( campaignCode,  type,  title,  joinEndDate,  choiceDate,  reviewStartDate,  reviewEndDate,  reviewUrl);

                    if(type.equals("JOIN")){
                        join_c.add(tmp_c);
                    }else if(type.equals("COMPLETE")){
                        comp_c.add(tmp_c);
                    }else if(type.equals("TARGET")){
                        target_c.add(tmp_c);
                    }else if(type.equals("NOT_TARGET")) {
                        notTarget_c.add(tmp_c);
                    }
                }

                if(join_c != null) join = new ReviewHistoryItemParent("신청 완료(선정 대기)",joinCnt,join_c);
                if(target_c != null) target = new ReviewHistoryItemParent("대상자 선정",targetCnt,target_c);
                if(notTarget_c != null) notTarget = new ReviewHistoryItemParent("대상자 미선정",notTargetCnt,notTarget_c);
                if(comp_c != null) comp = new ReviewHistoryItemParent("리뷰 종료",completeCnt,comp_c);

                ArrayList<ReviewHistoryItemParent> lists = new ArrayList<ReviewHistoryItemParent>();
                if(join != null) lists.add(join);
                if(target != null) lists.add(target);
                if(notTarget != null) lists.add(notTarget);
                if(comp != null) lists.add(comp);


                //확장 리스트 뷰 ##########################################################################################3

                //create and bind to adatper
                ReviewHistoryAdapter adapter = new ReviewHistoryAdapter(this, lists);
                elv.setAdapter(adapter);

                //set onclick listener
                Common.Logg("set Event");

               /* elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Common.Logg("!!");
                        Log.i("wtkim","groupPosition==>"+groupPosition);
                        if(groupPosition==1){
                            Log.i("wtkim","groupPosition==>"+groupPosition);
                            intent = new Intent(ReviewHistoryActivity.this,ReviewListActivity.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                });*/
                //확장 리스트 뷰 END ##########################################################################################3
            }else{
                Common.createDialog(this, getString(R.string.app_name).toString(),null, err,  getString(R.string.btn_ok),null, false, false);
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


    @Override
    public void onClick(View view) {
        ReviewHistoryAdapter.ReviewTag tag = new ReviewHistoryAdapter.ReviewTag();
        tag = (ReviewHistoryAdapter.ReviewTag)view.getTag();
        String type = tag.getType();
        campaignCode = tag.getCampaignCode();
        dialogType = 2;
        if(type.equals("신청 완료(선정 대기)")){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, "리뷰 신청을 취소하시겠습니까?",getString(R.string.btn_cancel) ,getString(R.string.btn_ok), false, false);
        }else if(type.equals("대상자 선정")){//리뷰등록
            Intent intent = new Intent(ReviewHistoryActivity.this,ReviewListActivity.class);
            startActivity(intent);
        }else if(type.equals("대상자 미선정")){

        }else if(type.equals("리뷰 종료")){

        }
    }
}
