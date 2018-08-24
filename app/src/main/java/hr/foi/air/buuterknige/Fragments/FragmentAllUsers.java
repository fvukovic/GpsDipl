package hr.foi.air.buuterknige.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.buuterknige.Adapter.RecyclerViewAdapter;
import hr.foi.air.buuterknige.Contact;
import hr.foi.air.buuterknige.R;

public class FragmentAllUsers extends Fragment {



    View view;
    private RecyclerView myRecyclerView;
    private List<Contact> lstContact;
    public FragmentAllUsers() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_users_fragment, container, false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.allcontact_recyclerview);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), lstContact);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lstContact = new ArrayList<>();
        lstContact.add(new Contact("Aaron Jones", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Elizabeta Azdajic", "(222) 323445456", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Pero Jones", "(151) 13455", R.drawable.females_female_avatar_woman_people_faces_18401));
        lstContact.add(new Contact("LaLa Jones", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Jole jole", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Rajko Jones", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Stipe Colak", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Marko Azdajic", "(222) 323445456", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Dario Mijac", "(151) 13455", R.drawable.females_female_avatar_woman_people_faces_18401));
        lstContact.add(new Contact("Ivana Calaga", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Jole jole", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
        lstContact.add(new Contact("Boris Mijac", "(111) 3455666", R.drawable.ic_account_circle_white_24dp));
    }
}
