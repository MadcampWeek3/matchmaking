package com.example.matchmaking;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchRoomActivity extends AppCompatActivity {

    private String userid;
    private MatchRoomRecyclerAdapter matchRoomRecyclerAdapter;
    private RecyclerView recyclerView;
    private MatchChatRecyclerAdapter matchChatRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_room);

        recyclerView = findViewById(R.id.match_room_recycler);
        matchRoomRecyclerAdapter = new MatchRoomRecyclerAdapter(getApplicationContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(matchRoomRecyclerAdapter);

        User user1 = new User("One","One","Master","미드","가능");
        User user2 = new User("Two","Two","Master","정글","가능");
        User user3 = new User("Three","Three","Master","서폿","가능");
        User user4 = new User("Four","Four","Master","원딜","가능");
        User user5 = new User("Five","Five","Master","탑","가능");

        matchRoomRecyclerAdapter.additem(user1);
        matchRoomRecyclerAdapter.additem(user2);
        matchRoomRecyclerAdapter.additem(user3);
        matchRoomRecyclerAdapter.additem(user4);
        matchRoomRecyclerAdapter.additem(user5);


        userid = getIntent().getStringExtra("userid");
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

        RecyclerView chatrecyclerView = findViewById(R.id.match_room_chat_recycler);
        matchChatRecyclerAdapter = new MatchChatRecyclerAdapter(userid,"0");

        chatrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatrecyclerView.setAdapter(matchChatRecyclerAdapter);

        matchChatRecyclerAdapter.additem("ㅎㅇㅎㅇ","16:20");
    }
}
