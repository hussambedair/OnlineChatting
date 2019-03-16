package com.example.android.onlinechatting.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.onlinechatting.Base.BaseActivity;
import com.example.android.onlinechatting.FireBaseUtils.Models.Room;
import com.example.android.onlinechatting.FireBaseUtils.RoomsDao;
import com.example.android.onlinechatting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRoomActivity extends BaseActivity {

    TextInputLayout roomName;
    TextInputLayout roomDescription;
    Button addRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        initViews();

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sRoomName = roomName.getEditText().getText().toString();
                String sRoomDescription = roomDescription.getEditText().getText().toString();

                Room room = new Room();
                room.setName(sRoomName);
                room.setDiscreption(sRoomDescription);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy  hh:mm:ss");
                String currentTime = sdf.format(new Date());
                room.setCreatedAt(currentTime);

                room.setCurrentActiveUsers(0);

                showProgressBar();

                RoomsDao.addNewRoom(room, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        hideProgressBar();
                        showConfirmationMessage(R.string.success,
                                R.string.room_added_successfully,
                                R.string.ok,
                                new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        finish();
                                    }
                                }) .setCancelable(false);


                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressBar();
                        showMessage(getString(R.string.failed),e.getMessage(),getString(R.string.ok));

                    }
                });


            }
        });



    }

    public void initViews () {
        roomName = findViewById(R.id.room_name);
        roomDescription = findViewById(R.id.room_description);
        addRoom = findViewById(R.id.add_room_button);
    }
}
