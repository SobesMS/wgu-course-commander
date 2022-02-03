package com.sobesworld.wgucoursecommander.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;
import com.sobesworld.wgucoursecommander.viewmodel.TermViewHolder;

public class TermListAdapter extends ListAdapter<TermEntity, TermViewHolder> {

    public TermListAdapter(@NonNull DiffUtil.ItemCallback<TermEntity> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TermViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(TermViewHolder holder, int position) {
        TermEntity current = getItem(position);
        holder.bind(current.getTermTitle());
    }

    static class TermDiff extends DiffUtil.ItemCallback<TermEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return oldItem.getTermID() == newItem.getTermID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return oldItem.getTermTitle().equals(newItem.getTermTitle());
        }
    }
}
