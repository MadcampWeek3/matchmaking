package com.example.matchmaking;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MatchChatActivity extends AppCompatActivity {

    private String userid;
    private String roomid;
    private MatchChatRecyclerAdapter matchChatRecyclerAdapter;
    private EditText editText;
    private Button acceptbtn;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_chat);
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
    }

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
