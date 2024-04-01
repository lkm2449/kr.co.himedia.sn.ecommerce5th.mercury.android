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
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyMstDto;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyListItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.ViewHolder> implements OnBuyListItemClickListener {

    ArrayList<BuyMstDto> items = new ArrayList<BuyMstDto>();
    OnBuyListItemClickListener listener;

    @NonNull
    @Override
    public BuyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_view_buy_list, viewGroup, false);

        return new BuyListAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnBuyListItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(BuyListAdapter.ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BuyListAdapter.ViewHolder viewHolder, int position) {
        BuyMstDto item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(BuyMstDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<BuyMstDto> items) {
        this.items = items;
    }

    public BuyMstDto getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_bmt_dt_reg;
        TextView txt_bmt_cd_state_delivery;
        TextView txt_bmt_buy_info;
        TextView txt_bmt_total_price;
        ImageView imgBuyMst;

        public ViewHolder(View itemView, final OnBuyListItemClickListener listener) {
            super(itemView);

            txt_bmt_dt_reg = itemView.findViewById(R.id.txt_bmt_dt_reg);
            txt_bmt_cd_state_delivery = itemView.findViewById(R.id.txt_bmt_cd_state_delivery);
            txt_bmt_buy_info = itemView.findViewById(R.id.txt_bmt_buy_info);
            txt_bmt_total_price = itemView.findViewById(R.id.txt_bmt_total_price);
            imgBuyMst = itemView.findViewById(R.id.imgBuyMst);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(BuyListAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(BuyMstDto buyMstDto) {
            txt_bmt_dt_reg.setText(buyMstDto.getDt_reg());
            txt_bmt_cd_state_delivery.setText(buyMstDto.getCd_state_delivery());
            txt_bmt_buy_info.setText(buyMstDto.getBuy_info());
            txt_bmt_total_price.setText(buyMstDto.getBuy_t_price() + " 원");
            new BuyListAdapter.ViewHolder.DownloadFilesTask().execute(Config.serverUrl + buyMstDto.getImg());
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
                if (result == null) imgBuyMst.setImageResource(R.drawable.noimage);
                else imgBuyMst.setImageBitmap(result);
            }

        }
    }
}