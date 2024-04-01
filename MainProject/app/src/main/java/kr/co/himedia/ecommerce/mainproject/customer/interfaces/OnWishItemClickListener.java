package kr.co.himedia.ecommerce.mainproject.customer.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.customer.adapter.WishAdapter;
import kr.co.himedia.ecommerce.mainproject.sale.adapter.SearchSaleAdapter;

public interface OnWishItemClickListener {

    public void onItemClick(WishAdapter.ViewHolder holder, View view, int position);
}
