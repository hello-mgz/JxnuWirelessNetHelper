package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 广播接收器：用于启动后台服务
 */
public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent startIntent=new Intent(context,LoginIntentService.class);
        startIntent.putExtra(LoginIntentService.StartServiceCode,LoginIntentService.LoginByBroadcast);
        context.startService(startIntent);
    }
}
