package hr.foi.air.buuterknige.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;

import hr.foi.air.buuterknige.LogInActivity;
import hr.foi.air.buuterknige.MainActivity;
import hr.foi.air.buuterknige.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentLogIn extends Fragment {

    View myMainView;
    private FirebaseAuth firebaseAuth;
    EditText emailEdit;
    private boolean logedIn;
    EditText passwordEdit;
    Button btnLogIn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myMainView = inflater.inflate(R.layout.fragment_login, container, false);
        firebaseAuth = firebaseAuth.getInstance();
        emailEdit = (EditText) myMainView.findViewById(R.id.input_email);
        passwordEdit = (EditText) myMainView.findViewById(R.id.input_password);
        btnLogIn = (Button) myMainView.findViewById(R.id.btn_enter);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        TextView lstPass = (TextView) myMainView.findViewById(R.id.lbl_register);


        lstPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container, new FragmentRegister());
                fr.commit();
            }
        });
        return myMainView;
    }

    public void logIn () {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        } else {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity)getContext(), new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();
                        logedIn = true;
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Incorrect username or password: ", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }


    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
