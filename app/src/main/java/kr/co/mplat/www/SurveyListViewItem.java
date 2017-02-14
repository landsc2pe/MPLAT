package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class SurveyListViewItem {
    private String campaign_code = "";
    private String title = "";
    private String startDate = "";
    private String endDate = "";
    private String point = "";
    private String surveyTime = "";
    private String surveyUrl = "";
    private String pointRealtimeYn = "";

    public SurveyListViewItem() {
    }

    public SurveyListViewItem(String campaign_code, String title, String startDate, String endDate, String point, String surveyTime, String surveyUrl, String pointRealtimeYn) {
        this.campaign_code = campaign_code;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.point = point;
        this.surveyTime = surveyTime;
        this.surveyUrl = surveyUrl;
        this.pointRealtimeYn = pointRealtimeYn;
    }

    public String getCampaign_code() {
        return campaign_code;
    }

    public void setCampaign_code(String campaign_code) {
        this.campaign_code = campaign_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSurveyTime() {
        return surveyTime;
    }

    public void setSurveyTime(String surveyTime) {
        this.surveyTime = surveyTime;
    }

    public String getSurveyUrl() {
        return surveyUrl;
    }

    public void setSurveyUrl(String surveyUrl) {
        this.surveyUrl = surveyUrl;
    }

    public String getPointRealtimeYn() {
        return pointRealtimeYn;
    }

    public void setPointRealtimeYn(String pointRealtimeYn) {
        this.pointRealtimeYn = pointRealtimeYn;
    }
}
