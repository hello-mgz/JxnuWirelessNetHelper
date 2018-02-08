package com.example.ma.jxnuwirelessnethelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent startIntent=new Intent(context,MyService.class);
        context.startService(startIntent);
    }
}
