package kr.co.mplat.www;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-20.
 */
/*
type,mission,appTitle,pointdate,point,image
* */
public class SurveyHistoryItemParent {
    public String type = "";
    public String point = "";
    public ArrayList<SurveyHistoryItemChild> childGroups = new ArrayList<SurveyHistoryItemChild>();

    public SurveyHistoryItemParent() {
    }

    public SurveyHistoryItemParent(String type, String point, ArrayList<SurveyHistoryItemChild> childGroups) {
        this.type = type;
        this.point = point;
        this.childGroups = childGroups;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public ArrayList<SurveyHistoryItemChild> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(ArrayList<SurveyHistoryItemChild> childGroups) {
        this.childGroups = childGroups;
    }
}

