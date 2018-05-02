package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.Context;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.net.TrafficStatsCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ma on 2018/5/2.
 */

public class InternetSpeedTextView extends AppCompatTextView
{
    private Handler mHandler;
    public InternetSpeedTextView(Context context)
    {
        super(context);
        init();
    }

    public InternetSpeedTextView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public InternetSpeedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        new Thread(()->{
            long last=0,cur=0;
            last= TrafficStats.getTotalRxBytes();
            while (true)
            {
                try
                {
                    Thread.sleep(1000);
                }catch (InterruptedException e){e.printStackTrace();}
                cur=TrafficStats.getTotalRxBytes();
                if(mHandler!=null)
                {
                    double speed=(cur-last)/1024.0;
                    String speedtext=String.format("%.1f",speed)+"KB/s";
                    if(speed>1024)
                    {
                        speedtext=String.format("%.1f",speed/1024.0)+"MB/s";
                    }
                    Message msg=new Message();
                    Bundle bundle=new Bundle();
                    bundle.putString("InternetSpeedData",speedtext);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
                last=cur;
            }
        }).start();
    }
    public void setHandler(Handler handler)
    {
        mHandler=handler;

    }
}
