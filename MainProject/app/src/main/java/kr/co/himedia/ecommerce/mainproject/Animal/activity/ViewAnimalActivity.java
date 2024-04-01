package kr.co.himedia.ecommerce.mainproject.Animal.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.Animal.dto.AnimalDto;

public class ViewAnimalActivity extends AppCompatActivity {

    TextView txt_speNm_age_colNm, txt_sex_neu_bdw, txt_pbl_no, txt_dt_pbl, txt_disc_info, txt_shter_nm, txt_shter_telno, txt_addr1;
    ImageView imageAnimalView;
    AnimalDto animalDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animal);

        txt_speNm_age_colNm = findViewById(R.id.txt_speNm_age_colNm);
        txt_sex_neu_bdw = findViewById(R.id.txt_sex_neu_bdw);
        txt_pbl_no = findViewById(R.id.txt_pbl_no);
        txt_dt_pbl = findViewById(R.id.txt_dt_pbl);
        txt_disc_info = findViewById(R.id.txt_disc_info);
        txt_shter_nm = findViewById(R.id.txt_shter_nm);
        txt_shter_telno = findViewById(R.id.txt_shter_telno);
        txt_addr1 = findViewById(R.id.txt_addr1);
        imageAnimalView = findViewById(R.id.imageAnimalView);

        Intent intent = getIntent();
        animalDto = (AnimalDto) intent.getSerializableExtra("animalDto");

        txt_speNm_age_colNm.setText(animalDto.getSpe_nm() + " | " + animalDto.getAge() + " | " + animalDto.getCol_nm());
        txt_sex_neu_bdw.setText(animalDto.getSex() + " | " + animalDto.getNeut_yn() + " | " + animalDto.getBdwgh());
        txt_pbl_no.setText("공고 번호 : " + animalDto.getPbl_no());
        txt_dt_pbl.setText("공고 기간 : " + animalDto.getDt_pbl_start() + " ~ " + animalDto.getDt_pbl_end());
        txt_disc_info.setText("발견 장소 : " + animalDto.getDisc_info());
        txt_shter_nm.setText("보호소명 : " + animalDto.getShter_nm());
        txt_shter_telno.setText("보호소 번호 : " + animalDto.getShter_telno());
        txt_addr1.setText("보호소 주소 : " + animalDto.getAddr1());

        new DownloadFilesTask().execute(animalDto.getImg());
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
            if (result == null) imageAnimalView.setImageResource(R.drawable.noimage);
            else imageAnimalView.setImageBitmap(result);
        }

    }
}