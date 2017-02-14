package kr.co.mplat.www;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import android.widget.Spinner;

public class NoticeActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    JSONArray ary_lists = new JSONArray();
    JSONArray ary_notice_codes = new JSONArray();
    ArrayList<String> notice_codes = new ArrayList<String>();
    Spinner spin_notice_code;
    String strNotice_code = "";
    private String lastseq = "";
    private Boolean flag_spinner = false;
    ExpandableListView elvList = null;
    private int lastLoadedCnt = 99999;
    private int rnum=0;
    private boolean busy = false;
    private boolean mustLoad = false;
    ArrayList<ConfigGroup> DataList = new ArrayList<ConfigGroup>();
    private boolean filterLoaded =false;

    private ExpandableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setTvTitle("공지사항");
        common = new Common(this);

        spin_notice_code = (Spinner)findViewById(R.id.notice_spNoticeCode);
        //spiner 이벤트 등록
        spin_notice_code.setOnItemSelectedListener(this);
        //리스트 뷰
        elvList = (ExpandableListView)findViewById(R.id.notice_elvList);

        dataload();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //더불러오기 처리
        elvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    if (lastLoadedCnt>0 && (rnum < (firstVisibleItem + visibleItemCount + 15)))
                        dataload();
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //기본정보 호출
        //common.loadData(CALLTYPE_LOAD, getString(R.string.url_notice), null);
        Log.i("wtkim","start!!");
    }

    public void dataload(){
        if(busy){
            mustLoad=true;
            return;
        }
        Log.i("wtkim","---------------------dataload");
        mustLoad=false;
        busy=true;
        Log.i("wtkim","dataload!!");
        Object[][] params = {
                 {"LAST_SEQ",lastseq}
                ,{"NOTICE_CODE",strNotice_code}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_notice), params);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try{
            switch (adapterView.getId()){
                case R.id.notice_spNoticeCode:
                    if(!filterLoaded){
                        filterLoaded =true;
                        return;
                    }
                    Log.i("wtkim","---------------------strNotice_code==>"+strNotice_code);
                    if(flag_spinner){
                        DataList = new ArrayList<ConfigGroup>();
                        rnum=0;
                    }
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_notice_codes.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strNotice_code = "";}else{ strNotice_code = object.get("CODE").toString(); }
                    Log.i("wtkim","strNotice_code==>"+strNotice_code);
                    lastseq = "";
                    dataload();
                    break;
            }
        }catch (Exception e){
            Log.i("wtkim",e.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
            String err = json.getString("ERR");
            Log.i("wtkim",json.toString());
            int i;
            if (err.equals("")) {
                ary_lists = json.getJSONArray("LIST");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;
                Log.i("wtkim","lastLoadedCnt==>"+lastLoadedCnt);
                Log.i("wtkim","rnum==>"+rnum);
                ary_notice_codes = json.getJSONArray("NOTICE_CODES");
                lastseq = json.getString("LAST_SEQ");

                listView = (ExpandableListView)findViewById(R.id.notice_elvList);
                ConfigGroup temp;

                //spinner
                if(!flag_spinner){
                    notice_codes.add("전체");
                    for(i=0;i<ary_notice_codes.length();i++){
                        notice_codes.add(((JSONObject) ary_notice_codes.get(i)).getString("LABEL"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, notice_codes);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spin_notice_code.setAdapter(aa);
                    }
                    Log.i("wtkim","-------------------------flag_spinner");
                }

                for(i=0;i<ary_lists.length();i++){
                    //String seq = ((JSONObject)ary_lists.get(i)).getString("SEQ");
                    String notice_type = ((JSONObject)ary_lists.get(i)).getString("NOTICE_TYPE");
                    String title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    String contents = ((JSONObject)ary_lists.get(i)).getString("CONTENTS");
                    //String top_yn = ((JSONObject)ary_lists.get(i)).getString("TOP_YN");
                    temp = new ConfigGroup(notice_type,title);
                    temp.child.add(contents);
                    DataList.add(temp);
                }

                flag_spinner = true;Display newDisplay = getWindowManager().getDefaultDisplay();
                int width = newDisplay.getWidth();
                ConfigAdapter adapter = new ConfigAdapter(getApplicationContext(),R.layout.listview_notice_parent_item,R.layout.listview_notice_child_item,DataList);
                listView.setIndicatorBounds(width-150, width); //이 코드를 지우면 화살표 위치가 바뀐다.
                listView.setAdapter(adapter);
            }
            busy=false;
            if(mustLoad)dataload();
        }catch (Exception e){

        }
    }

}
