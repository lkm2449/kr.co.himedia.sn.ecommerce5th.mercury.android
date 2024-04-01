package kr.co.himedia.ecommerce.mainproject.customer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyMstDto;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.BuyListAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.adapter.WishAdapter;
import kr.co.himedia.ecommerce.mainproject.customer.interfaces.OnBuyListItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.activity.SearchSaleActivity;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class BuyListActivity extends AppCompatActivity {

    RecyclerView recyclerBuyListView;
    BuyListAdapter buyListAdapter;

    static RequestQueue requestQueue;
    String requestJSON = "";
    ArrayList<BuyDtlDto> listBuyDtlDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list);

        recyclerBuyListView = findViewById(R.id.recyclerBuyListView);
        recyclerBuyListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerBuyListView.setLayoutManager(layoutManager);

        buyListAdapter = new BuyListAdapter();
        recyclerBuyListView.setAdapter(buyListAdapter);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        listBuyDtlDto = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<BuyMstDto> listBuyMstDto = (ArrayList<BuyMstDto>) intent.getSerializableExtra("listBuyMstDto");

        buyListAdapter.setItems(listBuyMstDto);

        buyListAdapter.notifyDataSetChanged();

        buyListAdapter.setOnClickListener(new OnBuyListItemClickListener() {
            @Override
            public void onItemClick(BuyListAdapter.ViewHolder holder, View view, int position) {
                BuyMstDto buyMstDto = buyListAdapter.getItem(position);

                makeRequestJSON(buyMstDto.getSeq_buy_mst());
            }
        });
    }

    public void makeRequestJSON(int seq_buy_mst){

        try {

            String url = Config.serverUrl + "/front/customer/buyListD.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("seq_buy_mst", seq_buy_mst);
            jsonObject.addProperty("register", SharedPreferencesManager.getSeq_cst(getApplicationContext()));
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listBuyDtlDto = makeSearchJSON(response);

                        Intent intent = new Intent(getApplicationContext(), BuyDetailActivity.class);
                        intent.putExtra("listBuyDtlDto", listBuyDtlDto);
                        startActivity(intent);
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
    public ArrayList<BuyDtlDto> makeSearchJSON(String response){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<BuyDtlDto>>(){}.getType();
        ArrayList<BuyDtlDto> arrBuyDtlDto = gson.fromJson(response, listType);

        return arrBuyDtlDto;
    }

    public void println(String data) {
        Log.d("BuyListActivity", data);
    }
}