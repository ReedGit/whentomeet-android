package com.giot.meeting.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giot.meeting.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private LayoutInflater layoutInflater;
    private List<JSONObject> list;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactInvited,contactName;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            contactInvited = (TextView) itemView.findViewById(R.id.contact_invited);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
        }
    }

    public ContactRecyclerAdapter(Context context, List<JSONObject> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        try {
            holder.contactInvited.setText(list.get(position).getString("username"));
            holder.contactName.setText(list.get(position).getString("nickname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(layoutInflater.inflate(R.layout.card_contact, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
