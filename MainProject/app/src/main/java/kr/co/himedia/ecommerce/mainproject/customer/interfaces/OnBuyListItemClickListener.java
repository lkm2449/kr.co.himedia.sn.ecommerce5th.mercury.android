package kr.co.himedia.ecommerce.mainproject.customer.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyListAdapter;

public interface OnBuyListItemClickListener {

    public void onItemClick(BuyListAdapter.ViewHolder holder, View view, int position);
}
