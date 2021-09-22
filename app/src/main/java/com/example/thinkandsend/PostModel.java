package com.example.thinkandsend;

import java.util.ArrayList;

public class PostModel {
    String Name,id;
    long  Timestamp;
    String url;

    public void setName(String name) {
        Name = name;
    }



    public PostModel() {

    }

    public PostModel(String name, String id, long timestamp, String url) {
        Name = name;
        this.id = id;
        Timestamp = timestamp;
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

   public void seturl(String url) {
        this.url = url;
    }
   public String geturl() {
        return url;
    }
    public String getName() {
        return Name;
    }



    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return Timestamp;
    }

}
