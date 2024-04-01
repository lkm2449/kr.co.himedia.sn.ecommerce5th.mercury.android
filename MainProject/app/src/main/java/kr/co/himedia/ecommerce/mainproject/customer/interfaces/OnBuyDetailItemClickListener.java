package kr.co.himedia.ecommerce.mainproject.customer.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyDetailAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyListAdapter;

public interface OnBuyDetailItemClickListener {

    public void onImageClick(BuyDetailAdapter.ViewHolder holder, View view, int position);

    public void onBtnReviewlick(BuyDetailAdapter.ViewHolder holder, View view, int position);
}
