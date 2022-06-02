package com.hit.sz.item.database;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class RecordListAdapter  extends ListAdapter<MyRecord, BoardViewHolder> {

    public RecordListAdapter (@NonNull DiffUtil.ItemCallback<MyRecord> diffCallback) {
        super(diffCallback);
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BoardViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        MyRecord current = getItem(position);
        holder.bind(current.getName(),current.getScore(),current.getDate(),String.valueOf(position+1));
    }

    public static class RecordDiff extends DiffUtil.ItemCallback<MyRecord> {

        @Override
        public boolean areItemsTheSame(@NonNull MyRecord oldItem, @NonNull MyRecord newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyRecord oldItem, @NonNull MyRecord newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }
}