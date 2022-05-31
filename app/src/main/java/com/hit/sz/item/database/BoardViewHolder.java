package com.hit.sz.item.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hit.sz.R;

class BoardViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;

    private BoardViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.nameView);
    }

    public void bind(String text) {
        wordItemView.setText(text);
    }

    static BoardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_recycler_view, parent, false);
        return new BoardViewHolder(view);
    }
}