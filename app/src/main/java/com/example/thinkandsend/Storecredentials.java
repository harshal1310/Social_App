package com.example.thinkandsend;


import android.content.Context;
import android.content.SharedPreferences;

public class Storecredentials {
   private static Context context;
    private static SharedPreferences sharedPreferences;
    private static Storecredentials instance;
    private  static String pref="TAS";
    public Storecredentials() {
        sharedPreferences=context.getSharedPreferences(pref,context.MODE_PRIVATE);
    }
    public  static Storecredentials getInstance(Context con)
    {

        context=con;
        if(instance==null)
        {
           instance =new Storecredentials();
        }

            return instance;
    }
    public void checkforlogin(String value)
    {
        sharedPreferences.edit().putString("login",value).apply();
    }
    public String getlogin()
    {
        String login=sharedPreferences.getString("login","");
        return login;
    }
    public void setid(String value)
    {
        sharedPreferences.edit().putString("userid",value).apply();
    }
    public String getuserid()
    {
        String getid=sharedPreferences.getString("userid","");
        return  getid;
    }
    public void setuserdata(String val)
    {
        sharedPreferences.edit().putString("username",val).apply();
    }
    public String getdata(String key)
    {
        return sharedPreferences.getString("username","");
    }
}
