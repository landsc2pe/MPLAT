package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-20.
 */

public class MissionHistoryItemChild {
    public String mission;
    public String appTitle;
    public String pointDate;
    public String point;
    public String reason;

    public MissionHistoryItemChild() {
    }

    public MissionHistoryItemChild(String mission, String appTitle, String pointDate, String point, String reason) {
        this.mission = mission;
        this.appTitle = appTitle;
        this.pointDate = pointDate;
        this.point = point;
        this.reason = reason;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getPointDate() {
        return pointDate;
    }

    public void setPointDate(String pointDate) {
        this.pointDate = pointDate;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
