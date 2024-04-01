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

public class SearchPwdActivity extends AppCompatActivity {

    EditText editId_pwd;
    EditText editName_pwd;
    EditText editEmail_pwd;

    static RequestQueue requestQueue;
    String requestJSON = "";

    Button btnSearchPwdProc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pwd);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        editId_pwd = findViewById(R.id.editId_pwd);
        editName_pwd = findViewById(R.id.editName_pwd);
        editEmail_pwd = findViewById(R.id.editEmail_pwd);

        btnSearchPwdProc = findViewById(R.id.btnSearchPwdProc);
        btnSearchPwdProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPwdProc();
            }
        });
    }

    public void searchPwdProc() {

        try {

            String url = Config.serverUrl + "/front/login/searchPwd.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", editId_pwd.getText().toString());
            jsonObject.addProperty("cst_nm", editName_pwd.getText().toString());
            jsonObject.addProperty("cst_email", editEmail_pwd.getText().toString());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);
                    if(response.equals(null) || response.equals("0")){
                        Toast.makeText(getApplicationContext(), "아이디, 이름, 이메일을 확인해주세요!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        CustomerDto customerDto = makeJSON(response);
                        int seq_cst = customerDto.getSeq_cst();
                        Intent intent = new Intent(getApplicationContext(), ResultPwdActivity.class);
                        intent.putExtra("seq_cst", seq_cst);
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
        Log.d("SearchPwdActivity", data);
    }
}