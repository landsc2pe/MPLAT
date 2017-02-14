package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-20.
 */

public class SurveyHistoryItemChild {
    public String type = "";
    public String title = "";
    public String point = "";
    public String pointGiveYn = "";

    public SurveyHistoryItemChild() {
    }

    public SurveyHistoryItemChild(String type, String title, String point, String pointGiveYn) {
        this.type = type;
        this.title = title;
        this.point = point;
        this.pointGiveYn = pointGiveYn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointGiveYn() {
        return pointGiveYn;
    }

    public void setPointGiveYn(String pointGiveYn) {
        this.pointGiveYn = pointGiveYn;
    }
}
