package kr.co.mplat.www;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-20.
 */
/*
type,mission,appTitle,pointdate,point,image
* */
public class MissionHistoryItemParent {
    public String type;
    public String point;
    public ArrayList<MissionHistoryItemChild> childGroups = new ArrayList<MissionHistoryItemChild>();

    public MissionHistoryItemParent() {
    }

    public MissionHistoryItemParent(String type,String point, ArrayList<MissionHistoryItemChild> childGroups) {
        this.type = type;
        this.point = point;
        this.childGroups = childGroups;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<MissionHistoryItemChild> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(ArrayList<MissionHistoryItemChild> childGroups) {
        this.childGroups = childGroups;
    }
}

