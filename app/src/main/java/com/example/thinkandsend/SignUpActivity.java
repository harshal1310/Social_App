package com.example.thinkandsend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
EditText username,emailsignup,passwordsignup;
Button signupbut;
TextView alreadyregistred;
    FirebaseAuth auth;

   private Storecredentials pref;
   DatabaseReference userref;
   FirebaseDatabase firebaseDatabase;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    pref=Storecredentials.getInstance(this);
    if(pref.getlogin().equals("1")) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
        setContentView(R.layout.activity_sign_up);
        username=findViewById(R.id.username);
        emailsignup=findViewById(R.id.emailsignup);
        passwordsignup=findViewById(R.id.passwordsignup);
        signupbut=findViewById(R.id.signup);
        alreadyregistred=findViewById(R.id.gotologin);
       // FirebaseApp.initializeApp(this);

         auth= FirebaseAuth.getInstance();

         firebaseDatabase=FirebaseDatabase.getInstance();
    signupbut.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createuser();
        }
    });
    alreadyregistred.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
            startActivity(intent);
        }
    });
    }

    private void createuser() {
    auth.createUserWithEmailAndPassword(emailsignup.getText().toString(),passwordsignup.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {

                Toast.makeText(getApplicationContext(), "succ", Toast.LENGTH_SHORT).show();
                savedcredentialse();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            } else {

                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    private void savedcredentialse() {
        Map<String,String>map=new HashMap<>();
    map.put("email",emailsignup.getText().toString());
    map.put("password",passwordsignup.getText().toString());
    map.put("username",username.getText().toString());
    map.put("id",auth.getUid());
    userref=firebaseDatabase.getReference().child("Users");
        userref.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    pref.checkforlogin("1");
                    pref.setid(auth.getUid());
                    pref.setuserdata(username.getText().toString());


                }
                else
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }
    }
