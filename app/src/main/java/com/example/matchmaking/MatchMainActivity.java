package com.example.matchmaking;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MatchMainActivity extends AppCompatActivity{

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_main);

        progressBar1 = findViewById(R.id.match_status_progress1);
        progressBar2 = findViewById(R.id.match_status_progress2);
        progressBar3 = findViewById(R.id.match_status_progress3);

        progressBar2.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.MULTIPLY);
        progressBar3.getIndeterminateDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);


        runOnUiThread(new ProgressBarRunnable(progressBar1,0,100));
        runOnUiThread(new ProgressBarRunnable(progressBar2,0,100));
        runOnUiThread(new ProgressBarRunnable(progressBar3,0,100));


    }


}
