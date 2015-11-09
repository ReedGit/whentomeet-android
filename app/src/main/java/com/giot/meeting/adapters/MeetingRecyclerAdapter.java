package com.giot.meeting.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giot.meeting.DetailActivity;
import com.giot.meeting.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;


public class MeetingRecyclerAdapter extends RecyclerView.Adapter<MeetingRecyclerAdapter.MeetingViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<JSONObject> list;

    public class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView textView, textPerson, textCreateTime;

        public MeetingViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            textCreateTime = (TextView) itemView.findViewById(R.id.text_create_time);
            textPerson = (TextView) itemView.findViewById(R.id.text_person);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MeetingRecyclerAdapter(Context context, List<JSONObject> list) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public void onBindViewHolder(final MeetingRecyclerAdapter.MeetingViewHolder holder, final int position) {
        try {
            holder.textView.setText(list.get(position).getString("title"));
            holder.textCreateTime.setText(list.get(position).getString("createTime"));
            String person = list.get(position).getString("response") + "/" + list.get(position).getString("guys");
            holder.textPerson.setText(person);
            if (mOnItemClickListener != null)
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.OnItemClick(holder.itemView, position);
                    }
                });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MeetingRecyclerAdapter.MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingViewHolder(layoutInflater.inflate(R.layout.card_meeting, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
