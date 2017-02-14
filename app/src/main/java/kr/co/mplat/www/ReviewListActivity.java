package kr.co.mplat.www;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewListActivity extends MAppCompatActivity implements I_loaddata,I_startFinish,I_dialogdata{
    private AlertDialog dialog = null;
    private final int CALLTYPE_LOAD = 1;
    Common common = null;
    Intent intent = null;
    private String lastseq = "";
    JSONArray ary_lists = new JSONArray();

    private int lastLoadedCnt = 99999;
    private int rnum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        common = new Common(this);

        //help 선택시 이벤트
        ((ImageView)findViewById(R.id.reviewlist_ivHelp)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog = createDialog(R.layout.custom_dialog_review_registinfo);
                dialog.show();
                ((TextView)dialog.findViewById(R.id.dialog_review_ok)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        //문구변경
        ((TextView)findViewById(R.id.reviewlist_descinfo1)).setText(Html.fromHtml("리뷰 진행 순서는 아래와 같습니다. <font color='#7161C4'>검수완료 상태가 되면 정상입니다.</font>"));
        ((TextView)findViewById(R.id.reviewlist_descinfo2)).setText(Html.fromHtml("<font color='#D57A76'>미등록</font> - 등록완료 - <font color='#D57A76'>(수정요청)</font> - (수정완료) - 검수완료"));

        //리뷰신청 선택시
        ((LinearLayout)findViewById(R.id.review_llReview)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewListActivity.this,ReviewActivity.class);
                startActivity(intent);
            }
        });
        //리뷰신청결과
        ((LinearLayout)findViewById(R.id.review_llHistory)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                intent = new Intent(ReviewListActivity.this,ReviewHistoryActivity.class);
                startActivity(intent);
            }
        });

        dataload();
    }

    public void dataload(){
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_reviewList), null);
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
                String notUploadCnt = json.getString("NOT_UPLOAD_CNT");
                String uploadCnt = json.getString("UPLOAD_CNT");
                String needUpdateCnt = json.getString("NEED_UPDATE_CNT");
                String checkedCnt = json.getString("CHECKED_CNT");
                String rejectCnt = json.getString("REJECT_CNT");
                String stopCnt = json.getString("STOP_CNT");

                String campaignCode = "";
                String type = "";
                String title = "";
                String joinEndDate = "";
                String choiceDate = "";
                String reviewStartDate = "";
                String reviewEndDate = "";
                String reviewUrl = "";
                String reviewReason = "";

                ExpandableListView elv1 = (ExpandableListView)findViewById(R.id.reviewlist_elvList1);
                ExpandableListView elv2 = (ExpandableListView)findViewById(R.id.reviewlist_elvList2);
                //리뷰 진행
                ArrayList<ReviewListItemChild> notUpload_c = new ArrayList<ReviewListItemChild>();
                ArrayList<ReviewListItemChild> upload_c = new ArrayList<ReviewListItemChild>();
                ArrayList<ReviewListItemChild> needUpdate_c = new ArrayList<ReviewListItemChild>();
                ArrayList<ReviewListItemChild> checked_c = new ArrayList<ReviewListItemChild>();
                //진행 중단(비정상 종료)
                ArrayList<ReviewListItemChild> reject_c = new ArrayList<ReviewListItemChild>();
                ArrayList<ReviewListItemChild> stop_c = new ArrayList<ReviewListItemChild>();


                //리뷰
                ReviewListItemParent notUpload = new ReviewListItemParent();
                ReviewListItemParent upload = new ReviewListItemParent();
                ReviewListItemParent needUpdate = new ReviewListItemParent();
                ReviewListItemParent checked = new ReviewListItemParent();
                //진행 중단
                ReviewListItemParent reject = new ReviewListItemParent();
                ReviewListItemParent stop = new ReviewListItemParent();

                ReviewListItemChild tmp_c = new ReviewListItemChild();

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
                    reviewReason = ((JSONObject)ary_lists.get(i)).getString("REVIEW_REASON");
                    tmp_c = new ReviewListItemChild(campaignCode, title, joinEndDate, choiceDate, reviewStartDate, reviewEndDate, reviewUrl, reviewReason);

                    if(type.equals("NOT_UPLOAD")){
                        notUpload_c.add(tmp_c);
                    }else if(type.equals("UPLOAD")){
                        upload_c.add(tmp_c);
                    }else if(type.equals("NEED_UPDATE")){
                        needUpdate_c.add(tmp_c);
                    }else if(type.equals("CHECKED")) {
                        checked_c.add(tmp_c);
                    }else if(type.equals("REJECT")) {
                        reject_c.add(tmp_c);
                    }else if(type.equals("STOP")) {
                        stop_c.add(tmp_c);
                    }
                }

                if(notUpload_c != null) notUpload = new ReviewListItemParent("미등록",notUploadCnt,notUpload_c);
                if(upload_c != null) upload = new ReviewListItemParent("등록(수정)완료 / 검수 대기",uploadCnt,upload_c);
                if(needUpdate_c != null) needUpdate = new ReviewListItemParent("수정요청",needUpdateCnt,needUpdate_c);
                if(checked_c != null) checked = new ReviewListItemParent("검수완료",checkedCnt,checked_c);

                if(reject_c != null) reject = new ReviewListItemParent("자격박탈",rejectCnt,reject_c);
                if(stop_c != null) stop = new ReviewListItemParent("기간만료 / 중단",stopCnt,stop_c);

                ArrayList<ReviewListItemParent> lists = new ArrayList<ReviewListItemParent>();
                if(notUpload != null) lists.add(notUpload);
                if(upload != null) lists.add(upload);
                if(needUpdate != null) lists.add(needUpdate);
                if(checked != null) lists.add(checked);

                //ArrayList<ReviewListItemParent> lists2 = new ArrayList<ReviewListItemParent>();
                if(reject != null) lists.add(reject);
                if(stop != null) lists.add(stop);


                //확장 리스트 뷰 ##########################################################################################3

                //create and bind to adatper
                ReviewListAdapter adapter1 = new ReviewListAdapter(this, lists);
                elv1.setAdapter(adapter1);

                /*ReviewListAdapter adapter2 = new ReviewListAdapter(this, lists2);
                elv2.setAdapter(adapter2);*/

                //set onclick listener
                elv1.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        /*Common.Logg("!!!!");
                        Log.i("wtkim","groupPosition==>"+groupPosition);
                        if(groupPosition==1){
                            Log.i("wtkim","groupPosition==>"+groupPosition);
                            //intent = new Intent(ReviewListActivity.this,ReviewListActivity.class);
                            startActivity(intent);
                        }*/
                        return false;
                    }
                });

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
}
