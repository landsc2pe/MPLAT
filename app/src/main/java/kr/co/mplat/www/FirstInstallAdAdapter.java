package kr.co.mplat.www;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by gdfwo on 2017-02-07.
 */

public class FirstInstallAdAdapter extends FragmentPagerAdapter{
    private final int NUM_ITEMS = 4;

    public FirstInstallAdAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return NUM_ITEMS;
    }

    public Fragment getItem(int position) {
        if (position == 0)
            return FirstInstallAdPage1Activity.newInstance();
        else if (position == 1)
            return FirstInstallAdPage2Activity.newInstance();
        else if (position == 2)
            return FirstInstallAdPage3Activity.newInstance();
        else if (position == 3)
            return FirstInstallAdPage4Activity.newInstance();
        return null;
    }
}
