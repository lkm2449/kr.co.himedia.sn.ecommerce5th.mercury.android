package kr.co.himedia.ecommerce.mainproject.main.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleWalkAdapter;


public interface OnMainSaleWalkItemClickListener {

    public void onItemClick(MainSaleWalkAdapter.ViewHolder holder, View view, int position);
}
