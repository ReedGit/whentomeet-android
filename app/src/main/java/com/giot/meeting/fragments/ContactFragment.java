package com.giot.meeting.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.giot.meeting.MeetApplication;
import com.giot.meeting.R;
import com.giot.meeting.adapters.ContactRecyclerAdapter;
import com.giot.meeting.configs.SysConstants;
import com.giot.meeting.utils.UrlParamCompleter;
import com.giot.meeting.utils.VolleyUtil;
import com.giot.meeting.widgets.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment extends Fragment {

    private final static String TAG = ContactFragment.class.toString();
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ContactRecyclerAdapter adapter;
    public List<JSONObject> list;
    private MeetApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.drawer_contact, null);
        app = (MeetApplication) getActivity().getApplication();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contact_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);

        /**
         * 设置recyclerView适配器、布局、动画效果以及分割线
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        contactData();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        /**
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*for (int i = 0; i < 5; i++) {
                    int temp = 0;
                    list.add(temp + i, "item" + temp + i);
                }
                adapter = new ContactRecyclerAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);*/
                list = new ArrayList<>();
                contactData();
            }
        });

        /**
         * 上拉加载
         */
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleLastIndex = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemsLastIndex = adapter.getItemCount() - 1;    //数据集最后一项的索引
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    *//*for (int i = 0; i < 5; i++) {
                        int temp = list.size();
                        list.add(temp, "item" + temp);
                        adapter.notifyItemInserted(temp - 1);
                    }*//*
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleLastIndex = linearLayoutManager.findLastVisibleItemPosition();

            }

        });*/
        return view;
    }

    protected void contactData(){
        String url = SysConstants.BaseUrl + SysConstants.DoFindAllContact;
        url=UrlParamCompleter.complete(url,app.getUser());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(TAG, "contactData:" + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    Log.i(TAG,"jsonArray:"+jsonArray+" "+jsonArray.length());
                    for (int i=0;i<jsonArray.length();i++) {
                        list.add(jsonArray.getJSONObject(i));
                    }
                    adapter = new ContactRecyclerAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.getMessage(),volleyError);
                Toast.makeText(context, "网络连接有问题", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        StringRequest dataRequest = new StringRequest(Request.Method.GET,url,listener,errorListener);
        VolleyUtil.getRequestQueue(context).cancelAll(getActivity());
        dataRequest.setTag(getActivity());
        VolleyUtil.getRequestQueue(context).add(dataRequest);

    }
}
