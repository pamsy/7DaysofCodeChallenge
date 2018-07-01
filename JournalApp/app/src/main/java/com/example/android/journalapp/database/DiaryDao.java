package com.example.android.journalapp.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DiaryDao {
    @Query("SELECT * FROM diary ORDER BY updatedAt")
    LiveData<List<DiaryEntry>> loadAllDiaries();

    @Insert
    void insertDiary(DiaryEntry diaryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDiary(DiaryEntry diaryEntry);

    @Delete
    void deleteDiary(DiaryEntry diaryEntry);

    //Create a Query method that receives an int id and returns a DiaryEntry Object
    // The query for this method should get all the data for that id in the diary table
    @Query("SELECT * FROM diary WHERE id = :id")
    LiveData<DiaryEntry> loadDiaryById(int id);

}
