package hr.foi.air.buuterknige.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hr.foi.air.buuterknige.Adapter.RecyclerViewAdapter;
import hr.foi.air.buuterknige.Contact;
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

        FirebaseRecyclerAdapter<User,AllUsersHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, AllUsersHolder>
                (
                        User.class,
                        R.layout.item_contact,
                        AllUsersHolder.class,
                        friendReference
                ) {
            @Override
            protected void populateViewHolder(AllUsersHolder viewHolder, final User model, int position) {
                viewHolder.setEmail(model.getEmail());
                viewHolder.setUsername(model.getUsername());
                View v;
                LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v= inflater.inflate(R.layout.item_contact,null);
                //final AllUsersHolder allUsersHolder = new AllUsersHolder(v);

                myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.dialog_contact);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



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
                        TextView dialogEmail = (TextView) myDialog.findViewById(R.id.dialog_phone_id);
                        TextView dialoguserName = (TextView) myDialog.findViewById(R.id.dialog_name_id);
                        dialogEmail.setText(model.getEmail());
                        dialoguserName.setText(model.getUsername());
                        myDialog.show();
                    }
                };
            }
        };
        myFriendsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class AllUsersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        static View mView;
        ItemClickListener itemClickListener;
        public AllUsersHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }



        public static void setEmail(String email) {
            TextView emailUser = (TextView) mView.findViewById(R.id.phone_contact);
            emailUser.setText(email);
        }

        public void setUsername(String username) {
            TextView userName = (TextView) mView.findViewById(R.id.name_contact);
            userName.setText(username);
        }

        public static void setEmail2(String email) {
            TextView emailUser2 = (TextView) mView.findViewById(R.id.phone_contact);
            emailUser2.setText(email);
        }


        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());

        }
    }

}
