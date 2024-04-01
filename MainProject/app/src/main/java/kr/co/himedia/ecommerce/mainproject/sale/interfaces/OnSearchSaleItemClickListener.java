package kr.co.himedia.ecommerce.mainproject.sale.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.sale.adapter.SearchSaleAdapter;

public interface OnSearchSaleItemClickListener {

    public void onItemClick(SearchSaleAdapter.ViewHolder holder, View view, int position);
}
