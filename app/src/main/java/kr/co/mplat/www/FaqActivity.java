package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FaqActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    private final int CALLTYPE_LOAD = 1;
    private int dialogType = 0;
    Common common = null;
    Intent intent = null;
    JSONArray ary_lists = new JSONArray();
    JSONArray ary_faq_codes = new JSONArray();
    ArrayList<String> faq_codes = new ArrayList<String>();
    Spinner spin_faq_code;
    String strFaq_code = "";
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
        setContentView(R.layout.activity_faq);
        setTvTitle("자주 묻는 질문");
        common = new Common(this);

        spin_faq_code = (Spinner)findViewById(R.id.faq_spFaqCode);
        //spiner 이벤트 등록
        spin_faq_code.setOnItemSelectedListener(this);
        //리스트 뷰
        elvList = (ExpandableListView)findViewById(R.id.faq_elvList);

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
    }

    public void dataload(){
        if(busy){
            mustLoad=true;
            return;
        }
        mustLoad=false;
        busy=true;
        Object[][] params = {
                {"LAST_SEQ",lastseq}
                ,{"QNA_CATEGORY1",strFaq_code}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_faq), params);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try{
            switch (adapterView.getId()){
                case R.id.faq_spFaqCode:
                    if(!filterLoaded){
                        filterLoaded =true;
                        return;
                    }
                    if(flag_spinner){
                        DataList = new ArrayList<ConfigGroup>();
                        rnum=0;
                    }
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_faq_codes.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strFaq_code = "";}else{ strFaq_code = object.get("CODE").toString(); }
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
                if(ary_faq_codes.length()==0)ary_faq_codes = json.getJSONArray("QNA_CODES");
                lastseq = json.getString("LAST_SEQ");

                listView = (ExpandableListView)findViewById(R.id.faq_elvList);
                ConfigGroup temp;

                //spinner
                if(!flag_spinner){
                    faq_codes.add("전체");
                    for(i=0;i<ary_faq_codes.length();i++){
                        faq_codes.add(((JSONObject) ary_faq_codes.get(i)).getString("CATEGORY1_NAME"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, faq_codes);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spin_faq_code.setAdapter(aa);
                    }
                    Log.i("wtkim","-------------------------flag_spinner");
                }

                for(i=0;i<ary_lists.length();i++){
                    //String seq = ((JSONObject)ary_lists.get(i)).getString("SEQ");
                    String qna_type = ((JSONObject)ary_lists.get(i)).getString("QNA_TYPE");
                    String title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    String answer = ((JSONObject)ary_lists.get(i)).getString("ANSWER");
                    //String top_yn = ((JSONObject)ary_lists.get(i)).getString("TOP_YN");
                    temp = new ConfigGroup(qna_type,title);
                    temp.child.add(answer);
                    DataList.add(temp);
                }
                Display newDisplay = getWindowManager().getDefaultDisplay();
                int width = newDisplay.getWidth();
                ConfigAdapter adapter = new ConfigAdapter(getApplicationContext(),R.layout.listview_faq_parent_item,R.layout.listview_faq_child_item,DataList);
                listView.setIndicatorBounds(width-150, width); //이 코드를 지우면 화살표 위치가 바뀐다.
                listView.setAdapter(adapter);
                flag_spinner = true;
            }
            busy=false;
            if(mustLoad)dataload();
        }catch (Exception e){
                Log.i("wtkim","33333333");
        }
    }
}
