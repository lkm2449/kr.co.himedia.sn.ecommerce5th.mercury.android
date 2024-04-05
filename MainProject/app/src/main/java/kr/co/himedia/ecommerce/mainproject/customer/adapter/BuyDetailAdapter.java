package kr.co.himedia.ecommerce.mainproject.customer.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.customer.activity.ReviewActivity;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyDetailItemClickListener;
import kr.co.himedia.ecommerce.mainproject.main.adapter.CartAdapter;

public class BuyDetailAdapter extends RecyclerView.Adapter<BuyDetailAdapter.ViewHolder> implements OnBuyDetailItemClickListener {

    ArrayList<BuyDtlDto> items = new ArrayList<BuyDtlDto>();
    OnBuyDetailItemClickListener listener;

    @NonNull
    @Override
    public BuyDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_view_buy_detail_list, viewGroup, false);

        return new BuyDetailAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnBuyDetailItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onImageClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onImageClick(holder, view, position);
        }
    }

    @Override
    public void onBtnReviewlick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onBtnReviewlick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BuyDetailAdapter.ViewHolder viewHolder, int position) {
        BuyDtlDto item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BuyDtlDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<BuyDtlDto> items) {
        this.items = items;
    }

    public BuyDtlDto getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_bdt_sle_nm;
        TextView txt_bdt_count;
        TextView txt_bdt_price;
        ImageView imgBuyDtl;
        Button btnReview;
        TextView txt_cd_state_rev;

        public ViewHolder(View itemView, final OnBuyDetailItemClickListener listener) {
            super(itemView);

            txt_bdt_sle_nm = itemView.findViewById(R.id.txt_bdt_sle_nm);
            txt_bdt_count = itemView.findViewById(R.id.txt_bdt_count);
            txt_bdt_price = itemView.findViewById(R.id.txt_bdt_price);
            imgBuyDtl = itemView.findViewById(R.id.imgBuyDtl);
            btnReview = itemView.findViewById(R.id.btnReview);
            txt_cd_state_rev = itemView.findViewById(R.id.txt_cd_state_rev);

            imgBuyDtl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onImageClick(BuyDetailAdapter.ViewHolder.this, v, position);
                    }
                }
            });

            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onBtnReviewlick(BuyDetailAdapter.ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(BuyDtlDto buyDtlDto) {
            txt_bdt_sle_nm.setText(buyDtlDto.getSle_nm());
            txt_bdt_count.setText(buyDtlDto.getCount() + "개");
            txt_bdt_price.setText(buyDtlDto.getPrice() + "원");
            new BuyDetailAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + buyDtlDto.getImg());

            if(buyDtlDto.getCd_state_pay().equals("결제취소")){
                txt_cd_state_rev.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.GONE);
                txt_cd_state_rev.setText("결제 취소를 한 상품입니다");
            } else if(buyDtlDto.getCd_state_rev() == null){
                txt_cd_state_rev.setVisibility(View.GONE);
                btnReview.setVisibility(View.VISIBLE);
            } else if(buyDtlDto.getCd_state_rev().equals("Y")){
                txt_cd_state_rev.setVisibility(View.VISIBLE);
                btnReview.setVisibility(View.GONE);
            } else {
                txt_cd_state_rev.setVisibility(View.GONE);
                btnReview.setVisibility(View.VISIBLE);
            }
        }

        private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bmp = null;
                try {
                    String img_url = strings[0];
                    URL url = new URL(img_url);
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                if (result == null) imgBuyDtl.setImageResource(R.drawable.noimage);
                else imgBuyDtl.setImageBitmap(result);
            }

        }
    }
}