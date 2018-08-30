package hr.foi.air.buuterknige.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hr.foi.air.buuterknige.LogInActivity;
import hr.foi.air.buuterknige.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FragmentRegister extends Fragment {

    View myMainView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference storeUserDefaultDataReference;
    EditText nameEdit;
    EditText emailEdit;
    EditText passwordEdit;
    EditText passwordConfirm;
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
        passwordConfirm = (EditText) myMainView.findViewById(R.id.input_confirmPassNew);

       btnRegister = (Button) myMainView.findViewById(R.id.btn_register);
       lblLogIn = (TextView) myMainView.findViewById(R.id.lbl_logIn);

       btnRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = emailEdit.getText().toString();
               String password = passwordEdit.getText().toString();
               final String name = nameEdit.getText().toString();
               String confirmPassword = passwordConfirm.getText().toString();

               if (TextUtils.isEmpty(name)) {
                   Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG).show();
                   return;
               } else if (TextUtils.isEmpty(email)||!isValidEmail(email)) {
                   Toast.makeText(getApplicationContext(), "Please enter a valid address", Toast.LENGTH_LONG).show();
                   return;
               } else if (TextUtils.isEmpty(password)||!isValidPassword(password)) {
                   Toast.makeText(getApplicationContext(), "Please enter a valid password", Toast.LENGTH_LONG).show();
                   return;
               } else if (!password.equals(confirmPassword)) {
                   Toast.makeText(getApplicationContext(), "Password does not match the confirm password.", Toast.LENGTH_LONG).show();
                   return;

               } else registerUser();
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

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }



    @Override
    public void onStart() {
        super.onStart();
    }
}
