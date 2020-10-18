package com.example.syncer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FolderDao {
    @Insert
    void insert(Folder folder);

    @Update
    void update(Folder folder);

    @Delete
    void delete(Folder folder);

    //LiveData<> to observe the <...>
    @Query("SELECT * FROM folders")
    LiveData<List<Folder>> getAllFolders();
}