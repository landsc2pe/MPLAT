package kr.co.mplat.www;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-20.
 */
public class ReviewListItemParent {
    public String type = "";
    public String count = "0";
    public ArrayList<ReviewListItemChild> childGroups = new ArrayList<ReviewListItemChild>();

    public ReviewListItemParent() {
    }

    public ReviewListItemParent(String type, String count, ArrayList<ReviewListItemChild> childGroups) {
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

    public ArrayList<ReviewListItemChild> getChildGroups() {
        return childGroups;
    }

    public void setChildGroups(ArrayList<ReviewListItemChild> childGroups) {
        this.childGroups = childGroups;
    }
}

