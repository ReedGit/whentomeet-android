package com.giot.meeting.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleyUtil {

    private volatile static RequestQueue requestQueue;

    /**
     * 返回RequestQueue单例
     **/
    public static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            synchronized (VolleyUtil.class) {
                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new OkHttpStack());
                }
            }
        }
        return requestQueue;
    }

    public static void addRequest(Context context, Request request, Object TAG){
        getRequestQueue(context).cancelAll(TAG);
        request.setTag(TAG);
        getRequestQueue(context).add(request);
    }
}

