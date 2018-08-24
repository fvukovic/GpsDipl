package hr.foi.air.buuterknige.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hr.foi.air.buuterknige.R;

public class ActiveFriendsFragment extends Fragment {

    View view;
    public ActiveFriendsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.active_friends_fragment, container, false);
        return view;
    }
}
