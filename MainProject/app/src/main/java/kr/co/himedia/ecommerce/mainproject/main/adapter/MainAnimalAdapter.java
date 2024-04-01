package kr.co.himedia.ecommerce.mainproject.main.adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.Animal.dto.AnimalDto;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainAnimalItemClickListener;

public class MainAnimalAdapter extends RecyclerView.Adapter<MainAnimalAdapter.ViewHolder> implements OnMainAnimalItemClickListener {

    ArrayList<AnimalDto> items = new ArrayList<AnimalDto>();
    OnMainAnimalItemClickListener listener;

    @NonNull
    @Override
    public MainAnimalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_main_animal, viewGroup, false);

        return new MainAnimalAdapter.ViewHolder(itemView, this);
    }

    public void setOnClickListener(OnMainAnimalItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null){
            listener.onItemClick(holder, view, position);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MainAnimalAdapter.ViewHolder viewHolder, int position) {
        AnimalDto item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(AnimalDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<AnimalDto> items) {
        this.items = items;
    }

    public AnimalDto getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAnimal;
        TextView txtSpeciesName;
        TextView txtSexAndNeu;
        TextView txtAge;
        TextView txtShterName;

        public ViewHolder(View itemView, final OnMainAnimalItemClickListener listener) {
            super(itemView);

            imgAnimal = itemView.findViewById(R.id.imgAnimal);
            txtSpeciesName = itemView.findViewById(R.id.txtSpeciesName);
            txtSexAndNeu = itemView.findViewById(R.id.txtSexAndNeu);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtShterName = itemView.findViewById(R.id.txtShterName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(MainAnimalAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(AnimalDto animalDto) {
            txtSpeciesName.setText(animalDto.getSpe_nm());
            txtSexAndNeu.setText(animalDto.getSex() + " | " + animalDto.getNeut_yn());
            txtAge.setText(animalDto.getAge());
            txtShterName.setText(animalDto.getShter_nm());

            new MainAnimalAdapter.ViewHolder.DownloadFilesTask().execute(animalDto.getImg());
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
                if (result == null) imgAnimal.setImageResource(R.drawable.noimage);
                else imgAnimal.setImageBitmap(result);
            }

        }
    }
}
