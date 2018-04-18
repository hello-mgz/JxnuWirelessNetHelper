package pers.jxnu.maguozhuang.jxnuwirelessnethelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
