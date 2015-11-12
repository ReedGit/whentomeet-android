package com.giot.meeting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.*;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = LoginActivity.class.toString();

    private EditText editTextEmail, editTextPassword;
    private TextView textViewNewUser;
    private Button buttonLogin;
    private ImageButton imageButtonWeChat;
    private ProgressDialog progressDialog;
    private SharedPreferences sp;
    private MeetApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        sp = getSharedPreferences("saveName", MODE_PRIVATE);
        editTextEmail.setText(sp.getString("email", ""));
        editTextPassword.setText(sp.getString("password", ""));
        initListener();
    }

    protected void initView() {

        app = (MeetApplication) getApplication();
        imageButtonWeChat = (ImageButton) findViewById(R.id.imageButton_weChat);
        editTextEmail = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        textViewNewUser = (TextView) findViewById(R.id.textView_newUser);
        buttonLogin = (Button) findViewById(R.id.button_login);

    }

    protected void initListener() {
        imageButtonWeChat.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        textViewNewUser.setOnClickListener(this);

    }

    protected void login(final String email, final String password) {
        //显示进度条
        progressDialog = ProgressDialog.show(LoginActivity.this, null, "努力登录中(๑•̀ㅂ•́)و✧", false, false);

        String loginUrl = SysConstants.BaseUrl + SysConstants.DoGetUser;
        loginUrl = UrlParamCompleter.complete(loginUrl, email, password);
        StringRequest loginRequest = new StringRequest(Method.GET, loginUrl, new Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if (!s.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        int validate = jsonObject.getInt("validate");
                        if (validate == 1) {
                            Editor editor = sp.edit();
                            editor.putString("email", email);
                            editor.putString("password", password);
                            editor.apply();
                            app.setIsLogin(true);
                            app.setUser(jsonObject.getString("userid"));
                            app.setEmail(jsonObject.getString("username"));
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("nickName", jsonObject.getString("nickname"));
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "快去邮箱把你的账号激活了(〃＞皿＜)", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getApplicationContext(), SysConstants.errorString, Toast.LENGTH_SHORT).show();
            }
        }

        );
        VolleyUtil.getRequestQueue(this).cancelAll(this);
        loginRequest.setTag(TAG);
        VolleyUtil.getRequestQueue(this).add(loginRequest);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageButton_weChat:
                Toast.makeText(getApplicationContext(),"功能开发中......",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_login:
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                login(email, password);
                break;
            case R.id.textView_newUser:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }
}
