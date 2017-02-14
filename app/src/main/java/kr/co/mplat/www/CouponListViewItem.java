package kr.co.mplat.www;

import android.graphics.drawable.Drawable;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class CouponListViewItem {
    private String campaign_code;
    private String img_url;
    private String app_title;
    private String developer_name;
    private String reward;
    private String reserve_yn;
    private String join_yn;

    public String getCampaign_code() {
        return campaign_code;
    }

    public void setCampaign_code(String campaign_code) {
        this.campaign_code = campaign_code;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }

    public String getDeveloper_name() {
        return developer_name;
    }

    public void setDeveloper_name(String developer_name) {
        this.developer_name = developer_name;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getReserve_yn() {
        return reserve_yn;
    }

    public void setReserve_yn(String reserve_yn) {
        this.reserve_yn = reserve_yn;
    }

    public String getJoin_yn() {
        return join_yn;
    }

    public void setJoin_yn(String join_yn) {
        this.join_yn = join_yn;
    }
}
