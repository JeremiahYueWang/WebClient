package com.example.webclient;

import java.io.*;
import java.net.*;

import com.example.protocol.*;

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
	        	    connection.setChunkedStreamingMode(0);
	        	    //connection.setRequestProperty("Content-Length", "0");
	        	   // connection.setRequestProperty("Content-type", "application/json");
	        	    
	        	    connection.connect();

	        	    OutputStream out = new BufferedOutputStream(connection.getOutputStream());
	        	    
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
	        	    
	        	    out.write(login.toString().getBytes());
	        	    out.flush();
	        	    out.close();
	        	    //
	        	    
	        		int code = connection.getResponseCode(); // 返回状态码  
	        		System.out.println("responsecode:"+code);
	        		if (code == 200) { 
	        			// 或得到输入流，此时流里面已经包含了服务端返回回来的JSON数据了,此时需要将这个流转换成字符串  

		        	    InputStream in = new BufferedInputStream(connection.getInputStream());
		        	    ByteArrayOutputStream receive=new ByteArrayOutputStream();;
		        	    
		        	    byte[] data = new byte[1024];
		        	    int len=-1;
		        	    while((in.read(data))!=-1){
		        	    	receive.write(data, 0, len);
		        	    }
		        	    receive.close();
		        	    in.close();
		        	    System.out.println(receive.toString());
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
