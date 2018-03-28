package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity
{
    private WebView mWebView;
    private final static String URL="http://219.229.251.5:8800/home/base/index";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mWebView=(WebView)findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(URL);
        setCookies(URL);
    }

    @Override
    protected void onDestroy()
    {
        saveCookies(URL);
        super.onDestroy();
    }

    private void saveCookies(String url)
    {
        CookieManager  mCookieManager=CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        String cookieStr = mCookieManager.getCookie(url);
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("cookie",cookieStr);
        editor.apply();
        Log.d("Debug","getCookie:"+cookieStr);
    }

    private void setCookies(String url)
    {
        /*CookieManager  mCookieManager=CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String cookieStr=pref.getString("cookie","");
        Log.d("Debug","setCookie:"+cookieStr);
        if(!cookieStr.isEmpty())
        mCookieManager.setCookie(cookieStr,url);*/
    }
}
