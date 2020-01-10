package com.example.matchmaking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SignActivity extends Activity {

    private User newUser = null;

    EditText signId;
    EditText signPw;
    EditText signNic;
    Spinner signTier;
    Spinner signPosi;
    Spinner signVoic;

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

        Button signCompButton = (Button)findViewById(R.id.signComplButton);

        signCompButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signId.getText() == null){
                    Toast.makeText(getApplicationContext(),"ID를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(signPw.getText() == null){
                    Toast.makeText(getApplicationContext(),"PassWord를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(signNic.getText() == null){
                    Toast.makeText(getApplicationContext(),"NickName을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                newUser = new User(signId.getText().toString(), signPw.getText().toString(), signNic.getText().toString(), signTier.getSelectedItem().toString(), signPosi.getSelectedItem().toString(), signVoic.getSelectedItem().toString());

                //server에 회원가입 요청

            }
        });
    }
}
