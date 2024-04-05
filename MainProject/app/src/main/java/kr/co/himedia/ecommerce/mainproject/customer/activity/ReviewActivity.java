package kr.co.himedia.ecommerce.mainproject.customer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyMstDto;
import kr.co.himedia.ecommerce.mainproject.common.AppDatabase;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyDetailAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyListAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyDetailItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class ReviewActivity extends AppCompatActivity {

    ArrayList<BuyDtlDto> items = new ArrayList<BuyDtlDto>();

    RequestQueue requestQueue;
    String requestJSON = "";
    ImageView imageSaleView;
    TextView txt_sle_nm;
    RatingBar ratingbar;
    EditText writeReview;
    Button btnWrite;
    String seq_sle = "";
    String seq_buy_dtl = "";

    ArrayList<BuyDtlDto> listBuyDtlDto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);



        txt_sle_nm = findViewById(R.id.txt_sle_nm);
        imageSaleView = findViewById(R.id.imageSaleView);
        btnWrite = findViewById(R.id.btnWrite);
        writeReview = findViewById(R.id.writeReview);
        ratingbar = findViewById(R.id.ratingbar);

        Intent intent = getIntent();
        seq_buy_dtl = intent.getStringExtra("seq_buy_dtl");
        seq_sle = intent.getStringExtra("seq_sle");
        String sle_nm = intent.getStringExtra("sle_nm");
        String img = intent.getStringExtra("img");

        ArrayList<BuyMstDto> listBuyMstDto = (ArrayList<BuyMstDto>) intent.getSerializableExtra("listBuyMstDto");


        txt_sle_nm.setText(sle_nm);
        //Toast.makeText(getApplicationContext(), "img=" + Config.serverUrl + img, Toast.LENGTH_LONG).show();
        new DownloadFilesTask().execute(Config.serverUrl + img);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        listBuyDtlDto = new ArrayList<>();


        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                writeReview();
            }
        });

    }
    public ArrayList<BuyDtlDto> makeSearchJSON(String response){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<BuyDtlDto>>(){}.getType();
        ArrayList<BuyDtlDto> arrBuyDtlDto = gson.fromJson(response, listType);

        return arrBuyDtlDto;
    }


    public void println(String data) {
        Log.d("BuyListActivity", data);
    }

    private class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
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
            // doInBackground 에서 받아온 total 값 사용 장소
            imageSaleView.setImageBitmap(result);
        }
    }


    private String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());

    }

    public void writeReview() {

        try {
            String url = Config.serverUrl+ "/front/customer/writeReview.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sle_nm", txt_sle_nm.getText().toString());
            jsonObject.addProperty("contents", writeReview.getText().toString());
            jsonObject.addProperty("rating", ratingbar.getRating());
            //jsonObject.addProperty("img", Config.serverUrl+imageSaleView);
            jsonObject.addProperty("seq_cst", SharedPreferencesManager.getSeq_cst(getApplicationContext()));
            jsonObject.addProperty("seq_sle", seq_sle);
            jsonObject.addProperty("seq_buy_dtl", seq_buy_dtl);
            //jsonObject.addProperty("seq_sle", 375);
            //jsonObject.addProperty("seq_buy_dtl", 102);

            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("[LOGGING][RESPONSE]", response);

                    Intent intent = new Intent(ReviewActivity.this, ReviewListActivity.class);
                    intent.putExtra("listBuyDtlDto", listBuyDtlDto);

                   /* intent.putExtra("review", writeReview.getText().toString());
                    intent.putExtra("rating", ratingbar.getRating());
                    intent.putExtra("sle_nm", txt_sle_nm.getText().toString());
                    //intent.putExtra("img", Config.serverUrl+imageSaleView);
                    intent.putExtra("dt_reg", getCurrentDateTime());*/

                    startActivity(intent);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("[LOGGING][ERROR]", error.getMessage());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accept", "application/json");
                    params.put("accept-charset", "utf-8");
                    params.put("content-type", "application/json; charset=UTF-8");
                    return params;
                }
                @Override
                public byte[] getBody() {
                    try {
                        if (requestJSON != null && requestJSON.length() > 0 && !requestJSON.equals("")) {
                            return requestJSON.getBytes("utf-8");
                        } else {
                            return null;
                        }
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }

            };
            request.setShouldCache(false);
            requestQueue.add(request);
            Log.d("[LOGGING][REQUEST]", requestJSON);
        } catch (Exception e) {
            Log.d("[LOGGING][ERROR]", e.getMessage());
        }
    }
}
