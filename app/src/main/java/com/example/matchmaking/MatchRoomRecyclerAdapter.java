package com.example.matchmaking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MatchRoomRecyclerAdapter extends RecyclerView.Adapter<MatchRoomRecyclerAdapter.ViewHolder> {

    private ArrayList<MatchRoomRecyclerItem> userlist = new ArrayList<>();
    private Context mContext;

    public MatchRoomRecyclerAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public MatchRoomRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.match_room_item,parent,false);
        MatchRoomRecyclerAdapter.ViewHolder tvh = new MatchRoomRecyclerAdapter.ViewHolder(view);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRoomRecyclerAdapter.ViewHolder holder, int position) {
        MatchRoomRecyclerItem item = userlist.get(position);
        holder.tierimg.setImageDrawable(mContext.getDrawable(R.drawable.platinum_1_36));
        holder.nickname.setText(item.getNickname());
        holder.tiertxt.setText(item.getTiertxt());
        holder.positiontxt.setText(item.getPositiontxt());
        holder.voice.setText(item.getVoice());
    }


    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView tierimg;
        private TextView nickname;
        private TextView tiertxt;
        private TextView positiontxt;
        private TextView voice;

        ViewHolder(View itemView){
            super(itemView);
            tierimg = itemView.findViewById(R.id.match_room_item_tier_img);
            nickname = itemView.findViewById(R.id.match_room_item_id);
            tiertxt = itemView.findViewById(R.id.match_room_item_tier_write);
            positiontxt = itemView.findViewById(R.id.match_room_item_position_write);
            voice = itemView.findViewById(R.id.match_room_item_voice_write);
        }
    }

    public void additem(User user){
        userlist.add(new MatchRoomRecyclerItem(user.getId(),user.getTier(),user.getPosition(),user.getVoice()));
    }
}