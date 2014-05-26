package com.example.webclient;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Aozaki_Shiro on 5/26/2014.
 */
public class UserRegister extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        btnEnter = (Button) this.findViewById(R.id.user_register_btnenter);
        etUserName = (EditText) this.findViewById(R.id.user_register_etusername);
        etPassword = (EditText) this.findViewById(R.id.user_register_etpassword);

        btnEnter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if( userName.length() <= 0){
                    Toast.makeText(UserRegister.this, "用户名为空", 1000).show();
                    etUserName.requestFocus();
                } else if( password.length() <= 0){
                    Toast.makeText(UserRegister.this, "密码为空", 1000).show();
                    etPassword.requestFocus();
                }

                registerP(userName, password);

            }

        });

    }

    private void registerP(String userName, String password){

    }

    private EditText etUserName;
    private EditText etPassword;
    private Button btnEnter;

}