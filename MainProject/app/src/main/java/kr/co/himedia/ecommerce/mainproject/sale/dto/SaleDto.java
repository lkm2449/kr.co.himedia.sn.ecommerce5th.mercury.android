package kr.co.himedia.ecommerce.mainproject.sale.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SaleDto implements Serializable {

    private int rnum				= 0;
    private int seq_sle			= 0;
    private int seq_prd			= 0;
    private String sle_nm			= "";
    private int cd_where			= 0;
    private String cd_ctg		= "";
    private String img				= "";
    private String desces			= "";
    private int price_sale			= 0;
    private int cd_state_sale		= 0;
    private String dt_sale_start	= "";
    private String dt_sale_end		= "";
    private String dt_reg			= "";
    private int register			= 0;
    private String dt_upt			= "";
    private int updater			= 0;

    private String corp_nm			= "";
    private int seq_sll			= 0;
    private String ctg_nm;

    private float rating        = 0;
    private int count;

    private String searchKey		= "";
    private String searchWord		= "";
    private String contents         = "";
    private int currentPage;

    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCtg_nm() {
        return ctg_nm;
    }
    public void setCtg_nm(String ctg_nm) {
        this.ctg_nm = ctg_nm;
    }
    public String getCd_ctg() {
        return cd_ctg;
    }
    public void setCd_ctg(String cd_ctg) {
        this.cd_ctg = cd_ctg;
    }
    public int getSeq_sll() {
        return seq_sll;
    }
    public void setSeq_sll(int seq_sll) {
        this.seq_sll = seq_sll;
    }
    public int getRnum() {
        return rnum;
    }
    public void setRnum(int rnum) {
        this.rnum = rnum;
    }
    public int getSeq_sle() {
        return seq_sle;
    }
    public void setSeq_sle(int seq_sle) {
        this.seq_sle = seq_sle;
    }
    public int getSeq_prd() {
        return seq_prd;
    }
    public void setSeq_prd(int seq_prd) {
        this.seq_prd = seq_prd;
    }
    public String getSle_nm() {
        return sle_nm;
    }
    public void setSle_nm(String sle_nm) {
        this.sle_nm = sle_nm;
    }
    public int getCd_where() {
        return cd_where;
    }
    public void setCd_where(int cd_where) {
        this.cd_where = cd_where;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getDesces() {
        return desces;
    }
    public void setDesces(String desces) {
        this.desces = desces;
    }
    public int getPrice_sale() {
        return price_sale;
    }
    public void setPrice_sale(int price_sale) {
        this.price_sale = price_sale;
    }
    public int getCd_state_sale() {
        return cd_state_sale;
    }
    public void setCd_state_sale(int cd_state_sale) {
        this.cd_state_sale = cd_state_sale;
    }
    public String getDt_sale_start() {
        return dt_sale_start;
    }
    public void setDt_sale_start(String dt_sale_start) {
        this.dt_sale_start = dt_sale_start;
    }
    public String getDt_sale_end() {
        return dt_sale_end;
    }
    public void setDt_sale_end(String dt_sale_end) {
        this.dt_sale_end = dt_sale_end;
    }
    public String getDt_reg() {
        return dt_reg;
    }
    public void setDt_reg(String dt_reg) {
        this.dt_reg = dt_reg;
    }
    public int getRegister() {
        return register;
    }
    public void setRegister(int register) {
        this.register = register;
    }
    public String getDt_upt() {
        return dt_upt;
    }
    public void setDt_upt(String dt_upt) {
        this.dt_upt = dt_upt;
    }
    public int getUpdater() {
        return updater;
    }
    public void setUpdater(int updater) {
        this.updater = updater;
    }
    public String getCorp_nm() {
        return corp_nm;
    }
    public void setCorp_nm(String corp_nm) {
        this.corp_nm = corp_nm;
    }
    public String getSearchKey() {
        return searchKey;
    }
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
    public String getSearchWord() {
        return searchWord;
    }
    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }


}
