package com.example.android.onlinechatting.FireBaseUtils;

import com.example.android.onlinechatting.FireBaseUtils.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class UsersDao {

    public static final String usersBranch = "users";

    public static DatabaseReference getUsersBranch () {

        return FirebaseDatabase.getInstance()
                .getReference(usersBranch);

    }

    public static void addNewUser (User user,
                                   OnSuccessListener onSuccessListener,
                                   OnFailureListener onFailureListener) {

        DatabaseReference newNode = getUsersBranch().push();

        user.setId(newNode.getKey());

        newNode.setValue(user)
                .addOnFailureListener(onFailureListener)
                .addOnSuccessListener(onSuccessListener);

    }


    public static Query getUsersByEmail (String email) {

        Query query = getUsersBranch()
                .orderByChild("email")
                .equalTo(email);

        return query;

    }

}
