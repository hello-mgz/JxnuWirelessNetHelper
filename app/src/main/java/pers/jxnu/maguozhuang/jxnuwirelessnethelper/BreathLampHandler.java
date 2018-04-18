package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by ma on 2018/4/18.
 */

public class BreathLampHandler extends Handler
{
    private GradientDrawable mGrad;
    private TextView wifi_text;
    private Context mContext;

    BreathLampHandler(GradientDrawable Grad,TextView wifi_text,Context context)
    {
        this.wifi_text=wifi_text;
        this.mGrad=Grad;
        mContext=context;
    }

    @Override
    public void handleMessage(Message msg)
    {
        /*WifiManager wifiManager = (WifiManager) mContext.getApplicationContext()
                .getSystemService(mContext.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid=wifiInfo.getSSID();
        if(ssid.equals(LoginIntentService.WifiName))
        {
            wifi_text.setText("连接wifi");
            for(int i=-255;i<=255;i+=5) {
               int t=255-Math.abs(i);
               mGrad.setColor(Color.rgb(255-t,255,255-t));
                try
                {
                    Thread.sleep(50);
                }catch (InterruptedException e){e.printStackTrace();}
                catch (Exception e){e.printStackTrace();}
            }
        }
        else
        {
            wifi_text.setText("未连接");
            for(int i=-255;i<=255;i+=5) {
                int t=255-Math.abs(i);
                mGrad.setColor(Color.rgb(255,255-t,255-t));
                try
                {
                    Thread.sleep(50);
                }catch (InterruptedException e){e.printStackTrace();}
                catch (Exception e){e.printStackTrace();}
            }
        }
        super.handleMessage(msg);
        this.sendEmptyMessage(0);*/
    }
}
