package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
        syncCookies(URL);
        mWebView.loadUrl(URL);
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

    private void syncCookies(String url)
    {
        //CookieSyncManager.createInstance(WebActivity.this);
        CookieManager  mCookieManager=CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        //mCookieManager.removeSessionCookie();// 移除
        //mCookieManager.removeAllCookie();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        String cookieStr=pref.getString("cookie","");
        Log.d("Debug","setCookie:"+cookieStr);
        if(!cookieStr.isEmpty())
        mCookieManager.setCookie(url,cookieStr);
        //CookieSyncManager.getInstance().sync();
    }
}
