package com.mmuhamadamirzaidi.qwisapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mmuhamadamirzaidi.qwisapp.Common.Common;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    TextView profiletype, profiletypemenu, profileusername, profileemail;

    View myFragment;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        profiletype = (TextView) myFragment.findViewById(R.id.profiletype);
        profiletypemenu = (TextView) myFragment.findViewById(R.id.profiletypemenu);

        profileusername = (TextView) myFragment.findViewById(R.id.profileusername);
        profileemail = (TextView) myFragment.findViewById(R.id.profileemail);

        setUserInformation();

        return myFragment;
    }

    private void setUserInformation() {

        profiletype.setText(Common.currentUser.getRole());
        profiletypemenu.setText(Common.currentUser.getRole());

        profileusername.setText(Common.currentUser.getUsername());
        profileemail.setText(Common.currentUser.getEmail());
    }

}
