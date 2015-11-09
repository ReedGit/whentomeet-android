package com.giot.meeting;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.adapters.LineAdapter;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeActivity extends AppCompatActivity {

    private final static String TAG = TimeActivity.class.toString();

    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private Context context;
    private String meetId;
    private List<String> dateList;
    private JSONObject dateJson;
    private JSONObject selectedJson;
    private ExpandableListAdapter adapter;
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        meetId = getIntent().getStringExtra("meetId");
        context = TimeActivity.this;
        initView();
        initListener();
        timeDate();
    }

    private void initView() {
        expandableListView = (ExpandableListView) findViewById(R.id.line_time);
        toolbar = (Toolbar) findViewById(R.id.toolbar_time);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.toolbar_back);
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void timeDate() {
        progressDialog = ProgressDialog.show(context, null, "请稍后......", true);

        String url = SysConstants.BaseUrl + SysConstants.DoFindAllTime;
        url = UrlParamCompleter.complete(url, meetId);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                dateList = new ArrayList<>();
                dateJson = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String result = jsonObject.getString("date");
                        String[] arr = result.split("#");
                        boolean flag = true;
                        for (int j = 0; j < dateList.size(); j++) {
                            if (dateList.get(j).equals(arr[0])) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            dateList.add(arr[0]);
                        }
                        if (!dateJson.has(arr[0])) {
                            dateJson.put(arr[0], new JSONArray());
                        }
                        dateJson.getJSONArray(arr[0]).put(arr[1]);
                    }
                    personDate();
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
                Toast.makeText(getApplicationContext(), "检查下你的网络吧！", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(context).cancelAll(TAG);
        request.setTag(TAG);
        VolleyUtil.getRequestQueue(context).add(request);
    }

    private void personDate() {

        String url = SysConstants.BaseUrl + SysConstants.DoFindAllPerson;

        url = UrlParamCompleter.complete(url, meetId);

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                selectedJson = new JSONObject();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String[] date = jsonObject.getString("ptime").split(",");
                        String email = jsonObject.getString("personEmail");
                        String name = jsonObject.getString("name");
                        String person;
                        if (!name.isEmpty())
                            person = name;
                        else
                            person = email;
                        for (int j = 0; j < date.length; j++) {
                            if (!selectedJson.has(date[j])) {
                                selectedJson.put(date[j], new JSONArray());
                            }
                            selectedJson.getJSONArray(date[j]).put(person);
                        }
                    }
                    adapter = new LineAdapter(TimeActivity.this, dateJson, dateList, selectedJson);
                    expandableListView.setAdapter(adapter);
                    int groupCount = expandableListView.getCount();
                    for (int k = 0; k < groupCount; k++)
                        expandableListView.expandGroup(k);
                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView expandableListView, View view, final int groupPosition, final int childPosition, long l) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("确定选择此时间为meet举办时间？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    confirmTime(groupPosition,childPosition);
                                }
                            });
                            builder.setNegativeButton("取消", null);
                            builder.create().show();
                            return false;
                        }
                    });
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
                Toast.makeText(getApplicationContext(), "检查下你的网络吧！", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(context).cancelAll(TAG);
        request.setTag(TAG);
        VolleyUtil.getRequestQueue(context).add(request);
    }

    private void confirmTime(int groupPosition, int childPosition){
        //String url =
    }
}
