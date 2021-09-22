package com.example.thinkandsend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addbut;
    private Storecredentials pref;


    RecyclerView rview;

    DatabaseReference postref;
    StorageReference storageref;
    Postadapter adapter;

    FirebaseRecyclerOptions<PostModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = Storecredentials.getInstance(this);
        addbut = findViewById(R.id.addbut);
        rview=findViewById(R.id.rview);

        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        storageref = FirebaseStorage.getInstance().getReference();
        options=new FirebaseRecyclerOptions.Builder<PostModel>().setQuery(postref,PostModel.class).build();
         setuprecyclerview();
        addbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addactivity = new Intent(MainActivity.this, AddPost.class);
                startActivity(addactivity);
            }
        });
    }



    private void setuprecyclerview() {


        rview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Postadapter(options,getApplicationContext());
        rview.setAdapter(adapter);


    }




    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}