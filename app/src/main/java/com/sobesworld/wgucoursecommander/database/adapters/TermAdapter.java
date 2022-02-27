package com.sobesworld.wgucoursecommander.database.adapters;

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

public class TermAdapter extends ListAdapter<TermEntity, TermAdapter.TermHolder> {
    private OnItemClickListener listener;

    public TermAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TermEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<TermEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return newItem.getTermID() == oldItem.getTermID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TermEntity oldItem, @NonNull TermEntity newItem) {
            return newItem.getTermTitle().equalsIgnoreCase(oldItem.getTermTitle()) && newItem.getTermStartDate().equals(oldItem.getTermStartDate()) &&
                    newItem.getTermEndDate().equals(oldItem.getTermEndDate());
        }
    };

    public TermEntity getTermEntityAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        TermEntity current = getItem(position);
        holder.textViewTermTitle.setText(current.getTermTitle());
        String dates = current.getTermStartDate() + " to " + current.getTermEndDate();
        holder.textViewTermDates.setText(dates);
    }

    class TermHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTermTitle;
        private final TextView textViewTermDates;

        private TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTermTitle = itemView.findViewById(R.id.text_view_term_title);
            textViewTermDates = itemView.findViewById(R.id.text_view_term_dates);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TermEntity termEntity);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
