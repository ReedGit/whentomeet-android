package com.giot.meeting.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.giot.meeting.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LineAdapter extends BaseExpandableListAdapter {

    private JSONObject dateJson;
    private List<String> dateList;
    private LayoutInflater inflater;
    private JSONObject selectedJson;

    public LineAdapter(Context context, JSONObject dateJson, List<String> dateList, JSONObject selectedJson) {
        this.dateJson = dateJson;
        this.dateList = dateList;
        this.inflater = LayoutInflater.from(context);
        this.selectedJson = selectedJson;
    }

    @Override
    public int getGroupCount() {
        return dateList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        try {
            return dateJson.getJSONArray(dateList.get(i)).length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String getGroup(int i) {
        return dateList.get(i);
    }

    @Override
    public String getChild(int i, int i1) {
        try {
            return dateJson.getJSONArray(dateList.get(i)).getString(i1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        int count = 0;
        for (int j = 0; j < i ; j++)
            count = count + getChildrenCount(j);
        count = count + i1;
        return count;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        GroupViewHolder holder = new GroupViewHolder();
        view = inflater.inflate(R.layout.line_date, viewGroup, false);
        holder.groupName = (TextView) view.findViewById(R.id.tv_date);
        holder.groupName.setText(dateList.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder viewHolder = new ChildViewHolder();
        view = inflater.inflate(R.layout.line_time, viewGroup, false);
        String child = getChild(i, i1);
        viewHolder.childName = (TextView) view.findViewById(R.id.tv_time);
        viewHolder.childPerson = (TextView) view.findViewById(R.id.tv_person);
        viewHolder.childName.setText(child);
        try {
            if (selectedJson.has(Long.toString(getChildId(i, i1)))) {
                JSONArray jsonArray = selectedJson.getJSONArray(Long.toString(getChildId(i, i1)));
                String personName = "";
                for (int j = 0; j < jsonArray.length(); j++) {
                    personName = personName  + jsonArray.getString(j) + ", ";
                }
                viewHolder.childPerson.setText(personName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupViewHolder {
        TextView groupName;

    }

    private class ChildViewHolder {
        TextView childName;
        TextView childPerson;
    }
}
