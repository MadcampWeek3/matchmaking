package com.example.matchmaking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Intent intent1 = getIntent();
        String userNick = intent1.getExtras().getString("userNick");
        String userTier = intent1.getExtras().getString("userTier");
        String userPosi = intent1.getExtras().getString("userPosi");
        String userVoic = intent1.getExtras().getString("userVoic");
        final String userAboutMe = intent1.getExtras().getString("userAboutMe");

        final EditText userNick_e = (EditText)findViewById(R.id.setNick);
        final Spinner userTier_s = (Spinner)findViewById(R.id.setTier);
        final Spinner userPosi_s = (Spinner)findViewById(R.id.setPosi);
        final Spinner userVoic_s = (Spinner)findViewById(R.id.setVoic);
        final EditText userAboutMe_e = (EditText)findViewById(R.id.setAboutMe);

        final Spinner hope_tendency_s = (Spinner)findViewById(R.id.setHope_voice);
        final Spinner hope_voice_s = (Spinner)findViewById(R.id.setHope_voice);
        final Spinner hope_num_s = (Spinner)findViewById(R.id.setHope_Num);

        userNick_e.setText(userNick);
        if(userTier.equals("Challenger")) userTier_s.setSelection(0);
        else if(userTier.equals("grandMaster")) userTier_s.setSelection(1);
        else if(userTier.equals("Master")) userTier_s.setSelection(2);
        else if(userTier.equals("Diamond")) userTier_s.setSelection(3);
        else if(userTier.equals("Platinum")) userTier_s.setSelection(4);
        else if(userTier.equals("Gold")) userTier_s.setSelection(5);
        else if(userTier.equals("Silver")) userTier_s.setSelection(6);
        else if(userTier.equals("Bronze")) userTier_s.setSelection(7);
        else if(userTier.equals("Iron")) userTier_s.setSelection(8);
        if(userPosi.equals("탑")) userPosi_s.setSelection(0);
        else if(userPosi.equals("정글")) userPosi_s.setSelection(1);
        else if(userPosi.equals("미드")) userPosi_s.setSelection(2);
        else if(userPosi.equals("바텀")) userPosi_s.setSelection(3);
        else if(userPosi.equals("서포터")) userPosi_s.setSelection(4);
        if(userVoic.equals("가능")) userVoic_s.setSelection(0);
        else if(userVoic.equals("불가능")) userVoic_s.setSelection(1);
        else if(userVoic.equals("상관없음")) userVoic_s.setSelection(2);
        userAboutMe_e.setText(userAboutMe);

        Button settingComplete = (Button)findViewById(R.id.settingComplete);
        settingComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userNick_e.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),"아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("userNick", userNick_e.getText().toString());
                intent.putExtra("userTier", userTier_s.getSelectedItem().toString());
                intent.putExtra("userPosi", userPosi_s.getSelectedItem().toString());
                intent.putExtra("userVoic", userVoic_s.getSelectedItem().toString());
                intent.putExtra("userAboutMe", userAboutMe_e.getText().toString());
                intent.putExtra("userHope_tendency", hope_tendency_s.getSelectedItem().toString());
                intent.putExtra("userHope_voice", hope_voice_s.getSelectedItem().toString());
                intent.putExtra("userHope_num", hope_num_s.getSelectedItem().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //바탕 클릭시 키보드 내리기
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.settingConst);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userNick_e.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(userAboutMe_e.getWindowToken(), 0);
            }
        });
    }
}
