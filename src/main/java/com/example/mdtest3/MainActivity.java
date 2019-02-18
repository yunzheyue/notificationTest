package com.example.mdtest3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.mdtest3.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        viewDataBinding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification1();
            }
        });

        viewDataBinding.bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification2();
            }
        });

        viewDataBinding.bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendNotification3();
            }
        });

        viewDataBinding.btStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                MyServiceConnection myServiceConnection = new MyServiceConnection();
                bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
            }
        });

        viewDataBinding.btChangeNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });
    }


    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("TAG", "服务已连接");
            MyService.MyBinder myBinder = (MyService.MyBinder) binder;
            MyService service = myBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    //    普通通知
    public void sendNotification1() {
        Toast.makeText(MainActivity.this, "普通通知", Toast.LENGTH_SHORT).show();
        String channel_id = "channel_000";
        String name = "nomalname";
//        一定要使用NotificationCompat，才会获得提示音和震动的权限，不能直接使用new Notification.Builder()方法。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id);
//        android8.0后要设置channelId，否则就不能进行提示。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.canBypassDnd();//是否可以绕过请勿打扰模式
            mChannel.canShowBadge();//是否可以显示icon角标
            mChannel.enableLights(true);//是否显示通知闪灯
            mChannel.enableVibration(true);//收到小时时震动提示
            mChannel.setBypassDnd(true);//设置绕过免打扰
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            mChannel.setLightColor(Color.RED);//设置闪光灯颜色
            mChannel.getAudioAttributes();//获取设置铃声设置
            mChannel.setVibrationPattern(new long[]{100, 200, 100});//设置震动模式
            mChannel.shouldShowLights();//是否会闪光
            notifyManager.createNotificationChannel(mChannel);
            builder.setChannelId(channel_id);
        } else {
//           低版本不用管理.
        }


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setAutoCancel(true);
        // 什么时候提醒的
        builder.setWhen(System.currentTimeMillis());
        // 设置通知栏的优先级
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        // 设置是否震动等
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test));
        builder.setContentTitle("啦啦啦");
        builder.setSubText("你好啊");
        builder.setContentText("美女");
        builder.setProgress(100, 30, false);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notifyManager.notify(new Random().nextInt(1000), notification);

    }

    private final int REQUEST_CODE = 30000;
    // 给Service 发送广播  注意在service中注册广播
    public static final String MUSIC_NOTIFICATION_ACTION_PLAY = "musicnotificaion.To.PLAY";
    //    这是折叠的通知view
    private RemoteViews expandedView;
    //    设置的暂存的通知
    private int notificationId = 0;
    private NotificationCompat.Builder builder;

    // 折叠通知
    public void sendNotification2() {
        Toast.makeText(MainActivity.this, "折叠通知", Toast.LENGTH_SHORT).show();
        String channel_id = "channel_001";
        String name = "foldname";
//        一定要使用NotificationCompat，才会获得提示音和震动的权限，不能直接使用new Notification.Builder()方法。
        builder = new NotificationCompat.Builder(this, channel_id);
//        android8.0后要设置channelId，否则就不能进行提示。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.canBypassDnd();//是否可以绕过请勿打扰模式
            mChannel.canShowBadge();//是否可以显示icon角标
            mChannel.enableLights(true);//是否显示通知闪灯
            mChannel.enableVibration(true);//收到小时时震动提示
            mChannel.setBypassDnd(true);//设置绕过免打扰
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            mChannel.setLightColor(Color.RED);//设置闪光灯颜色
            mChannel.getAudioAttributes();//获取设置铃声设置
            mChannel.setVibrationPattern(new long[]{100, 200, 100});//设置震动模式
            mChannel.shouldShowLights();//是否会闪光
            notifyManager.createNotificationChannel(mChannel);
            builder.setChannelId(channel_id);
        } else {
//           低版本不用管理.
        }


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setAutoCancel(true);
        // 什么时候提醒的
        builder.setWhen(System.currentTimeMillis());
        // 设置通知栏的优先级
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        // 设置是否震动等
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test));
        builder.setContentTitle("啦啦啦");
        builder.setSubText("你好啊");
        builder.setContentText("折叠美女");
        builder.setProgress(100, 30, false);
        /**Notification.VISIBILITY_PUBLIC:任何情况都会通知
         * VISIBILITY_PRIVATE:在没有锁屏的情况下通知
         * VISIBILITY_SECRET：在安全锁和没有锁屏的情况下通知
         */
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        expandedView = new RemoteViews(getPackageName(), R.layout.expand_contentview);
        expandedView.setTextViewText(R.id.tv_content, "新内容");

        Intent play = new Intent();
        //设置广播
        play.setAction(MUSIC_NOTIFICATION_ACTION_PLAY);
        play.putExtra("type", "音乐类型");
//        这里发送的是广播
        PendingIntent pplay = PendingIntent.getBroadcast(this, REQUEST_CODE, play, PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.bt_control, pplay);
//        如果设置就出现折叠内容
//        builder.setCustomBigContentView(expandedView);  //设置折叠中的内容
        builder.setCustomContentView(expandedView); //直接设置通知的内容，和上面的setCustomBigContentView()不能同时使用

        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationId = new Random().nextInt(1000);
        notifyManager.notify(notificationId, notification);
    }


    //    直接进行更新通知内容
    private void updateNotification() {
// 设置添加内容
        expandedView.setTextViewText(R.id.tv_content, "新新内容");
        expandedView.setTextColor(R.id.bt_control, Color.parseColor("#ff0000"));
        notifyManager.notify(notificationId, builder.build());
    }

    /**
     * 悬挂通知
     * 思路：应该是setContentIntent()方法设置通知栏的通知，
     * 但是在获取通知的时候也会调用setFullScreenIntent()方法，这时候会触发后进入相应的activity,
     * 这时候将activity设置成弹窗的形式，那么就能实现悬挂的功能,但是在一些手机上不能实现
     */
    private void sendNotification3() {
        Toast.makeText(MainActivity.this, "悬挂通知", Toast.LENGTH_SHORT).show();

        String channel_id = "channel_002";
        String name = "suspensionname";
//        一定要使用NotificationCompat，才会获得提示音和震动的权限，不能直接使用new Notification.Builder()方法。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id);
//        android8.0后要设置channelId，否则就不能进行提示。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channel_id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.canBypassDnd();//是否可以绕过请勿打扰模式
            mChannel.canShowBadge();//是否可以显示icon角标
            mChannel.enableLights(true);//是否显示通知闪灯
            mChannel.enableVibration(true);//收到小时时震动提示
            mChannel.setBypassDnd(true);//设置绕过免打扰
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            mChannel.setLightColor(Color.RED);//设置闪光灯颜色
            mChannel.getAudioAttributes();//获取设置铃声设置
            mChannel.setVibrationPattern(new long[]{100, 200, 100});//设置震动模式
            mChannel.shouldShowLights();//是否会闪光
            notifyManager.createNotificationChannel(mChannel);
            builder.setChannelId(channel_id);
        } else {
//           低版本不用管理.
        }


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setAutoCancel(true);
        // 什么时候提醒的
        builder.setWhen(System.currentTimeMillis());
        // 设置通知栏的优先级
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        // 设置是否震动等
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test));
        builder.setContentTitle("啦啦啦");
        builder.setSubText("你好啊");
        builder.setContentText("美女");
        builder.setProgress(100, 30, false);
        builder.setContentIntent(pendingIntent);

//        设置一个新的悬挂
        Intent intent1 = new Intent(this, NotificationActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setFullScreenIntent(pendingIntent1, true);

        Notification notification = builder.build();
        notifyManager.notify(new Random().nextInt(1000), notification);
    }
}
