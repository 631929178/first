package com.qianfeng.hyh.day23media;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 16-8-24.
 */
public class MediaPlayService extends Service {
    MediaPlayer mediaPlayer;
    ProgressReceiver progressReceiver;
    boolean isPlaying=false;
    TimeThread timeThread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建media对象
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.p1);
        progressReceiver = new ProgressReceiver();
        registerReceiver(progressReceiver, new IntentFilter("com.qianfeng.progress"));
        timeThread=new TimeThread();
        mediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (isPlaying) {
            mediaPlayer.pause();

        } else {
            mediaPlayer.start();
            timeThread.start();
        }
        isPlaying=!isPlaying;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        timeThread.interrupt();
        mediaPlayer.release();
        unregisterReceiver(progressReceiver);
    }

    class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.qianfeng.progress")) {
                int progress = intent.getExtras().getInt("progress");
                Log.d("hyh", "seekto__" + progress);
                mediaPlayer.seekTo(progress);
            }
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            while (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
                int cur = mediaPlayer.getCurrentPosition();
                int max = mediaPlayer.getDuration();
                Intent intent = new Intent("com.qianfeng.time");
                intent.putExtra("cur", cur);
                intent.putExtra("max", max);
                sendBroadcast(intent);
            }
        }
    }
}
