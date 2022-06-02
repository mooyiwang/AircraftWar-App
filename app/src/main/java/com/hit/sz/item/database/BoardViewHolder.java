package com.hit.sz.item.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hit.sz.R;

class BoardViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameItemView;
    private final TextView rankItemView;
    private final TextView scoreItemView;
    private final TextView dateItemView;

    private BoardViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        rankItemView = itemView.findViewById(R.id.rankView);
        scoreItemView = itemView.findViewById(R.id.scoreView);
        dateItemView = itemView.findViewById(R.id.dateView);
    }

    public void bind(String text) {
        nameItemView.setText(text);
        rankItemView.setText(text);
        scoreItemView.setText(text);
        dateItemView.setText(text);
    }
    public void bind(String name, String score,String date, String rank) {
        nameItemView.setText(name);
        rankItemView.setText(rank);
        scoreItemView.setText(score);
        dateItemView.setText(date);
    }

    static BoardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_recycler_view, parent, false);
        return new BoardViewHolder(view);
    }
}