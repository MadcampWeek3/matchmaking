package com.example.matchmaking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MatchMainActivity extends AppCompatActivity {

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    TextView nicknametxt;
    ImageView tierimg;

    TextView tiertxt;
    TextView positiontxt;
    TextView voicetxt;

    TextView amusednum;
    TextView mentalnum;
    TextView leadershipnum;

    private String userid = "";

    User myinfo;

    private final static int EVALUATION_MAX_NUM = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_main);

        userid = getIntent().getStringExtra("userId");

        nicknametxt = findViewById(R.id.match_main_id);
        tierimg = findViewById(R.id.match_main_tier_img);

        tiertxt = findViewById(R.id.match_main_tier_write);
        positiontxt = findViewById(R.id.match_main_position_write);
        voicetxt = findViewById(R.id.match_main_voice_write);

        amusednum = findViewById(R.id.match_status_num1);
        mentalnum = findViewById(R.id.match_status_num2);
        leadershipnum = findViewById(R.id.match_status_num3);

        progressBar1 = findViewById(R.id.match_status_progress1);
        progressBar2 = findViewById(R.id.match_status_progress2);
        progressBar3 = findViewById(R.id.match_status_progress3);

        progressBar1.setMax(EVALUATION_MAX_NUM);
        progressBar2.setMax(EVALUATION_MAX_NUM);
        progressBar3.setMax(EVALUATION_MAX_NUM);

        findViewById(R.id.match_main_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MatchRoomActivity.class);
                intent.putExtra("userid",userid);
                startActivity(intent);
            }
        });

        RetrofitHelper.getApiService().receiveUser(userid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                myinfo = response.body();
                Log.e("Success",myinfo.getId());
                nicknametxt.setText(myinfo.getId());
                tiertxt.setText(myinfo.getTier());
                positiontxt.setText(myinfo.getPosition());
                voicetxt.setText(myinfo.getVoice());
                amusednum.setText(Integer.toString(myinfo.getUserEval().getAmused()));
                mentalnum.setText(Integer.toString(myinfo.getUserEval().getMental()));
                leadershipnum.setText(Integer.toString(myinfo.getUserEval().getLeadership()));

                runOnUiThread(new ProgressBarRunnable(progressBar1, 0, myinfo.getUserEval().getAmused()));
                runOnUiThread(new ProgressBarRunnable(progressBar2, 0, myinfo.getUserEval().getMental()));
                runOnUiThread(new ProgressBarRunnable(progressBar3, 0, myinfo.getUserEval().getLeadership()));
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Get Failed",t.getMessage());
            }
        });
    }
}
