package com.sobesworld.wgucoursecommander.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(TermEntity termEntity);

    @Update
    void update(TermEntity termEntity);

    @Query("SELECT * FROM term_table ORDER BY termID ASC")
    LiveData<List<TermEntity>> getAllTerms();

    @Query("DELETE FROM term_table WHERE termID = :i")
    void deleteTermByID(int i);

    @Query("SELECT * FROM term_table WHERE termID = (SELECT MAX(termID) FROM term_table)")
    LiveData<TermEntity> getMaxTermID();
}
