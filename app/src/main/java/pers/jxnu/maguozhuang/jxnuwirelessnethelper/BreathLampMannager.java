package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

/**
 * Created by ma on 2018/4/18.
 */

public class BreathLampMannager
{
    private GradientDrawable mGrad;
    private TextView wifi_text;
    private Context mContext;
    private Handler mHandler;

    public BreathLampMannager(GradientDrawable grad, TextView wifi_text, Context context)
    {
        mGrad = grad;
        this.wifi_text = wifi_text;
        mContext = context;
        mHandler=new Handler()//更新UI
        {
            @Override
            public void handleMessage(Message msg)
            {
                Bundle bundle=msg.getData();
                mGrad.setColor(bundle.getInt("Color"));
                wifi_text.setText(bundle.getString("Text"));
                super.handleMessage(msg);
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            while (true)
            {
                WifiManager wifiManager = (WifiManager) mContext.getApplicationContext()
                        .getSystemService(mContext.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid=wifiInfo.getSSID();
                if(ssid.equals(LoginIntentService.WifiName))
                {
                    for(int i=-255;i<=255;i+=5) {
                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("Text","连接wifi");
                        bundle.putInt("Color", Color.rgb(Math.abs(i),255,Math.abs(i)));
                        msg.setData(bundle);
                        //mHandler.sendMessageDelayed(msg,20);
                        mHandler.sendMessage(msg);
                        try {
                            Thread.sleep(50);
                        }
                        catch (InterruptedException e){e.printStackTrace();}
                        catch (Exception e){e.printStackTrace();}
                    }
                }
                else
                {
                    for(int i=-255;i<=255;i+=5) {
                        Message msg=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("Text","未发现wifi");
                        bundle.putInt("Color",Color.rgb(255,Math.abs(i),Math.abs(i)));
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                        try {
                            Thread.sleep(50);
                        }catch (InterruptedException e){e.printStackTrace();}
                        catch (Exception e){e.printStackTrace();}
                    }
                }
            }
        }).start();
    }
}
