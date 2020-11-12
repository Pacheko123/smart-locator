package com.androidcodefinder.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {

    Button regUser;
    ProgressBar toNext;
    EditText mail,password;
    TextView loginPage;
    final FirebaseAuth authLogin = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        toNext = findViewById(R.id.toNext);
        toNext.setVisibility(View.GONE);


        regUser = findViewById(R.id.register);
        loginPage = findViewById(R.id.toLogin);
        mail = findViewById(R.id.email);
        password = findViewById(R.id.password);

        regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNext.setVisibility(View.VISIBLE);
                String mailReg = mail.getText().toString();
                String passReg = password.getText().toString();
                if (mailReg.isEmpty() || passReg.isEmpty()){
                    toNext.setVisibility(View.GONE);
                    Toast.makeText(register.this,"Please fill in all fields",Toast.LENGTH_SHORT).show();
                }else{

                    //create a new user
                    authLogin.createUserWithEmailAndPassword(mailReg,passReg)
                            .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                    /* If signup fails, display a message to the user. If signup succeeds
                                     the auth state listener will be notified and logic to handle the Signup*/
                                    if (!task.isSuccessful()) {
                                        toNext.setVisibility(View.GONE);
                                        Toast.makeText(register.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        password.getText().clear();
                                        mail.getText().clear();
                                        Toast.makeText(register.this, "Account creation successfull, redirection for log in" ,
                                                Toast.LENGTH_SHORT).show();
                                        Intent success = new Intent(register.this,home.class);
                                        startActivityForResult(success,0);
                                    }
                                }
                            });

                     }
                }
            });

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(register.this,home.class);
                startActivityForResult(login,0);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

}

