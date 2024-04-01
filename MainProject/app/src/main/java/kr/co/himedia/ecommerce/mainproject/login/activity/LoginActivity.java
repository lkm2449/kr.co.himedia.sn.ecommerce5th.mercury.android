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
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.customer.dto.CustomerDto;
import kr.co.himedia.ecommerce.mainproject.join.activity.TosActivity;
import kr.co.himedia.ecommerce.mainproject.main.activity.MainActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editId;
    EditText editPasswd;
    Button btnLoginProc;
    Button btnSearchId;
    Button btnSearchPwd;
    Button btnTos;
    static RequestQueue requestQueue;
    String requestJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        editId = findViewById(R.id.editId);
        editPasswd = findViewById(R.id.editPasswd);

        btnLoginProc = findViewById(R.id.btnLoginProc);
        btnLoginProc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginProc();
            }
        });

        btnSearchId = findViewById(R.id.btnSearchId);
        btnSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchIdActivity.class);
                startActivity(intent);
            }
        });

        btnSearchPwd = findViewById(R.id.btnSearchPwd);
        btnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchPwdActivity.class);
                startActivity(intent);
            }
        });

        btnTos = findViewById(R.id.btnTos);
        btnTos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TosActivity.class);
                startActivity(intent);
            }
        });
    }

    public void LoginProc() {

        try {

            String url = Config.serverUrl + "/front/login/loginProc.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", editId.getText().toString());
            jsonObject.addProperty("passwd", editPasswd.getText().toString());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);
                    if(response.equals(null) || response.equals("")){
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        CustomerDto customerDto = makeJSON(response);
                        String cst_nm = customerDto.getCst_nm();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("id", editId.getText().toString());
                        intent.putExtra("cst_nm", cst_nm);

                        SharedPreferencesManager.setCustomer(getApplicationContext(), customerDto.getSeq_cst(), customerDto.getCst_nm());
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

    public CustomerDto makeJSON(String response) {
        Gson gson = new Gson();
        CustomerDto customerDto = gson.fromJson(response, CustomerDto.class);

        return customerDto;
    }

    public void println(String data) {
        Log.d("LoginFragment", data);
    }

}