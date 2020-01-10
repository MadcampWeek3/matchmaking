package com.example.matchmaking;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignActivity extends Activity {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    private User newUser = null;

    EditText signId;
    EditText signPw;
    EditText signNic;
    Spinner signTier;
    Spinner signPosi;
    Spinner signVoic;
    EditText signAboutMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        signId = (EditText)findViewById(R.id.signId);
        signPw = (EditText)findViewById(R.id.signPw);
        signNic = (EditText)findViewById(R.id.signNick);
        signTier = (Spinner)findViewById(R.id.signTier);
        signPosi = (Spinner) findViewById(R.id.signPosi);
        signVoic = (Spinner) findViewById(R.id.signVoic);
        signAboutMe = (EditText) findViewById(R.id.signAboutMe);

        Button signCompButton = (Button)findViewById(R.id.signComplButton);

        signCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signId.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(signPw.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"패스워드를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(signNic.getText().toString().length() == 0){
                    Toast.makeText(getApplicationContext(),"닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                newUser = new User(signId.getText().toString(), signPw.getText().toString(), signNic.getText().toString(), signTier.getSelectedItem().toString(), signPosi.getSelectedItem().toString(), signVoic.getSelectedItem().toString(), signAboutMe.getText().toString());

                //server에 회원가입 요청
                retrofit = new Retrofit.Builder().baseUrl(retrofitInterface.API_URL).addConverterFactory(GsonConverterFactory.create()).build();
                retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<String> comment = retrofitInterface.sendSign(newUser);
                comment.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String response_ = response.body();
                        if(response_.equals("성공")){
                            Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("SignActivity",t.toString());
                    }
                });
            }
        });
    }
}
