package hr.foi.air.buuterknige.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;
import java.util.concurrent.Executor;

import hr.foi.air.buuterknige.LogInActivity;
import hr.foi.air.buuterknige.R;

public class FragmentRegister extends Fragment {

    View myMainView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference storeUserDefaultDataReference;
    EditText nameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    Button btnRegister;
    TextView lblLogIn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.register_fragment, container, false);
        firebaseAuth = firebaseAuth.getInstance();
        nameEdit = (EditText) myMainView.findViewById(R.id.input_nameNew);
        emailEdit = (EditText) myMainView.findViewById(R.id.input_emailNew);
        passwordEdit = (EditText) myMainView.findViewById(R.id.input_passwordNew);

       btnRegister = (Button) myMainView.findViewById(R.id.btn_register);
       lblLogIn = (TextView) myMainView.findViewById(R.id.lbl_logIn);

       btnRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               registerUser();
           }
       });
       lblLogIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FragmentTransaction fr = getFragmentManager().beginTransaction();
               fr.replace(R.id.fragment_container, new FragmentLogIn());
               fr.commit();
           }
       });

       return myMainView;
    }


    public void registerUser() {

        final String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        final String name = nameEdit.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String currentUserId = firebaseAuth.getCurrentUser().getUid();
                            storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
                            storeUserDefaultDataReference.child("email").setValue(email);
                            storeUserDefaultDataReference.child("username").setValue(name);
                            Toast.makeText(getContext(), "Sucessful", Toast.LENGTH_SHORT).show();
                            FragmentTransaction fr = getFragmentManager().beginTransaction();
                            fr.replace(R.id.fragment_container, new FragmentLogIn());
                            fr.commit();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
