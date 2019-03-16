package com.example.android.onlinechatting.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.onlinechatting.FireBaseUtils.Models.Message;
import com.example.android.onlinechatting.FireBaseUtils.Models.Room;
import com.example.android.onlinechatting.R;

import java.util.ArrayList;
import java.util.List;

public class ChatMessagesRecyclerAdapter extends RecyclerView.Adapter<ChatMessagesRecyclerAdapter.ViewHolder> {


    List <Message> messages;
    String userId;



    public ChatMessagesRecyclerAdapter(List<Message> messages, String userId) {
        this.messages = messages;
        this.userId = userId;
    }

    final int outgoing = 1 ;
    final int incoming = 2 ;

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if (message.getSenderId().equals(userId)) {
            return outgoing;
        }
        return incoming;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;

        if (viewType== incoming) {
            view = LayoutInflater.from (viewGroup.getContext())
                    .inflate(R.layout.item_message_incoming, viewGroup,false);
            return new IncomingViewHolder(view);


        } else if (viewType == outgoing) {
            view = LayoutInflater.from (viewGroup.getContext())
                    .inflate(R.layout.item_message_outgoing, viewGroup,false);
            return new ViewHolder(view);

        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        Message message = messages.get(position);
        viewHolder.messageContent.setText(message.getContent());
        viewHolder.messageDate.setText(message.getSentAt());

        if (viewType == incoming) {

            ((IncomingViewHolder) viewHolder).senderName.setText(message.getSenderName());

        }

    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        }
        return messages.size();
    }

    public void changeData (List <Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();

    }

    public void insertNewItem (Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);

        notifyItemInserted(messages.size()-1); // index of the last added item


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageContent;
        TextView messageDate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageContent = itemView.findViewById(R.id.message);
            messageDate = itemView.findViewById(R.id.date);
        }
    }


    class IncomingViewHolder extends ViewHolder {

        TextView senderName;

        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name_text_view);
        }
    }

}
