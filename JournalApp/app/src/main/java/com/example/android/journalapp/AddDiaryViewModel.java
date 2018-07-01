package com.example.android.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.journalapp.database.AppDatabase;
import com.example.android.journalapp.database.DiaryEntry;

public class AddDiaryViewModel extends ViewModel {

    //Add a diary member variable for the DiaryEntry object wrapped in a LiveData
    private LiveData<DiaryEntry> diary;

    //Create a constructor where you call loadDiaryById of the diaryDao to initialize the diaries variable
    // Note: The constructor should receive the database and the diaryId

    public AddDiaryViewModel(AppDatabase database, int diaryId) {
        diary = database.diaryDao().loadDiaryById(diaryId);
    }

    //Create a getter for the diary variable
    public LiveData<DiaryEntry> getDiary() {
        return diary;
    }
}
