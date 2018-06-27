package pers.jxnu.maguozhuang.jxnuwirelessnethelper.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import pers.jxnu.maguozhuang.jxnuwirelessnethelper.R;

/**
 * Created by ma on 2018/6/27.
 */

public class ConfigManager
{
    public static String getConfigInfo(Context context,String key)
    {
        Properties prop=new Properties();
        String ans="";
        try(InputStream in=context.getAssets().open("appConfig.json"))
        {
            prop.load(in);
            ans=prop.getProperty(key);
        }catch (IOException e){e.printStackTrace();}
        return ans;
    }
    public static void setConfigInfo(Context context,String key,String value)
    {

    }
}
