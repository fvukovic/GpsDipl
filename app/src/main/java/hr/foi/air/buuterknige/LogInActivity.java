package hr.foi.air.buuterknige;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.foi.air.buuterknige.Adapter.ViewPagerAdapter;
import hr.foi.air.buuterknige.Fragments.ActiveFriendsFragment;
import hr.foi.air.buuterknige.Fragments.FragmentAllUsers;
import hr.foi.air.buuterknige.Fragments.FragmentLogIn;
import hr.foi.air.buuterknige.Fragments.FragmentRegister;
import hr.foi.air.buuterknige.Fragments.FriendsFragment;

public class LogInActivity extends FragmentActivity {

    private FirebaseAuth firebaseAuth;
    private boolean logedIn;
    private static final String TAG = "BuuterKnige";
    private String userId;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference storeUserDefaultDataReference;
    private ViewPager viewPager;
    TabLayout tabLayout;


    TextView btnRegister;

    @BindView(R.id.input_email)
    EditText inputEmail;

    @BindView(R.id.lbl_password)
    TextView lblPassword;

    @BindView(R.id.input_password)
    EditText inputPassword;




    @BindView(R.id.btn_enter)
    Button btnEnter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

//        ButterKnife.bind(this);

        firebaseAuth = firebaseAuth.getInstance();
        CurrentFirebaseAuth.setFirebaseAuth(firebaseAuth);
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

//        if (savedInstanceState == null){
//            getSupportFragmentManager().beginTransaction()
//                    .add(android.R.id.content, new FragmentLogIn()).commit();}

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentLogIn());
        fragmentTransaction.commit();

    }

//
//    @OnClick(R.id.btn_enter)
//    public void onButtonClick() {
//        final String email = inputEmail.getText().toString();
//        final String password = inputPassword.getText().toString();
//
//
//            if (TextUtils.isEmpty(email)) {
//                Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
//                return;
//            } else if (TextUtils.isEmpty(password)) {
//                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
//                return;
//            } else {
//                signIn();
//            }
//
//            firebaseAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                String currentUserId = firebaseAuth.getCurrentUser().getUid();
//                                storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);
//                                storeUserDefaultDataReference.child("email").setValue(email);
//
//
//
//                                Toast.makeText(LogInActivity.this, "Sucessful", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(LogInActivity.this, "Create user", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//        }
//
//
//
//
//
//
//    private void signIn() {
//        String email = inputEmail.getText().toString();
//        String password = inputPassword.getText().toString();
//
//
//        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if (task.isSuccessful()) {
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    logedIn = true;
//                }
//               else if (!task.isSuccessful()){
//                    Toast.makeText(getApplicationContext(),"Incorrect username or password: ", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
//

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

