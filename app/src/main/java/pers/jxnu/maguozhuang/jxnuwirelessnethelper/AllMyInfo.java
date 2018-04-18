package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ma on 2018/4/18.
 * 采用实例模式保存所有需要的信息
 */

public class AllMyInfo
{
    public static final String LOCAL_DATA="pers.jxnu.maguozhuang.jxnuwirelessnethelper.DATA";
    public static final String USERNAME="DATA.username";
    public static final String PASSWORD="DATA.password";
    public static final String DOMAIN="DATA.domain";
    public static final String AUTOLOGIN="DATA.autoLogin";
    public static final String SHOWNOTIFICATION="DATA.autoLogin";

    //静态变量
    private static AllMyInfo sInfo;//唯一实例

    //静态方法
    public static AllMyInfo getInstance(Context context)
    {
        if(sInfo==null)
        {
            sInfo=new AllMyInfo(context);
        }
        return sInfo;
    }

    //实例变量
    private Context mContext;
    private String username;//学号或工号
    private String password;//密码
    private String domain;//运营商域名
    private boolean AutoLogin;//是否自动登录，默认自动
    private boolean ShowNotification;//是否显示状态栏通知，默认显示

    //构造方法
    private AllMyInfo(Context context)
    {
        mContext=context;
        SharedPreferences pref=mContext.getSharedPreferences(LOCAL_DATA,Context.MODE_PRIVATE);
        username=pref.getString(USERNAME,"");
        password=pref.getString(PASSWORD,"");
        domain=pref.getString(DOMAIN,"");
        AutoLogin=pref.getBoolean(AUTOLOGIN,true);
        ShowNotification=pref.getBoolean(SHOWNOTIFICATION,true);
    }

    public boolean isAutoLogin()
    {
        return AutoLogin;
    }

    public boolean isShowNotification()
    {
        return ShowNotification;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getDomain()
    {
        return domain;
    }

    public AllMyInfo setUsername(String username)
    {
        this.username=username;
        return this;
    }

    public AllMyInfo setPassword(String password)
    {
        this.password=password;
        return this;
    }

    public AllMyInfo setDomain(String domain)
    {
        this.domain=domain;
        return this;
    }

    public AllMyInfo setAutoLogin(boolean autoLogin)
    {
        this.AutoLogin=autoLogin;
        return this;
    }

    public AllMyInfo setShowNotification(boolean showNotification)
    {
        this.ShowNotification=showNotification;
        return this;
    }

    public void execute()
    {
        SharedPreferences pref=mContext.getSharedPreferences(LOCAL_DATA,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.putString(DOMAIN,domain);
        editor.putBoolean(AUTOLOGIN,AutoLogin);
        editor.putBoolean(SHOWNOTIFICATION,ShowNotification);
        editor.apply();
    }
}
