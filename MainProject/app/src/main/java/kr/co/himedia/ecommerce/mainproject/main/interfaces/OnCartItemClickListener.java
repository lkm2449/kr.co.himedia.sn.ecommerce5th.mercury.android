package kr.co.himedia.ecommerce.mainproject.main.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.main.adapter.CartAdapter;

public interface OnCartItemClickListener {

    public void onMinusButtonClick(CartAdapter.ViewHolder holder, View view, int position);

    public void onPlusButtonClick(CartAdapter.ViewHolder holder, View view, int position);

    public void onCancelButtonClick(CartAdapter.ViewHolder holder, View view, int position);
}
