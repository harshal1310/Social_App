package com.example.thinkandsend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Postadapter extends FirebaseRecyclerAdapter<PostModel,Postadapter.MyviewHolder> {

    DatabaseReference likeref;
    boolean likeclick=false;
    Context context;
    public Postadapter(@NonNull FirebaseRecyclerOptions<PostModel> options, Context con) {
        super(options);
        this.context=con;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyviewHolder holder, int position, @NonNull PostModel model) {

        holder.name.setText(model.getName());
        holder.time.setText(gettime(model.Timestamp));
        Glide.with(holder.postimg.getContext())
                .load(model.geturl())
                .into(holder.postimg);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid=user.getUid();
                String postid=getRef(position).getKey();
               holder.addlike( userid,postid);
               holder.like.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       likeclick=true;
                       likeref=FirebaseDatabase.getInstance().getReference().child(Constants.Like);
                       likeref.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               if(likeclick)
                               {
                                   if(snapshot.child(postid).hasChild(userid))
                                   {
                                       likeref.child(postid).removeValue();
                                   }
                                   else
                                   {
                                       likeref.child(postid).child(userid).setValue(true);
                                   }
                                   likeclick=false;
                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });
                   }
               });
               holder.Comment.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent=new Intent(context,CommentsActivity.class);
                       intent.putExtra(Constants.Postid,postid);
                       v.getContext().startActivity(intent);
                   }
               });
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_list,parent,false);

        return new MyviewHolder(view);
    }
private String gettime(long time)

{
    long timesmp=time*1000;
    SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
    String tsmp=sdf.format(new Date(timesmp));
    return  tsmp;
}    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView name,time,likecount;
        ImageView postimg,like,share,Comment;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            likecount=itemView.findViewById(R.id.likecount);
           postimg=itemView.findViewById(R.id.postimg);
            like = itemView.findViewById(R.id.like);

               Comment=itemView.findViewById(R.id.Comments);
            share = itemView.findViewById(R.id.share);
        }

        public void addlike(String userid, String postid) {
             likeref= FirebaseDatabase.getInstance().getReference().child(Constants.Like);
             likeref.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if(snapshot.child(postid).hasChild(userid))
                     {
                         int count= (int) snapshot.child(postid).getChildrenCount();
                       likecount.setText(count+"Likes");
                         like.setImageResource(R.drawable.ic_baseline_favorite_24);
                     }
                     else
                     {
                         int count= (int) snapshot.child(postid).getChildrenCount();
                         likecount.setText(count+"Likes");
                         like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
        }
    }
}
