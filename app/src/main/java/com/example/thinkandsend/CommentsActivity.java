package com.example.thinkandsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CommentsActivity extends AppCompatActivity {
//RecyclerView recyclerView;
  Button commentbut;
  EditText commentbox;
  DatabaseReference commentsref;
    private Storecredentials pref;
    String postid;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
      Intent intent = getIntent();
       postid = intent.getStringExtra(Constants.Postid);
        pref=Storecredentials.getInstance(this);

        commentbox=findViewById(R.id.commentsbox);
        commentbut=findViewById(R.id.commentbut);
        recyclerView=findViewById(R.id.listofcomments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsref= FirebaseDatabase.getInstance().getReference().child(Constants.Posts).child(postid).child(Constants.Comment);
        commentbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adcomment();
            }
        });

    }

    private void adcomment() {
        String randomkey = pref.getuserid() + "" + new Random().nextInt(1000);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yy");
        String date = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
        String time = timeformat.format(calendar.getTime());
        HashMap map = new HashMap<>();
        map.put(Constants.Name, pref.getdata("username"));
        map.put(Constants.Comment, commentbox.getText().toString());
        map.put("Date", date);
        map.put("Time", time);

        commentsref.child(randomkey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CommentsModel>options=new FirebaseRecyclerOptions.Builder<CommentsModel>().setQuery(commentsref,CommentsModel.class).build();

        adapter = new CommentsAdapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}