package com.example.syncer.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="folders")
public class Folder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String path;
    private int hour;
    private int minute;
    private int inSync;

    public Folder(String title, String path, int hour, int minute, int inSync) {
        this.title = title;
        this.path = path;
        this.hour = hour;
        this.minute = minute;
        this.inSync = inSync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getInSync() {
        return inSync;
    }

    public void setInSync(int inSync) {
        this.inSync = inSync;
    }
}


