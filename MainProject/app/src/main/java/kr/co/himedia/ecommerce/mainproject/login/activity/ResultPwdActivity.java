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
import kr.co.himedia.ecommerce.mainproject.login.activity.LoginActivity;

public class ResultPwdActivity extends AppCompatActivity {

    EditText editPwd;
    EditText editPwd_;
    Button btnPwdLogin;

    static RequestQueue requestQueue;
    String requestJSON = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pwd);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        editPwd = findViewById(R.id.editPwd);
        editPwd_ = findViewById(R.id.editPwd_);

        btnPwdLogin = findViewById(R.id.btnPwdLogin);
        btnPwdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editPwd.getText().toString().equals(editPwd_.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요!", Toast.LENGTH_LONG).show();
                } else if(!isValidPassword(editPwd.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호는 특수문자(!, @, #, $, %)가 포함된 8자 이상 16자 미만이어야 합니다! ", Toast.LENGTH_LONG).show();
                } else {
                    changePasswd();
                }
            }
        });
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return false;
        }

        // 특수 문자 포함 여부 확인
        boolean containsSpecialCharacter = false;
        for (char c : password.toCharArray()) {
            if (c == '!' || c == '@' || c == '#' || c == '$' || c == '%') {
                containsSpecialCharacter = true;
                break;
            }
        }

        return containsSpecialCharacter;
    }

    public void changePasswd() {

        try {

            String url = Config.serverUrl + "/front/login/changePasswd.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();

            Intent intent = getIntent();
            int seq_cst = intent.getIntExtra("seq_cst", 0);

            if(seq_cst == 0){
                Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패하였습니다. 잠시 후 다시 이용해주세요!", Toast.LENGTH_LONG).show();
                return;
            }
            jsonObject.addProperty("seq_cst", seq_cst);
            jsonObject.addProperty("passwd", editPwd.getText().toString());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);
                    if(response.equals(null) || response.equals("0")){
                        Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패하였습니다. 잠시 후 다시 이용해주세요!", Toast.LENGTH_LONG).show();
                    } else if(response.equals("false")) {
                        Toast.makeText(getApplicationContext(), "비밀번호 변경에 실패하였습니다. 잠시 후 다시 이용해주세요!", Toast.LENGTH_LONG).show();
                    } else if(response.equals("true")){
                        Toast.makeText(getApplicationContext(), "비밀번호 변경에 성공하였습니다. 로그인 페이지로 이동합니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

    public void println(String data) {
        Log.d("ResultPwdActivity", data);
    }
}