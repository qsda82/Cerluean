package com.example.datingapptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private EditText account, password;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(listener);
        btn_register.setOnClickListener(listener);
        mAuth = FirebaseAuth.getInstance();
       // mAuth.signOut();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    userUID = user.getUid();
                   /* Toast.makeText(MainActivity.this,userUID ,
                            Toast.LENGTH_SHORT).show();*/
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,HomePage.class);
                    startActivity(intent);

                }


            }
        };
    }


    private Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    login(account.getText().toString().trim(),password.getText().toString());
                    break;
                case R.id.btn_register:
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,register_page.class);
                    startActivity(intent);
                    break;

            }
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListener);
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "登入成功.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.setClass(MainActivity.this,HomePage.class);
                            startActivity(intent);


                        } else {
                            Toast.makeText(MainActivity.this, "登入失敗." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


}
