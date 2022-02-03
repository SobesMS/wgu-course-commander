package com.sobesworld.wgucoursecommander.viewmodel;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

public class TermViewHolder extends RecyclerView.ViewHolder {

    private final TextView termItemView;
    // private TermEntity mTerm;

    private TermViewHolder(View itemView) {
        super(itemView);
        termItemView = itemView.findViewById(R.id.term_item);

        /* TODO: OnClickListener for lists
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Intent intent = new Intent(context, TermAdd.class);
            }
        });*/
    }

    public void bind(String text) {
        termItemView.setText(text);
        // mTerm = current;
    }

    public static TermViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_recyclerview_item, parent, false);
        return new TermViewHolder(view);
    }
}
