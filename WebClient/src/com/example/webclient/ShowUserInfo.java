package com.example.webclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.UserInfo;
import com.example.protocol.Config;
import com.example.protocol.PullUserInfoDown;
import com.example.protocol.PullUserInfoUp;
import com.example.webclient.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Aozaki_Shiro on 5/15/2014.
 */
public class ShowUserInfo extends Activity {

    private TextView tvName;

    private TextView tvSex;

    private TextView tvBirthday;

    private TextView tvEmail;

    private ImageView ivPhoto;

    UserInfo userInfo = null;

    String token = null;
    String uid = null;
    boolean isLogedIn = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user_info);

        tvName = (TextView) this.findViewById(R.id.user_info_name);
        tvSex = (TextView) this.findViewById(R.id.user_info_sex);
        tvBirthday = (TextView) this.findViewById(R.id.user_info_birthday);
        tvEmail = (TextView) this.findViewById(R.id.user_info_email);
        ivPhoto = (ImageView) this.findViewById(R.id.user_info_photo);

        Intent intent = getIntent();
        isLogedIn = intent.getBooleanExtra("islogedin", false);
        if(!isLogedIn) {

            isLogedIn = true;

            token = intent.getStringExtra(PullUserInfoUp.TOKEN);
            uid = intent.getStringExtra(PullUserInfoUp.CONTENT_UID);
            String name = intent.getStringExtra(PullUserInfoDown.CONTENT_NAME);
            String sex = intent.getStringExtra(PullUserInfoDown.CONTENT_SEX);
            String birthday = intent.getStringExtra(PullUserInfoDown.CONTENT_BIRTHDAY);
            String email = intent.getStringExtra(PullUserInfoDown.CONTENT_EMAIL);
            String photo = intent.getStringExtra(PullUserInfoDown.CONTENT_PHOTO);
            String user = intent.getStringExtra("user");

            tvName.setText(name);
            tvSex.setText(sex);
            tvBirthday.setText(birthday);
            tvEmail.setText(email);
            Bitmap bitmap = ImageUtil.stringToBitmap(photo);

            userInfo = new UserInfo();
            userInfo.setUid(uid);
            userInfo.setUser(user);
            userInfo.setName(name);
            userInfo.setSex(sex.trim().compareTo("男") == 0 ? 1 : 0);
            userInfo.setEmail(email);
            userInfo.setPhoto(bitmap);
            userInfo.setBirthdayString(birthday);

            int width = bitmap.getWidth();
            int screenWidth;
            System.out.println("bitmap.width:" + width);
            ivPhoto.setImageBitmap(bitmap);
        }else{

            getProfileFromFile();
            tvName.setText(userInfo.getName());
            tvSex.setText(userInfo.getSex()==1?"男":"女");
            tvBirthday.setText(userInfo.getBirthdyString());
            ivPhoto.setImageBitmap(userInfo.getPhoto());
            tvEmail.setText(userInfo.getEmail());
        }

//        DisplayMetrics dm = getResources().getDisplayMetrics();
//
//        System.out.println("widthPixels:"+dm.widthPixels);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_client, menu);
        return true;
    }

    private boolean isLogOff = false;

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_quit) {

            isLogOff = true;

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getProfileFromFile(){

        userInfo = new UserInfo();
        try {

            FileInputStream fis = this.getBaseContext().openFileInput(Config.USERINFOFILE);
            int length=fis.available();
            byte[] buffer=new byte[length];
            fis.read(buffer);
            String result= EncodingUtils.getString(buffer, "UTF-8");
            userInfo = new UserInfo(result);
            fis.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void saveProfiletoFile(){

        try {
            FileOutputStream fos = this.getBaseContext().openFileOutput(Config.USERINFOFILE, Context.MODE_PRIVATE);
            fos.write(EncodingUtils.getBytes(userInfo.toString(), "UTF-8"));
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void clearProfiletoFile(){

        try {
            FileOutputStream fos = this.getBaseContext().openFileOutput(Config.USERINFOFILE, Context.MODE_PRIVATE);
            fos.write(EncodingUtils.getBytes("", "UTF-8"));
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop(){
        super.onStop();

        isLogedIn = false;
        if(isLogOff) {  //点退出键了

            System.out.println("点退出键");
            clearProfiletoFile();

        }else{

            saveProfiletoFile();

        }
    }

    protected void onStart(){

        super.onStart();
        if(!isLogedIn) {
            getProfileFromFile();
        }

    }

}