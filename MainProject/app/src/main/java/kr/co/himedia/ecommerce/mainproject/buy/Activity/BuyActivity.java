package kr.co.himedia.ecommerce.mainproject.buy.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.himedia.ecommerce.mainproject.R;
import kr.co.himedia.ecommerce.mainproject.buy.dto.BuyDtlDto;
import kr.co.himedia.ecommerce.mainproject.common.AppDatabase;
import kr.co.himedia.ecommerce.mainproject.common.Config;
import kr.co.himedia.ecommerce.mainproject.common.MyWebChromeClient;
import kr.co.himedia.ecommerce.mainproject.common.SharedPreferencesManager;
import kr.co.himedia.ecommerce.mainproject.main.activity.MainActivity;
import kr.co.himedia.ecommerce.mainproject.sale.dto.SaleDto;

public class BuyActivity extends AppCompatActivity {

    WebView webView;
    private String postDataHtml;
    Handler handler;
    ArrayList<BuyDtlDto> listBuyDtlDto;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        Intent intent = getIntent();
        listBuyDtlDto = (ArrayList<BuyDtlDto>) intent.getSerializableExtra("listBuyDtlDto");

        db = AppDatabase.getInstance(getApplicationContext());

        // WebView 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        webView = (android.webkit.WebView) findViewById(R.id.webView_buy);

        // webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("intent://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                        if (existPackage != null) {
                            startActivity(intent);
                        } else {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (url != null && url.startsWith("market://")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            startActivity(intent);
                        }
                        return true;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                view.loadUrl(url);
                return false;
            }
        });

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true); // javascript를 사용할 경우 다음과 같이 설정
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 자바스크립트가 window.open()을 사용가능
        webSettings.setSupportMultipleWindows(true); // 다중 윈도우 허용(팝업을 위해 추가)


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new BuyActivity.AndroidBridge(), "TestPay");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new MyWebChromeClient(this));

        // HTML 코드를 생성
        postDataHtml = generateHtml();

        // WebView에 HTML 코드 로드
        webView.loadData(postDataHtml, "text/html", "UTF-8");

        // webview url load
        // webView.loadUrl(Config.serverUrl + "/front/buy/payForm.api");
    }

    // 동적으로 생성된 HTML 코드를 반환하는 메서드
    private String generateHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>POST Data</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<form id=\"postForm\" action=\"" + Config.serverUrl + "/front/buy/payForm.api\" method=\"post\">");

        // 동적 데이터 생성 및 추가
        for(int i=0; i<listBuyDtlDto.size(); i++){
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].seq_sle\" value=\"" + listBuyDtlDto.get(i).getSeq_sle() +"\" >");
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].price\" value=\"" + listBuyDtlDto.get(i).getPrice() +"\" >");
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].count\" value=\"" + listBuyDtlDto.get(i).getCount() +"\" >");
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].sle_nm\" value=\"" + listBuyDtlDto.get(i).getSle_nm() +"\" >");
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].cst_nm\" value=\"" + SharedPreferencesManager.getCst_nm(getApplicationContext()) +"\" >");
            html.append("<input type=\"hidden\" name=\"buyList[" + i + "].register\" value=\"" + SharedPreferencesManager.getSeq_cst(getApplicationContext()) +"\" >");
        }

        html.append("</form>");
        html.append("<script>");
        html.append("document.getElementById('postForm').submit();"); // 자동으로 폼 제출
        html.append("</script>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    private class AndroidBridge {

        @JavascriptInterface
        public void toastM(final String arg1) {

            class DeleteRunnable implements Runnable {
                @Override
                public void run() {
                    db.buyDtlDao().deleteAll();
                }
            }
            if(arg1.equals("결제 성공")){
                DeleteRunnable deleteRunnable = new DeleteRunnable();
                new Thread(deleteRunnable).start();
            }

            Toast.makeText(getApplicationContext(), arg1, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void println(String data) {
        Log.d("BuyActivity", data);
    }
}
