package com.example.android.journalapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.journalapp.database.AppDatabase;

public class AddDiaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    //Adding two member variables. One for the database and one for the diaryId
    private final AppDatabase mDb;
    private final int mDiaryId;

    //Initialize the member variables in the constructor with the parameters received
    public AddDiaryViewModelFactory(AppDatabase database, int diaryId) {
        mDb = database;
        mDiaryId = diaryId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddDiaryViewModel(mDb, mDiaryId);
    }
}
