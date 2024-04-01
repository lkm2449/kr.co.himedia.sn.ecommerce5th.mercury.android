package kr.co.himedia.ecommerce.mainproject.main.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleToyAdapter;

public interface OnMainSaleToyItemClickListener {

    public void onItemClick(MainSaleToyAdapter.ViewHolder holder, View view, int position);
}
