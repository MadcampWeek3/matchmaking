package com.example.matchmaking;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchEvaluationActivity extends AppCompatActivity {

    private MatchEvaluationRecyclerAdapter matchEvaluationRecyclerAdapter;
    private ArrayList<String> userlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_evaluation);

        userlist = getIntent().getStringArrayListExtra("userlist");

        matchEvaluationRecyclerAdapter = new MatchEvaluationRecyclerAdapter(userlist,this);

        RecyclerView evaluation_recyclerview = findViewById(R.id.match_eval_recycler);
        evaluation_recyclerview.setAdapter(matchEvaluationRecyclerAdapter);
        evaluation_recyclerview.setLayoutManager(new LinearLayoutManager(this));


    }
}
