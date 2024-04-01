package kr.co.himedia.ecommerce.mainproject.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import java.util.List;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.Animal.dto.AnimalDto;
import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyMstDto;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.customer.activity.BuyListActivity;
import kr.co.himedia.ecommerce.mainproject.customer.activity.ReviewListActivity;
import kr.co.himedia.ecommerce.mainproject.customer.activity.WishListActivity;
import kr.co.himedia.ecommerce.mainproject.join.activity.TosActivity;
import kr.co.himedia.ecommerce.mainproject.login.activity.LoginActivity;
import kr.co.himedia.ecommerce.mainproject.main.activity.MainActivity;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class MyPageFragment extends Fragment {

    static RequestQueue requestQueue;
    String requestJSON = "";
    ArrayList<SaleDto> listSaleDto = new ArrayList<>();
    ArrayList<BuyMstDto> listBuyMstDto = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(SharedPreferencesManager.getSeq_cst(getActivity().getApplicationContext()) == 0) {
            View view = inflater.inflate(R.layout.fragment_mypage_non, container, false);
            
            TextView btnLogin = view.findViewById(R.id.txtCst_nm);
            btnLogin.setText("로그인");
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            Button btnBuyList = view.findViewById(R.id.btnBuyList);
            btnBuyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "로그인이 필요한 서비스입니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            Button btnLike = view.findViewById(R.id.btnLike);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "로그인이 필요한 서비스입니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            Button btnReview = view.findViewById(R.id.btnReview);
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "로그인이 필요한 서비스입니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }
        else{
            View view = inflater.inflate(R.layout.fragment_mypage, container, false);

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            }

            TextView txtCst_nm = view.findViewById(R.id.txtCst_nm);
            txtCst_nm.setText(SharedPreferencesManager.getCst_nm(getActivity().getApplicationContext()) + "님 환영합니다!");

            Button btnBuyList = view.findViewById(R.id.btnBuyList);
            btnBuyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeRequestBuyListJSON();
                }
            });

            Button btnLike = view.findViewById(R.id.btnLike);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeRequestWishJSON();
                }
            });

            Button btnReview = view.findViewById(R.id.btnReview);
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeRequestReviewJSON();
                }
            });

            Button btnLogout = view.findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    println("로그인 정보 : " + String.valueOf(SharedPreferencesManager.getSeq_cst(getActivity().getApplicationContext())));

                    SharedPreferencesManager.clearPreferences(getActivity().getApplicationContext());

                    Toast.makeText(getActivity().getApplicationContext(), "로그아웃 되었습니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            return view;
        }
    }

    public void makeRequestBuyListJSON() {

        try {

            String url = Config.serverUrl + "/front/customer/buyListM.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("seq_cst", SharedPreferencesManager.getSeq_cst(getActivity().getApplicationContext()));
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listBuyMstDto = makeBuyMstJSON(response);
                        Intent intent = new Intent(getActivity().getApplicationContext(), BuyListActivity.class);
                        intent.putExtra("listBuyMstDto", listBuyMstDto);
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

    public ArrayList<BuyMstDto> makeBuyMstJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<BuyMstDto>>(){}.getType();
        ArrayList<BuyMstDto> arrBuyMstDto = gson.fromJson(response, listType);

        return arrBuyMstDto;
    }

    public void makeRequestReviewJSON() {

        try {

            String url = Config.serverUrl + "/front/customer/reviewList.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("seq_cst", SharedPreferencesManager.getSeq_cst(getActivity().getApplicationContext()));
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listSaleDto = makeSaleJSON(response);
                        Intent intent = new Intent(getActivity().getApplicationContext(), ReviewListActivity.class);
                        intent.putExtra("listSaleDto", listSaleDto);
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

    public void makeRequestWishJSON() {

        try {

            String url = Config.serverUrl + "/front/customer/wishList.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("seq_cst", SharedPreferencesManager.getSeq_cst(getActivity().getApplicationContext()));
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if(!response.equals(null) && !response.equals("")){
                        listSaleDto = makeSaleJSON(response);
                        Intent intent = new Intent(getActivity().getApplicationContext(), WishListActivity.class);
                        intent.putExtra("listSaleDto", listSaleDto);
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

    public ArrayList<SaleDto> makeSaleJSON(String response) {

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<SaleDto>>(){}.getType();
        ArrayList<SaleDto> arrSaleDto = gson.fromJson(response, listType);

        return arrSaleDto;
    }

    public void println(String data) {
        Log.d("MyPageFragment", data);
    }
}