package com.qianfeng.hyh.day23media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button buttonplay,buttonstop;
    Intent intent;
    private  static boolean isPlay=false;
    TextView timeText;
    SeekBar seekBar;
    private  int progress;
    TimeReceiver timeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        buttonplay= (Button) findViewById(R.id.buttonplay);
        buttonstop= (Button) findViewById(R.id.buttonstop);
        timeText= (TextView) findViewById(R.id.timeId);
        seekBar= (SeekBar) findViewById(R.id.seekId);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                 progress=seekBar.getProgress();
                Intent intentProgress=new Intent("com.qianfeng.progress");
                intentProgress.putExtra("progress",progress);
                sendBroadcast(intentProgress);
            }
        });
        intent=new Intent("com.qianfeng.music");
         timeReceiver=new TimeReceiver();
         registerReceiver(timeReceiver,new IntentFilter("com.qianfeng.time"));

    }
    public void playMusic(View view){
        startService(intent);
        isPlay=!isPlay;
        if (isPlay){
            buttonplay.setText("暂停");
        }else{
            buttonplay.setText("播放");
        }
    }
    public void stopMusic(View view){
        stopService(intent);
         isPlay=false;
        buttonplay.setText("播放");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
    }

    class TimeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int cur=intent.getExtras().getInt("cur");
            int max=intent.getExtras().getInt("max");
            seekBar.setProgress(cur);
            seekBar.setMax(max);
            int m=cur/1000/60;
            int s=cur/1000%60;
            int _m=max/1000/60;
            int _s=max/1000%60;
            StringBuilder builder=new StringBuilder();
            builder.append(m).append(":").append(s).append("/").append(_m).append(":").append(_s);
            timeText.setText(builder.toString());
        }
    }
}
