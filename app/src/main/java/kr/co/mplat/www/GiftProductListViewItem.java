package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-02-01.
 */

public class GiftProductListViewItem {
    private String productCode = "";
    private String point = "";
    private String goodsImg = "";
    private String product = "";

    public GiftProductListViewItem() {
    }

    public GiftProductListViewItem(String productCode, String point, String goodsImg, String product) {
        this.productCode = productCode;
        this.point = point;
        this.goodsImg = goodsImg;
        this.product = product;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
