package com.example.android.onlinechatting.Activities;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.android.onlinechatting.Adapters.ChatMessagesRecyclerAdapter;
import com.example.android.onlinechatting.Base.BaseActivity;
import com.example.android.onlinechatting.FireBaseUtils.DataHolder;
import com.example.android.onlinechatting.FireBaseUtils.MessagesDao;
import com.example.android.onlinechatting.FireBaseUtils.Models.Message;
import com.example.android.onlinechatting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomActivity extends BaseActivity {

    RecyclerView messagesRecyclerView;
    ChatMessagesRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Message> messages;


    ImageButton sendButton;
    EditText message;

    OnSuccessListener onMessageSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            message.setText("");

        }
    };

    OnFailureListener onMessageFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            showMessage(getString(R.string.error), e.getLocalizedMessage(), getString(R.string.ok));

        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Message myMessage = new Message();

            String messageContent = message.getText().toString();
            myMessage.setContent(messageContent);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            String date = simpleDateFormat.format(new Date());
            myMessage.setSentAt(date);

            myMessage.setSenderId(DataHolder.currentUser.getId());

            String senderName = DataHolder.currentUser.getUserName();
            myMessage.setSenderName(senderName);

            myMessage.setRoomId(DataHolder.currentRoom.getId());

            MessagesDao.addNewMessage(myMessage,
                    onMessageSuccessListener,
                    onMessageFailureListener);

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();


        messagesRecyclerView = findViewById(R.id.messages_recycler_view);
        adapter = new ChatMessagesRecyclerAdapter(null, DataHolder.currentUser.getId());


        layoutManager = new LinearLayoutManager(activity);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

        messagesRecyclerView.setAdapter(adapter);
        messagesRecyclerView.setLayoutManager(layoutManager);

        query = MessagesDao.getMessagesByRoomId(DataHolder.currentRoom.getId());
        query.addChildEventListener(childEventListener);


        sendButton.setOnClickListener(onClickListener);




    }

    Query query;

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Message message = dataSnapshot.getValue(Message.class);
            adapter.insertNewItem(message);

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



    public void initViews() {

        sendButton = findViewById(R.id.send_button);
        message = findViewById(R.id.message_edit_text);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.removeEventListener(childEventListener);
    }
}
