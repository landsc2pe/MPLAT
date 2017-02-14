package kr.co.mplat.www;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstInstallAdPage4Activity extends Fragment {

    public static FirstInstallAdPage4Activity newInstance() {
        FirstInstallAdPage4Activity fragment = new FirstInstallAdPage4Activity();
        return fragment;
    }

    public FirstInstallAdPage4Activity() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_first_install_ad_page4, null);
        return root;
    }
}
