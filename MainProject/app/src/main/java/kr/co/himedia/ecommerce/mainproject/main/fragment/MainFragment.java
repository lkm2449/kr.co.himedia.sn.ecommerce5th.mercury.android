package kr.co.himedia.ecommerce.mainproject.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleToyAdapter;
import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleWalkAdapter;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainSaleToyItemClickListener;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainSaleWalkItemClickListener;
import kr.co.himedia.ecommerce.mainproject.sale.activity.SearchSaleActivity;
import kr.co.himedia.ecommerce.mainproject.Animal.activity.ViewAnimalActivity;
import kr.co.himedia.ecommerce.mainproject.sale.activity.ViewSaleActivity;
import kr.co.himedia.ecommerce.mainproject.main.adapter.MainAnimalAdapter;
import kr.co.himedia.ecommerce.mainproject.main.adapter.MainSaleFeedAdapter;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.Animal.dto.AnimalDto;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainAnimalItemClickListener;
import kr.co.himedia.ecommerce.mainproject.main.interfaces.OnMainSaleFeedItemClickListener;

public class MainFragment extends Fragment {

    RecyclerView recyclerMainAnimalView;
    RecyclerView recyclerMainSaleFeedView;
    RecyclerView recyclerMainSaleWalkView;
    RecyclerView recyclerMainSaleToyView;
    MainAnimalAdapter mainAnimalAdapter;
    MainSaleFeedAdapter mainSaleFeedAdapter;
    MainSaleWalkAdapter mainSaleWalkAdapter;
    MainSaleToyAdapter mainSaleToyAdapter;
    static RequestQueue requestQueue;
    String requestJSON = "";
    EditText editSearchWord;
    Button btnSearch;
    TextView txt_goFeed, txt_goWalk, txt_goToy;

    ArrayList<SaleDto> listSaleDto = new ArrayList<>();
    int totalCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        recyclerMainAnimalView = view.findViewById(R.id.recyclerMainAnimalView);
        recyclerMainSaleFeedView = view.findViewById(R.id.recyclerMainSaleFeedView);
        recyclerMainSaleWalkView = view.findViewById(R.id.recyclerMainSaleWalkView);
        recyclerMainSaleToyView = view.findViewById(R.id.recyclerMainSaleToyView);

        animalList();
        saleFeedList();
        saleWalkList();
        saleToyList();

        editSearchWord = view.findViewById(R.id.editSearchWord);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editSearchWord.getText().toString().length() >= 2){
                    searchCountRequest();
                    searchRequest();
                } else{
                    Toast.makeText(view.getContext(), "2글자 이상 입력해주세요!", Toast.LENGTH_LONG).show();
                }
            }
        });

        txt_goFeed = view.findViewById(R.id.txt_goFeed);
        txt_goFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countRequestJSON("0101");
                makeRequest("0101");
            }
        });

        txt_goWalk = view.findViewById(R.id.txt_goWalk);
        txt_goWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countRequestJSON("0102");
                makeRequest("0102");
            }
        });

        txt_goToy = view.findViewById(R.id.txt_goToy);
        txt_goToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countRequestJSON("0103");
                makeRequest("0103");
            }
        });

        return view;
    }

    public void searchCountRequest() {
        try {
            String url = Config.serverUrl + "/front/sale/searchCounting.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("searchWord", editSearchWord.getText().toString());
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

    public void countRequestJSON(String cd_ctg) {
        try {
            String url = Config.serverUrl + "/front/sale/counting.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cd_ctg", cd_ctg);
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
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listSaleDto = makeRequestJSON(response);
                        Intent intent = new Intent(getActivity().getApplicationContext(), SearchSaleActivity.class);
                        intent.putExtra("listSaleDto", listSaleDto);
                        intent.putExtra("cd_ctg", cd_ctg);
                        intent.putExtra("totalCount", totalCount);
                        startActivity(intent);
                        editSearchWord.setText("");
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
            jsonObject.addProperty("searchWord", editSearchWord.getText().toString());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listSaleDto = makeRequestJSON(response);
                        Intent intent = new Intent(getActivity().getApplicationContext(), SearchSaleActivity.class);
                        intent.putExtra("searchWord", editSearchWord.getText().toString());
                        intent.putExtra("listSaleDto", listSaleDto);
                        intent.putExtra("totalCount", totalCount);
                        startActivity(intent);
                        editSearchWord.setText("");
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

    public void saleFeedList(){
        recyclerMainSaleFeedView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMainSaleFeedView.setLayoutManager(layoutManager);

        mainSaleFeedAdapter = new MainSaleFeedAdapter();
        recyclerMainSaleFeedView.setAdapter(mainSaleFeedAdapter);

        saleFeedRequest();

        mainSaleFeedAdapter.setOnClickListener(new OnMainSaleFeedItemClickListener() {
            @Override
            public void onItemClick(MainSaleFeedAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = mainSaleFeedAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });
    }

    public void saleWalkList(){
        recyclerMainSaleWalkView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMainSaleWalkView.setLayoutManager(layoutManager);

        mainSaleWalkAdapter = new MainSaleWalkAdapter();
        recyclerMainSaleWalkView.setAdapter(mainSaleWalkAdapter);

        saleWalkRequest();

        mainSaleWalkAdapter.setOnClickListener(new OnMainSaleWalkItemClickListener() {
            @Override
            public void onItemClick(MainSaleWalkAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = mainSaleWalkAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });
    }

    public void saleToyList(){
        recyclerMainSaleToyView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMainSaleToyView.setLayoutManager(layoutManager);

        mainSaleToyAdapter = new MainSaleToyAdapter();
        recyclerMainSaleToyView.setAdapter(mainSaleToyAdapter);

        saleToyRequest();

        mainSaleToyAdapter.setOnClickListener(new OnMainSaleToyItemClickListener() {
            @Override
            public void onItemClick(MainSaleToyAdapter.ViewHolder holder, View view, int position) {
                SaleDto saleDto = mainSaleToyAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewSaleActivity.class);
                intent.putExtra("saleDto", saleDto);
                startActivity(intent);
            }
        });
    }

    public void animalList(){

        recyclerMainAnimalView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMainAnimalView.setLayoutManager(layoutManager);

        mainAnimalAdapter = new MainAnimalAdapter();
        recyclerMainAnimalView.setAdapter(mainAnimalAdapter);

        animalRequest();

        mainAnimalAdapter.setOnClickListener(new OnMainAnimalItemClickListener() {
            @Override
            public void onItemClick(MainAnimalAdapter.ViewHolder holder, View view, int position) {
                AnimalDto animalDto = mainAnimalAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewAnimalActivity.class);
                intent.putExtra("animalDto", animalDto);
                startActivity(intent);
            }
        });
    }

    public void saleFeedRequest(){
        try {

            String url = Config.serverUrl + "/front/sale/mainFeedListing.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeFeedJSON(response);

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

    public void saleWalkRequest(){
        try {

            String url = Config.serverUrl + "/front/sale/mainWalkListing.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeWalkJSON(response);

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

    public void saleToyRequest(){
        try {

            String url = Config.serverUrl + "/front/sale/mainToyListing.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeToyJSON(response);

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

    public void animalRequest() {

        try {

            String url = Config.serverUrl + "/front/animal/mainListing.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeAnimalJSON(response);
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

    public ArrayList<SaleDto> makeRequestJSON(String response){
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        return arrSaleDto;
    }

    public void makeFeedJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        mainSaleFeedAdapter.setItems(arrSaleDto);

        mainSaleFeedAdapter.notifyDataSetChanged();
    }

    public void makeWalkJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        mainSaleWalkAdapter.setItems(arrSaleDto);

        mainSaleWalkAdapter.notifyDataSetChanged();
    }

    public void makeToyJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        mainSaleToyAdapter.setItems(arrSaleDto);

        mainSaleToyAdapter.notifyDataSetChanged();
    }

    public void makeAnimalJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<AnimalDto>>(){}.getType();
        ArrayList<AnimalDto> arrAnimalDto = gson.fromJson(response, listType);

        mainAnimalAdapter.setItems(arrAnimalDto);

        mainAnimalAdapter.notifyDataSetChanged();
    }

    public void println(String data) {
        Log.d("MainFragment", data);
    }
}
