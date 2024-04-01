package kr.co.himedia.ecommerce.mainproject.main.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.main.adapter.MainAnimalAdapter;

public interface OnMainAnimalItemClickListener {

    public void onItemClick(MainAnimalAdapter.ViewHolder holder, View view, int position);
}
