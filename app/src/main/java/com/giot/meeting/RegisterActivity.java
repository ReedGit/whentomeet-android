package com.giot.meeting;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.Response.*;
import com.android.volley.Request.*;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private String TAG = RegisterActivity.class.toString();

    private EditText editTextUsername, editTextNickname, editTextPassword, editTextRePassword;
    private Button button;
    private String username, nickname, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }

    protected void initView() {
        Toolbar toolbarRegister = (Toolbar) findViewById(R.id.toolbar_register);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextNickname = (EditText) findViewById(R.id.editText_nickname);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        editTextRePassword = (EditText) findViewById(R.id.editText_re_password);
        button = (Button) findViewById(R.id.button_register);

        toolbarRegister.setTitle("");
        setSupportActionBar(toolbarRegister);
        toolbarRegister.setNavigationIcon(R.mipmap.toolbar_back);
        toolbarRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    protected void initListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editTextUsername.getText().toString();
                nickname = editTextNickname.getText().toString();
                password = editTextPassword.getText().toString();
                String rePassword = editTextRePassword.getText().toString();

                if (username == null || username.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!username.matches(SysConstants.RuleMail)) {
                    Toast.makeText(getApplicationContext(),"邮箱格式不正确",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nickname == null || nickname.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"昵称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password == null || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.length() < 8 || password.length() > 20) {
                    Toast.makeText(getApplicationContext(),"密码不能少于8位或大于20位", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rePassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"确认密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!rePassword.equals(password)) {
                    Toast.makeText(getApplicationContext(),"两次输入的密码不同",Toast.LENGTH_SHORT).show();
                    return;
                }
                existUsername();
            }
        });
    }

    //验证邮箱是否注册
    protected void existUsername() {

        progressDialog = ProgressDialog.show(this, null, "(ง ˙o˙)ว我们正在为您创建账号......", true);

        //服务器地址
        String urlFind = SysConstants.BaseUrl + SysConstants.DoFindUser;
        urlFind = UrlParamCompleter.complete(urlFind, username);

        StringRequest existRequest = new StringRequest(Method.GET, urlFind, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (Boolean.parseBoolean(s)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "该邮箱已被注册了，换一个吧！", Toast.LENGTH_SHORT).show();
                } else {
                    postRegister();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage(), volleyError);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), SysConstants.errorString, Toast.LENGTH_SHORT).show();
            }
        });
        VolleyUtil.addRequest(RegisterActivity.this, existRequest, TAG);
    }

    //提交注册信息
    protected void postRegister() {

        String urlRegister = SysConstants.BaseUrl + SysConstants.DoUserRegister;

        Listener<String> listener = new Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                if (s != null) {
                    Toast.makeText(getApplicationContext(), "欢迎加入我们！记得去邮箱激活哦！", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "很抱歉，您注册失败了，请重新尝试!", Toast.LENGTH_LONG).show();
                }
            }
        };
        ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getApplicationContext(), SysConstants.errorString, Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest registerRequest = new StringRequest(Method.POST, urlRegister, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> registerMap = new HashMap<>();
                registerMap.put("nickname", nickname);
                registerMap.put("username", username);
                registerMap.put("password", password);
                return registerMap;
            }
        };
        VolleyUtil.addRequest(RegisterActivity.this, registerRequest, TAG);
    }

}
