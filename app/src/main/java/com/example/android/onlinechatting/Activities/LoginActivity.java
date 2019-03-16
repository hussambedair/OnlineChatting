package com.example.android.onlinechatting.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.onlinechatting.Base.BaseActivity;
import com.example.android.onlinechatting.FireBaseUtils.DataHolder;
import com.example.android.onlinechatting.FireBaseUtils.Models.User;
import com.example.android.onlinechatting.FireBaseUtils.UsersDao;
import com.example.android.onlinechatting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {

    TextInputLayout email;
    TextInputLayout password;
    Button login;
    TextView register;

    View.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String sEmail = email.getEditText().getText().toString();
            final String sPassword = password.getEditText().getText().toString();

            showProgressBar();
            UsersDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();
                            if (!dataSnapshot.hasChildren()) {
                                showMessage(R.string.error, R.string.invalid_email_or_password, R.string.ok);
                            } else {
                                for (DataSnapshot object:dataSnapshot.getChildren()){
                                    User user = object.getValue(User.class);
                                    if (user.getPassword().equals(sPassword)) {
                                        //go to HomeActivity
                                        DataHolder.currentUser =user;
                                        startActivity(new Intent(activity,HomeActivity.class));
                                        finish();
                                    } else {
                                        showMessage(R.string.error, R.string.invalid_email_or_password, R.string.ok);
                                    }
                                }




                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error),databaseError.getMessage(), getString(R.string.ok));

                        }
                    });




        }
    };

    View.OnClickListener registerationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, RegisterationActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        login.setOnClickListener(loginOnClickListener);

        register.setOnClickListener(registerationOnClickListener);


    }



    public void initView() {
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_text_view);

    }
}
