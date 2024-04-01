package kr.co.himedia.ecommerce.mainproject.main.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleFeedAdapter;

public interface OnMainSaleFeedItemClickListener {

    public void onItemClick(MainSaleFeedAdapter.ViewHolder holder, View view, int position);
}
