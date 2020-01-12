package com.example.matchmaking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    private User user;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_main);

        Intent intent1 = getIntent();
        userId = intent1.getExtras().getString("userId");

        retrofit = new Retrofit.Builder().baseUrl(retrofitInterface.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<User> comment = retrofitInterface.receiveUser(userId);
        comment.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("MatchMainActivity", t.toString());
            }
        });




        progressBar1 = findViewById(R.id.match_status_progress1);
        progressBar2 = findViewById(R.id.match_status_progress2);
        progressBar3 = findViewById(R.id.match_status_progress3);

        progressBar2.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.MULTIPLY);
        progressBar3.getIndeterminateDrawable().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);


        runOnUiThread(new ProgressBarRunnable(progressBar1, 0, 100));
        runOnUiThread(new ProgressBarRunnable(progressBar2, 0, 100));
        runOnUiThread(new ProgressBarRunnable(progressBar3, 0, 100));


        ImageButton settingButton = (ImageButton) findViewById(R.id.match_main_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                intent2.putExtra("userNick", user.getNickname());
                intent2.putExtra("userTier", user.getTier());
                intent2.putExtra("userPosi", user.getPosition());
                intent2.putExtra("userVoic", user.getVoice());
                intent2.putExtra("userAboutMe", user.getAboutMe());
                startActivityForResult(intent2, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            user.setNickname(data.getStringExtra("userNick"));
            user.setTier(data.getStringExtra("userTier"));
            user.setPosition(data.getStringExtra("userPosi"));
            user.setVoice(data.getStringExtra("userVoic"));
            user.setAboutMe(data.getStringExtra("userAboutMe"));
            user.setHope_tendency(data.getStringExtra("userHope_tendency"));
            user.setHope_voice(data.getStringExtra("userHope_voice"));
            user.setHope_num(Integer.parseInt(data.getStringExtra("userHope_num")));
        }
    }
}
