package kr.co.himedia.ecommerce.mainproject.customer.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnWishItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;


public class WishAdapter extends RecyclerView.Adapter<WishAdapter.ViewHolder> implements OnWishItemClickListener {

    ArrayList<SaleDto> items = new ArrayList<SaleDto>();
    OnWishItemClickListener listener;

    @NonNull
    @Override
    public WishAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_view_sale, viewGroup, false);

        return new WishAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnWishItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(WishAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WishAdapter.ViewHolder viewHolder, int position) {
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

        ImageView imgProduct;
        TextView txtSaleName;
        TextView txtSalePrice;
        RatingBar search_sale_ratingbar;

        public ViewHolder(View itemView, final OnWishItemClickListener listener) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtSaleName = itemView.findViewById(R.id.txtSaleName);
            txtSalePrice = itemView.findViewById(R.id.txtSalePrice);
            search_sale_ratingbar = itemView.findViewById(R.id.search_sale_ratingbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(WishAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(SaleDto saleDto) {
            txtSaleName.setText(saleDto.getSle_nm());
            txtSalePrice.setText(saleDto.getPrice_sale() + " 원");
            new WishAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + saleDto.getImg());
            search_sale_ratingbar.setRating(saleDto.getRating());
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
                if (result == null) imgProduct.setImageResource(R.drawable.noimage);
                else imgProduct.setImageBitmap(result);
            }

        }
    }
}
