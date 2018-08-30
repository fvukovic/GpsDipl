package hr.foi.air.buuterknige.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.buuterknige.AllUserHolder;
import hr.foi.air.buuterknige.ItemClickListener;
import hr.foi.air.buuterknige.R;
import hr.foi.air.buuterknige.Requests;
import hr.foi.air.buuterknige.User;

public class RequestFriendsFragment extends Fragment {

    private View myMainView;
    private RecyclerView myRequestList;
    private DatabaseReference requestReference;
    private DatabaseReference usersReference;
    private FirebaseAuth mAuth;
    String onlineUserId;

    public RequestFriendsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.request_friends_fragment, container, false);
        myRequestList = (RecyclerView) myMainView.findViewById(R.id.request_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        requestReference = FirebaseDatabase.getInstance().getReference().child("Friend_Requests").child(onlineUserId);
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        myRequestList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myRequestList.setLayoutManager(linearLayoutManager);

        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Requests,RequestViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(
                Requests.class,
                R.layout.item_contact,
                RequestViewHolder.class,
                requestReference
        ) {
            @Override
            protected void populateViewHolder(final RequestViewHolder viewHolder, final Requests model, int position) {
                final String listUsersId = getRef(position).getKey();
                DatabaseReference getTypRef = getRef(position).child("request_type").getRef();


                                usersReference.child(listUsersId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String username = dataSnapshot.child("username").getValue().toString();
                                        final String email = dataSnapshot.child("email").getValue().toString();

                                        viewHolder.setUserName(username);
                                        viewHolder.setEmail(email);
                                        viewHolder.itemClickListener = new ItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position) {

                                                FragmentDialog fragmentDialog = new FragmentDialog();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("username", username);
                                                bundle.putString("email", email);
                                                bundle.putString("friendId",getRef(position).getKey());
                                                fragmentDialog.setArguments(bundle);
                                                fragmentDialog.show(getFragmentManager(), "FragmentDialog");
                                            }
                                        };

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                }
        };
        myRequestList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        public ItemClickListener itemClickListener;

        public RequestViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setUserName(String userName) {
            TextView userNameDisplay = (TextView) mView.findViewById(R.id.name_contact);
            userNameDisplay.setText(userName);
        }

        public void setEmail(String email) {
            TextView userEmailDisplay = (TextView) mView.findViewById(R.id.phone_contact);
            userEmailDisplay.setText(email);
        }


        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());
        }
    }

}
