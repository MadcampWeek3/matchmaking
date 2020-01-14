package com.example.matchmaking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MatchEvaluationRecyclerAdapter extends RecyclerView.Adapter<MatchEvaluationRecyclerAdapter.ViewHolder> {

    private ArrayList<String> recyclerItems = new ArrayList<>();
    private Context mContext;

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public MatchEvaluationRecyclerAdapter(ArrayList<String> userlist, Context context){
        for(int i = 0; i < userlist.size(); i++)
            recyclerItems.add(userlist.get(i));
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.match_evaluation_item,parent,false);
        MatchEvaluationRecyclerAdapter.ViewHolder tvh = new MatchEvaluationRecyclerAdapter.ViewHolder(view);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchEvaluationRecyclerAdapter.ViewHolder holder, int position) {
        holder.nickname.setText(recyclerItems.get(position));
        holder.amusedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItems.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.mentalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItems.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.leadershipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItems.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button amusedbtn;
        Button mentalbtn;
        Button leadershipbtn;
        TextView nickname;

        ViewHolder(View itemView){
            super(itemView);
            nickname = itemView.findViewById(R.id.match_eval_nickname);
            amusedbtn = itemView.findViewById(R.id.match_eval_amused);
            mentalbtn = itemView.findViewById(R.id.match_eval_mental);
            leadershipbtn = itemView.findViewById(R.id.match_eval_leadership);
        }
    }
}
