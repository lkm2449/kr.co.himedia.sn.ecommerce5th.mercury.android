package kr.co.himedia.ecommerce.mainproject.Animal.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AnimalDto implements Serializable {

    private int rownum;
    private String cd_sig;
    private String sig_nm;
    private String abd_no;
    private String dt_recept;
    private String disc_info;
    private String state;
    private String pbl_no;
    private String dt_pbl_start;
    private String dt_pbl_end;
    private String spe_nm;
    private String col_nm;
    private String age;
    private String bdwgh;
    private String sex;
    private String neut_yn;
    private String sfert;
    private String shter_nm;
    private String shter_telno;
    private String postcode;
    private String addr1;
    private String addr2;
    private String jurisd;
    private String img;
    private String img_thumb;

    private int currentPage;

    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getRownum() {
        return rownum;
    }
    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
    public String getCd_sig() {
        return cd_sig;
    }
    public void setCd_sig(String cd_sig) {
        this.cd_sig = cd_sig;
    }
    public String getSig_nm() {
        return sig_nm;
    }
    public void setSig_nm(String sig_nm) {
        this.sig_nm = sig_nm;
    }
    public String getAbd_no() {
        return abd_no;
    }
    public void setAbd_no(String abd_no) {
        this.abd_no = abd_no;
    }
    public String getDt_recept() {
        return dt_recept;
    }
    public void setDt_recept(String dt_recept) {
        this.dt_recept = dt_recept;
    }
    public String getDisc_info() {
        return disc_info;
    }
    public void setDisc_info(String disc_info) {
        this.disc_info = disc_info;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getPbl_no() {
        return pbl_no;
    }
    public void setPbl_no(String pbl_no) {
        this.pbl_no = pbl_no;
    }
    public String getDt_pbl_start() {
        return dt_pbl_start;
    }
    public void setDt_pbl_start(String dt_pbl_start) {
        this.dt_pbl_start = dt_pbl_start;
    }
    public String getDt_pbl_end() {
        return dt_pbl_end;
    }
    public void setDt_pbl_end(String dt_pbl_end) {
        this.dt_pbl_end = dt_pbl_end;
    }
    public String getSpe_nm() {
        return spe_nm;
    }
    public void setSpe_nm(String spe_nm) {
        this.spe_nm = spe_nm;
    }
    public String getCol_nm() {
        return col_nm;
    }
    public void setCol_nm(String col_nm) {
        this.col_nm = col_nm;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getBdwgh() {
        return bdwgh;
    }
    public void setBdwgh(String bdwgh) {
        this.bdwgh = bdwgh;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getNeut_yn() {
        return neut_yn;
    }
    public void setNeut_yn(String neut_yn) {
        this.neut_yn = neut_yn;
    }
    public String getSfert() {
        return sfert;
    }
    public void setSfert(String sfert) {
        this.sfert = sfert;
    }
    public String getShter_nm() {
        return shter_nm;
    }
    public void setShter_nm(String shter_nm) {
        this.shter_nm = shter_nm;
    }
    public String getShter_telno() {
        return shter_telno;
    }
    public void setShter_telno(String shter_telno) {
        this.shter_telno = shter_telno;
    }
    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    public String getAddr1() {
        return addr1;
    }
    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
    public String getAddr2() {
        return addr2;
    }
    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }
    public String getJurisd() {
        return jurisd;
    }
    public void setJurisd(String jurisd) {
        this.jurisd = jurisd;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getImg_thumb() {
        return img_thumb;
    }
    public void setImg_thumb(String img_thumb) {
        this.img_thumb = img_thumb;
    }
}
