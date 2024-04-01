package kr.co.himedia.ecommerce.mainproject.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import kr.co.himedia.ecommerce.mainproject.Animal.activity.ViewAnimalActivity;
import kr.co.himedia.ecommerce.mainproject.Animal.adapter.ViewAnimalAdapter;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.Animal.dto.AnimalDto;
import kr.co.himedia.ecommerce.mainproject.Animal.interfaces.OnAnimalItemClickListener;

public class AnimalFragment extends Fragment {

    RecyclerView recyclerAnimalView;
    ViewAnimalAdapter animalAdapter;
    static RequestQueue requestQueue;
    String requestJSON = "";
    AnimalDto animalDto = new AnimalDto();
    int currentPage = 1;
    int totalCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }

        recyclerAnimalView = view.findViewById(R.id.recyclerAnimalView);
        recyclerAnimalView.setHasFixedSize(true);;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerAnimalView.setLayoutManager(layoutManager);

        countRequestJSON();

        recyclerAnimalView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        animalDto.setCurrentPage(currentPage);
                        makeRequestJSON();
                    } else{
                        Toast.makeText(getActivity().getApplicationContext(), "마지막 스크롤입니다", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        animalAdapter = new ViewAnimalAdapter();
        recyclerAnimalView.setAdapter(animalAdapter);


        animalDto.setCurrentPage(currentPage);
        makeRequestJSON();

        animalAdapter.setOnClickListener(new OnAnimalItemClickListener() {
            @Override
            public void onItemClick(ViewAnimalAdapter.ViewHolder holder, View view, int position) {
                AnimalDto animalDto = animalAdapter.getItem(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), ViewAnimalActivity.class);
                intent.putExtra("animalDto", animalDto);
                startActivity(intent);
            }
        });

        return view;
    }

    public void countRequestJSON() {
        try {
            String url = Config.serverUrl + "/front/animal/counting.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
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

    public void makeRequestJSON() {

        try {

            String url = Config.serverUrl + "/front/animal/listing.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("currentPage", animalDto.getCurrentPage());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        makeJSON(response);
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

    public void makeJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<AnimalDto>>(){}.getType();
        ArrayList<AnimalDto> arrAnimalDto = gson.fromJson(response, listType);

        animalAdapter.setItems(arrAnimalDto);

        println("읽은 데이터 수=" + arrAnimalDto.size());

        animalAdapter.notifyDataSetChanged();
    }

    public void println(String data) {
        Log.d("AnimalFragment", data);
    }
}
