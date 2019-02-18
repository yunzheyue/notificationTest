package com.example.mdtest3;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private MyBroadCast myBroadCast;

    public class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }
    //通过binder实现调用者client与Service之间的通信
    private MyBinder binder = new MyBinder();




    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBroadCast = new MyBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MainActivity.MUSIC_NOTIFICATION_ACTION_PLAY);
        this.registerReceiver(myBroadCast, intentFilter);
        Log.e("TAG", "服务开启,注册完毕");
    }

    private class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "收到消息");
            if (intent.getStringExtra("type") != null) {
                Log.e("TAG", "msg==" + intent.getStringExtra("type"));
//                收到消息后，应该修改通知中的状态

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCast);
        Log.e("TAG", "服务关闭,解注册");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
