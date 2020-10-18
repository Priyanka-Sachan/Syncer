package com.example.syncer.ui.home;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ammarptn.gdriverest.DriveServiceHelper;
import com.ammarptn.gdriverest.GoogleDriveFileHolder;
import com.example.syncer.R;
import com.example.syncer.database.Folder;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.Gson;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    //    private Button create_text_file;
//    private Button create_folder;
//    private Button search_file;
//    private Button search_folder;
//    private Button upload_file;
//    private Button download_file;
//    private Button delete_file;
//    private Button view_folder;
    private TextView syncedFolderTitle;
    private LinearLayout emptyFolders;
    private ExtendedFloatingActionButton uploadFolder;

    DriveServiceHelper mDriveServiceHelper;
    GoogleAccountCredential credential;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        create_text_file = root.findViewById(R.id.create_text_file);
//        create_folder = root.findViewById(R.id.create_folder);
//        search_file = root.findViewById(R.id.search_file);
//        search_folder = root.findViewById(R.id.search_folder);
//        upload_file = root.findViewById(R.id.upload_file);
//        download_file = root.findViewById(R.id.download_file);
//        delete_file = root.findViewById(R.id.delete_file_folder);
//        view_folder = root.findViewById(R.id.view_file_folder);
        syncedFolderTitle = root.findViewById(R.id.synced_folder_title);
        emptyFolders = root.findViewById(R.id.empty_folders);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            Log.i(TAG, "not null");
        }
        credential = GoogleAccountCredential.usingOAuth2(getContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(account.getAccount());

        Drive googleDrive = new Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("Syncer")
                .build();
        mDriveServiceHelper = new DriveServiceHelper(DriveServiceHelper.getGoogleDriveService(getContext(), account, "Syncer"));

        uploadFolder = root.findViewById(R.id.upload_folder);
        uploadFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ChooserDialog(requireActivity())
                        .withFilter(true, false)
                        .withStartFile(Environment.getExternalStorageDirectory().getAbsolutePath())
                        // to handle the result(s)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File folder) {
                                Toast.makeText(getContext(), "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                createFolder(folder, null);
                                homeViewModel.insert(new Folder(folder.getName(), path, 12, 0, 1));
                            }
                        })
                        .build()
                        .show();
            }
        });

        RecyclerView folderRecyclerView = root.findViewById(R.id.folder_recycler_view);
        final FolderAdapter folderAdapter = new FolderAdapter();
        folderRecyclerView.setAdapter(folderAdapter);
        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Folder folder) {
                Log.d(TAG, folder.getTitle());
            }
        });

        homeViewModel.getAllFolders().observe(getViewLifecycleOwner(), new Observer<List<Folder>>() {
            @Override
            public void onChanged(List<Folder> folders) {
                if (folders.isEmpty()) {
                    syncedFolderTitle.setVisibility(View.GONE);
                    folderRecyclerView.setVisibility(View.GONE);
                    emptyFolders.setVisibility(View.VISIBLE);
                } else {
                    syncedFolderTitle.setVisibility(View.VISIBLE);
                    folderRecyclerView.setVisibility(View.VISIBLE);
                    emptyFolders.setVisibility(View.GONE);
                }
                folderAdapter.submitList(folders);
            }
        });

//        create_text_file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDriveServiceHelper == null) {
//                    Log.i(TAG, "null tha");
//                    return;
//                }
//                // you can provide  folder id in case you want to save this file inside some folder.
//                // if folder id is null, it will save file to the root
//                mDriveServiceHelper.createTextFile("textfilename.txt", "some text", null)
//                        .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
//                            @Override
//                            public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
//                                Gson gson = new Gson();
//                                Log.d("homefrag", "onSuccess: " + gson.toJson(googleDriveFileHolder));
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("homefrag", "onFailure: " + e.getMessage());
//                            }
//                        });
//            }
//        });
//
//        search_file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDriveServiceHelper == null) {
//                    return;
//                }
//                mDriveServiceHelper.searchFile("textfilename.txt", "text/plain")
//                        .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
//                            @Override
//                            public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
//
//                                Gson gson = new Gson();
//                                Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders));
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.getMessage());
//                            }
//                        });
//
//            }
//        });
//
//        search_folder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDriveServiceHelper == null) {
//                    return;
//                }
//
//                mDriveServiceHelper.searchFolder("testDummy")
//                        .addOnSuccessListener(new OnSuccessListener<List<GoogleDriveFileHolder>>() {
//                            @Override
//                            public void onSuccess(List<GoogleDriveFileHolder> googleDriveFileHolders) {
//                                Gson gson = new Gson();
//                                Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolders));
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d(TAG, "onFailure: " + e.getMessage());
//                            }
//                        });
//            }
//        });
//
//        download_file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mDriveServiceHelper == null) {
//                    return;
//                }
//                mDriveServiceHelper.downloadFile(new java.io.File(getContext().getFilesDir(), "filename.txt"), "google_drive_file_id_here")
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        });
//            }
//        });
        return root;
    }

    void createFolder(File folder, String folderId) {
        mDriveServiceHelper.createFolder(folder.getName(), folderId)
                .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                    @Override
                    public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                        Gson gson = new Gson();
                        Log.d(TAG, "onSuccess: Folder Created " + gson.toJson(googleDriveFileHolder));
                        File[] files = folder.listFiles();
                        if (files != null) {
                            Log.d("Files", "Size: " + files.length);
                            for (File file : files) {
                                Log.d("Files", "FileName:" + file.getName());
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadFile(file, googleDriveFileHolder.getId());
                                    }
                                };
                                new Thread(runnable).start();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());

                    }
                });
    }

    void uploadFile(File file, String folderId) {
        if (mDriveServiceHelper == null) {
            return;
        }
        if (getMimeType(file.getAbsolutePath()) != null) {
            mDriveServiceHelper.uploadFile(new java.io.File(file.getParentFile().getAbsolutePath(), file.getName()), getMimeType(file.getAbsolutePath()), folderId)
                    .addOnSuccessListener(new OnSuccessListener<GoogleDriveFileHolder>() {
                        @Override
                        public void onSuccess(GoogleDriveFileHolder googleDriveFileHolder) {
                            Gson gson = new Gson();
                            Log.d(TAG, "onSuccess: " + gson.toJson(googleDriveFileHolder));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        } else {
            createFolder(file, folderId);
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}