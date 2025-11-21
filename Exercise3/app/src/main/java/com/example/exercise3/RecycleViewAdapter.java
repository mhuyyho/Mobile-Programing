package com.example.exercise3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(int position);
    }

    private final Context context;
    private final int layoutRes;
    private final List<MonHoc> list;
    private final OnItemClickListener clickListener;
    private final OnItemLongClickListener longClickListener;

    public RecycleViewAdapter(Context context, int layoutRes, List<MonHoc> list,
                              OnItemClickListener clickListener,
                              OnItemLongClickListener longClickListener) {
        this.context = context;
        this.layoutRes = layoutRes;
        this.list = list;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    public static class VH extends RecyclerView.ViewHolder {
        public final ImageView ivPic;
        public final TextView tvName;
        public final TextView tvDesc;

        public VH(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.iv_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutRes, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, final int position) {
        MonHoc data = list.get(position);
        holder.tvName.setText(data.getName());
        holder.tvDesc.setText(data.getDesc());
        holder.ivPic.setImageResource(data.getPic());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) return longClickListener.onLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}