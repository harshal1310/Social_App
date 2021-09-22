package com.example.thinkandsend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CommentsAdapter extends FirebaseRecyclerAdapter<CommentsModel,CommentsAdapter.Myholder> {


    public CommentsAdapter(@NonNull FirebaseRecyclerOptions<CommentsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentsAdapter.Myholder holder, int position, @NonNull CommentsModel model) {
        holder.commetedtext.setText(model.getComments());
        holder.commeteduser.setText(model.getUsername());
        holder.commeteduser.setText("Date:"+model.getDate()+""+"Time:"+model.getTime());

    }

    @NonNull
    @Override
    public CommentsAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_list,parent,false);
        return new  Myholder (view);
    }
    class Myholder extends RecyclerView.ViewHolder{
    TextView commetedtext,commeteduser,date;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            commetedtext=itemView.findViewById(R.id.commetedtext);
            commeteduser=itemView.findViewById(R.id.commentedusername);
            date=itemView.findViewById(R.id.datetime);
        }
    }
}
