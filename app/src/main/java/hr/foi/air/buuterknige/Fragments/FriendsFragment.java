package hr.foi.air.buuterknige.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.buuterknige.Friends;
import hr.foi.air.buuterknige.ItemClickListener;
import hr.foi.air.buuterknige.ListOnline;
import hr.foi.air.buuterknige.ListOnlineViewHolder;
import hr.foi.air.buuterknige.MapTracking;
import hr.foi.air.buuterknige.R;
import hr.foi.air.buuterknige.Tracking;
import hr.foi.air.buuterknige.User;


public class FriendsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    private RecyclerView myFriendsList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersReference;
    private DatabaseReference onlinUsersReferemce;
    private View myMainView;
    private FirebaseUser currentUser;
    Dialog myDialog;

    //iz list
    DatabaseReference onlineRef,currentUserRef, counterRef, locations, friendReference;
    FirebaseRecyclerAdapter <User,ListOnlineViewHolder> adapter;

    RecyclerView listOnline;
    RecyclerView.LayoutManager layoutManager;



    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mlastLocation;
    private String onlineUserId;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;



    public FriendsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myMainView = inflater.inflate(R.layout.friends_fragment, container, false);
        myFriendsList = (RecyclerView) myMainView.findViewById(R.id.friends_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(onlineUserId);
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        myFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));


        listOnline = (RecyclerView) myMainView.findViewById(R.id.friends_recyclerview);
        listOnline.setHasFixedSize(true);
        listOnline.setLayoutManager(new LinearLayoutManager(getContext()));
        onlineUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(onlineUserId);

        locations = FirebaseDatabase.getInstance().getReference("Locations");
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(onlineUserId);



        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();

            }

        }

      //  onlineSettings();

        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (
                        Friends.class,
                        R.layout.item_contact,
                        FriendsViewHolder.class,
                        friendReference
                ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder, final Friends model, int position) {
               // viewHolder.setDate(model.getDate());

                String listUserId = getRef(position).getKey();
                usersReference.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.child("username").getValue().toString();
                        final String email = dataSnapshot.child("email").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {
                            final Boolean onlineStatus = (boolean) dataSnapshot.child("online").getValue();
                            viewHolder.setUserOnline(onlineStatus);
                            viewHolder.itemClickListener = new ItemClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                        if (onlineStatus == true) {
                                            if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                Intent map = new Intent(getContext(), MapTracking.class);
                                                map.putExtra("email", email);
                                                map.putExtra("lat", mlastLocation.getLatitude());
                                                map.putExtra("lng",mlastLocation.getLongitude());
                                                startActivity(map);
                                            }

                                        } else if (onlineStatus == false) {
                                            Toast.makeText(getContext(), "Korisnik nije online", Toast.LENGTH_SHORT).show();
                                        }



                                    }
                                };

                        }
                        viewHolder.setUsername(username);
                        viewHolder.setEmail(email);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        myFriendsList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mlastLocation = location;
        displayLocation();

    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        ItemClickListener itemClickListener;
        public FriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(this);

        }
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public void setDate(String date) {
           TextView sinceFriends = (TextView) mView.findViewById(R.id.phone_contact);
           sinceFriends.setText(date);
        }



        public void setEmail(String email) {
            TextView emailUser = (TextView) mView.findViewById(R.id.phone_contact);
            emailUser.setText(email);
        }

        public void setUsername(String username) {
            TextView userName = (TextView) mView.findViewById(R.id.name_contact);
            userName.setText(username);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition());

        }

        public void setUserOnline(Boolean onlineStatus) {
            ImageView onlineStatusDot = (ImageView) mView.findViewById(R.id.img_online);
            if (onlineStatus == true) {
                onlineStatusDot.setVisibility(View.VISIBLE);

            } else onlineStatusDot.setVisibility(View.INVISIBLE);
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mlastLocation != null) {
            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(mlastLocation.getLatitude()),
                            String.valueOf(mlastLocation.getLongitude())));
        } else {
            Toast.makeText(getContext(), "Couldn't get the location", Toast.LENGTH_SHORT).show();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RES_REQUEST).show();
            } else {
                Toast.makeText(getContext(), "This device is not supported", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

//    private void updateList() {
//        adapter = new FirebaseRecyclerAdapter<User, ListOnlineViewHolder>(
//                User.class,
//                R.layout.item_contact,
//                ListOnlineViewHolder.class,
//                counterRef) {
//            @Override
//            protected void populateViewHolder(ListOnlineViewHolder viewHolder, final User model, int position) {
//
//
//                viewHolder.txtEmail.setText(model.getEmail());
//                viewHolder.txtUserName.setText(model.getUsername());
//
//                viewHolder.itemClickListener = new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position) {
//
//                        if (!model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
//                            Intent map = new Intent(getContext(), MapTracking.class);
//                            map.putExtra("email", model.getEmail());
//                            map.putExtra("lat", mlastLocation.getLatitude());
//                            map.putExtra("lng",mlastLocation.getLongitude());
//                            startActivity(map);
//
//                        }
//
//                    }
//                };
//
//            }
//        };
//        adapter.notifyDataSetChanged();
//        listOnline.setAdapter(adapter);
//    }

//    private void setupSystem() {
//        onlineRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue(Boolean.class)) {
//                    currentUserRef.onDisconnect().removeValue();
//                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online",FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        counterRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
//                    User user = postSnapshot.getValue(User.class);
//                    Log.d("LOG", ""+user.getEmail()+" is "+user.getStatus());
//                }
//            }
//
//            @Override
//            public void onCancelled( DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void onlineSettings () {
        if (currentUser != null) {
            onlinUsersReferemce = FirebaseDatabase.getInstance().getReference().child("users").child(onlineUserId);
            onlinUsersReferemce.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    onlinUsersReferemce.child("online").onDisconnect().setValue(false);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
