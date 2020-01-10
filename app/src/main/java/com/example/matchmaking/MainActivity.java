package com.example.matchmaking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText loginId;
    EditText loginPw;

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginId = (EditText)findViewById(R.id.loginId);
        loginPw = (EditText)findViewById(R.id.loginPw);

        final Button loginButton = (Button)findViewById(R.id.loginButton);
        Button signButton = (Button)findViewById(R.id.signButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginId.getText() == null){
                    Toast.makeText(getApplicationContext(),"아이디 입력",Toast.LENGTH_SHORT).show();
                    return;
                }
                else (loginPw.getText() == null){
                    Toast.makeText(getApplicationContext(),"비밀번호 입력",Toast.LENGTH_SHORT).show();
                    return;
                }
                //server에 id/pw 보내고 확인받기
                retrofit = new Retrofit.Builder().baseUrl(retrofitInterface.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<String> comment = retrofitInterface.sendLogin(loginId.getText().toString(), loginPw.getText().toString());
                comment.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String response_ = response.body();
                        if(response_.equals("성공")){
                            Intent intent = new Intent(getApplicationContext(), MatchMainActivity.class);
                            intent.putExtra("userId",loginId.getText().toString());
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("MainActivity", t.toString());
                    }
                });


            }
        });

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
            }
        });
    }
}
