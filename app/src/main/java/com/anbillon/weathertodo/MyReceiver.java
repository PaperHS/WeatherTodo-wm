package com.anbillon.weathertodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("debug","收到广播");
        Log.d("debug","myreceiver:   "+intent.getStringExtra("info"));
        context.startService(new Intent(context,MyService.class));
    }
}
