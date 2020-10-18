package com.example.syncer.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.syncer.database.Folder;
import com.example.syncer.database.FolderRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    FolderRepository folderRepository;
    LiveData<List<Folder>> allFolders;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        folderRepository = new FolderRepository(application);
        allFolders = folderRepository.getAllFolders();
    }

    void insert(Folder folder) {
        folderRepository.Insert(folder);
        Log.d("HomeViewModel",folder.getTitle()+" created.");
    }

    void update(Folder folder) {
        folderRepository.Update(folder);
    }

    void delete(Folder folder) {
        folderRepository.Delete(folder);
    }

    LiveData<List<Folder>> getAllFolders() {
        return folderRepository.getAllFolders();
    }
}