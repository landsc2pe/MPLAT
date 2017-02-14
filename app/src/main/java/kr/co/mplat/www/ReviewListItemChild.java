package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-20.
 */

public class ReviewListItemChild {
    public String campaign_code;
    public String title;
    public String joinEndDate;
    public String choiceDate;
    public String reviewStartDate;
    public String reviewEndDate;
    public String reviewUrl;
    public String reviewReason;

    public ReviewListItemChild() {
    }

    public ReviewListItemChild(String campaign_code, String title, String joinEndDate, String choiceDate, String reviewStartDate, String reviewEndDate, String reviewUrl, String reviewReason) {
        this.campaign_code = campaign_code;
        this.title = title;
        this.joinEndDate = joinEndDate;
        this.choiceDate = choiceDate;
        this.reviewStartDate = reviewStartDate;
        this.reviewEndDate = reviewEndDate;
        this.reviewUrl = reviewUrl;
        this.reviewReason = reviewReason;
    }

    public String getCampaign_code() {
        return campaign_code;
    }

    public void setCampaign_code(String campaign_code) {
        this.campaign_code = campaign_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJoinEndDate() {
        return joinEndDate;
    }

    public void setJoinEndDate(String joinEndDate) {
        this.joinEndDate = joinEndDate;
    }

    public String getChoiceDate() {
        return choiceDate;
    }

    public void setChoiceDate(String choiceDate) {
        this.choiceDate = choiceDate;
    }

    public String getReviewStartDate() {
        return reviewStartDate;
    }

    public void setReviewStartDate(String reviewStartDate) {
        this.reviewStartDate = reviewStartDate;
    }

    public String getReviewEndDate() {
        return reviewEndDate;
    }

    public void setReviewEndDate(String reviewEndDate) {
        this.reviewEndDate = reviewEndDate;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }
}
