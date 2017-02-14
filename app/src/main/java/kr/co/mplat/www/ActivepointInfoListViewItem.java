package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-13.
 */

public class ActivepointInfoListViewItem {
    private String activePointCode = "";
    private String name = "";
    private String activePoint = "";
    private String limitDesc = "";

    public String getActivePointCode() {
        return activePointCode;
    }

    public void setActivePointCode(String activePointCode) {
        this.activePointCode = activePointCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivePoint() {
        return activePoint;
    }

    public void setActivePoint(String activePoint) {
        this.activePoint = activePoint;
    }

    public String getLimitDesc() {
        return limitDesc;
    }

    public void setLimitDesc(String limitDesc) {
        this.limitDesc = limitDesc;
    }
}
