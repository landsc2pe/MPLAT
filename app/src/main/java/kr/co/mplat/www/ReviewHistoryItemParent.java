package kr.co.mplat.www;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-20.
 */
/*
type,mission,appTitle,pointdate,point,image
* */
public class ReviewHistoryItemParent {
    public String type = "";
    public String count = "0";
    public ArrayList<ReviewHistoryItemChild> childGroups = new ArrayList<ReviewHistoryItemChild>();

    public ReviewHistoryItemParent() {
    }

    public ReviewHistoryItemParent(String type, String count, ArrayList<ReviewHistoryItemChild> childGroups) {
        this.type = type;
        this.count = count;
        this.childGroups = childGroups;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList<ReviewHistoryItemChild> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(ArrayList<ReviewHistoryItemChild> childGroups) {
        this.childGroups = childGroups;
    }
}

