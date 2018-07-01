package com.example.android.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.journalapp.database.AppDatabase;
import com.example.android.journalapp.database.DiaryEntry;
import com.google.firebase.auth.FirebaseAuth;

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

                // Use DEFAULT_DIARY_ID as the default
                mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID, DEFAULT_DIARY_ID);

                // LoadTaskById, this is done in the ViewModel now
                // Declare a AddDiaryViewModelFactory using mDb and mDiaryId
                AddDiaryViewModelFactory factory = new AddDiaryViewModelFactory(mDb, mDiaryId);
                // Declare a AddDiaryViewModel variable and initialize it by calling ViewModelProviders.of
                // for that use the factory created above AddDiaryViewModel
                final AddDiaryViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(AddDiaryViewModel.class);

                // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
                viewModel.getDiary().observe(this, new Observer<DiaryEntry>() {
                    @Override
                    public void onChanged(@Nullable DiaryEntry diaryEntry) {
                        viewModel.getDiary().removeObserver(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

}
