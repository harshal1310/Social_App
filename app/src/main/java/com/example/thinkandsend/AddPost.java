package com.example.thinkandsend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddPost extends AppCompatActivity {
ImageView imageView;
Button select,upload;
Storecredentials pref;
Uri URI;
StorageReference storageReference;
DatabaseReference postref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        pref = Storecredentials.getInstance(this);
        imageView=findViewById(R.id.img);
        select=findViewById(R.id.select_image);
        upload=findViewById(R.id.upload_image);

        storageReference= FirebaseStorage.getInstance().getReference();

        postref= FirebaseDatabase.getInstance().getReference().child("Posts");
        select.setOnClickListener(v->getimage());
        upload.setOnClickListener(v->uploadpost());
    }

    private void getimage() {
        if (ActivityCompat.checkSelfPermission(AddPost.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AddPost.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            try {
                startActivityForResult(intent, 101);

            } catch (ActivityNotFoundException e) {

                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

    private void uploadpost() {
        if(URI==null)
        {
            Toast.makeText(getApplicationContext(),"Select img first",Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        StorageReference uploadit=storageReference.child("PostsImg/"+System.currentTimeMillis()+"."+getextension());
        uploadit.putFile(URI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
           if(task.isSuccessful())
           {   uploadit.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
               if(task.isSuccessful())
               {

                  //Map<String,Object> map=new HashMap<>();
                   PostModel model=new PostModel(pref.getdata("username"),pref.getuserid(),System.currentTimeMillis() / 1000,URI.toString());


               //    Log.d("check",URI.toString());

                   //map.put("url",URI.toString());
                   //map.put(Constants.TIMESTAMP, System.currentTimeMillis() / 1000);
                  //map.put(Constants.Id, pref.getuserid());
                  // map.put(Constants.Name, pref.getdata("username"));
                   postref.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful())
                      {progressDialog.dismiss();
                          Toast.makeText(getApplicationContext(),"Uploaded Succesfully",Toast.LENGTH_LONG).show();
                      }
                      else
                      {
                          Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                      }
                       }
                   });
               }
               else {
                   Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
               }
               }
           }) ;


           }
           else
           { Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
           }
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percentage=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)percentage+"%");
            }
        });
    }

    private String getextension() {
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
       return mimeTypeMap.getExtensionFromMimeType(getContentResolver().getType(URI));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK)  {
            {
                if (data.getData() != null) {
                    URI = data.getData();
                    imageView.setImageURI(URI);

                }
            }
        }
    }
}