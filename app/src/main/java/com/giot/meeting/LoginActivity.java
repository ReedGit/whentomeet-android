package com.giot.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 伟 on 2015/10/14.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewHead;
    private TextInputLayout textInputLayoutUsername, textInputLayoutPassword;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewForget, textViewNewUser;
    private Button buttonLogin;
    private ImageButton imageButtonWeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    void initView() {
        imageViewHead = (ImageView) findViewById(R.id.imageView_head);
        imageButtonWeChat = (ImageButton) findViewById(R.id.imageButton_weChat);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayout_username);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayout_password);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        textViewForget = (TextView) findViewById(R.id.textView_forget);
        textViewNewUser = (TextView) findViewById(R.id.textView_newUser);
        buttonLogin = (Button) findViewById(R.id.button_login);

        textInputLayoutUsername.setHint("请输入用户名");
        textInputLayoutPassword.setHint("请输入密码");
    }

    void initListener() {
        imageButtonWeChat.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        textViewNewUser.setOnClickListener(this);
        textViewForget.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.imageButton_weChat:
                break;
            case R.id.button_login:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.textView_forget:
                break;
            case R.id.textView_newUser:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }
}
