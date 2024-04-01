package kr.co.himedia.ecommerce.mainproject.join.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.common.MyWebChromeClient;
import kr.co.himedia.ecommerce.mainproject.login.activity.LoginActivity;


public class JoinActivity extends AppCompatActivity {

    static RequestQueue requestQueue;
    String requestJSON = "";

    EditText editId;
    EditText editPasswd;
    EditText editPasswd2;
    EditText editName;
    EditText editPhone;
    EditText editPostcode;
    EditText editAddress;
    EditText editAddress2;

    EditText editAddress3;

    EditText editEmail;

    Button btnPostCode;
    Button btnIdCheck;

    Button btnJoin;

    WebView webView;
    Handler handler;

    String flg_sms;
    String flg_email;
    Boolean exist = false;

    private static final String PASSWORD_PATTERN = "^(?=.*[!@#$%])[A-Za-z0-9!@#$%]{8,16}$";

    private boolean checkPasswd(String passwd) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(passwd);
        return matcher.matches();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btnPostCode = findViewById(R.id.btnPostCode);
        btnIdCheck = findViewById(R.id.btnIdCheck);
        btnJoin = findViewById(R.id.btnJoin);

        editId =findViewById(R.id.editId);
        editPasswd = findViewById(R.id.editPasswd);
        editPasswd2 = findViewById(R.id.editPasswd2);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editPostcode = findViewById(R.id.editPostcode);
        editAddress = findViewById(R.id.editAddress);
        //editAddress2 = findViewById(R.id.editAddress2);
        editAddress3 = findViewById(R.id.editAddress3);
        editEmail = findViewById(R.id.editEmail);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("flg_sms", false)){
            flg_sms = "Y";
        } else{
            flg_sms = "N";
        }

        if(intent.getBooleanExtra("flg_email", false)){
            flg_email = "Y";
        } else{
            flg_email = "N";
        }

        println("intent sms get : " + intent.getBooleanExtra("flg_sms", false));
        println("intent email get : " + intent.getBooleanExtra("flg_email", false));
        println("flg_email : " + flg_email);
        println("flg_sms : " + flg_sms);

        btnPostCode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                init_webView();
                handler = new Handler();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        btnIdCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idToCheck = editId.getText().toString().trim();

                if (idToCheck.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else{
                    checkIdRequest();
                }
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exist){
                    Toast.makeText(JoinActivity.this, "아이디 중복 확인 해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                String id = editId.getText().toString();
                String passwd = editPasswd.getText().toString();
                String passwd2 = editPasswd2.getText().toString();
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                String postcode = editPostcode.getText().toString();
                String address = editAddress.getText().toString();
                String address3 = editAddress3.getText().toString();
                String email = editEmail.getText().toString();

                if (id.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwd.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "암호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (passwd2.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "암호 재확인을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (phone.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "연락처를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (postcode.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "우편번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (address.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "주소를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (address3.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "상세주소를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (email.isEmpty()) {
                    Toast.makeText(JoinActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!editPasswd.getText().toString().equals(editPasswd2.getText().toString())) {
                    Toast.makeText(JoinActivity.this, "암호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    editPasswd.setText("");
                    editPasswd2.setText("");
                } else if (!checkPasswd(passwd)) {
                    Toast.makeText(JoinActivity.this, "암호는 8자 이상 16자 이하,  ! @ # $ % 중 하나를 포함해야 합니다.", Toast.LENGTH_LONG).show();
                } else {
                    checkDuplicate();
                }
            }
        });

        editPasswd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwd = editPasswd.getText().toString();
                String confirm = editPasswd2.getText().toString();

                if (passwd.equals(confirm)) {
                    editPasswd.setBackgroundColor(Color.LTGRAY);
                    editPasswd2.setBackgroundColor(Color.LTGRAY);
                } else {
                    editPasswd.setBackgroundColor(Color.GRAY);
                    editPasswd2.setBackgroundColor(Color.GRAY);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void checkDuplicate() {

        try {

            String url = Config.serverUrl + "/front/join/duplicateCheck.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cst_nm", editName.getText().toString().trim());
            jsonObject.addProperty("cst_email", editEmail.getText().toString().trim());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("[LOGGING][RESPONSE]", response);


                    if (response != null && !response.isEmpty()) {
                        if (response.equals("false")) {
                            Toast.makeText(JoinActivity.this, "이미 가입된 회원입니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            joinProc();
                        }
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("[LOGGING][ERROR]", error.getMessage());
                        }
                    }
                    ){
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
                        if (requestJSON != null && requestJSON.length() > 0 && !requestJSON.equals("")) {
                            return requestJSON.getBytes("utf-8");
                        } else {
                            return null;
                        }
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }
            };

            request.setShouldCache(false);
            requestQueue.add(request);
            Log.d("[LOGGING][REQUEST]", requestJSON);

            } catch (Exception e) {
            Log.d("[LOGGING][ERROR]", e.getMessage());
        }
    }

    public void checkIdRequest(){

        try {

            String url = Config.serverUrl + "/front/join/idCheck.api";
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", editId.getText().toString().trim());
            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    println("[응답] " + response);

                    if (response != null && !response.isEmpty()) {
                        if (response.equals("false")) {
                            Toast.makeText(JoinActivity.this, "중복된 아이디 입니다.", Toast.LENGTH_LONG).show();
                            exist = false;
                        } else {
                            Toast.makeText(JoinActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
                            exist = true;
                        }
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

    public void joinProc() {
        try {
            /*
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("seq_sle", 1);
            jsonParams.put("seq_prd", 2);
            requestJSON = String.valueOf(jsonParams.toString());
            */

            //String url = Config.serverUrl+"/front/join/joinProc.api";

            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", editId.getText().toString());
            jsonObject.addProperty("passwd", editPasswd.getText().toString());
            jsonObject.addProperty("passwd2", editPasswd2.getText().toString());
            jsonObject.addProperty("cst_nm", editName.getText().toString());
            jsonObject.addProperty("phone", editPhone.getText().toString());
            jsonObject.addProperty("postcode", editPostcode.getText().toString());
            jsonObject.addProperty("addr1", editAddress.getText().toString());
            //jsonObject.addProperty("addr2", editAddress2.getText().toString());
            jsonObject.addProperty("addr3", editAddress3.getText().toString());
            jsonObject.addProperty("cst_email", editEmail.getText().toString());
            jsonObject.addProperty("flg_sms", flg_sms);
            jsonObject.addProperty("flg_email", flg_email);

            requestJSON = gson.toJson(jsonObject);

            StringRequest request = new StringRequest(Request.Method.POST, Config.serverUrl + "/front/join/joinProc.api"
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("[LOGGING][RESPONE]", response);

                    if (response != null && !response.equals("")) {
                        if (response.equals("true")) {
                            Toast.makeText(JoinActivity.this, "회원 가입 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "회원 가입 실패", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "회원 가입 실패", Toast.LENGTH_LONG).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("[LOGGING][ERROR]", error.getMessage());
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
            Log.d("[LOGGING][REQUEST]", requestJSON);
        }
        catch (Exception e) {
            Log.d("[LOGGING][ERROR]", e.getMessage());
        }

    }
    public void init_webView() {
        // WebView 설정
        webView = (WebView) findViewById(R.id.webView_address);

        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true); // javascript를 사용할 경우 다음과 같이 설정
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트가 window.open()을 사용가능
        webSettings.setSupportMultipleWindows(true); // 다중 윈도우 허용(팝업을 위해 추가)


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new MyWebChromeClient(this));

        // webview url load
        webView.loadUrl(Config.serverUrl + "/front/customer/daumAddress.api");

    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    editPostcode.setText(String.format("%s", arg1));
                    editAddress.setText(String.format("%s", arg2));
                }
            });
        }
    }

    public void println(String data) {
        Log.d("JoinActivity", data);
    }
}