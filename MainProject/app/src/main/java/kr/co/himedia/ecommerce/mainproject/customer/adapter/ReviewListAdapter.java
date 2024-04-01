package kr.co.himedia.ecommerce.mainproject.customer.adapter;

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
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnReviewItemClickListener;
import kr.co.himedia.ecommerce.mainproject.main.adapter.CartAdapter;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> implements OnReviewItemClickListener {

    ArrayList<SaleDto> items = new ArrayList<SaleDto>();
    OnReviewItemClickListener listener;

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_view_review, viewGroup, false);

        return new ReviewListAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnReviewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder viewHolder, int position) {
        SaleDto item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SaleDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<SaleDto> items) {
        this.items = items;
    }

    public SaleDto getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSaleName;
        TextView txt_review_dt_reg;
        TextView txt_review_contents;
        ImageView img_review;
        RatingBar review_ratingbar;

        public ViewHolder(View itemView, final OnReviewItemClickListener listener) {
            super(itemView);

            txtSaleName = itemView.findViewById(R.id.txtSaleName);
            txt_review_dt_reg = itemView.findViewById(R.id.txt_review_dt_reg);
            txt_review_contents = itemView.findViewById(R.id.txt_review_contents);
            img_review = itemView.findViewById(R.id.img_review);
            review_ratingbar = itemView.findViewById(R.id.review_ratingbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ReviewListAdapter.ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(SaleDto saleDto) {
            txtSaleName.setText(saleDto.getSle_nm());
            txt_review_dt_reg.setText(saleDto.getDt_reg());
            txt_review_contents.setText(saleDto.getContents());
            new ReviewListAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + saleDto.getImg());
            review_ratingbar.setRating(saleDto.getRating());
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
                if (result == null) img_review.setImageResource(R.drawable.noimage);
                else img_review.setImageBitmap(result);
            }

        }
    }
}