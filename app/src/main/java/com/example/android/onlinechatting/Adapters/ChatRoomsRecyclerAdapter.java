package com.example.android.onlinechatting.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.onlinechatting.FireBaseUtils.Models.Room;
import com.example.android.onlinechatting.R;

import java.util.List;

public class ChatRoomsRecyclerAdapter extends RecyclerView.Adapter<ChatRoomsRecyclerAdapter.ViewHolder> {


    List <Room> rooms;

    OnItemClickListener onRoomItemClickListener;

    public void setOnRoomItemClickListener(OnItemClickListener onRoomItemClickListener) {
        this.onRoomItemClickListener = onRoomItemClickListener;
    }

    public ChatRoomsRecyclerAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from (viewGroup.getContext())
                .inflate(R.layout.item_chat_room, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Room room = rooms.get(position);

        viewHolder.roomName.setText(room.getName());
        viewHolder.roomDescription.setText(room.getDiscreption());

        if (onRoomItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRoomItemClickListener.onItemClick(position,room);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (rooms == null) {
            return 0;
        }
        return rooms.size();
    }

    public void changeData (List <Room> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView roomName;
        TextView roomDescription;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.roomName_text_view);
            roomDescription = itemView.findViewById(R.id.roomDescription_text_view);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int pos, Room room);
    }

}
