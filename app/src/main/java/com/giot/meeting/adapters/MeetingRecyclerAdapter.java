package com.giot.meeting.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giot.meeting.DetailActivity;
import com.giot.meeting.R;

import java.util.List;

/**
 * Created by 伟 on 2015/10/8.
 */
public class MeetingRecyclerAdapter extends RecyclerView.Adapter<MeetingRecyclerAdapter.MeetingViewHolder> {

    private static LayoutInflater meetingLayoutInflater;
    private static Context meetingContext;
    private List<String> list;

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MeetingViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "meeting内容", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(meetingContext, DetailActivity.class);
                    meetingContext.startActivity(intent);

                }
            });
        }
    }

    public MeetingRecyclerAdapter(Context context, List<String> list) {
        this.meetingContext = context;
        this.meetingLayoutInflater = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeetingViewHolder(meetingLayoutInflater.inflate(R.layout.card_meeting, parent, false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
