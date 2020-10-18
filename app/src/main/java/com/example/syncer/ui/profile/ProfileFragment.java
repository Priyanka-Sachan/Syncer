package com.example.syncer.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.syncer.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class ProfileFragment extends Fragment {

    private ProfileViewModel notificationsViewModel;
    String personName;
    ImageView profilephoto;
    TextView profilename;
    TextView profieEmail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profilephoto = root.findViewById(R.id.profilePic);
        profilename = root.findViewById(R.id.profileName);
        profieEmail = root.findViewById(R.id.profileEmail);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            profilephoto.setImageURI(personPhoto);
            profilename.setText(personName);
            profieEmail.setText(personEmail);
        }
        return root;
    }
}