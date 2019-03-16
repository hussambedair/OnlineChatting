package com.example.android.onlinechatting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.onlinechatting.Adapters.ChatRoomsRecyclerAdapter;
import com.example.android.onlinechatting.Base.BaseActivity;
import com.example.android.onlinechatting.FireBaseUtils.DataHolder;
import com.example.android.onlinechatting.FireBaseUtils.Models.Room;
import com.example.android.onlinechatting.FireBaseUtils.RoomsDao;
import com.example.android.onlinechatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    RecyclerView chatRoomsRecyclerView;
    ChatRoomsRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Room> mRooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chatRoomsRecyclerView = findViewById(R.id.rooms_recycler_view);
        adapter = new ChatRoomsRecyclerAdapter(null);

        adapter.setOnRoomItemClickListener(new ChatRoomsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, Room room) {
                DataHolder.currentRoom = room;

                startActivity(new Intent(activity, ChatRoomActivity.class));

            }
        });
        layoutManager = new LinearLayoutManager(activity);

        chatRoomsRecyclerView.setAdapter(adapter);
        chatRoomsRecyclerView.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.add_room);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start a new activity to add the room
                startActivity(new Intent(activity, AddRoomActivity.class));

            }
        });

        RoomsDao.getRoomsBranch()
                .addValueEventListener(roomsValueEventListener);


    }

    ValueEventListener roomsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Room> rooms = new ArrayList<>();
            for (DataSnapshot roomData : dataSnapshot.getChildren()) {
                Room room = roomData.getValue(Room.class);
                rooms.add(room);
            }
            adapter.changeData(rooms);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            showMessage(getString(R.string.error),
                    databaseError.getMessage(), getString(R.string.ok));


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RoomsDao.getRoomsBranch().removeEventListener(roomsValueEventListener);


    }
}
