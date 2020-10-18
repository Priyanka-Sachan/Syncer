package com.example.syncer.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Folder.class},version = 1)
public abstract class FolderDatabase extends RoomDatabase {

    //only one instance of database
    private static FolderDatabase instance;
    public abstract FolderDao folderDao();

    public static synchronized FolderDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    FolderDatabase.class,"FolderDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;

    }
    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase){
            super.onCreate(supportSQLiteDatabase);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private FolderDao folderDao;
        private PopulateDbAsyncTask(FolderDatabase FolderDatabase){
            this.folderDao=FolderDatabase.folderDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            folderDao.insert(new Folder("Title 1","Content 1",1,2,1));
            folderDao.insert(new Folder("Title 2","Content 2",2,5,1));
            folderDao.insert(new Folder("Title 3","Content 3",3,7,1));
            return null;
        }
    }




}
