package hr.foi.air.buuterknige.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import hr.foi.air.buuterknige.AllUserHolder;
import hr.foi.air.buuterknige.ItemClickListener;
import hr.foi.air.buuterknige.R;
import hr.foi.air.buuterknige.User;

public class FragmentAllUsers extends Fragment {


    private RecyclerView myFriendsList;
    private DatabaseReference friendReference;
    private FirebaseAuth mAuth;
    private DatabaseReference usersReference;
    String online_user_id;
    private View myMainView;
    Dialog myDialog;

    public FragmentAllUsers() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.friends_fragment, container, false);
        myFriendsList = (RecyclerView) myMainView.findViewById(R.id.friends_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        friendReference = FirebaseDatabase.getInstance().getReference().child("users");
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        myFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<User,AllUserHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, AllUserHolder>
                (
                        User.class,
                        R.layout.item_contact,
                        AllUserHolder.class,
                        friendReference
                ) {
            @Override
            protected void populateViewHolder(final AllUserHolder viewHolder, final User model, int position) {
                viewHolder.setEmail(model.getEmail());
                viewHolder.setUsername(model.getUsername());

                View v;
                LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v= inflater.inflate(R.layout.item_contact,null);
                //final AllUsersHolder allUsersHolder = new AllUsersHolder(v);

//                myDialog = new Dialog(getContext());
//                myDialog.setContentView(R.layout.dialog_contact);




//                String list_user_id = getRef(position).getKey();
//                usersReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                      //  String email = dataSnapshot.child("email").getValue().toString();
//                      //  FriendsViewHolder.setEmail2(email);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                viewHolder.itemClickListener = new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        FragmentDialog fragmentDialog = new FragmentDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("username", model.getUsername());
                        bundle.putString("email",model.getEmail());
                        bundle.putString("friendId",getRef(position).getKey());
                        fragmentDialog.setArguments(bundle);
                        fragmentDialog.show(getFragmentManager(), "FragmentDialog");
                    }
                };
            }
        };
        myFriendsList.setAdapter(firebaseRecyclerAdapter);
    }
}
