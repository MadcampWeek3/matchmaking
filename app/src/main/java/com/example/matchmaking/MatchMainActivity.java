package com.example.matchmaking;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HEAD;


public class MatchMainActivity extends AppCompatActivity {

    private io.socket.client.Socket mSocket;

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    TextView nicknametxt;
    ImageView tierimg;

    TextView tiertxt;
    TextView positiontxt;
    TextView voicetxt;
    private TextView aboutMetxt;

    TextView amusednum;
    TextView mentalnum;
    TextView leadershipnum;

    private String userid = "";

    User myinfo;

    private final static int EVALUATION_MAX_NUM = 200;

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    private boolean issetted = false;
    private Button match_start_btn;
    private ImageButton settingButton;
    private boolean ismatching = false;


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
        aboutMetxt = findViewById(R.id.match_main_introduce_write);

        amusednum = findViewById(R.id.match_status_num1);
        mentalnum = findViewById(R.id.match_status_num2);
        leadershipnum = findViewById(R.id.match_status_num3);

        progressBar1 = findViewById(R.id.match_status_progress1);
        progressBar2 = findViewById(R.id.match_status_progress2);
        progressBar3 = findViewById(R.id.match_status_progress3);

        progressBar1.setMax(EVALUATION_MAX_NUM);
        progressBar2.setMax(EVALUATION_MAX_NUM);
        progressBar3.setMax(EVALUATION_MAX_NUM);

        RetrofitHelper.getApiService().receiveUser(userid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                myinfo = response.body();
                Log.e("Success",myinfo.getId());
                tierimg.setImageDrawable(gettierimg(myinfo.getTier()));
                nicknametxt.setText(myinfo.getNickname());
                tiertxt.setText(myinfo.getTier());
                positiontxt.setText(myinfo.getPosition());
                voicetxt.setText(myinfo.getVoice());
                aboutMetxt.setText(myinfo.getAboutMe());
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



        settingButton = (ImageButton) findViewById(R.id.match_main_setting);
        match_start_btn = findViewById(R.id.match_main_start);

        match_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(issetted == true) {
                    if(ismatching == false) {
                        try {
                            mSocket = IO.socket("http://192.249.19.251:8780");
                            mSocket.connect();
                            mSocket.on(Socket.EVENT_CONNECT, onMatchStart); //Socket.EVENT_CONNECT : 연결이 성공하면 발생하는 이벤트, onConnect : callback 객체
                            mSocket.on("matchComplete", onMatchComplete);

                            match_start_btn.setText("MATCHING...");
                            match_start_btn.setBackgroundColor(getResources().getColor(R.color.canclecolor));
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                            ismatching = true;
                            settingButton.setClickable(false);
                            settingButton.startAnimation(animation);

                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }else{
                        settingButton.setClickable(true);
                        ismatching = false;
                        settingButton.clearAnimation();
                        match_start_btn.setText("MATCHING START");
                        match_start_btn.setBackgroundColor(getResources().getColor(R.color.MatchButtonColor));
                    }
                }else
                    Toast.makeText(getApplicationContext(),"설정을 완료해주세요.",Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton settingButton = (ImageButton) findViewById(R.id.match_main_setting);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                intent2.putExtra("userId", myinfo.getId());
                startActivityForResult(intent2, 1);
            }
        });

        findViewById(R.id.match_main_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSocket = IO.socket("http://192.249.19.251:8780");
                    mSocket.connect();
                    mSocket.on(Socket.EVENT_CONNECT, onMatchStart);
                    mSocket.on("matchComplete", onMatchComplete);
                } catch(URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            RetrofitHelper.getApiService().receiveUser(myinfo.getId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    myinfo = response.body();
                    //view update
                    tierimg.setImageDrawable(gettierimg(myinfo.getTier()));
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
                    issetted = true;
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("MatchMainActivity", t.toString());
                }
            });
        }
    }

    public void pushMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("수락하시겠습니까?");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    //matching start 버튼 누르면 소켓에 연결하고 User 정보를 보냄
    private Emitter.Listener onMatchStart = new Emitter.Listener() {
        int roomNumber;
        @Override
        public void call(Object... args) {
            if(Numbering.tendency(myinfo.getHope_tendency()) == 2 && Numbering.voice(myinfo.getHope_voice()) == 2){
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 0, 0, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 0, 1, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 1, 0, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 1, 1, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
            }
            else if(Numbering.tendency(myinfo.getHope_tendency()) == 2){
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 0, Numbering.voice(myinfo.getHope_voice()), myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), 1, Numbering.voice(myinfo.getHope_voice()), myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
            }
            else if(Numbering.voice(myinfo.getHope_voice()) == 2){
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), Numbering.tendency(myinfo.getHope_tendency()), 0, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), Numbering.tendency(myinfo.getHope_tendency()), 1, myinfo.getHope_num() - 2);
                sendRoom(roomNumber);

            }
            else {
                roomNumber = Numbering.room(Numbering.tier(myinfo.getTier()), Numbering.tendency(myinfo.getHope_tendency()), Numbering.voice(myinfo.getHope_voice()), myinfo.getHope_num() - 2);
                sendRoom(roomNumber);
            }
        }
    };
    //start match 에서 사용할
    public void sendRoom(int num){
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("userId", myinfo.getId());
        userInfo.addProperty("userPosi", myinfo.getPosition());
        userInfo.addProperty("roomNumber", num);

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(userInfo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("enterRoom", jsonObject);
    }

    //
    private Emitter.Listener onMatchComplete = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(ismatching == true) {
                int check = 0;
                final String receiveData = args[0].toString();
                Log.d("matched", receiveData);
                StringTokenizer st = new StringTokenizer(receiveData, "\"");
                final ArrayList<String> userList = new ArrayList<String>();
                while (st.hasMoreElements()) {
                    String userId_ = st.nextToken();
                    if (userId_.equals("[") || userId_.equals("]") || userId_.equals(",")) continue;
                    if (myinfo.getId().equals(userId_)) check++;
                    userList.add(userId_);
                }
                Log.d("check", check + "");
                Log.d("user", userList.get(0));
                if (check == 0) return;
                Intent intent = new Intent(getApplicationContext(), MatchRoomActivity.class);
                intent.putExtra("userid", myinfo.getId());
                intent.putExtra("roomName", receiveData);
                intent.putStringArrayListExtra("userList", userList);

//            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//            builder.setTitle("안내");
//            builder.setMessage("수락하시겠습니까?");
//            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Intent intent = new Intent(getApplicationContext(), MatchRoomActivity.class);
//                    intent.putExtra("roomName", receiveData);
//                    intent.putStringArrayListExtra("userList", userList);
//                }
//            });
//            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });

                settingButton.clearAnimation();
                settingButton.setClickable(true);
                match_start_btn.setText("MATCHING START");
                match_start_btn.setBackgroundColor(getResources().getColor(R.color.MatchButtonColor));

                ismatching = false;
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }


    @Override
    public void onBackPressed() {
        if(ismatching == false)
            super.onBackPressed();
        else {
            Toast.makeText(getApplicationContext(), "매칭을 취소합니다.", Toast.LENGTH_SHORT).show();
            settingButton.setClickable(true);
            ismatching = false;
            settingButton.clearAnimation();
            match_start_btn.setText("MATCHING START");
            match_start_btn.setBackgroundColor(getResources().getColor(R.color.MatchButtonColor));
        }
    }

    public Drawable gettierimg(String tier){
        switch (tier){
            case "Challenger":
                return getResources().getDrawable(R.drawable.emblem_challenger_128);
            case "GrandMaster":
                return getResources().getDrawable(R.drawable.emblem_grandmaster_128);
            case "Master":
                return getResources().getDrawable(R.drawable.emblem_master_128);
            case "Diamond":
                return getResources().getDrawable(R.drawable.emblem_diamond_128);
            case "Platinum":
                return getResources().getDrawable(R.drawable.emblem_platinum_128);
            case "Gold":
                return getResources().getDrawable(R.drawable.emblem_gold_128);
            case "Silver":
                return getResources().getDrawable(R.drawable.emblem_silver_128);
            case "Bronze":
                return getResources().getDrawable(R.drawable.emblem_bronze_128);
            case "Iron":
                return getResources().getDrawable(R.drawable.emblem_iron_128);
            default:
                return getResources().getDrawable(R.drawable.emblem_iron_128);
        }
    }
}
