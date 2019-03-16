package com.example.android.onlinechatting.FireBaseUtils;

import com.example.android.onlinechatting.FireBaseUtils.Models.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RoomsDao {

    public static final String roomsBranch = "rooms";

    public static DatabaseReference getRoomsBranch () {

        return FirebaseDatabase.getInstance()
                .getReference(roomsBranch);
    }

    public static void addNewRoom (Room room,
                              OnSuccessListener onSuccessListener,
                              OnFailureListener onFailureListener) {

        DatabaseReference newNode = getRoomsBranch().push();

        room.setId(newNode.getKey());

        newNode.setValue(room)
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener);

    }






}
