package com.sobesworld.wgucoursecommander.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Delete
    void delete(AssessmentEntity assessmentEntity);

    @Query("DELETE FROM assessment_table WHERE assessmentID = :i")
    void deleteUsingAssessmentID(int i);

    @Query("DELETE FROM assessment_table WHERE assessmentLinkedCourseID = :i")
    void deleteLinkedAssessments(int i);

    @Query("SELECT * FROM assessment_table ORDER BY assessmentID ASC")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE assessmentLinkedCourseID = :i ORDER BY assessmentID ASC")
    LiveData<List<AssessmentEntity>> getLinkedAssessments(int i);
}
