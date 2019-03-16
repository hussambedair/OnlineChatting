package com.example.android.onlinechatting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.android.onlinechatting.Base.BaseActivity;
import com.example.android.onlinechatting.FireBaseUtils.DataHolder;
import com.example.android.onlinechatting.FireBaseUtils.Models.User;
import com.example.android.onlinechatting.FireBaseUtils.UsersDao;
import com.example.android.onlinechatting.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterationActivity extends BaseActivity {

    TextInputLayout userName;
    TextInputLayout email;
    TextInputLayout password;
    Button register;
    TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // this is the data that we need to insert into firebase
                final String sUserName = userName.getEditText().getText().toString();
                final String sEmail = email.getEditText().getText().toString();
                final String sPassword = password.getEditText().getText().toString();

                //username validation
                //make sure that username is not empty
                if (sUserName.trim().length() == 0) {
                    userName.setError(getString(R.string.username_required));
                    return;
                }
                userName.setError(null);

                //email validation
                //make sure email format is right
                if (!isValidEmail(sEmail)) {
                    email.setError(getString(R.string.invalid_email));
                    return;

                }
                email.setError(null);

                //password validation
                //make sure that password is no less than 6 chars
                if (sPassword.length() <6) {
                    password.setError(getString(R.string.invalid_password));
                    return;
                }
                password.setError(null);


                showProgressBar();



                //user validation
                // we need to check weather this user exists in the database or not
                // before making the registeration
                // so we need to make sure that the email hasn't registered before
                UsersDao.getUsersByEmail(sEmail)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                hideProgressBar();

                                if (dataSnapshot.hasChildren()) { //user already registered
                                    showMessage(R.string.error, R.string.email_already_exist, R.string.ok);

                                } else {
                                    // register the new user
                                    final User user = new User(sUserName,sEmail,sPassword);
                                    registerUser(user);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                hideProgressBar();

                                showMessage(getString(R.string.error),databaseError.getMessage(),getString(R.string.ok));


                            }
                        });




            }

        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void registerUser(final User user) {

        showProgressBar();

        UsersDao.addNewUser(user, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                //registeration is successful and the user is allowed to
                // proceed to the app

                hideProgressBar();

                DataHolder.currentUser = user;

                showConfirmationMessage(R.string.success,
                        R.string.registered_successfully,
                        R.string.ok,
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //start HomeActivity
                                Intent i = new Intent(activity,HomeActivity.class);
                                startActivity(i);
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

    public static boolean isValidEmail (CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    public void initView() {
        userName = findViewById(R.id.username_edit_text);
        email = findViewById(R.id.email_edit_text);
        password = findViewById(R.id.password_edit_text);
        register = findViewById(R.id.register_button);
        login = findViewById(R.id.login_text_view);


    }

}
