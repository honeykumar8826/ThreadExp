package com.thread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "TAG";
    private TextView value;
    private Button start,openToast,nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initialize the id
        inItId();
        //  Log.i(TAG, "onCreate: "+Thread.currentThread().getId());
//        checking the ui thread or main thread diff
  /*  this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: inside ui thread"+Thread.currentThread().getId());
        }
    });*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run:1 " + Thread.currentThread().getId());
            }
        }).start();
//        listener for button to start the count down
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final StringBuilder stringBuilder=new StringBuilder();
                //    loop for stocking the ui part
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(MainActivity.this, "Value ", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "run: "+Thread.currentThread().getId());
                        for (int i = 0; i < 100000; i++) {
                            stringBuilder.append("").append(i);
                        }
                 Log.i(TAG,"abc"+stringBuilder);
                        value.post(new Runnable() {
                            @Override
                            public void run() {
                                value.setText(stringBuilder);
                            }
                        });
                    }

                }).start();

                Log.i(TAG, "onCreate: " + stringBuilder);
            }

        });
//        listener for toast open
        openToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Open Thread", Toast.LENGTH_SHORT).show();
            }
        });
//        listener for going on  next activity
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HandlerActivity.class);
                startActivity(intent);
            }
        });
    }
    private void inItId() {
        value = findViewById(R.id.tv_value);
        start = findViewById(R.id.btn_start);
        openToast = findViewById(R.id.btn_open_toast);
        nextPage = findViewById(R.id.btn_next_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // setContentView(R.layout.activity_main);
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i(TAG, "run:2 " + Thread.currentThread().getId());
            }
        };
        thread.start();
    }

}
