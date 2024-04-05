package kr.co.himedia.ecommerce.mainproject.buy.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class BuyDtlDto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int seq_buy_dtl;
    private int seq_buy_mst;
    private int seq_prd;
    private int seq_sle;
    private int price;
    private Integer count;
    private String dt_reg;
    private int register;
    private String cd_state_rev;

    private String sle_nm;
    private String img;
    private String prd_nm;
    private String desces;
    private int seq_sll;
    private int seq_cst;
    private String cd_state_pay;

    public String getCd_state_pay() {
        return cd_state_pay;
    }

    public void setCd_state_pay(String cd_state_pay) {
        this.cd_state_pay = cd_state_pay;
    }

    public String getCd_state_rev() {
        return cd_state_rev;
    }
    public void setCd_state_rev(String cd_state_rev) {
        this.cd_state_rev = cd_state_rev;
    }
    public int getSeq_cst() {
        return seq_cst;
    }
    public void setSeq_cst(int seq_cst) {
        this.seq_cst = seq_cst;
    }
    public int getSeq_sll() {
        return seq_sll;
    }
    public void setSeq_sll(int seq_sll) {
        this.seq_sll = seq_sll;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getSle_nm() {
        return sle_nm;
    }
    public void setSle_nm(String sle_nm) {
        this.sle_nm = sle_nm;
    }
    public String getDesces() {
        return desces;
    }
    public void setDesces(String desces) {
        this.desces = desces;
    }
    public int getSeq_sle() {
        return seq_sle;
    }
    public void setSeq_sle(int seq_sle) {
        this.seq_sle = seq_sle;
    }
    public int getSeq_buy_dtl() {
        return seq_buy_dtl;
    }
    public void setSeq_buy_dtl(int seq_buy_dtl) {
        this.seq_buy_dtl = seq_buy_dtl;
    }
    public int getSeq_buy_mst() {
        return seq_buy_mst;
    }
    public void setSeq_buy_mst(int seq_buy_mst) {
        this.seq_buy_mst = seq_buy_mst;
    }
    public int getSeq_prd() {
        return seq_prd;
    }
    public void setSeq_prd(int seq_prd) {
        this.seq_prd = seq_prd;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
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
    public String getPrd_nm() {
        return prd_nm;
    }
    public void setPrd_nm(String prd_nm) {
        this.prd_nm = prd_nm;
    }
}
