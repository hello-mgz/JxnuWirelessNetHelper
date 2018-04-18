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
    private AllMyInfo mAllMyInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_username=(EditText)findViewById(R.id.edit_username);
        edit_password=(EditText)findViewById(R.id.edit_password);
        spinner=(Spinner)findViewById(R.id.spinner);
        button_start=(Button)findViewById(R.id.button_start);
        button_about=(Button)findViewById(R.id.button_about);
        button_disconnect=(Button)findViewById(R.id.button_disconnect);
        oval=(Button)findViewById(R.id.oval);
        wifi_text=(TextView)findViewById(R.id.wifi_state);
        myGrad=(GradientDrawable)oval.getBackground();
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mSwitch=(Switch)findViewById(R.id.mySwitch);

        mAllMyInfo=AllMyInfo.getInstance(getApplicationContext());
        BreathLampMannager mannager=new BreathLampMannager(myGrad,wifi_text,getApplicationContext());


        setDefault();//恢复数据

        //启动后台登录服务
        button_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent startIntent=new Intent(MainActivity.this,LoginIntentService.class);
                startIntent.putExtra(LoginIntentService.StartServiceCode,LoginIntentService.LoginByActivity);
                startService(startIntent);
                //Toast.makeText(MainActivity.this,"启动成功",Toast.LENGTH_SHORT).show();
            }
        });

        //启动OtherActivity
        button_about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,OtherActivity.class);
                startActivity(intent);
            }
        });

        //断开连接
        button_disconnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                runOnUiThread(()->{
                    String info=LoginAdapter.executeLoginOut(mAllMyInfo);
                    Toast.makeText(MainActivity.this,info,Toast.LENGTH_SHORT).show();
                });
            }
        });

        //开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mAllMyInfo.setAutoLogin(isChecked)
                        .execute();
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
        if(!username.equals(mAllMyInfo.getUsername()))
            return true;
        if(!domain.equals(mAllMyInfo.getDomain()))
            return true;
        if(!password.equals(mAllMyInfo.getPassword()))
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
        String selection="@cucc";
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

        //使用建造者模式保存信息
        mAllMyInfo.setUsername(username)
            .setPassword(password)
            .setDomain(selection)
            .execute();
    }

    private void setDefault()
    {
        mSwitch.setChecked(mAllMyInfo.isAutoLogin());
        edit_username.setText(mAllMyInfo.getUsername());
        edit_password.setText(mAllMyInfo.getPassword());
        switch (mAllMyInfo.getDomain())
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
            default:spinner.setSelection(0,true);
                break;
        }
    }
}
