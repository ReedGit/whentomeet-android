package com.giot.meeting.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giot.meeting.R;

import java.util.List;

/**
 * Created by 伟 on 2015/10/8.
 */
public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ContactViewHolder> {

    private final LayoutInflater contactLayoutInflater;
    private final Context contactContext;
    private List<String> list;

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactInvited,contactName;

        public ContactViewHolder(final View itemView) {
            super(itemView);
            contactInvited = (TextView) itemView.findViewById(R.id.contact_invited);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Contact" + textView.getText() + "内容", Snackbar.LENGTH_SHORT).show();
                }
            });*/
        }
    }

    public ContactRecyclerAdapter(Context context, List<String> list) {
        this.contactContext = context;
        this.contactLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.contactInvited.setText(list.get(position)+"invited");
        holder.contactName.setText(list.get(position)+"name");
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(contactLayoutInflater.inflate(R.layout.card_contact, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
