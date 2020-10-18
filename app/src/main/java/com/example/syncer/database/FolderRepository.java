package com.example.syncer.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FolderRepository {
    private FolderDao folderDao;
    private LiveData<List<Folder>> allFolders;

    public FolderRepository(Application application){

        //since application is a subclass of context
        FolderDatabase folderDatabase= FolderDatabase.getInstance(application);
        //Though it was abstract method,it has been populated by ROOM,hence we can use it here
        folderDao=folderDatabase.folderDao();
        allFolders=folderDao.getAllFolders();
    }

    public void Insert(Folder folder){
        new InsertFolderAsyncTask(folderDao).execute(folder);
    }
    public void Update(Folder folder){
        new UpdateFolderAsyncTask(folderDao).execute(folder);
    }
    public void Delete(Folder folder){
        new DeleteFolderAsyncTask(folderDao).execute(folder);
    }
    //LiveData process is automatically done on background thread
    public LiveData<List<Folder>> getAllFolders(){
        return allFolders;
    }

    public static class InsertFolderAsyncTask extends AsyncTask<Folder,Void,Void>{

        FolderDao folderDao;
        public InsertFolderAsyncTask (FolderDao folderDao){
            this.folderDao=folderDao;
        }
        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.insert(folders[0]);
            return null;
        }
    }

    public static class UpdateFolderAsyncTask extends AsyncTask<Folder,Void,Void>{

        FolderDao folderDao;
        public UpdateFolderAsyncTask (FolderDao folderDao){
            this.folderDao=folderDao;
        }
        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.update(folders[0]);
            return null;
        }
    }

    public static class DeleteFolderAsyncTask extends AsyncTask<Folder,Void,Void>{

        FolderDao folderDao;
        public DeleteFolderAsyncTask (FolderDao folderDao){
            this.folderDao=folderDao;
        }
        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.delete(folders[0]);
            return null;
        }
    }
}
