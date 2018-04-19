package cn.edu.chd.yitu;

import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebcaidanActivity extends Activity {

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_caidan);
        mWebView = findViewById(R.id.mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());


        mWebView.loadUrl("http://moha.nwafu.me");
    }

}
