package com.giot.meeting.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.DetailActivity;
import com.giot.meeting.MeetApplication;
import com.giot.meeting.NewMeetingActivity;
import com.giot.meeting.R;
import com.giot.meeting.adapters.MeetingRecyclerAdapter;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;
import com.giot.meeting.widgets.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingFragment extends Fragment {

    private final static String TAG = MeetingFragment.class.toString();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private MeetingRecyclerAdapter adapter;
    public List<JSONObject> list;
    private MeetApplication app;
    private int page = 0;
    private int pageCount;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_meeting, container, false);
        app = (MeetApplication) getActivity().getApplication();
        recyclerView = (RecyclerView) view.findViewById(R.id.meeting_recycler);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_meeting);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.meeting_refresh);

        /**
         * 设置recyclerView适配器、布局、动画效果以及分割线
         */
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        meetingData("0");
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        /**
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<>();
                meetingData("0");
            }
        });

        /**
         * 上拉加载
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int visibleLastIndex = 0;

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemsLastIndex = adapter.getItemCount() - 1;    //数据集最后一项的索引
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {

                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    /*for (int i = 0; i < 5; i++) {
                        int temp = list.size();
                        list.add(temp, "item" + temp);
                        adapter.notifyItemInserted(temp - 1);
                    }*/
                    page++;
                    if (page < pageCount) {
                        String url = SysConstants.BaseUrl + SysConstants.DoFindAllMeeting;
                        url = UrlParamCompleter.complete(url, app.getUser(), Integer.toString(page));
                        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    pageCount = jsonObject.getInt("page");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        int temp = list.size();
                                        list.add(temp, jsonArray.getJSONObject(i));
                                        adapter.notifyItemInserted(temp - 1);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        Response.ErrorListener errorListener = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                swipeRefreshLayout.setRefreshing(false);
                                Log.e(TAG, volleyError.getMessage(), volleyError);
                                Toast.makeText(getActivity().getApplicationContext(), "网络连接有问题", Toast.LENGTH_SHORT).show();
                            }
                        };
                        JsonObjectRequest dataRequest = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
                    /*Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.i(TAG, "meetingData:" + s);
                            try {
                                JSONArray jsonArray = new JSONArray(s);
                                Log.i(TAG, "jsonArray:" + jsonArray + " " + jsonArray.length());
                                if (jsonArray.length() == 0) {
                                    Toast.makeText(getActivity().getApplicationContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        int temp = list.size();
                                        list.add(temp, jsonArray.getJSONObject(i));
                                        adapter.notifyItemInserted(temp - 1);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            swipeRefreshLayout.setRefreshing(false);
                            Log.e(TAG, volleyError.getMessage(), volleyError);
                            Toast.makeText(getActivity().getApplicationContext(), "网络连接有问题", Toast.LENGTH_SHORT).show();
                        }
                    };
                    StringRequest dataRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);*/
                        VolleyUtil.getRequestQueue(getActivity().getApplicationContext()).cancelAll(getActivity());
                        dataRequest.setTag(getActivity());
                        VolleyUtil.getRequestQueue(getActivity().getApplicationContext()).add(dataRequest);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleLastIndex = layoutManager.findLastVisibleItemPosition();

            }
        });

        /**
         * 新建meeting
         */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewMeetingActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    protected void meetingData(String start) {

        String url = SysConstants.BaseUrl + SysConstants.DoFindAllMeeting;
        url = UrlParamCompleter.complete(url, app.getUser(), start);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    pageCount = jsonObject.getInt("page");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(jsonArray.getJSONObject(i));
                    }
                    adapter = new MeetingRecyclerAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new MeetingRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, int position) {
                            try {
                                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                                intent.putExtra("meetId", list.get(position).getString("meetId"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getActivity().getApplicationContext(), "网络连接有问题", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest dataRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(getActivity().getApplicationContext()).cancelAll(getActivity());
        dataRequest.setTag(getActivity());
        VolleyUtil.getRequestQueue(getActivity().getApplicationContext()).add(dataRequest);
    }
}
