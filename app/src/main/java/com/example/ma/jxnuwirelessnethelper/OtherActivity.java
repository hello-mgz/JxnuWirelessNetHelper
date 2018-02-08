package com.example.ma.jxnuwirelessnethelper;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class OtherActivity extends AppCompatActivity
{
    private TextView text_net;
    private Button but_reback;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        text_net=(TextView)findViewById(R.id.text_net);
        but_reback=(Button)findViewById(R.id.button_reback);
        but_reback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final String str=getLastestVersion();
                Log.d("Debug",str);
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        text_net.setText(str);
                    }
                });
            }
        }).start();
    }

    private String getLastestVersion()
    {
        try{
            Document doc = Jsoup.connect("http://www.cnblogs.com/mgz-/p/8361083.html").get();
            Element e=doc.getElementById("cnblogs_post_body");
            return e.text();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "最新版本：获取失败";
        }
    }
}
