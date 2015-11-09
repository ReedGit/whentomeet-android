package com.giot.meeting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.adapters.ShareRecyclerAdapter;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.VolleyUtil;
import com.giot.meeting.widgets.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectActivity extends AppCompatActivity {

    private final static String TAG = SelectActivity.class.toString();

    private Toolbar toolbarShare;
    private List<String> name;
    private List<String> email;
    private ShareRecyclerAdapter adapter;
    private EditText etName, etEmail;
    private Context context;
    private ProgressDialog progressDialog;
    private MeetApplication app;
    private String meetId;
    private FloatingActionButton btAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_person);
        Intent intent = getIntent();
        meetId = intent.getStringExtra("meetId");
        initView();
        initListener();
    }

    private void initView() {
        context = getApplicationContext();
        app = (MeetApplication) getApplication();
        toolbarShare = (Toolbar) findViewById(R.id.toolbar_share);
        btAdd = (FloatingActionButton) findViewById(R.id.add_share_person);
        toolbarShare.setTitle("");
        setSupportActionBar(toolbarShare);
        toolbarShare.setNavigationIcon(R.mipmap.toolbar_back);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_recycler);
        name = new ArrayList<>();
        email = new ArrayList<>();
        adapter = new ShareRecyclerAdapter(name, email, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        adapter.setOnItemClickListener(new ShareRecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                email.remove(position);
                name.remove(position);
                adapter.notifyItemRemoved(position);
                if (position != email.size()) {
                    adapter.notifyItemRangeChanged(position, email.size() - position);
                }
            }
        });

    }

    private void initListener() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View layout = getLayoutInflater().inflate(R.layout.input_share, null);
                etName = (EditText) layout.findViewById(R.id.share_name);
                etEmail = (EditText) layout.findViewById(R.id.share_email);
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);
                builder.setView(layout);
                builder.setTitle("请输入参与人员信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nameString = etName.getText().toString();
                        String emailString = etEmail.getText().toString();
                        if (emailString.isEmpty()) {
                            Toast.makeText(context, "邮箱不能为空！", Toast.LENGTH_SHORT).show();
                        } else if (!emailString.matches(SysConstants.RuleMail)) {
                            Toast.makeText(context, "邮箱格式不正确！", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int j = 0; j < email.size(); j++) {
                                if (emailString.equals(email.get(j))) {
                                    Toast.makeText(context, "您已添加过该联系人！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            name.add(name.size(), nameString);
                            email.add(email.size(), emailString);
                            adapter.notifyItemInserted(email.size() - 1);
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        toolbarShare.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarShare.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.send_menu:
                        Log.i(TAG, name + " " + email);
                        if (email.size() > 0) {
                            sendEmail();
                        } else {
                            Toast.makeText(context, "请添加人员！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });

    }

    private void sendEmail() {
        progressDialog = ProgressDialog.show(SelectActivity.this, null, "发送中......", true);
        String url = SysConstants.BaseUrl + SysConstants.DoSendMail;
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Boolean result = jsonObject.getBoolean("result");
                    if (result) {
                        Toast.makeText(context, "邮件已成功发送！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(context, "邮件发送失败！请重试！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(context, "网络出现问题！发送失败！", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String nameString = name.get(0), emailString = email.get(0);
                for (int i = 1; i < email.size(); i++) {
                    nameString = nameString + ";;" + name.get(i);
                    emailString = emailString + ";;" + email.get(i);
                }
                nameString = nameString + ";;#";
                emailString = emailString + ";;#";
                Map<String, String> map = new HashMap<>();
                map.put("meetId", meetId);
                map.put("emailString", emailString);
                map.put("nameString", nameString);
                map.put("userId", app.getUser());
                return map;
            }
        };
        VolleyUtil.getRequestQueue(context).

                cancelAll(TAG);

        request.setTag(TAG);
        VolleyUtil.getRequestQueue(context).

                add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_menu, menu);
        return true;
    }
}
