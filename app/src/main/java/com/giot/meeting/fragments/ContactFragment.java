package com.giot.meeting.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
    private CoordinatorLayout contact;
    private ContactRecyclerAdapter adapter;
    public List<JSONObject> list;
    private MeetApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.drawer_contact, container, false);
        app = (MeetApplication) getActivity().getApplication();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contact_refresh);
        contact = (CoordinatorLayout) view.findViewById(R.id.contact);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_recycler);

        /**
         * set recyclerView's layout, animator&decoration
         */
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        swipeRefreshLayout.setRefreshing(true);
        contactData();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        /**
         * pull to refresh
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<>();
                contactData();
            }
        });
        return view;
    }

    /**
     * show contact's data
     */
    protected void contactData() {
        String url = SysConstants.BaseUrl + SysConstants.DoFindAllContact;
        url = UrlParamCompleter.complete(url, app.getUser());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    if (jsonArray.length()==0){
                        contact.setBackgroundResource(R.mipmap.contact_null);
                    }else {
                        contact.setBackgroundResource(R.color.windowBackground);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(jsonArray.getJSONObject(i));
                    }
                    adapter = new ContactRecyclerAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                    adapter.setOnItemClickListener(new ContactRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemLongClick(View view, final int position) {
                            try {
                                Snackbar snackbar = Snackbar.make(view, "你想删除" + list.get(position).getString("username") + "么？", Snackbar.LENGTH_SHORT).setAction("删除", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            deleteContact(list.get(position).getString("contactid"), position);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).setActionTextColor(Color.RED);
                                ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                                snackbar.show();
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
                Log.e(TAG, volleyError.getMessage(), volleyError);
                Toast.makeText(context, "网络连接有问题", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        StringRequest dataRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        VolleyUtil.getRequestQueue(context).cancelAll(getActivity());
        dataRequest.setTag(getActivity());
        VolleyUtil.getRequestQueue(context).add(dataRequest);

    }

    /**
     * delete contact
     * @param contactId contactId
     * @param position recyclerView's position
     */
    private void deleteContact(String contactId, final int position) {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null, "删除中......", true);
        String url = SysConstants.BaseUrl + SysConstants.DoDeleteContact;
        url = UrlParamCompleter.complete(url, contactId);
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
                    contact.setBackgroundResource(R.mipmap.contact_null);
                }else {
                    contact.setBackgroundResource(R.color.windowBackground);
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
