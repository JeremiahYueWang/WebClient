package com.example;

import com.example.model.UserInfo;

/**
 * Created by Aozaki_Shiro on 5/16/2014.
 */
public class Test {
    public static void main(){
        UserInfo userInfo = new UserInfo();
        userInfo.setSex(1);
        userInfo.setUid("uidddddddddddd");
        userInfo.setEmail("email@@@@");
        userInfo.setBirthdayString("1922-02-22");
        userInfo.setName("wangyue");

        System.out.println(userInfo.toString());

    }
}
