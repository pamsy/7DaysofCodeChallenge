package com.example.android.journalapp.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

//Annotate the class with Entity. Use "task" for the table name
@Entity(tableName = "diary")
public class DiaryEntry {

    // Annotate the id as PrimaryKey. Set autoGenerate to true.
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date updatedAt;

//    Use the Ignore annotation so Room knows that it has to use the other constructor instead
    @Ignore
    public DiaryEntry(String title, String description, Date updatedAt) {
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public DiaryEntry(int id, String title, String description, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
