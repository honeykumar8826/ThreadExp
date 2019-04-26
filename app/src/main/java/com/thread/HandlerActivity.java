package com.thread;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HandlerActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button startDownload,nextPage;
    private Handler handler;
    private final String TAG = "TAG";
    private TextView downloadPercentage;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
//        initialize the id
        inItId();
//          listener for button to start downloading
        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //          handler initialization
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Log.i(TAG, "handleMessage: " + msg.arg1);
                        downloadPercentage.setText(String.valueOf(msg.arg1));
                        progressBar.setProgress(msg.arg1);
                        // startDownload.setText(""+msg.arg2);

                    }
                };
//        thread for updating the ui part
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 1; i<= 100; i++) {
                            Message message = Message.obtain();
                            message.arg1 = i;
                            //message.arg2=i;
                            handler.sendMessage(message);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
//    listener for button to going on a next activity
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandlerActivity.this,AsyncTaskActivity.class);
                startActivity(intent);
            }
        });

    }

    private void inItId() {
        progressBar = findViewById(R.id.progressBar);
        startDownload = findViewById(R.id.btn_start_download);
        downloadPercentage = findViewById(R.id.download_percentage);
        nextPage = findViewById(R.id.btn_next_page);
    }
}
