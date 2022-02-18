package com.sobesworld.wgucoursecommander.database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;
import com.sobesworld.wgucoursecommander.ui.TermDetail;

public class TermListAdapter extends ListAdapter<TermEntity, TermListAdapter.TermViewHolder> {

    public TermListAdapter(@NonNull DiffUtil.ItemCallback<TermEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TermListAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        TermEntity current = getItem(position);
        holder.termItemView.setText(current.getTermTitle());
    }

    public static class TermDiff extends DiffUtil.ItemCallback<TermEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return oldItem.getTermID() == newItem.getTermID() && oldItem.getTermTitle().equals(newItem.getTermTitle()) &&
                    oldItem.getTermStartDate().equals(newItem.getTermStartDate()) &&
                    oldItem.getTermEndDate().equals(newItem.getTermEndDate()) && oldItem.getTermNotes().equals(newItem.getTermNotes());
        }
    }

    class TermViewHolder extends RecyclerView.ViewHolder {

        private final TextView termItemView;

        private TermViewHolder(@NonNull View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.item_view);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Context context = view.getContext();
                final TermEntity current = getItem(position);
                Intent intent = new Intent(context, TermDetail.class);
                intent.putExtra(context.getString(R.string.is_new_record), false);
                intent.putExtra(context.getString(R.string.idnum), current.getTermID());
                intent.putExtra(context.getString(R.string.title), current.getTermTitle());
                intent.putExtra(context.getString(R.string.start_date), current.getTermStartDate());
                intent.putExtra(context.getString(R.string.end_date), current.getTermEndDate());
                intent.putExtra(context.getString(R.string.notes), current.getTermNotes());
                context.startActivity(intent);
            });
        }
    }
}
