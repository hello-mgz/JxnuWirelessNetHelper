package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
    private Button button_start,button_about,button_disconnect;
    private Button oval;
    private TextView wifi_text;
    private GradientDrawable myGrad;
    private Toolbar mToolbar;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.layout.activity_main);

        edit_username=(EditText)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.edit_username);
        edit_password=(EditText)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.edit_password);
        spinner=(Spinner)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.spinner);
        button_start=(Button)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.button_start);
        button_about=(Button)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.button_about);
        button_disconnect=(Button)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.button_disconnect);
        oval=(Button)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.oval);
        wifi_text=(TextView)findViewById(pers.jxnu.maguozhuang.jxnuwirelessnethelper.R.id.wifi_state);
        myGrad=(GradientDrawable)oval.getBackground();
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mSwitch=(Switch)findViewById(R.id.mySwitch);

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
                            {wifi_text.setText("连接wifi");}
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

        setDefault();//恢复数据

        button_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent startIntent=new Intent(MainActivity.this,MyService.class);
                startIntent.putExtra(MyService.MyServiceData,MyService.LoginByActivity);
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

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
                if(isChecked) {
                    editor.putBoolean("auto_login",true);
                }
                else{
                    editor.putBoolean("auto_login",false);
                }
                editor.apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.connectWeb:
                Intent intent=new Intent(MainActivity.this,WebActivity.class);
                startActivity(intent);
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onDestroy()
    {

        super.onDestroy();
    }

    private boolean isInfoChange()
    {
        String username=edit_username.getText().toString();
        String password=edit_password.getText().toString();
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
        String domain=selection;
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        if(!username.equals(pref.getString("username","")))
            return true;
        if(!domain.equals(pref.getString("domain","")))
            return true;
        if(!password.equals(pref.getString("password","")))
            return true;
        return false;
    }

    @Override
    public void onBackPressed()
    {
        if(isInfoChange())
        {
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("提示：");
            dialog.setMessage("信息发生改变，是否保存信息？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    saveData();
                    finish();
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });
            dialog.show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void saveData()
    {
        String username=edit_username.getText().toString();
        String password=edit_password.getText().toString();
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
        String domain=selection;
        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.putString("domain",domain);
        editor.putString("password",password);
        editor.apply();
    }

    private void setDefault()
    {
        String username,password,domain;
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        username=pref.getString("username","");
        domain=pref.getString("domain","");
        password=pref.getString("password","");
        boolean auto;
        auto=pref.getBoolean("auto_login",true);
        if(auto)
            mSwitch.setChecked(auto);
        if(!username.isEmpty())
        {
            edit_username.setText(username);
        }
        if(!password.isEmpty())
        {
            edit_password.setText(password);
        }
        if(!domain.isEmpty())
        switch (domain)
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

    private void disconnectHost()//断开连接
    {
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
        RequestBody loginform=new FormBody.Builder()
                .add("action", "logout")
                .add("username", pref.getString("username",""))
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
