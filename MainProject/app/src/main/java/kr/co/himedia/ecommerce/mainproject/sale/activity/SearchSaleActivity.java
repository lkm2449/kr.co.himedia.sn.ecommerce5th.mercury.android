package kr.co.himedia.ecommerce.mainproject.sale.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import kr.co.himedia.ecommerce.mainproject.sale.adapter.SearchSaleAdapter;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;
import kr.co.himedia.ecommerce.mainproject.sale.interfaces.OnSearchSaleItemClickListener;

public class SearchSaleActivity extends AppCompatActivity {

    EditText editReSearchWord;
    Button btnReSearch;
    RecyclerView recyclerSearchSaleView;
    static RequestQueue requestQueue;
    String requestJSON = "";
    SearchSaleAdapter searchSaleAdapter;
    int currentPage = 1;
    int totalCount;
    SaleDto saleDto = new SaleDto();
    String cd_ctg = "";
    String search_original_word = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sale);

        editReSearchWord = findViewById(R.id.editReSearchWord);
        btnReSearch = findViewById(R.id.btnReSearch);


        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        recyclerSearchSaleView = findViewById(R.id.recyclerSearchSaleView);
        recyclerSearchSaleView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerSearchSaleView.setLayoutManager(layoutManager);

        searchSaleAdapter = new SearchSaleAdapter();
        recyclerSearchSaleView.setAdapter(searchSaleAdapter);

        Intent intent = getIntent();
        editReSearchWord.setText(intent.getStringExtra("searchWord"));
        cd_ctg = intent.getStringExtra("cd_ctg");
        totalCount = intent.getIntExtra("totalCount", 0);
        search_original_word = intent.getStringExtra("searchWord");
        ArrayList<SaleDto> listSaleDto = (ArrayList<SaleDto>) intent.getSerializableExtra("listSaleDto");

        searchSaleAdapter.setItems(listSaleDto);

        searchSaleAdapter.notifyDataSetChanged();

        btnReSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editReSearchWord.getText().toString().length() >= 2){
                    currentPage = 1;
                    cd_ctg = "";
                    search_original_word = editReSearchWord.getText().toString();
                    searchCountRequest();
                    searchRequest();
                } else{
                    Toast.makeText(getApplicationContext(), "2글자 이상 입력해주세요!", Toast.LENGTH_LONG).show();
                }

            }
        });

        searchSaleAdapter.setOnClickListener(new OnSearchSaleItemClickListener() {
            @Override
            public void onItemClick(SearchSaleAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = searchSaleAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });

        recyclerSearchSaleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 리사이클러뷰 가장 마지막 index
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();

                int itemCount = recyclerView.getAdapter().getItemCount();

                // 스크롤을 맨 끝까지 한 것!
                if(lastPosition == itemCount -1){
                    if(currentPage * 10 < totalCount) {
                        currentPage++;
                        saleDto.setCurrentPage(currentPage);
                        if(cd_ctg != ""){
                            makeRequest(cd_ctg);
                        } else{
                            searchRequest();
                        }
                    } else{
                        Toast.makeText(getApplicationContext(), "마지막 스크롤입니다", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void searchCountRequest() {
        try {
            String url = Config.serverUrl + "/front/sale/searchCounting.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("searchWord", search_original_word);
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        totalCount = Integer.parseInt(response);
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

    public void makeRequest(String cd_ctg){

        try {

            String url = Config.serverUrl + "/front/sale/salesList.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cd_ctg", cd_ctg);
            jsonObject.addProperty("currentPage", currentPage);
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeSearchJSON(response);
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

    public void searchRequest(){

        try {

            String url = Config.serverUrl + "/front/sale/search.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("searchWord", search_original_word);
            jsonObject.addProperty("currentPage", currentPage);
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeSearchJSON(response);
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
    public void makeSearchJSON(String response){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        searchSaleAdapter.setItems(arrSaleDto);

        searchSaleAdapter.notifyDataSetChanged();
    }
    public void println(String data) {
        Log.d("SearchSaleActivity", data);
    }
}