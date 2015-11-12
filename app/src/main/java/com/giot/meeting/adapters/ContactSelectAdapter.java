package com.giot.meeting.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.giot.meeting.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactSelectAdapter extends RecyclerView.Adapter<ContactSelectAdapter.ContactViewHolder> {
    private LayoutInflater layoutInflater;
    private List<JSONObject> list;
    private Set<Integer> positionSet;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactEmail, contactName;
        CheckBox cbSelect;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            contactEmail = (TextView) itemView.findViewById(R.id.contact_select_email);
            contactName = (TextView) itemView.findViewById(R.id.contact_select_name);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cb_select);
        }
    }

    public ContactSelectAdapter(Context context, List<JSONObject> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }


    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        try {
            holder.contactEmail.setText(list.get(position).getString("username"));
            holder.contactName.setText(list.get(position).getString("nickname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    positionSet.add(position);
                } else {
                    positionSet.remove(position);
                }
            }
        });
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        positionSet = new HashSet<>();
        return new ContactViewHolder(layoutInflater.inflate(R.layout.view_contact, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<Integer> getListSelect(){
        List<Integer> list = new ArrayList<>();
        list.addAll(positionSet);
        return list;
    }
}
