package com.example.webclient;

import java.io.*;
import java.net.*;

import javax.security.auth.login.LoginException;

import com.example.protocol.*;

import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.net.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.os.Build;

public class WebClientActivity extends Activity {
	public WebClientActivity(){
		this.webClientActivity=this;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_client);
        
        etUserName=(EditText) this.findViewById(R.id.etUserName);
        etPassWord=(EditText) this.findViewById(R.id.etPassWord);
        btnEnter=(Button) this.findViewById(R.id.btnEnter);
        
        btnEnter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String userName=etUserName.getText().toString().trim();
				String password=etPassWord.getText().toString().trim();
				
				System.out.println(userName+" "+password);
				new DownloadWebpageTask().execute();
				System.out.println("main thread end!!");
			}
		});
        
        //GenIDs.getDeviceToken(this.getBaseContext());
       // GenIDs.getDUID(this.getBaseContext());

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        

        
    }
    
    private EditText etUserName;
    private EditText etPassWord;
    private Button btnEnter;
    public static WebClientActivity webClientActivity=null;
    static public WebClientActivity getWebClientActivity(){
    	return webClientActivity;
    }
    
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

//    	public void execute(){
//    		doInBackground();
//    	}
    	
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
	        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	        if(networkInfo != null && networkInfo.isConnected()){
	        	// fetch data\
	        	String url_path="http://www.jiananhuaxia.com/a/ios/user/";

	        	
	        	
	        	try {	        
	        		URL url = new URL(url_path);
	        		HttpURLConnection connection = (HttpURLConnection) url.openConnection();		
	        		connection.setConnectTimeout(3000); // 请求超时时间3s 
	        		connection.setRequestMethod("POST");
	        		connection.setDoInput(true);
	        		connection.setDoOutput(true);
	        	    //connection.setChunkedStreamingMode(0);
	        	    

	        	    JSONObject login=new JSONObject();
	        	    login.put(LoginUp.DUID, "BB6D8252-3321-46C5-A077-74EE33246211");
	        	    login.put(LoginUp.TYPE, 61);
	        	    JSONObject logincontent=new JSONObject();
	        	    logincontent.put(LoginUp.CONTENT_DEVICETOKEN, GenIDs.getDeviceToken(WebClientActivity.getWebClientActivity().getBaseContext()));
	        	    logincontent.put(LoginUp.CONTENT_USERNAME, "Magical");
	        	    logincontent.put(LoginUp.CONTENT_PASSWORD, "123456");
	        	    login.put(LoginUp.CONTENT, logincontent);
	        	    login.put(LoginUp.VERSION, "0.5.4.140424");
	        	    
	        	    System.out.println("upload data:"+login.toString());
	        	    
	        	    
	        	    connection.setRequestProperty("Transfer-Encoding","identity");
	        	    connection.setRequestProperty("Content-Length", login.toString().getBytes().length+"");
	        	    connection.setRequestProperty("Content-type", "application/json");
	        	    connection.setRequestProperty("Connection", "keep-alive");
	        	    //connection.setUseCaches(false);
	        	    //connection.setInstanceFollowRedirects(true);
	        	    connection.connect();

	        	    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
	        	    
	        	    
	        	    out.write(login.toString().getBytes());
	        	    out.flush();
	        	    //out.close();
	        	    //
	        	    
	        		int code = connection.getResponseCode(); // 返回状态码  
	        		System.out.println("responsecode:"+code);
	        		if (code == 200) { 
	        			// 或得到输入流，此时流里面已经包含了服务端返回回来的JSON数据了,此时需要将这个流转换成字符串  

		        	    InputStream in = new BufferedInputStream(connection.getInputStream());
		        	    ByteArrayOutputStream receive=new ByteArrayOutputStream();;
		        	    
		        	    byte[] data = new byte[1024];
		        	    int len=-1;
		        	    while((len=in.read(data))!=-1){
		        	    	receive.write(data, 0, len);
		        	    }
		        	    receive.close();
		        	    in.close();
		        	    
		        	    System.out.println(receive.toString());
		        	    
		        	    JSONObject loginDown=new JSONObject(receive.toString());
		        	    String token=loginDown.getString(LoginDown.TOKEN);
		        	    String type=loginDown.getString(LoginDown.TYPE);
		        	    String duid=loginDown.getString(LoginDown.DUID);
		        	    boolean success=loginDown.getBoolean(LoginDown.SUCCESS);
		        	    if(success){
		        	    	JSONObject loginDownContent=loginDown.getJSONObject(LoginDown.CONTENT);
		        	    	String uid=loginDownContent.getString(LoginDown.CONTENT_UID);
		        	    	
		        	    	JSONObject userInfo=new JSONObject();
		        	    	userInfo.put(PullUserInfoDown.DUID, GenIDs.getDUID(WebClientActivity.getWebClientActivity().getBaseContext()));
		        	    	userInfo.put(PullUserInfoDown.TOKEN, token);
		        	    	userInfo.put(PullUserInfoDown.TYPE, 64);
		        	    	userInfo.put(PullUserInfoDown.VERSION, "0.5.4.140424");
		        	    	JSONObject userInfoContent=new JSONObject();
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_UID, uid);
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_BIRTHDAY,"");
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_NAME, "");
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_PHOTO, "");
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_SEX, "");
		        	    	userInfoContent.put(PullUserInfoDown.CONTENT_EMAIL, "");
		        	    	userInfo.put(PullUserInfoDown.CONTENT,userInfoContent);
		        	    	
		        	    	System.out.println("pull user info:"+userInfo.toString());
		        	    	out = new BufferedOutputStream(connection.getOutputStream());
		        	    	out.write(userInfo.toString().getBytes());
		        	    	out.flush();
		        	    	
		        	    	int pullUserCode=connection.getResponseCode();
		        	    	if(pullUserCode==200){
		        	    		InputStream pullUserIn = new BufferedInputStream(connection.getInputStream());
				        	    ByteArrayOutputStream pullUserReceive=new ByteArrayOutputStream();;
				        	    
				        	    byte[] userData = new byte[1024];
				        	    int userLen=-1;
				        	    while((userLen=in.read(userData))!=-1){
				        	    	pullUserReceive.write(userData, 0, userLen);
				        	    }
				        	    pullUserReceive.close();
				        	    pullUserIn.close();
				        	    System.out.println(pullUserReceive.toString());
		        	    	}
		        	    }else{
		        	    	JSONObject errorMessage=loginDown.getJSONObject("error");
		        	    	int errorCode=errorMessage.getInt(ErrorMessage.CODE);
		        	    	String errorDescription=errorMessage.getString(ErrorMessage.DESCRIPTION);		        	    	
		        	    }
	                 }  
	        		connection.disconnect();
	        		System.out.println("connection.disconnect()");
	            } catch (Exception e) {  
	                 // TODO: handle exception  
	            	e.printStackTrace();
	            }finally{
	            	//connection.disconnect();
	            }
	        	
	        }else{
	        	// display error
	        }
			
			return null;
		}
		
		private void logInProcess(){
			
			JSONObject logIn=new JSONObject();
			try {
				logIn.put("duid", getDuid());
				logIn.put("type", 61);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    	
    }
    private String duid="sss";	//每次通信唯一标识，通信标识码，客户端生成
    
    private String getDuid(){
    	return duid;
    }
    private void genDuid(){
    	duid="hello, this is a test";
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_web_client, container, false);
            return rootView;
        }
    }

}
