package com.anbillon.weathertodo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug","onCreate");
        Notification notification = new Notification.Builder(this.getApplicationContext())
                .setContentText("This is foreground service")
                .setSmallIcon(R.drawable.ic_stat_ac_unit)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(111,notification);
    }

    @Override
    public void onDestroy() {
        Log.d("debug","onDestroy");
        stopForeground(true);
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("debug","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("debug","onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("debug","onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("debug","onRebind");
    }


    public class MyBinder extends Binder{
        public int getTest(){
            return 100;
        }
        public MyService getService(){
            return MyService.this;
        }
    }
}
