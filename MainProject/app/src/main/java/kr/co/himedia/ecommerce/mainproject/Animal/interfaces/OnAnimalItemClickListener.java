package kr.co.himedia.ecommerce.mainproject.Animal.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.Animal.adapter.ViewAnimalAdapter;

public interface OnAnimalItemClickListener {

    public void onItemClick(ViewAnimalAdapter.ViewHolder holder, View view, int position);
}
