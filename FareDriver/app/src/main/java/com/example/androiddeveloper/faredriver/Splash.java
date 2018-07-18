package com.example.androiddeveloper.faredriver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context c;
    TextView textloader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        c = this;
        textloader=(TextView)findViewById(R.id.dot);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Handler handler = new Handler();

            for (int i = 100; i <= 3900; i=i+50) {
                final int finalI = i;
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(finalI %600 == 0){
                            textloader.setText(".");
                        }else if(finalI %400 == 0){
                            textloader.setText("..");
                        }else if(finalI %200 == 0){
                            textloader.setText("...");
                        }
                    }
                }, i);
            }

        }
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(Splash.this, MapsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        };
        timerThread.start();
    }
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        moveTaskToBack(true);
    }

}
