package com.example.thinkandsend;

public class CommentsModel {
    String Name,Comments,Date,Time;

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setUsername(String username) {
        this.Name = username;
    }

    public void setComments(String comments) {
        this.Comments = comments;
    }

    public String getUsername() {
        return Name;
    }

    public String getComments() {
        return Comments;
    }
}
