package com.sobesworld.wgucoursecommander.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AssessmentEntity assessmentEntity);

    @Update
    void update(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessment_table ORDER BY assessmentID ASC")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE courseID = :i ORDER BY assessmentID ASC")
    LiveData<List<AssessmentEntity>> getLinkedAssessments(int i);

    @Query("DELETE FROM assessment_table WHERE assessmentID = :i")
    void deleteAssessmentByID(int i);

    @Query("DELETE FROM assessment_table WHERE courseID = :i")
    void deleteLinkedAssessments(int i);
}
