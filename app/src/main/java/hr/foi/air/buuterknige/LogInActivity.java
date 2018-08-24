package hr.foi.air.buuterknige;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private boolean logedIn;
    private static final String TAG = "BuuterKnige";

    @BindView(R.id.lbl_email)
    TextView lblEmail;

    @BindView(R.id.input_email)
    EditText inputEmail;

    @BindView(R.id.lbl_password)
    TextView lblPassword;

    @BindView(R.id.input_password)
    EditText inputPassword;

    @BindView(R.id.input_answer)
    EditText answer;

    @BindView(R.id.btn_enter)
    Button btnEnter;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        firebaseAuth = firebaseAuth.getInstance();
        CurrentFirebaseAuth.setFirebaseAuth(firebaseAuth);
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @OnClick(R.id.btn_enter)
    public void onButtonClick() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        } else {
            signIn();
        }

    }



    private void signIn() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                    logedIn = true;
                }
               else if (!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Incorrect username or password: ", Toast.LENGTH_SHORT).show();

                }
            }
        });
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

