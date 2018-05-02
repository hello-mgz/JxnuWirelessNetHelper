package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    private Button button_start,button_disconnect;
    private Button oval;
    private TextView wifi_text;
    private GradientDrawable myGrad;
    private Toolbar mToolbar;
    private InternetSpeedTextView speed_text;
    private AllMyInfo mAllMyInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_start=(Button)findViewById(R.id.button_start);
        button_disconnect=(Button)findViewById(R.id.button_disconnect);
        oval=(Button)findViewById(R.id.oval);
        wifi_text=(TextView)findViewById(R.id.wifi_state);
        myGrad=(GradientDrawable)oval.getBackground();
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        speed_text=(InternetSpeedTextView) findViewById(R.id.text_speed);
        speed_text.setText("0.0kb/s");
        speed_text.setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg)
            {
                Bundle bundle=msg.getData();
                String speedData=bundle.getString("InternetSpeedData");
                speed_text.setText(speedData);
                super.handleMessage(msg);
            }
        });

        mAllMyInfo=AllMyInfo.getInstance(getApplicationContext());
        BreathLampMannager mannager=new BreathLampMannager(myGrad,wifi_text,getApplicationContext());

        mToolbar.setNavigationOnClickListener((v)->{
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
        });

        //启动后台登录服务
        button_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Callback callback=new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        runOnUiThread(()-> {
                        Toast.makeText(MainActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        final String backInfo=response.body().string().toString();
                        runOnUiThread(()-> {
                            if(backInfo.contains("login_ok"))
                                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            else if(backInfo.contains("延迟"))
                                Toast.makeText(MainActivity.this, "已经连接成功", Toast.LENGTH_SHORT).show();
                            else Toast.makeText(MainActivity.this, backInfo, Toast.LENGTH_SHORT).show();
                        });
                    }
                };
                LoginAdapter.executeLoginIn(mAllMyInfo,callback);//调用登录方法
            }
        });


        //断开连接
        button_disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Callback callback=new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        String info=response.body().string().toString();
                        runOnUiThread(()->{
                            Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
                        });
                        //Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
                    }
                };
                LoginAdapter.executeLoginOut(mAllMyInfo,callback);
            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(mAllMyInfo.isFirstLogin())
        {
            Log.d("Debug", "onStart: "+mAllMyInfo.isFirstLogin());
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
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
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.connectWeb:
                intent=new Intent(MainActivity.this,WebActivity.class);
                startActivity(intent);
                break;
            case R.id.button_about:
                intent=new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
            default:break;
        }
        return true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

}
