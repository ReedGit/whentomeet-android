package com.giot.meeting.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.giot.meeting.R;

import java.util.List;

public class ShareRecyclerAdapter extends RecyclerView.Adapter<ShareRecyclerAdapter.ShareViewHolder> {

    private List<String> name;
    private List<String> email;
    private LayoutInflater layoutInflater;

    public ShareRecyclerAdapter(List<String> name, List<String> email, Context context) {
        this.name = name;
        this.email = email;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(layoutInflater.inflate(R.layout.view_person, parent, false));
    }

    @Override
    public void onBindViewHolder(final ShareViewHolder holder, final int position) {
        String emailText = "邮箱：" + email.get(position);
        String nameText = "姓名：" + name.get(position);
        holder.tvEmail.setText(emailText);
        holder.tvName.setText(nameText);

        if (mOnItemClickListener != null) {

            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.OnItemClick(holder.imageButton, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return email.size();
    }

    public class ShareViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail;
        ImageButton imageButton;

        public ShareViewHolder(View itemView) {
            super(itemView);
            tvEmail = (TextView) itemView.findViewById(R.id.tv_email);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            imageButton = (ImageButton) itemView.findViewById(R.id.delete);
        }
    }

}
