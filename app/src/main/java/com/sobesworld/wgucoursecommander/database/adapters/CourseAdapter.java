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
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.ui.CourseDetail;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<CourseEntity> mCourses;
    private final Context context;

    public CourseAdapter(Context context) {
        this.context = context;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseItemView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.item_view);
            itemView.setOnClickListener((view -> {
                int position = getAdapterPosition();
                final CourseEntity current = mCourses.get(position);
                Intent intent = new Intent(context, CourseDetail.class);
                intent.putExtra(context.getString(R.string.is_new_record), false);
                intent.putExtra("id", current.getCourseID());
                context.startActivity(intent);
            }));
        }
    }

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new CourseAdapter.CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if (mCourses != null) {
            CourseEntity current = mCourses.get(position);
            String title = current.getCourseTitle();
            holder.courseItemView.setText(title);
        } else {
            holder.courseItemView.setText(R.string.no_course_title);
        }
    }

    @Override
    public int getItemCount() {
        if (mCourses != null) {
            return mCourses.size();
        } else {
            return 0;
        }
    }

    public void setCourses(List<CourseEntity> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }
}
