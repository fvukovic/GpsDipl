package hr.foi.air.buuterknige.Fragments;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import hr.foi.air.buuterknige.R;

public class FragmentDialog extends DialogFragment{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private DatabaseReference friendReference;
    private FirebaseAuth mAuth;
    String friendId;
    String myId;
    private String CURRENT_STATE= "not_friends";

    TextView txUsername;
    TextView txEmail;
    Button btnSendRequest;
    String username;
    String email;
    View myMainView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.fragment_dialog, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("Friend_Requests");
        friendReference = mDatabase.getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        myId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        txUsername = (TextView) myMainView.findViewById(R.id.fragment_dialog_name_id);
        txEmail = (TextView) myMainView.findViewById(R.id.fragment_dialog_email_id);
        btnSendRequest = (Button) myMainView.findViewById(R.id.btn_send_request);

        if (getArguments()!=null) {
            username = getArguments().getString("username","");
            email = getArguments().getString("email","");
            friendId = getArguments().getString("friendId","");
            txUsername.setText(username);
            txEmail.setText(email);
        }
        mReference.child(myId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()) {
                   if (dataSnapshot.hasChild(friendId)) {
                       String req_type = dataSnapshot.child(friendId).child("request_type").getValue().toString();
                       if (req_type.equals("sent")) {
                           CURRENT_STATE = "request_sent";
                           btnSendRequest.setText("Cancel request");
                       } else if (req_type.equals("received")) {
                           CURRENT_STATE = "request_received";
                           btnSendRequest.setText("Accept request");
                       }
                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CURRENT_STATE.equals("not_friends")) {
                    sendFriendRequest();
                }
                if (CURRENT_STATE.equals("request_sent")) {
                    cancelFriendRequest();
                }
                if (CURRENT_STATE.equals("request_received")) {
                    acceptFriendRequest();
                }
            }
        });
        return myMainView;
    }

    private void acceptFriendRequest() {

        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate = currentDate.format(date.getTime());
        friendReference.child(myId).child(friendId).setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        friendReference.child(friendId).child(myId).setValue(saveCurrentDate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mReference.child(myId).child(friendId).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            mReference.child(friendId).child(myId).removeValue()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                btnSendRequest.setEnabled(true);
                                                                                CURRENT_STATE = "friends";
                                                                                btnSendRequest.setText("Unfriend");
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });

                                    }
                                });
                    }
                });

    }

    private void cancelFriendRequest() {
        mReference.child(myId).child(friendId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mReference.child(friendId).child(myId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                btnSendRequest.setEnabled(true);
                                                CURRENT_STATE = "not_friends";
                                                btnSendRequest.setText("Send request");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendFriendRequest() {
        btnSendRequest.setEnabled(false);
            mReference.child(myId).child(friendId)
                    .child("request_type").setValue("sent")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mReference.child(friendId).child(myId)
                                        .child("request_type").setValue("received")
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    btnSendRequest.setEnabled(true);
                                                    CURRENT_STATE = "request_sent";
                                                    btnSendRequest.setText("Cancel request");
                                                }
                                            }
                                        });
                            }
                        }
                    });

    }

}
