package kr.co.himedia.ecommerce.mainproject.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebChromeClient extends WebChromeClient {

    WebView myWebViewPop; // 팝업에 쓰일 웹뷰
    private Activity mActivity = null;

    public MyWebChromeClient(Activity activity) {
        this.mActivity = activity;
    }

    // onCreateWindow() Overriding을 통해 새로운 WebView를 dialog형식으로 띄우도록 설정
    @Override
    public boolean onCreateWindow(android.webkit.WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        myWebViewPop = new android.webkit.WebView(view.getContext());
        WebSettings webSettings = myWebViewPop.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebViewPop.setWebViewClient(new WebViewClient());

        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(myWebViewPop);

        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.show();

        // 뒤로가기
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) { // 뒤로가기 버튼 유무와 ACTION_DOWN일 경우
                    if (myWebViewPop.canGoBack()) {
                        myWebViewPop.goBack();
                    } else {
                        dialog.dismiss();
                        myWebViewPop.destroy();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        myWebViewPop.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onCloseWindow(android.webkit.WebView window) {
                dialog.dismiss();
                window.destroy();
            }
        });

        ((WebView.WebViewTransport) resultMsg.obj).setWebView(myWebViewPop);
        resultMsg.sendToTarget();
        return true;
    }
}
