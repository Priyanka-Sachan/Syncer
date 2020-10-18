package com.example.syncer.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FolderRepository {
    private FolderDao folderDao;
    private LiveData<List<Folder>> allFolders;

    public FolderRepository(Application application) {

        FolderDatabase folderDatabase = FolderDatabase.getInstance(application);
        folderDao = folderDatabase.folderDao();
        allFolders = folderDao.getAllFolders();
    }

    public void Insert(Folder folder) {
        new InsertFolderAsyncTask(folderDao).execute(folder);
    }

    public void Update(Folder folder) {
        new UpdateFolderAsyncTask(folderDao).execute(folder);
    }

    public void Delete(Folder folder) {
        new DeleteFolderAsyncTask(folderDao).execute(folder);
    }

    public LiveData<List<Folder>> getAllFolders() {
        return allFolders;
    }

    public static class InsertFolderAsyncTask extends AsyncTask<Folder, Void, Void> {

        FolderDao folderDao;

        public InsertFolderAsyncTask(FolderDao folderDao) {
            this.folderDao = folderDao;
        }

        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.insert(folders[0]);
            return null;
        }
    }

    public static class UpdateFolderAsyncTask extends AsyncTask<Folder, Void, Void> {

        FolderDao folderDao;

        public UpdateFolderAsyncTask(FolderDao folderDao) {
            this.folderDao = folderDao;
        }

        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.update(folders[0]);
            return null;
        }
    }

    public static class DeleteFolderAsyncTask extends AsyncTask<Folder, Void, Void> {

        FolderDao folderDao;

        public DeleteFolderAsyncTask(FolderDao folderDao) {
            this.folderDao = folderDao;
        }

        @Override
        protected Void doInBackground(Folder... folders) {
            folderDao.delete(folders[0]);
            return null;
        }
    }
}
