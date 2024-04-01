package kr.co.himedia.ecommerce.mainproject.login.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.customer.dto.CustomerDto;

public class SearchIdActivity extends AppCompatActivity {

    EditText editName_id;
    EditText editEmail_id;
    Button btnSearchIdProc;
    static RequestQueue requestQueue;
    String requestJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        editName_id = findViewById(R.id.editName_id);
        editEmail_id = findViewById(R.id.editEmail_id);

        btnSearchIdProc = findViewById(R.id.btnSearchIdProc);
        btnSearchIdProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchIdProc();
            }
        });
    }

    public void SearchIdProc() {

        try {

            String url = Config.serverUrl + "/front/login/searchId.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cst_nm", editName_id.getText().toString());
            jsonObject.addProperty("cst_email", editEmail_id.getText().toString());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);
                    if(response.equals(null) || response.equals("")){
                        Toast.makeText(getApplicationContext(), "등록된 회원이 없습니다!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        CustomerDto customerDto = makeJSON(response);
                        String id = customerDto.getId();
                        Intent intent = new Intent(getApplicationContext(), ResultIdActivity.class);
                        intent.putExtra("id", id);

                        startActivity(intent);
                        finish();
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

    public CustomerDto makeJSON(String response) {
        Gson gson = new Gson();
        CustomerDto customerDto = gson.fromJson(response, CustomerDto.class);

        return customerDto;
    }

    public void println(String data) {
        Log.d("SearchIdActivity", data);
    }

}