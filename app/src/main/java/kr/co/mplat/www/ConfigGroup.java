package kr.co.mplat.www;

import java.util.ArrayList;

/**
 * Created by gdfwo on 2017-01-09.
 */

public class ConfigGroup {
    public ArrayList<String> child;
    public String groupCate;
    public String groupName;

    ConfigGroup(String cate,String name){
        groupCate = cate;
        groupName = name;
        child = new ArrayList<String>();
    }

}
