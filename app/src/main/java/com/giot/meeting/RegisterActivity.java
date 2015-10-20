package com.giot.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 伟 on 2015/10/13.
 */
public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbarRegister;
    private TextInputLayout textInputLayoutEmail, textInputLayoutUsername, textInputLayoutPassword, textInputLayoutRepassword;
    private EditText editTextEmail, editTextUsername, editTextPassword, editTextRepassword;
    private Button button;
    private String email, username, password, repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString();
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();
                repassword = editTextRepassword.getText().toString();


                /*
                 * 验证邮箱
                 */
                if (email == null || email.isEmpty()) {
                    textInputLayoutEmail.setError("邮箱不能为空");
                    textInputLayoutEmail.setErrorEnabled(true);
                    return;
                } else if (!email.matches("^[a-z0-9]+([._\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$")) {
                    textInputLayoutEmail.setError("邮箱格式不正确");
                    textInputLayoutEmail.setErrorEnabled(true);
                    return;
                }
                textInputLayoutEmail.setErrorEnabled(false);

                /*
                 * 验证用户名
                 */
                if (username == null || username.isEmpty()) {
                    textInputLayoutUsername.setError("用户名不能为空");
                    textInputLayoutUsername.setErrorEnabled(true);
                    return;
                }
                textInputLayoutUsername.setErrorEnabled(false);

                /*
                 * 验证密码
                 */
                if (password == null || password.isEmpty()) {
                    textInputLayoutPassword.setError("密码不能为空");
                    textInputLayoutPassword.setErrorEnabled(true);
                    return;
                } else if (password.length() < 6) {
                    textInputLayoutPassword.setError("密码长度不能小于6位");
                    textInputLayoutPassword.setErrorEnabled(true);
                    return;
                }
                textInputLayoutPassword.setErrorEnabled(false);

                /*
                 * 验证密码一致性
                 */
                if (repassword == null || repassword.isEmpty()) {
                    textInputLayoutRepassword.setError("再次输入密码不能为空");
                    textInputLayoutRepassword.setErrorEnabled(true);
                    return;
                } else if (!repassword.equals(password)) {
                    textInputLayoutRepassword.setError("两次密码输入不一致");
                    textInputLayoutRepassword.setErrorEnabled(true);
                    return;
                }
                textInputLayoutRepassword.setErrorEnabled(false);

                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initView() {
        toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayout_email);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayout_username);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayout_password);
        textInputLayoutRepassword = (TextInputLayout) findViewById(R.id.textInputLayout_repassword);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextRepassword = (EditText) findViewById(R.id.editText_repassword);
        button = (Button) findViewById(R.id.button_register);

        /**
         * 设置toolbar样式
         */
        toolbarRegister.setTitle("");
        setSupportActionBar(toolbarRegister);
        toolbarRegister.setNavigationIcon(R.mipmap.toolbar_back);
        toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 设置textInputLayout样式
         */
        textInputLayoutEmail.setHint("请输入邮箱：");
        textInputLayoutUsername.setHint("请输入用户名：");
        textInputLayoutPassword.setHint("请输入密码：");
        textInputLayoutRepassword.setHint("请再次输入密码：");
    }

}
