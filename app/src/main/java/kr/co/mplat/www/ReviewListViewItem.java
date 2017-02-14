package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class ReviewListViewItem {
    private String campaignCode = "";
    private String imgIcon = "";
    private String title = "";
    private String reward = "";
    private String reviewMethod = "";
    private String targetCnt = "";
    private String joinCnt = "";
    private String joinYn = "";
    private String remainDays = "";

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(String imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getReviewMethod() {
        return reviewMethod;
    }

    public void setReviewMethod(String reviewMethod) {
        this.reviewMethod = reviewMethod;
    }

    public String getTargetCnt() {
        return targetCnt;
    }

    public void setTargetCnt(String targetCnt) {
        this.targetCnt = targetCnt;
    }

    public String getJoinCnt() {
        return joinCnt;
    }

    public void setJoinCnt(String joinCnt) {
        this.joinCnt = joinCnt;
    }

    public String getJoinYn() {
        return joinYn;
    }

    public void setJoinYn(String joinYn) {
        this.joinYn = joinYn;
    }

    public String getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(String remainDays) {
        this.remainDays = remainDays;
    }
}
