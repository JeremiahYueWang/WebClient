package com.example.webclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Aozaki_Shiro on 5/26/2014.
 */
public class UserSetting extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);


    }

    public void clickOnUserStateMenu(View paramView){

        Toast.makeText(this, "是不是可以呢？"+paramView.toString(), 1000).show();

    }
}