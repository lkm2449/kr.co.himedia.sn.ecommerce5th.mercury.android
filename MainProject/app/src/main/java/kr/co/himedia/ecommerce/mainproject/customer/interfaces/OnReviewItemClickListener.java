package kr.co.himedia.ecommerce.mainproject.customer.interfaces;

import android.view.View;

import kr.co.himedia.ecommerce.mainproject.customer.adapter.ReviewListAdapter;

public interface OnReviewItemClickListener {

    public void onItemClick(ReviewListAdapter.ViewHolder holder, View view, int position);
}
