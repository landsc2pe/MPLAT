package kr.co.mplat.www;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QnaHistoryActivity extends NAppCompatActivity implements AdapterView.OnItemSelectedListener,I_loaddata,I_startFinish,I_dialogdata{
    Common common = null;
    Intent intent = null;
    private final int CALLTYPE_LOAD = 1;
    private final int CALLTYPE_SAVE = 2;
    private int dialogType=0;
    private String lastseq = "";
    private Boolean flag_spinner = false;
    Spinner spin_qnacode = null;
    JSONArray ary_qnacodes = new JSONArray();
    JSONArray ary_lists = new JSONArray();
    ListView listView = null;
    private boolean filterLoaded =false;
    private int lastLoadedCnt = 99999;
    private int rnum=0;

    String strQnacode = "";
    ArrayList<String> qnacodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_history);
        setTvTitle("문의내역 조회");
        common = new Common(this);

        spin_qnacode = (Spinner)findViewById(R.id.qnaHistory_spQnaCode);
        spin_qnacode.setOnItemSelectedListener(this);
        dataload();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //start(null);
        //더불러오기 처리
       /* elvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                try {
                    Log.d("wtkim","-------scroll!");
                    Log.d("wtkim","-------rnum:"+rnum);
                    Log.d("wtkim","-------itemcnt:"+(firstVisibleItem + visibleItemCount + 10));
                    if (lastLoadedCnt>0 && (rnum < (firstVisibleItem + visibleItemCount + 15)))
                        dataload();
                } catch (Exception e) {

                }
            }
        });*/
    }

    @Override
    public void start(View view) {
        //네트워크 상태 확인
        if(!common.isConnected()) {
            common.showCheckNetworkDialog();
            return;
        }
        //common.loadData(CALLTYPE_LOAD, getString(R.string.url_qnaHistory), null);
    }

    public void dataload(){

        Object[][] params = {
                {"LAST_SEQ",lastseq}
                ,{"QNA_CATEGORY1",strQnacode}
        };
        common.loadData(CALLTYPE_LOAD, getString(R.string.url_qnaHistory), params);

    }

    @Override
    public void loaddataHandler(int calltype, String str) {
        if (calltype == CALLTYPE_LOAD) loadHandler(str);
    }

    //기본 불러오기
    public void loadHandler(String str) {
        try {
            JSONObject json = new JSONObject(str);
            String err = json.getString("ERR");
            //Log.i("wtkim",json.toString());
            if (err.equals("")) {
                if(ary_qnacodes.length()==0)ary_qnacodes = json.getJSONArray("QNA_CODES");
                lastseq = json.getString("LAST_SEQ");
                ary_lists = json.getJSONArray("LIST");
                lastLoadedCnt=ary_lists.length();
                rnum+=lastLoadedCnt;

                int i;
                qnacodes = new ArrayList<>();
                if(!flag_spinner) {
                    qnacodes.add("전체");
                    for (i = 0; i < ary_qnacodes.length(); i++) {
                        qnacodes.add(((JSONObject) ary_qnacodes.get(i)).getString("CATEGORY1_NAME"));
                        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.spinner_item, qnacodes);
                        aa.setDropDownViewResource(R.layout.spinner_item);
                        spin_qnacode.setAdapter(aa);
                    }
                }
                //리스트뷰 로드
                ListView listview ;
                QnaHistoryAdapter adapter;
                //Adapter 생성
                adapter = new QnaHistoryAdapter();
                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.qnaHistory_lvHistory);
                listview.setAdapter(adapter);
                String seq;
                String qna_type;
                String title;
                String question;
                String question_date;
                String answer;
                String answer_date;
                String gubun;

                for(i=0;i<ary_lists.length();i++){
                    gubun = "";
                    seq = ((JSONObject)ary_lists.get(i)).getString("SEQ");
                    qna_type = ((JSONObject)ary_lists.get(i)).getString("QNA_TYPE");
                    title = ((JSONObject)ary_lists.get(i)).getString("TITLE");
                    question = ((JSONObject)ary_lists.get(i)).getString("QUESTION");
                    question_date = ((JSONObject)ary_lists.get(i)).getString("QUESTION_DATE");
                    answer = ((JSONObject)ary_lists.get(i)).getString("ANSWER");
                    answer_date = ((JSONObject)ary_lists.get(i)).getString("ANSWER_DATE");
                    if(!answer.equals("")){gubun = "답변완료";}else{gubun="확인중";}

                    adapter.addItem(seq,qna_type,title,question,question_date,answer,answer_date,gubun);
                }
                flag_spinner = true;
                // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        // get item
                        QnaHistoryListViewItem item = (QnaHistoryListViewItem) parent.getItemAtPosition(position) ;

                        String seqstr = item.getSeq();
                        String qnatypestr = item.getQnaType();
                        String titlestr = item.getTitle();
                        String questionstr = item.getQuestion();
                        String questiondatestr = item.getQuestion_date();
                        String answerstr = item.getAnswer();
                        String answerdatestr = item.getAnswer_date();

                        intent = new Intent(QnaHistoryActivity.this,QnaDetailActivity.class);
                        intent.putExtra("SEQ",seqstr);
                        intent.putExtra("QNA_TYPE",qnatypestr);
                        intent.putExtra("TITLE",titlestr);
                        intent.putExtra("QUESTION",questionstr);
                        intent.putExtra("QUESTION_DATE",questiondatestr);
                        intent.putExtra("ANSWER",answerstr);
                        intent.putExtra("ANSWER_DATE",answerdatestr);
                        startActivity(intent);
                    }
                }) ;
            }

            //기존정보가 있는지 로드
            //common.loadData(CALLTYPE_INFOCHECK, getString(R.string.url_addinfo), null);
        } catch (Exception e) {
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }
    @Override
    public void dialogHandler(String result) {
        /*if(result.equals("ok") && dialogType == 2 && !pre_activity.equals("3")){
            intent = new Intent(JoinAddInfoActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(result.equals("ok") && dialogType == 2 && pre_activity.equals("3")){
            intent = new Intent(JoinAddInfoActivity.this,MypageActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        JSONObject object;
        int addNum=0;
        try{
            switch (adapterView.getId()){
                case R.id.qnaHistory_spQnaCode:
                   if(!filterLoaded){
                        filterLoaded =true;
                        return;
                    }
                    //Log.i("wtkim","---------------------strFaq_code==>"+strFaq_code);

                    if(flag_spinner){
                        //DataList = new ArrayList<ConfigGroup>();
                        rnum=0;
                    }
                    if(adapterView.getSelectedItemPosition()>0){ addNum = -1; }else{ addNum=0; }
                    object = (JSONObject)ary_qnacodes.get(adapterView.getSelectedItemPosition()+addNum);
                    if(addNum==0){ strQnacode = "";}else{ strQnacode = object.get("CODE").toString(); }
                    lastseq = "";
                    dataload();
                    break;
            }
        }catch (Exception e){
            Common.createDialog(this, getString(R.string.app_name).toString(),null, e.toString(), getString(R.string.btn_ok),null, false, false);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
