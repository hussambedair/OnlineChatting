package com.example.android.onlinechatting.FireBaseUtils;

import com.example.android.onlinechatting.FireBaseUtils.Models.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MessagesDao {

    public static final String messagesBranch = "messages";

    public static DatabaseReference getMessagesBranch() {

        return FirebaseDatabase.getInstance()
                .getReference(messagesBranch);
    }


    public static void addNewMessage(Message message,
                                     OnSuccessListener onSuccessListener,
                                     OnFailureListener onFailureListener) {

        DatabaseReference newNode = getMessagesBranch().push();

        message.setId(newNode.getKey());

        newNode.setValue(message)
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener);

    }

    public static Query getMessagesByRoomId(String roomId) {

        Query query = getMessagesBranch()
                .orderByChild("roomId")
                .equalTo(roomId);

        return query;

    }

}
