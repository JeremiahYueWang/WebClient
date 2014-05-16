package com.example.model;

import android.graphics.Bitmap;

import com.example.webclient.ImageUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aozaki_Shiro on 5/16/2014.
 */
public class UserInfo {
    private String uid;
    private String user;
    private String name;
    private int sex;
    private Date birthday;
    private String email;
    private Bitmap photo;

    public UserInfo(){

    }

    public UserInfo(String s){

        try{

            JSONObject userInfo = new JSONObject(s);
            uid = userInfo.getString("uid");
            user = userInfo.getString("user");
            name = userInfo.getString("name");
            sex = userInfo.getInt("sex");
            email = userInfo.getString("email");
            photo = ImageUtil.stringToBitmap(userInfo.getString("photo"));
            setBirthdayString(userInfo.getString("birthday"));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setUser(String s){
        user = s;
    }

    public String getUser(){
        return user;
    }

    public void setUid(String s){
        uid = s;
        //System.out.println("in UserInfo setuid:"+uid);
    }

    public String getUid(){
        return uid;
    }

    public void setName(String s){
        name = s;
    }

    public String getName(){
        return name;
    }

    public void setSex(int i){
        sex = i;
    }

    public int getSex(){
        return sex;
    }

    public void setBirthdayString(String s){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-mm-dd");
        try {
            birthday = dateFormater.parse(s);
            System.out.println("user info:"+birthday.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Date getBirthday(){
        return birthday;
    }
    public String getBirthdyString(){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormater.format(birthday);
    }

    public void setEmail(String s){
        email = s;
    }

    public String getEmail(){
        return email;
    }

    public void setPhoto(Bitmap bm){
        photo = bm;
    }

    public Bitmap getPhoto(){
        return photo;
    }

    public String toString(){

        String result=null;
        try {

            JSONObject userInfo = new JSONObject();
            userInfo.put("a","a");
            userInfo.put("uid", getUid());
            userInfo.put("user", getUser());
            userInfo.put("name", getName());
            userInfo.put("sex", getSex());
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-mm-dd");
            userInfo.put("birthday", dateFormater.format(getBirthday()));
            userInfo.put("email", getEmail());
            userInfo.put("photo", ImageUtil.bitmapToString(getPhoto()));
            result = userInfo.toString();
            System.out.println("in UserInfo:"+result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }
}
