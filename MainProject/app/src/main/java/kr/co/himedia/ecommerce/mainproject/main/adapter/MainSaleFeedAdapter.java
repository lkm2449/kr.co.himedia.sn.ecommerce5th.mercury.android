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
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainSaleFeedItemClickListener;

public class MainSaleFeedAdapter extends RecyclerView.Adapter<MainSaleFeedAdapter.ViewHolder> implements OnMainSaleFeedItemClickListener {
    ArrayList<SaleDto> items = new ArrayList<SaleDto>();
    OnMainSaleFeedItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_main_sale_feed, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnMainSaleFeedItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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

        ImageView imgFeed;
        TextView txtFeedName;
        TextView txtFeedPrice;
        RatingBar main_feed_ratingbar;

        public ViewHolder(View itemView, final OnMainSaleFeedItemClickListener listener) {
            super(itemView);

            imgFeed = itemView.findViewById(R.id.imgFeed);
            txtFeedName = itemView.findViewById(R.id.txtFeedName);
            txtFeedPrice = itemView.findViewById(R.id.txtFeedPrice);
            main_feed_ratingbar = itemView.findViewById(R.id.main_feed_ratingbar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(SaleDto saleDto) {
            txtFeedName.setText(saleDto.getSle_nm());
            txtFeedPrice.setText(saleDto.getPrice_sale() + " 원");
            new DownloadFilesTask().execute(Config.serverUrl + saleDto.getImg());
            main_feed_ratingbar.setRating(saleDto.getRating());
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
                if (result == null) imgFeed.setImageResource(R.drawable.noimage);
                else imgFeed.setImageBitmap(result);
            }

        }
    }
}