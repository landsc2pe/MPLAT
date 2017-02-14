package kr.co.mplat.www;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstInstallAdPage2Activity extends Fragment {

    public static FirstInstallAdPage2Activity newInstance() {
        FirstInstallAdPage2Activity fragment = new FirstInstallAdPage2Activity();
        return fragment;
    }

    public FirstInstallAdPage2Activity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_first_install_ad_page2, null);
        return root;
    }
}
