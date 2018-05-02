package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;


public class LoginActivity extends AppCompatActivity
{
    private MyEditText edit_username,edit_password;
    private Spinner spinner;
    private Switch mSwitch;
    private AllMyInfo mAllMyInfo;
    private Toolbar mToolbar;
    private Switch mSwitch_showNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_username=(MyEditText)findViewById(R.id.edit_username);
        edit_password=(MyEditText)findViewById(R.id.edit_password);
        spinner=(Spinner)findViewById(R.id.spinner);
        mSwitch=(Switch)findViewById(R.id.mySwitch);
        mAllMyInfo=AllMyInfo.getInstance(getApplicationContext());
        mToolbar=(Toolbar)findViewById(R.id.login_toolbar);
        mSwitch_showNotification=(Switch) findViewById(R.id.switch_showNotification);

        setSupportActionBar(mToolbar);
        Log.d("Debug","开始时"+mAllMyInfo.isAutoLogin()+" "+mSwitch.isChecked());
        setDefault();//恢复数据
        Log.d("Debug","恢复后"+mAllMyInfo.isAutoLogin()+" "+mSwitch.isChecked());
        //开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Log.d("Debug","change1"+mAllMyInfo.isAutoLogin());
                mAllMyInfo.setAutoLogin(isChecked)
                        .execute();
                Log.d("Debug","change2"+mAllMyInfo.isAutoLogin());
            }
        });
        mSwitch_showNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Log.d("Debug","change1"+mAllMyInfo.isAutoLogin());
                mAllMyInfo.setShowNotification(isChecked)
                        .execute();
                Log.d("Debug","change2"+mAllMyInfo.isAutoLogin());
            }
        });
        mToolbar.setNavigationOnClickListener((v)->{
            onBackPressed();
        });
    }

    @Override
    protected void onDestroy()
    {
        Log.d("Debug","退出时"+mAllMyInfo.isAutoLogin()+" "+mSwitch.isChecked());
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
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
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
        mSwitch_showNotification.setChecked(mAllMyInfo.isShowNotification());

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
