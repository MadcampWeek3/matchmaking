package com.example.matchmaking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchChatActivity extends AppCompatActivity {

    private String userid;
    private String roomid;
    private MatchChatRecyclerAdapter matchChatRecyclerAdapter;
    private EditText editText;
    private Button acceptbtn;
    private RecyclerView recyclerView;
    private Socket mSocket;
    private User user;
    private boolean response_catch = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_chat);

        RetrofitHelper.getApiService().receiveUser(userid).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                response_catch = true;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ChatError",t.getMessage());
            }
        });

        try {
            mSocket = IO.socket("http://192.249.19.251:9980");
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT,onChatMake);
            mSocket.on("arrivedchat:roomid", onChatArrived);
        } catch (Exception e) {
            e.printStackTrace();
        }

        acceptbtn = findViewById(R.id.match_chat_acceptbtn);
        editText = findViewById(R.id.match_chat_edittxt);

        userid = getIntent().getStringExtra("userid");
        roomid = getIntent().getStringExtra("roomid");

        matchChatRecyclerAdapter = new MatchChatRecyclerAdapter(userid,roomid);

        recyclerView = findViewById(R.id.match_chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(matchChatRecyclerAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edit_check();
            }
        });

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (!response_catch);
            }
        });
    }

    private Emitter.Listener onChatArrived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    private Emitter.Listener onChatMake = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            JsonObject chatinfo = new JsonObject();
            chatinfo.addProperty("roomid",roomid);

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(chatinfo.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("EMIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIT");
            mSocket.emit("MakeChatRoom", jsonObject);
        }
    };


    private void edit_check() {
        if(editText.getText().toString().trim().length() == 0){
            acceptbtn.setVisibility(View.INVISIBLE);
            acceptbtn.setEnabled(false);
        }else{
            acceptbtn.setVisibility(View.VISIBLE);
            acceptbtn.setEnabled(true);
        }
    }
}
