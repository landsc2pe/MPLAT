package kr.co.mplat.www;

/**
 * Created by gdfwo on 2017-01-15.
 */

public class AddressListViewItem {
    private String detBdNmList = "";
    private String engAddr = "";
    private String zipNo = "";
    private String roadAddrPart2 = "";
    private String jibunAddr = "";
    private String roadAddrPart1 = "";
    private String rnMgtSn = "";
    private String admCd = "";
    private String bdMgtSn = "";
    private String roadAddr = "";

    public AddressListViewItem() {
    }

    public AddressListViewItem(String detBdNmList, String engAddr, String zipNo, String roadAddrPart2, String jibunAddr, String roadAddrPart1, String rnMgtSn, String admCd, String bdMgtSn, String roadAddr) {
        this.detBdNmList = detBdNmList;
        this.engAddr = engAddr;
        this.zipNo = zipNo;
        this.roadAddrPart2 = roadAddrPart2;
        this.jibunAddr = jibunAddr;
        this.roadAddrPart1 = roadAddrPart1;
        this.rnMgtSn = rnMgtSn;
        this.admCd = admCd;
        this.bdMgtSn = bdMgtSn;
        this.roadAddr = roadAddr;
    }

    public String getDetBdNmList() {
        return detBdNmList;
    }

    public void setDetBdNmList(String detBdNmList) {
        this.detBdNmList = detBdNmList;
    }

    public String getEngAddr() {
        return engAddr;
    }

    public void setEngAddr(String engAddr) {
        this.engAddr = engAddr;
    }

    public String getZipNo() {
        return zipNo;
    }

    public void setZipNo(String zipNo) {
        this.zipNo = zipNo;
    }

    public String getRoadAddrPart2() {
        return roadAddrPart2;
    }

    public void setRoadAddrPart2(String roadAddrPart2) {
        this.roadAddrPart2 = roadAddrPart2;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getRoadAddrPart1() {
        return roadAddrPart1;
    }

    public void setRoadAddrPart1(String roadAddrPart1) {
        this.roadAddrPart1 = roadAddrPart1;
    }

    public String getRnMgtSn() {
        return rnMgtSn;
    }

    public void setRnMgtSn(String rnMgtSn) {
        this.rnMgtSn = rnMgtSn;
    }

    public String getAdmCd() {
        return admCd;
    }

    public void setAdmCd(String admCd) {
        this.admCd = admCd;
    }

    public String getBdMgtSn() {
        return bdMgtSn;
    }

    public void setBdMgtSn(String bdMgtSn) {
        this.bdMgtSn = bdMgtSn;
    }

    public String getRoadAddr() {
        return roadAddr;
    }

    public void setRoadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }
}
