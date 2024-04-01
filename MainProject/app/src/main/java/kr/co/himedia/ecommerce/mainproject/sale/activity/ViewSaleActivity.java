package kr.co.himedia.ecommerce.mainproject.sale.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.common.AppDatabase;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.customer.dto.CustomerDto;
import kr.co.himedia.ecommerce.mainproject.login.activity.LoginActivity;
import kr.co.himedia.ecommerce.mainproject.main.activity.MainActivity;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class ViewSaleActivity extends AppCompatActivity {

    TextView txt_sle_nm, txt_price_sale, txt_desces;
    RatingBar view_ratingbar;
    EditText editCount;
    ImageView imageSaleView;
    SaleDto saleDto;
    BuyDtlDto buyDtlDto;
    Button btnLike, btnCart;
    AppDatabase db;
    static RequestQueue requestQueue;
    String requestJSON = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sale);

        txt_sle_nm = findViewById(R.id.txt_sle_nm);
        txt_price_sale = findViewById(R.id.txt_price_sale);
        txt_desces = findViewById(R.id.txt_desces);
        imageSaleView = findViewById(R.id.imageSaleView);
        btnLike = findViewById(R.id.btnLike);
        btnCart = findViewById(R.id.btnCart);
        editCount = findViewById(R.id.editCount);
        view_ratingbar = findViewById(R.id.view_ratingbar);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        Intent intent = getIntent();
        saleDto = (SaleDto) intent.getSerializableExtra("saleDto");

        txt_sle_nm.setText(saleDto.getSle_nm());
        txt_price_sale.setText("가격 : " + saleDto.getPrice_sale() + " 원");

        String desces = saleDto.getDesces();

        desces = desces.replace("</strong>", "");
        desces = desces.replace("<strong>", "");
        desces = desces.replace("<p>", "\n");
        desces = desces.replace("</p>", "\n");
        desces = desces.replace("<br/>", "\n");

        txt_desces.setText(desces);
        new DownloadFilesTask().execute(Config.serverUrl + saleDto.getImg());
        view_ratingbar.setRating(saleDto.getRating());

        buyDtlDto = new BuyDtlDto();
        buyDtlDto.setSle_nm(saleDto.getSle_nm());
        buyDtlDto.setPrice(saleDto.getPrice_sale());
        buyDtlDto.setImg(saleDto.getImg());
        buyDtlDto.setSeq_sle(saleDto.getSeq_sle());

        db = AppDatabase.getInstance(this);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedPreferencesManager.getSeq_cst(getApplicationContext()) == 0){
                    Toast.makeText(getApplicationContext(), "로그인이 필요한 서비스입니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else{
                    wishProc();
                }

            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editCount.getText() == null){
                    Toast.makeText(getApplicationContext(), "수량을 입력해주세요!", Toast.LENGTH_LONG).show();
                } else if(editCount.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "수량을 입력해주세요!", Toast.LENGTH_LONG).show();
                } else if(Integer.parseInt(editCount.getText().toString()) == 0){
                    Toast.makeText(getApplicationContext(), "수량을 입력해주세요!", Toast.LENGTH_LONG).show();
                } else {
                    class InsertRunnable implements Runnable {
                        @Override
                        public void run() {
                            int count = db.buyDtlDao().selectCount(saleDto.getSeq_sle());

                            if(count == 0){
                                buyDtlDto.setCount(Integer.parseInt(editCount.getText().toString()));

                                // 데이터 삽입
                                db.buyDtlDao().insert(buyDtlDto);

                                // UI 스레드에서 Toast를 띄우기 위해 Handler를 사용
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // UI 작업 실행
                                        Toast.makeText(v.getContext(), "장바구니 등록됨", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                // UI 스레드에서 Toast를 띄우기 위해 Handler를 사용
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // UI 작업 실행
                                        Toast.makeText(v.getContext(), "이미 장바구니에 등록된 상품입니다!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                    InsertRunnable insertRunnable = new InsertRunnable();
                    new Thread(insertRunnable).start();
                }
            }
        });
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
            if (result == null) imageSaleView.setImageResource(R.drawable.noimage);
            else imageSaleView.setImageBitmap(result);
        }

    }

    public void wishProc() {

        try {

            String url = Config.serverUrl + "/front/customer/insertWish.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("seq_cst", SharedPreferencesManager.getSeq_cst(getApplicationContext()));
            jsonObject.addProperty("seq_sle", saleDto.getSeq_sle());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);
                    if(response.equals(null) || response.equals("")){
                        Toast.makeText(getApplicationContext(), "오류 발생! 잠시 후에 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                    } else if (response.equals("0")){
                        Toast.makeText(getApplicationContext(), "오류 발생! 잠시 후에 다시 시도해주세요!", Toast.LENGTH_LONG).show();
                    } else if (response.equals("1")){
                        Toast.makeText(getApplicationContext(), "찜 등록 성공", Toast.LENGTH_LONG).show();
                    } else if (response.equals("2")){
                        Toast.makeText(getApplicationContext(), "찜 등록 해제", Toast.LENGTH_LONG).show();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            println("[에러] " + error.getMessage());
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
                        if (requestJSON != null && requestJSON.length()>0 && !requestJSON.equals("")){
                            return requestJSON.getBytes("utf-8");
                        }
                        else {
                            return null;
                        }
                    }
                    catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);
            println("[요청] " + requestJSON);
        }
        catch(Exception e){
            println(e.getMessage());
        }
    }

    public void println(String data) {
        Log.d("ViewSaleActivity", data);
    }
}
