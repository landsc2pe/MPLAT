package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-10.
 */

public class QnaHistoryListViewItem {
    private String seq = "";
    private String qnaType = "";
    private String title = "";
    private String question = "";
    private String question_date = "";
    private String answer = "";
    private String answer_date = "";
    private String gubun = "";

    public String getQuestion_date() {
        return question_date;
    }

    public void setQuestion_date(String question_date) {
        this.question_date = question_date;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getQnaType() {
        return qnaType;
    }

    public void setQnaType(String qnaType) {
        this.qnaType = qnaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_date() {
        return answer_date;
    }

    public void setAnswer_date(String answer_date) {
        this.answer_date = answer_date;
    }
}
