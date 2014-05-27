package com.example.webclient;

import java.io.*;

import com.example.protocol.*;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class WebClientActivity extends Activity implements WaitingForResult {

    static boolean isLogedIn = false;

	public WebClientActivity(){
		this.webClientActivity=this;
	}

    private ProgressBar progressBar;
    private String user = "";

    private String url_str = "http://www.jiananhuaxia.com/a/ios/user/";

    @Override
    protected void onResume(){
        super.onResume();

    }

    private boolean isLogedIn(){
        try {

            FileInputStream fis = this.getBaseContext().openFileInput(Config.USERINFOFILE);
            int length = fis.available();
            System.out.println("找到文件："+length);
            if(length==0){
                return false;
            }
            return true;

        }catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        etUserName = (EditText) this.findViewById(R.id.etUserName);
        etPassWord = (EditText) this.findViewById(R.id.etPassWord);
        btnEnter = (Button) this.findViewById(R.id.btnEnter);
        btnRegister = (Button) this.findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        //System.out.println("widthPixels:"+dm.widthPixels+"\ndensity:"+dm.density+"\ndensityDpi:"+dm.densityDpi+"\nscaledDensity:"+dm.scaledDensity+"\nxdpi:"+dm.xdpi);

        if(isLogedIn()){
            Intent intent = new Intent(this, ShowUserInfo.class);
            intent.putExtra("islogedin", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        btnEnter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                user = etUserName.getText().toString();
                btnEnter.setEnabled(false);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                loginP();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebClientActivity.this, UserRegister.class);
                intent = new Intent(WebClientActivity.this, DailyRecord.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {

        }
        
    }

    private void loginP(){
        String userName=etUserName.getText().toString().trim();
        String password=etPassWord.getText().toString().trim();

        JSONObject login=null;
        try {

            login = new JSONObject();
            login.put(LoginUp.DUID, "BB6D8252-3321-46C5-A077-74EE33246211");
            login.put(LoginUp.TYPE, 61);
            JSONObject logincontent = new JSONObject();
            logincontent.put(LoginUp.CONTENT_DEVICETOKEN, GenIDs.getDeviceToken(WebClientActivity.getWebClientActivity().getBaseContext()));
            logincontent.put(LoginUp.CONTENT_USERNAME, userName);
            logincontent.put(LoginUp.CONTENT_PASSWORD, password);
            login.put(LoginUp.CONTENT, logincontent);
            login.put(LoginUp.VERSION, "0.5.4.140424");

            ByteArrayOutputStream receive=new ByteArrayOutputStream();
            new HttpUtil(url_str, login.toString(), WebClientActivity.getWebClientActivity()).execute();

        }catch (Exception e){
            e.printStackTrace();
            //显示一个警告框
        }

    }

    public void doSomeThingOnResult(String result){

        Boolean success = false;
        try {
            JSONObject down = new JSONObject(result);
            int type = down.getInt(LoginDown.TYPE);
            String duid = down.getString(LoginDown.DUID);
            success = down.getBoolean(LoginDown.SUCCESS);
            String token = null;
            String uid = null;
            if(type == 61) {
                if (success) {    // 开始获取用户信息

                    token = down.getString(LoginDown.TOKEN);
                    JSONObject loginDownContent = down.getJSONObject(LoginDown.CONTENT);
                    uid = loginDownContent.getString(LoginDown.CONTENT_UID);

                    pullUserInfo(token, uid);

                } else {
                    JSONObject errorMessage = down.getJSONObject("error");
                    int errorCode = errorMessage.getInt(ErrorMessage.CODE);
                    String errorDescription = errorMessage.getString(ErrorMessage.DESCRIPTION);
                    //显示一个对话框是否重新连接
                    Toast.makeText(this, errorDescription, 1000).show();
                    reEnter();  //重新可以输入

                }
            }else if(type == 64){
                if (success){
                    try{
                        JSONObject userInfo = new JSONObject(result);
                        String duidDown = userInfo.getString(PullUserInfoDown.DUID);
                        String tokenDown = userInfo.getString(PullUserInfoDown.TOKEN);
                        int typeDown = userInfo.getInt(PullUserInfoDown.TYPE);
                        boolean userInfoSuccess = userInfo.getBoolean(PullUserInfoDown.SUCCESS);
                        if(userInfoSuccess){
                            JSONObject userInfoContent = userInfo.getJSONObject(PullUserInfoDown.CONTENT);
                            String uidDown = userInfoContent.getString(PullUserInfoDown.CONTENT_UID);
                            String name = userInfoContent.getString(PullUserInfoDown.CONTENT_NAME);
                            String sex = userInfoContent.getInt(PullUserInfoDown.CONTENT_SEX)==1?"男":"女";
                            String birthday = userInfoContent.getString(PullUserInfoDown.CONTENT_BIRTHDAY);
                            String email = userInfoContent.getString(PullUserInfoDown.CONTENT_EMAIL);
                            String photo = userInfoContent.getString(PullUserInfoDown.CONTENT_PHOTO);

                            Intent intent = new Intent(WebClientActivity.this, ShowUserInfo.class);
                            intent.putExtra(PullUserInfoDown.CONTENT_NAME,name);
                            intent.putExtra(PullUserInfoDown.CONTENT_SEX, sex);
                            intent.putExtra(PullUserInfoDown.CONTENT_BIRTHDAY, birthday);
                            intent.putExtra(PullUserInfoDown.CONTENT_EMAIL, email);
                            intent.putExtra(PullUserInfoDown.CONTENT_PHOTO, photo);
                            intent.putExtra(PullUserInfoDown.CONTENT_UID, uidDown);
                            intent.putExtra("user", user);
                            intent.putExtra("islogedin", false);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } else {
                            //  重新获取一次
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {

                    pullUserInfo(token, uid);

                }
            }
        } catch (JSONException e){

            Toast.makeText(this, "网络连接问题！", 1000).show();

            reEnter();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void reEnter(){

        btnEnter.setEnabled(true);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

    }

    private void pullUserInfo(String token, String uid){

        try {
            JSONObject userInfo = new JSONObject();
            userInfo.put(PullUserInfoDown.DUID, GenIDs.getDUID(WebClientActivity.getWebClientActivity().getBaseContext()));
            userInfo.put(PullUserInfoDown.TOKEN, token);
            userInfo.put(PullUserInfoDown.TYPE, 64);
            userInfo.put(PullUserInfoDown.VERSION, "0.5.4.140424");
            JSONObject userInfoContent = new JSONObject();
            userInfoContent.put(PullUserInfoDown.CONTENT_UID, uid);
            userInfoContent.put(PullUserInfoDown.CONTENT_BIRTHDAY, "");
            userInfoContent.put(PullUserInfoDown.CONTENT_NAME, "");
            userInfoContent.put(PullUserInfoDown.CONTENT_PHOTO, "");
            userInfoContent.put(PullUserInfoDown.CONTENT_SEX, "");
            userInfoContent.put(PullUserInfoDown.CONTENT_EMAIL, "");
            userInfo.put(PullUserInfoDown.CONTENT, userInfoContent);

            new HttpUtil(url_str, userInfo.toString(), this).execute();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void doSomeThingOnResultOld(String result){

        Boolean success = false;
        try {
            JSONObject loginDown = new JSONObject(result);
            String token = loginDown.getString(LoginDown.TOKEN);
            String type = loginDown.getString(LoginDown.TYPE);
            String duid = loginDown.getString(LoginDown.DUID);
            success = loginDown.getBoolean(LoginDown.SUCCESS);
            if (success) {    // 开始一个新的intent
                JSONObject loginDownContent = loginDown.getJSONObject(LoginDown.CONTENT);
                String uid = loginDownContent.getString(LoginDown.CONTENT_UID);
                Intent intent = new Intent(WebClientActivity.this, ShowUserInfo.class);
                intent.putExtra(PullUserInfoUp.CONTENT_UID, uid);
                intent.putExtra(PullUserInfoUp.TOKEN, token);
                startActivity(intent);

            } else {
                JSONObject errorMessage = loginDown.getJSONObject("error");
                int errorCode = errorMessage.getInt(ErrorMessage.CODE);
                String errorDescription = errorMessage.getString(ErrorMessage.DESCRIPTION);
                //显示一个对话框是否重新连接
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showMessage(String text){
    	this.tvHello.setText(text);
    }
    
    private EditText etUserName;
    private EditText etPassWord;
    private Button btnEnter;
    private Button btnRegister;
    private TextView tvHello;
    public static WebClientActivity webClientActivity=null;

    static public WebClientActivity getWebClientActivity(){
    	return webClientActivity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_client, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_quit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}