package com.example.thinkandsend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
EditText emailtext,passwordtext;
Button signinbut;
FirebaseAuth auth;
Storecredentials pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailtext=findViewById(R.id.emailsignin);
        passwordtext=findViewById(R.id.passwordsignin);
        signinbut=findViewById(R.id.signin);
        pref=Storecredentials.getInstance(this);
        auth=FirebaseAuth.getInstance();

        signinbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });

    }

    private void userlogin() {
    auth.signInWithEmailAndPassword(emailtext.getText().toString(),passwordtext.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful())
            {pref.checkforlogin("1");
                pref.setid(auth.getUid());

                Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else {

                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });
    }
}