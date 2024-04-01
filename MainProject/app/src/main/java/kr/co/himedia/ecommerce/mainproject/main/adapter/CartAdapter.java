package kr.co.himedia.ecommerce.mainproject.main.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.common.AppDatabase;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnCartItemClickListener;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements OnCartItemClickListener {

    List<BuyDtlDto> items = new ArrayList<BuyDtlDto>();
    OnCartItemClickListener listener;

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_cart, viewGroup, false);

        return new CartAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnCartItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onCancelButtonClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onCancelButtonClick(holder, view, position);
        }
    }

    @Override
    public void onMinusButtonClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onMinusButtonClick(holder, view, position);
        }
    }

    @Override
    public void onPlusButtonClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onPlusButtonClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder viewHolder, int position) {
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

    public void setItems(List<BuyDtlDto> items) {
        this.items = items;
    }

    public BuyDtlDto getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCartProduct;
        TextView txtCartSaleName;
        TextView txtCartSalePrice;
        TextView txtCartCount;
        ImageView btnCartCancel;
        ImageView btnMinus;
        ImageView btnPlus;
        AppDatabase db;

        public ViewHolder(View itemView, final OnCartItemClickListener listener) {
            super(itemView);

            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            txtCartSaleName = itemView.findViewById(R.id.txtCartSaleName);
            txtCartSalePrice = itemView.findViewById(R.id.txtCartSalePrice);
            txtCartCount = itemView.findViewById(R.id.txtCartCount);
            btnCartCancel = itemView.findViewById(R.id.btnCartCancel);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);

            db = AppDatabase.getInstance(itemView.getContext());

            btnCartCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onCancelButtonClick(CartAdapter.ViewHolder.this, v, position);
                    }
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onMinusButtonClick(CartAdapter.ViewHolder.this, v, position);
                    }

                }
            });

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onPlusButtonClick(CartAdapter.ViewHolder.this, v, position);
                    }
                }
            });

        }

        public void setItem(BuyDtlDto buyDtlDto) {
            txtCartSaleName.setText(buyDtlDto.getSle_nm());
            txtCartSalePrice.setText("총 " + buyDtlDto.getPrice() * buyDtlDto.getCount()+ " 원");
            txtCartCount.setText(buyDtlDto.getCount() + " 개");
            new CartAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + buyDtlDto.getImg());
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
                if (result == null) imgCartProduct.setImageResource(R.drawable.noimage);
                else imgCartProduct.setImageBitmap(result);
            }

        }
    }
}
