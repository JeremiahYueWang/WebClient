package com.example.webclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Aozaki_Shiro on 5/15/2014.
 */
public class HttpUtil extends AsyncTask<String, Integer, String>{

    private String urlString = "http://www.jiananhuaxia.com/a/ios/user/";
    private String uploadString = "";
    private int count=0;
    WaitingForResult activity;

    public HttpUtil(String urlStr, String uploadString, WaitingForResult activity){
        urlString = urlStr;
        this.uploadString = uploadString;
        this.activity = activity;
    }

    public String getUrlString(){
        return urlString;
    }

    public void setUrlString(String s){
        urlString=s;
    }

    @Override
    protected String doInBackground(String... arg0) {

        URL url=null;
        HttpURLConnection connection=null;
        String result="";
        try {
            url = new URL(getUrlString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestProperty("Transfer-Encoding","identity");
            connection.setRequestProperty("Content-Length", uploadString.getBytes().length+"");
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());


            out.write(uploadString.getBytes());
            out.flush();
            out.close();

            int code = connection.getResponseCode(); // 返回状态码
            if (code == 200) {
                // 或得到输入流，此时流里面已经包含了服务端返回回来的JSON数据了,此时需要将这个流转换成字符串

                InputStream in = new BufferedInputStream(connection.getInputStream());
                ByteArrayOutputStream o = new ByteArrayOutputStream();

                byte[] data = new byte[1024];
                int len = -1;
                while ((len = in.read(data)) != -1) {
                    o.write(data, 0, len);
                }
                result=o.toString();
                in.close();

            }
            connection.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }

    protected void onPostExecute(String result) {

        activity.doSomeThingOnResult(result);

        super.onPostExecute(result);
    }

    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }

    protected void onPreExecute(Integer... progress) {
        //textView.setText("开始执行异步线程");
        super.onPreExecute();
    }

    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate();
    }

}
