package kr.co.himedia.ecommerce.mainproject.main.adapter;

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
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainSaleWalkItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class MainSaleWalkAdapter extends RecyclerView.Adapter<MainSaleWalkAdapter.ViewHolder> implements OnMainSaleWalkItemClickListener {

    ArrayList<SaleDto> items = new ArrayList<SaleDto>();
    OnMainSaleWalkItemClickListener listener;

    @NonNull
    @Override
    public MainSaleWalkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_main_sale_walk, viewGroup, false);

        return new MainSaleWalkAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnMainSaleWalkItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(MainSaleWalkAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MainSaleWalkAdapter.ViewHolder viewHolder, int position) {
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

        ImageView imgWalk;
        TextView txtWalkName;
        TextView txtWalkPrice;
        RatingBar main_walk_ratingbar;

        public ViewHolder(View itemView, final OnMainSaleWalkItemClickListener listener) {
            super(itemView);

            imgWalk = itemView.findViewById(R.id.imgWalk);
            txtWalkName = itemView.findViewById(R.id.txtWalkName);
            txtWalkPrice = itemView.findViewById(R.id.txtWalkPrice);
            main_walk_ratingbar = itemView.findViewById(R.id.main_walk_ratingbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(MainSaleWalkAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(SaleDto saleDto) {
            txtWalkName.setText(saleDto.getSle_nm());
            txtWalkPrice.setText(saleDto.getPrice_sale() + " 원");
            new MainSaleWalkAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + saleDto.getImg());
            main_walk_ratingbar.setRating(saleDto.getRating());
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
                if (result == null) imgWalk.setImageResource(R.drawable.noimage);
                else imgWalk.setImageBitmap(result);
            }

        }
    }
}
