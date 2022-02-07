package com.sobesworld.wgucoursecommander.database.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;
import com.sobesworld.wgucoursecommander.ui.TermAddEdit;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private List<TermEntity> mTerms;
    private final Context context;

    public TermAdapter(Context context) {
        this.context = context;
    }

    class TermViewHolder extends RecyclerView.ViewHolder {

        private final TextView termItemView;

        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.item_view);
            itemView.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final TermEntity current = mTerms.get(position);
                    Intent intent = new Intent(context, TermAddEdit.class);
                    intent.putExtra("is new record", false);
                    intent.putExtra("id", current.getTermID());
                    intent.putExtra("title", current.getTermTitle());
                    intent.putExtra("start date", current.getTermStartDate());
                    intent.putExtra("end date", current.getTermEndDate());
                    context.startActivity(intent);
                }
            }));
        }
    }

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if (mTerms != null) {
            TermEntity current = mTerms.get(position);
            String title = current.getTermTitle();
            holder.termItemView.setText(title);
        } else {
            holder.termItemView.setText("No term title");
        }
    }

    @Override
    public int getItemCount() {
        if (mTerms != null) {
            return mTerms.size();
        } else {
            return 0;
        }
    }

    public void setTerms(List<TermEntity> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }
}
