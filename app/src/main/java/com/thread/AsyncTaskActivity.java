package com.thread;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncTaskActivity extends AppCompatActivity {
    private final String TAG = "TAG";
    private ProgressBar progressBar;
    private Button asyncTask, cancel,next;
    private TextView downloadPercentage;
    private BackgroundTask backgroundTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

//        initialize the id
        inItId();
//    listener to start the asyncTask
        asyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backgroundTask == null) {
                    backgroundTask = new BackgroundTask();
                    Log.i(TAG, "onClick: '");
                    backgroundTask.execute(100);

                } else if (backgroundTask.getStatus() == AsyncTask.Status.FINISHED) {
                    Log.i(TAG, "onClick: inside else if1");
                    backgroundTask = new BackgroundTask();
                    backgroundTask.execute(100);
                } else if (backgroundTask.getStatus() == AsyncTask.Status.RUNNING && !backgroundTask.getPause()) {
                    Log.i(TAG, "onClick: inside else if 2");
                    backgroundTask.pauseMyTask();
                } else {
                    Log.i(TAG, "onClick:inside else ");
                    backgroundTask.wakeUpTask();
                }
            }
        });
//        listener to go on a next activity
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AsyncTaskActivity.this,ThreadExecutor.class);
                startActivity(intent);
            }
        });
    }

    private void inItId() {
        progressBar = findViewById(R.id.progressBar);
        downloadPercentage = findViewById(R.id.download_percentage);
        asyncTask = findViewById(R.id.btn_async_task);
        cancel = findViewById(R.id.btn_cancel);
        next = findViewById(R.id.btn_thread_poll_activity);
    }

    //    async class for download the content
    class BackgroundTask extends AsyncTask<Integer, Integer, String> {
        StringBuilder stringBuilder;
        boolean resume = true;
        boolean pause = false;
        private String WATCH_DOG = "barabbas";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "onPreExecute: ");
            //        listener to stop the downloading
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel(true);
                }
            });
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int counter = 0;
            stringBuilder = new StringBuilder();
            Log.i(TAG, "doInBackground: " + integers[0]);
            while (resume) {
                if (!isCancelled()) {
                    publishProgress(counter);
                    stringBuilder.append(counter);
                    try {
                        resume = (counter++ == integers[0]) ? false : true;
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (pause) {
                    synchronized (WATCH_DOG){
                        Log.i(TAG, "doInBackground: inside the pause");
                        publishProgress(-1);
                        try {

                            WATCH_DOG.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        pause = false;
                    }

                }
            }

        /*    for(int i=1;i<=100;i++)
            {
                if(!isCancelled())
                {
                    publishProgress(i);
                    stringBuilder.append(i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Log.i(TAG, "doInBackground: inside else");

            }*/
            return stringBuilder.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "onProgressUpdate: ");
            downloadPercentage.setText((String.valueOf(values[0])));
            progressBar.setProgress(values[0]);
            if (values[0] == -1) {
                downloadPercentage.setText("paused");
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i(TAG, "onPostExecute: " + s);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i(TAG, "onCancelled: ");
        }

        /**
         * Get a loop-flag
         */
        public boolean getPause() {
            return pause;
        }

        /**
         * Pause task for a while
         */
        public void pauseMyTask() {
            pause = true;
        }

        /**
         * Wake up task from sleeping
         */
        public void wakeUpTask() {
            synchronized (WATCH_DOG) {
                WATCH_DOG.notify();
            }
        }
    }
}


