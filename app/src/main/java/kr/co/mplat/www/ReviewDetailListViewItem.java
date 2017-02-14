package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-02-06.
 */

public class ReviewDetailListViewItem {
    private String email = "";
    private String comment = "";
    private String registDatetime = "";

    public ReviewDetailListViewItem() {
    }

    public ReviewDetailListViewItem(String email, String comment, String registDatetime) {
        this.email = email;
        this.comment = comment;
        this.registDatetime = registDatetime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRegistDatetime() {
        return registDatetime;
    }

    public void setRegistDatetime(String registDatetime) {
        this.registDatetime = registDatetime;
    }
}
