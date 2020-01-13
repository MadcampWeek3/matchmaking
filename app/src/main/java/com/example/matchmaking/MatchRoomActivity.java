package com.example.matchmaking;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchRoomActivity extends AppCompatActivity {

    private String userid;
    private String roomid;
    private MatchRoomRecyclerAdapter matchRoomRecyclerAdapter;
    private RecyclerView recyclerView;
    private MatchChatRecyclerAdapter matchChatRecyclerAdapter;
    private RecyclerView chatrecyclerView;
    private Button chatbtn;
    private Activity activity;
    private Boolean isready = false;
    private Button readybtn;
    private String userlist[];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_room);

        activity = this;


        userid = getIntent().getStringExtra("userid");
        roomid = "0";

        //room
        matchRoomRecyclerAdapter = new MatchRoomRecyclerAdapter(getApplicationContext());
        matchRoomRecyclerAdapter.setOnUserClickListener(new MatchRoomRecyclerAdapter.OnUserClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(),MatchUserInfoActivity.class);
                intent.putExtra("someoneid",matchRoomRecyclerAdapter.getUserlist().get(position).getId());
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.match_room_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(matchRoomRecyclerAdapter);

        //chat
        matchChatRecyclerAdapter = new MatchChatRecyclerAdapter(userid,roomid);
        matchChatRecyclerAdapter.additem("ㅎㅇㅎㅇ","16:20");
        matchChatRecyclerAdapter.additem("ㄱㄱ?","16:20");

        chatrecyclerView = findViewById(R.id.match_room_chat_recycler);
        chatrecyclerView.setClickable(true);
        chatrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatrecyclerView.setAdapter(matchChatRecyclerAdapter);



        User user1 = new User("One","One","Master","미드","가능");
        User user2 = new User("Two","Two","Master","정글","가능");
        User user3 = new User("Three","Three","Master","서폿","가능");
        User user4 = new User("Four","Four","Master","원딜","가능");
        //User user5 = new User("Five","Five","Master","탑","가능");

        matchRoomRecyclerAdapter.additem(user1);
        matchRoomRecyclerAdapter.additem(user2);
        matchRoomRecyclerAdapter.additem(user3);
        matchRoomRecyclerAdapter.additem(user4);
        //matchRoomRecyclerAdapter.additem(user5);


        RetrofitHelper.getApiService().receiveUser(userid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                matchRoomRecyclerAdapter.additem(user);
                matchRoomRecyclerAdapter.notifyDataSetChanged();
                recyclerView.invalidateItemDecorations();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("MatchRoomActivity",t.getMessage());
            }
        });

        chatbtn = findViewById(R.id.match_room_chat_btn);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MatchChatActivity.class);
                intent.putExtra("userid",userid);
                intent.putExtra("roomid",roomid);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,chatbtn,"custom_transition1");
                startActivity(intent,options.toBundle());
            }
        });

        readybtn = findViewById(R.id.match_room_ready);
        readybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //소켓에 신호
                if(isready == false) {
                    v.animate().translationY(-500);
                    for(int i = 0; i < matchRoomRecyclerAdapter.getUserlist().size(); i++){
                        if(!matchRoomRecyclerAdapter.getUserlist().get(i).getId().equals(userid))
                            continue;
                        matchRoomRecyclerAdapter.getUserlist().get(i).setTierimg(false);
                        matchRoomRecyclerAdapter.notifyDataSetChanged();
                        recyclerView.invalidateItemDecorations();
                    }
                    isready = true;
                }
            }
        });

        findViewById(R.id.match_room_ready_cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isready == true) {
                    readybtn.animate().translationY(0);
                    for(int i = 0; i < matchRoomRecyclerAdapter.getUserlist().size(); i++){
                        if(!matchRoomRecyclerAdapter.getUserlist().get(i).getId().equals(userid))
                            continue;
                        matchRoomRecyclerAdapter.getUserlist().get(i).setTierimg(true);
                        matchRoomRecyclerAdapter.notifyDataSetChanged();
                        recyclerView.invalidateItemDecorations();
                    }
                    isready = false;
                }
            }
        });
    }
}
