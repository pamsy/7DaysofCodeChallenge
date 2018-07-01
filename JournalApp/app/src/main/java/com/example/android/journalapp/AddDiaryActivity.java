package com.example.android.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.journalapp.database.AppDatabase;
import com.example.android.journalapp.database.DiaryEntry;

import java.util.Date;

public class AddDiaryActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_DIARY_ID = "extraDiaryId";

    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_DIARY_ID = "instanceDiaryId";


    // Constant for default diary id to be used when not in update mode
    private static final int DEFAULT_DIARY_ID = -1;
    // Constant for logging
    private static final String TAG = AddDiaryActivity.class.getSimpleName();
    // Fields for views
    EditText mEditTexttitle;
    EditText mEditTextdes;
    Button mButton;

    private int mDiaryId = DEFAULT_DIARY_ID;

    // Member variable for the Database
    private AppDatabase mDb;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ID)) {
            mButton.setText(R.string.update_button);
            if (mDiaryId == DEFAULT_DIARY_ID) {
                // populate the UI
                // Assign the value of EXTRA_DIARY_ID in the intent to mDiaryId
                // Use DEFAULT_DIARY_ID as the default
                mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);

                Log.d(TAG, "Actively retrieving a specific Diary from the DataBase");

                final LiveData<DiaryEntry> diary = mDb.diaryDao().loadDiaryById(mDiaryId);

                diary.observe(this, new Observer<DiaryEntry>() {
                    @Override
                    public void onChanged(@Nullable DiaryEntry diaryEntry) {

                        diary.removeObserver(this);
                        Log.d(TAG, "Receiving database update from LiveData");
                        populateUI(diaryEntry);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_DIARY_ID, mDiaryId);
        super.onSaveInstanceState(outState);
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {

        mEditTexttitle = findViewById(R.id.editTextdiarytitle);
        mEditTextdes = findViewById(R.id.editTextdiaryDescription);

        mButton = findViewById(R.id.addButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param diary the taskEntry to populate the UI
     */
    private void populateUI(DiaryEntry diary) {
        //return if the diary is null
        if (diary == null) {
            return;
        }

        // use the variable diary to populate the UI
        mEditTexttitle.setText(diary.getTitle());
        mEditTextdes.setText(diary.getDescription());
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String title  = mEditTexttitle.getText().toString();
        String description  = mEditTextdes.getText().toString();

        Date date = new Date();

        final DiaryEntry diary = new DiaryEntry(title, description, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //insert the diary only if mDiaryId matches DEFAULT_DIARY_ID
                // Otherwise update it
                // call finish in any case
                if (mDiaryId == DEFAULT_DIARY_ID) {
                    // insert new diary
                    mDb.diaryDao().insertDiary(diary);
                } else {
                    //update diary
                    diary.setId(mDiaryId);
                    mDb.diaryDao().updateDiary(diary);
                }
                finish();
            }
        });
    }

}
