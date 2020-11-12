package com.androidcodefinder.loginapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {

    public Button login, signup;
    public EditText mail, password;
    public CheckBox remember;
    private ProgressBar toNext;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.register);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.password);
        toNext = findViewById(R.id.pBar);
        TextView forgot = findViewById(R.id.forgot);
        remember = findViewById(R.id.remember);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final FirebaseAuth authLogin = FirebaseAuth.getInstance();
        toNext.setVisibility(View.GONE);

        signup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick( View v) {
            Intent toRegister= new Intent(home.this,register.class);
            startActivityForResult(toRegister,0);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    });

    forgot.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            toNext.setVisibility(View.VISIBLE);
            final String email = mail.getText().toString();

            if (email.isEmpty()){
                toNext.setVisibility(View.GONE);
                mail.setError("Please input  email address");
                mail.requestFocus();
            }else{
                authLogin.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    toNext.setVisibility(View.GONE);
                                    Toast.makeText(home.this, "An email with the reset link has been sent", Toast.LENGTH_SHORT).show();
                                } else {
                                    toNext.setVisibility(View.GONE);
                                    Toast.makeText(home.this,"Connect to internet or check your email address" ,Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                }
            }
        });

/*login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent login = new Intent(home.this, MapsActivity.class);   //This code section is commented out for purpose
                                                                     //of testing to bypass login authentication
        startActivityForResult(login, 0);
    }
});*/


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mail.getText().toString();
                final String password_string = password.getText().toString();


                if(remember.isChecked()){
                    editor.putString("email",mail.getText().toString());
                    editor.putString("password",password.getText().toString());
                    editor.apply();
                }else{
                    editor.putString("email","");
                    editor.putString("name","");
                    editor.putString("password","");
                    editor.commit();
                }

                if (email.isEmpty() || password_string.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                } else {


                    authLogin.signInWithEmailAndPassword(email, password_string)
                            .addOnCompleteListener(home.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {

                                        if (password.length() < 6) {
                                            toNext.setVisibility(View.VISIBLE);
                                            password.setError(getString(R.string.minimum_password));
                                            toNext.setVisibility(View.GONE);
                                        } else {
                                            toNext.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            toNext.setVisibility(View.GONE);
                                        }
                                    } else {
                                        toNext.setVisibility(View.VISIBLE);
                                        password.getText().clear();
                                        mail.getText().clear();
                                        Intent login = new Intent(home.this, MapsActivity.class);
                                        startActivityForResult(login, 0);
//
                                    }

                                }
                            });
                    }
                }
            });
        }


    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to exit ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Confirm exit operation");
        alert.show();
        }

    }