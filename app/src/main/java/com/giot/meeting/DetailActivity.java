package com.giot.meeting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends AppCompatActivity {

    private final static String TAG = DetailActivity.class.toString();

    private Toolbar toolbarDetail;
    private TextView detailTitle, detailContent, detailLocation, detailDuration, detailRemark, detailVisible, confirmTime;
    private String meetId;
    private Context context;
    private ProgressDialog progressDialog;
    private LinearLayout meetDetail;
    private Button checkTime;
    private String meetTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = getApplicationContext();
        meetId = getIntent().getStringExtra("meetId");
        initView();
        showData();
        initListener();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        meetDetail = (LinearLayout) findViewById(R.id.meeting_detail);
        detailTitle = (TextView) findViewById(R.id.detail_title);
        detailContent = (TextView) findViewById(R.id.detail_content);
        detailLocation = (TextView) findViewById(R.id.detail_location);
        detailDuration = (TextView) findViewById(R.id.detail_duration);
        detailRemark = (TextView) findViewById(R.id.detail_remark);
        detailVisible = (TextView) findViewById(R.id.detail_visible);
        checkTime = (Button) findViewById(R.id.check_time);
        toolbarDetail = (Toolbar) findViewById(R.id.toolbar_detail);
        confirmTime = (TextView) findViewById(R.id.confirm_time);
        setSupportActionBar(toolbarDetail);
        toolbarDetail.setNavigationIcon(R.mipmap.toolbar_back);

    }

    private void initListener() {
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbarDetail.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        Intent intent = new Intent(DetailActivity.this, SelectActivity.class);
                        intent.putExtra("meetId", meetId);
                        intent.putExtra("meetTheme", meetTheme);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        checkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, TimeActivity.class);
                intent.putExtra("meetId", meetId);
                intent.putExtra("meetTheme", meetTheme);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void showData() {
        progressDialog = ProgressDialog.show(DetailActivity.this, null, "请稍等....", true);
        String url = SysConstants.BaseUrl + SysConstants.DoFindMeeting;
        url = UrlParamCompleter.complete(url, meetId);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    meetTheme = jsonObject.getString("title");
                    detailTitle.setText(meetTheme);
                    detailLocation.setText(jsonObject.getString("location"));
                    detailContent.setText(jsonObject.getString("content"));
                    detailRemark.setText(jsonObject.getString("remark"));
                    if (!(jsonObject.getString("confirmTime").equals("null"))) {
                        confirmTime.setText(jsonObject.getString("confirmTime"));
                        toolbarDetail.getMenu().clear();
                    }
                    String durationString;
                    int durationCount = Integer.parseInt(jsonObject.getString("duration"));
                    double duration = durationCount / 4.0;
                    if (duration == 0.25) {
                        durationString = "15分钟";
                    } else if (duration == 24) {
                        durationString = "全天";
                    } else {
                        durationString = duration + "小时";
                    }
                    detailDuration.setText(durationString);
                    String visible = jsonObject.getString("visible");
                    if (visible.equals("1")) {
                        detailVisible.setText("所有人可见");
                    } else {
                        detailVisible.setText("只有我可见");
                    }
                    meetDetail.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
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
                Toast.makeText(context, "网络连接有问题！", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(context).cancelAll(TAG);
        request.setTag(TAG);
        VolleyUtil.getRequestQueue(context).add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            toolbarDetail.getMenu().clear();
            confirmTime.setText(data.getStringExtra("confirmTime"));
        }
    }
}
