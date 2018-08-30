package hr.foi.air.buuterknige;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import hr.foi.air.buuterknige.Fragments.FragmentLogIn;

public class LogInActivity extends FragmentActivity {

    private FirebaseAuth firebaseAuth;

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

        firebaseAuth = FirebaseAuth.getInstance();
        CurrentFirebaseAuth.setFirebaseAuth(firebaseAuth);
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new FragmentLogIn());
        fragmentTransaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

