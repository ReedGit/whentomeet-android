package com.giot.meeting.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giot.meeting.R;
import com.giot.meeting.adapters.ContactRecyclerAdapter;
import com.giot.meeting.widgets.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 伟 on 2015/10/6.
 */
public class ContactFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private ContactRecyclerAdapter adapter;
    public List<String> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        for (int i = 0; i < 10; i++) {
            list.add("item" + i);
        }

        View view = inflater.inflate(R.layout.drawer_contact, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contact_refresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);

        /**
         * 设置recyclerView适配器、布局、动画效果以及分割线
         */
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ContactRecyclerAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        /**
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (int i = 0; i < 5; i++) {
                    int temp = 0;
                    list.add(temp + i, "item" + temp + i);
                }
                adapter = new ContactRecyclerAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        /**
         * 上拉加载
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleLastIndex = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int itemsLastIndex = adapter.getItemCount() - 1;    //数据集最后一项的索引
                if (newState == RecyclerView.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    for (int i = 0; i < 5; i++) {
                        int temp = list.size();
                        list.add(temp, "item" + temp);
                        adapter.notifyItemInserted(temp - 1);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleLastIndex = linearLayoutManager.findLastVisibleItemPosition();

            }

        });
        return view;
    }
}
