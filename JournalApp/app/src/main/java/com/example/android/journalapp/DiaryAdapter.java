package com.example.android.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.journalapp.database.DiaryEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * This TaskAdapter creates and binds ViewHolders, that hold the description and Title of a diary,
 * to a RecyclerView to efficiently display data.
 */
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

//  Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds diaries data and the Context
    private List<DiaryEntry> mDiariesEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    /**
     * Constructor for the DiaryAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public DiaryAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new DiaryViewHolder that holds the view for each diary
     */
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.diary_layout, parent, false);

        return new DiaryViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        // Determine the values of the wanted data
        DiaryEntry diaryEntry = mDiariesEntries.get(position);
        String title = diaryEntry.getTitle();
        String description = diaryEntry.getDescription();
        String updatedAt = dateFormat.format(diaryEntry.getUpdatedAt());

        //Set values
        holder.diaryTitleView.setText(title);
        holder.diaryDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);

        //not color no priority
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mDiariesEntries == null) {
            return 0;
        }
        return mDiariesEntries.size();
    }

    public List<DiaryEntry> getDiaries() {
        return mDiariesEntries;
    }

    /**
     * When data changes, this method updates the list of diaryEntries
     * and notifies the adapter to use the new values on it
     */
    public void setDiaries(List<DiaryEntry> diaryEntries) {
        mDiariesEntries = diaryEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Class variables for the diary description and title TextViews
        TextView diaryTitleView;
        TextView diaryDescriptionView;
        TextView updatedAtView;

        /**
         * Constructor for the TaskViewHolders.
         *
         * @param itemView The view inflated in onCreateViewHolder
         */
        public DiaryViewHolder(View itemView) {
            super(itemView);

            diaryTitleView = itemView.findViewById(R.id.diarytitle);
            diaryDescriptionView = itemView.findViewById(R.id.diarydescription);
            updatedAtView = itemView.findViewById(R.id.diaryUpdatedAt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mDiariesEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }


}
