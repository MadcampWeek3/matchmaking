package com.example.matchmaking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.List;
import java.util.StringTokenizer;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

    TextView amusednum;
    TextView mentalnum;
    TextView leadershipnum;

    private String userid = "";

    User myinfo;

    private final static int EVALUATION_MAX_NUM = 1000;

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    private User user;
    private String userId;

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

        progressBar1.setMax(EVALUATION_MAX_NUM);
        progressBar2.setMax(EVALUATION_MAX_NUM);
        progressBar3.setMax(EVALUATION_MAX_NUM);

        findViewById(R.id.match_main_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSocket = IO.socket("http://192.249.19.251:9180");
                    mSocket.connect();
                    mSocket.on(Socket.EVENT_CONNECT, onMatchStart); //Socket.EVENT_CONNECT : 연결이 성공하면 발생하는 이벤트, onConnect : callback 객체
                    mSocket.on("matchComplete", onMatchComplete);
                } catch(URISyntaxException e) {
                    e.printStackTrace();
                }
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
        ImageButton settingButton = (ImageButton) findViewById(R.id.match_main_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), SettingActivity.class);
                intent2.putExtra("userId", user.getId());
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
            RetrofitHelper.getApiService().receiveUser(userid).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    myinfo = response.body();
                    //view update
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
            if(Numbering.tendency(user.getHope_tendency()) == 2 && Numbering.voice(user.getHope_voice()) == 2){
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 0, 0, user.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 0, 1, user.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 1, 0, user.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 1, 1, user.getHope_num() - 2);
                sendRoom(roomNumber);
            }
            else if(Numbering.tendency(user.getHope_tendency()) == 2){
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 0, Numbering.voice(user.getHope_voice()), user.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), 1, Numbering.voice(user.getHope_voice()), user.getHope_num() - 2);
                sendRoom(roomNumber);
            }
            else if(Numbering.voice(user.getHope_voice()) == 2){
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), Numbering.tendency(user.getHope_tendency()), 0, user.getHope_num() - 2);
                sendRoom(roomNumber);
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), Numbering.tendency(user.getHope_tendency()), 1, user.getHope_num() - 2);
                sendRoom(roomNumber);

            }
            else {
                roomNumber = Numbering.room(Numbering.tier(user.getTier()), Numbering.tendency(user.getHope_tendency()), Numbering.voice(user.getHope_voice()), user.getHope_num() - 2);
                sendRoom(roomNumber);
            }
        }
    };
    //start match 에서 사용할
    public void sendRoom(int num){
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("userId", user.getId());
        userInfo.addProperty("userPosi", user.getPosition());
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
            int check = 0;
            final String receiveData = args[0].toString();
            Log.d("matched", receiveData);
            StringTokenizer st = new StringTokenizer(receiveData, "\"");
            final ArrayList<String> userList = new ArrayList<String>();
            while(st.hasMoreElements()){
                String userId_ = st.nextToken();
                if(userId_.equals("[") || userId_.equals("]") || userId_.equals(",")) continue;
                if(user.getId().equals(userId_)) check++;
                userList.add(userId_);
            }
            Log.d("check", check+"");
            if(check == 0) return;
            Intent intent = new Intent(getApplicationContext(), MatchRoomActivity.class);
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
        }
    };
}
