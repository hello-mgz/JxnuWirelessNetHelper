package com.example.ma.jxnuwirelessnethelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    private EditText edit_username,edit_password;
    private Spinner spinner;
    private Button button_save,button_start,button_about,button_disconnect;
    private Button oval;
    private TextView wifi_text;
    private GradientDrawable myGrad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_username=(EditText)findViewById(R.id.edit_username);
        edit_password=(EditText)findViewById(R.id.edit_password);
        spinner=(Spinner)findViewById(R.id.spinner);
        button_save=(Button)findViewById(R.id.button_save);
        button_start=(Button)findViewById(R.id.button_start);
        button_about=(Button)findViewById(R.id.button_about);
        button_disconnect=(Button)findViewById(R.id.button_disconnect);
        oval=(Button)findViewById(R.id.oval);
        wifi_text=(TextView)findViewById(R.id.wifi_state);
        myGrad=(GradientDrawable)oval.getBackground();


        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                while (true)
                {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    final String ssid=wifiInfo.getSSID();
                    if(ssid.equals("\"jxnu_stu\""))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {wifi_text.setText("已连接");}
                        });
                        for(int i=-255;i<=255;i+=5) {
                            final int t=255-Math.abs(i);;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {myGrad.setColor(Color.rgb(255-t,255,255-t));}
                            });
                            try
                            {
                                Thread.sleep(50);
                            }catch (InterruptedException e){e.printStackTrace();}
                        }
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {wifi_text.setText("未连接");}
                        });
                        for(int i=-255;i<=255;i+=5) {
                            final int t=255-Math.abs(i);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {myGrad.setColor(Color.rgb(255,255-t,255-t));}
                            });
                            try
                            {
                                Thread.sleep(50);
                            }catch (InterruptedException e){e.printStackTrace();}
                        }
                    }
                }

            }
        }).start();

        setDefault();
        button_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String selection="";
                switch (spinner.getSelectedItem().toString())
                {
                    case "联通校园宽带":
                        selection="@cucc";
                        break;
                    case "移动校园宽带":
                        selection="@cmcc";
                        break;
                    case "电信校园宽带":
                        selection="@ctcc";
                        break;
                    case "校园宽带":
                        selection="@jxnu";
                        break;
                    default:break;
                }
                saveData(edit_username.getText().toString()+selection,edit_password.getText().toString());
                Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });

        button_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent startIntent=new Intent(MainActivity.this,MyService.class);
                startService(startIntent);
                //Toast.makeText(MainActivity.this,"启动成功",Toast.LENGTH_SHORT).show();
            }
        });

        button_about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
            }
        });

        button_disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        disconnectHost();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void saveData(String username, String password)
    {
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.apply();
    }

    private void setDefault()
    {
        String username,password;
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        password=pref.getString("password","");
        if(!username.isEmpty())
        {
            edit_username.setText(username.substring(0,username.length()-5));
        }
        if(!password.isEmpty())
        {
            edit_password.setText(password);
        }
        if(!username.isEmpty())
        switch (username.substring(username.length()-5))
        {
            case "@cucc":
                spinner.setSelection(0,true);
                break;
            case "@cmcc":
                spinner.setSelection(1,true);
                break;
            case "@ctcc":
                spinner.setSelection(2,true);
                break;
            case "@jxnu":
                spinner.setSelection(3,true);
                break;
            default:break;
        }
    }

    private void disconnectHost()
    {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        RequestBody loginform=new FormBody.Builder()
                .add("action", "logout")
                .add("username", pref.getString("username","").substring(0,12))
                .add("ajax", "1")
                .build();
        Request loginRequest = new Request.Builder()
                .url("http://219.229.251.2/include/auth_action.php")
                .post(loginform)
                .build();
        try {
            //非异步执行
            Response loginResponse = client.newCall(loginRequest).execute();
            String body=loginResponse.body().string();
            final String info=body.toString();
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast.makeText(MainActivity.this,"访问失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
