package com.giot.meeting.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.List;

public class MeetingFragment extends Fragment {

    private final static String TAG = MeetingFragment.class.toString();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout meeting;
    private MeetingRecyclerAdapter adapter;
    public List<JSONObject> list;
    private MeetApplication app;
    private int pageCount;
    //private HeaderViewRecyclerAdapter recyclerAdapter;
    //private View loadMoreView;
    private int page = 0;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_meeting, container, false);
        app = (MeetApplication) getActivity().getApplication();
        recyclerView = (RecyclerView) view.findViewById(R.id.meeting_recycler);
        meeting = (CoordinatorLayout) view.findViewById(R.id.meeting);
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_meeting);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.meeting_refresh);

        /**
         * 设置recyclerView适配器、布局、动画效果以及分割线
         */
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
                page = 0;
                meetingData("0");
            }
        });

        /**
         * 上拉加载
         */
       /* recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                Log.i(TAG, "当前page:" + page);
                loadMoreData(page);
            }
        });*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int visibleLastIndex = 0;

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemsLastIndex = adapter.getItemCount() - 1;    //数据集最后一项的索引
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {
                    loadMoreData();
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

    private void loadMoreData() {
        if ((page / SysConstants.items) < pageCount) {
            Log.i(TAG, "当前page:" + page + " " + pageCount);
            page = page + SysConstants.items;
            //loadMoreView.setVisibility(View.VISIBLE);
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
            VolleyUtil.getRequestQueue(getActivity()).cancelAll(TAG);
            dataRequest.setTag(TAG);
            VolleyUtil.getRequestQueue(getActivity()).add(dataRequest);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
            //loadMoreView.setVisibility(View.GONE);
        }
    }

    /*private void createLoadMoreView() {
        loadMoreView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, recyclerView, false);
        recyclerAdapter.addFooterView(loadMoreView);
        if (pageCount > 1) {
            loadMoreView.setVisibility(View.VISIBLE);
        } else {
            loadMoreView.setVisibility(View.GONE);
        }
    }*/

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
                    if (jsonArray.length()==0){
                        meeting.setBackgroundResource(R.mipmap.empty);
                    }else {
                        meeting.setBackgroundResource(R.color.windowBackground);
                    }
                    pageCount = jsonObject.getInt("page");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(jsonArray.getJSONObject(i));
                    }
                    adapter = new MeetingRecyclerAdapter(getActivity(), list);
                    //recyclerAdapter = new HeaderViewRecyclerAdapter(adapter);
                    recyclerView.setAdapter(adapter);
                    //createLoadMoreView();
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

                        @Override
                        public void OnItemLongClick(View view, final int position) {
                            Snackbar snackbar = Snackbar.make(view, "你想删除该meet么？", Snackbar.LENGTH_SHORT).setAction("删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        deleteMeet(list.get(position).getString("meetId"), position);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setActionTextColor(Color.RED);
                            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                            snackbar.show();
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
        VolleyUtil.getRequestQueue(getActivity()).cancelAll(TAG);
        dataRequest.setTag(TAG);
        VolleyUtil.getRequestQueue(getActivity()).add(dataRequest);
    }

    private void deleteMeet(final String meetId, final int position) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "删除中......", true);
        String url = SysConstants.BaseUrl + SysConstants.DoDeleteMeeting;
        url = UrlParamCompleter.complete(url, meetId);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                list.remove(position);
                adapter.notifyItemRemoved(position);
                if (position != list.size()) {
                    adapter.notifyItemRangeChanged(position, list.size() - position);
                }
                Toast.makeText(getActivity().getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                if (list.size()==0){
                    meeting.setBackgroundResource(R.mipmap.empty);
                }else {
                    meeting.setBackgroundResource(R.color.windowBackground);
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(getActivity().getApplicationContext(), "网络连接有问题", Toast.LENGTH_SHORT).show();
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(getActivity()).cancelAll(TAG);
        request.setTag(TAG);
        VolleyUtil.getRequestQueue(getActivity()).add(request);
    }
}
