package hr.foi.air.buuterknige;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import hr.foi.air.buuterknige.Adapter.ViewPagerAdapter;
import hr.foi.air.buuterknige.Fragments.ActiveFriendsFragment;
import hr.foi.air.buuterknige.Fragments.FragmentAllUsers;
import hr.foi.air.buuterknige.Fragments.FriendsFragment;


public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.lbl_email)
//    TextView lblEmail;
//
//    @BindView(R.id.input_email)
//    EditText inputEmail;
//
//    @BindView(R.id.lbl_password)
//    TextView lblPassword;
//
//    @BindView(R.id.input_password)
//    EditText inputPassword;
//
//    @BindView(R.id.input_answer)
//    EditText answer;
//
//    @BindView(R.id.btn_enter)
//    Button btnEnter;
//
//    @BindView(R.id.btn_logout)
//    Button btnLogOut;


    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;

    private FirebaseAuth firebaseAuth;

    private DrawerLayout drawer;


    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CurrentActivity.setActivity(this);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  toolbar.showOverflowMenu();
      //  setSupportActionBar(toolbar);

 //       ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        CurrentFirebaseAuth.setFirebaseAuth(firebaseAuth);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");


        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpagerid);

        // Adding fragments
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAllUsers(), "All users" );
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new ActiveFriendsFragment(), "Active friends");

        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.activity_logout) {
           logOut();

        }

        return super.onOptionsItemSelected(item);
    }



    public void logOut() {
        firebaseAuth.signOut();
        finish();
        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);
    }

//    @OnClick(R.id.btn_logout)
//    public void onButtonClick() {
//        Toast.makeText(this, "You have entered: ",
//                Toast.LENGTH_SHORT).show();
//        logOut();
//        Toast.makeText(getApplicationContext(), "You have entered: " + inputEmail.getText().toString()+ " "+inputPassword.getText().toString(),
//                Toast.LENGTH_SHORT).show();
//
//        String email = inputEmail.getText().toString();
//        String password = inputPassword.getText().toString();
//        createUser(email, password);

   // }

//    private void createUser(String name, String surname) {
//        if (TextUtils.isEmpty(userId)) {
//            userId = mFirebaseDatabase.push().getKey();
//        }
//
//        User user = new User(name, surname);
//        mFirebaseDatabase.child(userId).setValue(user);
//
//        addaUserChangeListener();
//    }

//    private void addaUserChangeListener() {
//        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//
//                if (user == null) {
//                    Log.e(TAG, "User data is null");
//                    return;
//                }
//                Log.e(TAG, "User data is changed" + user.email + ", " + user.password);
//
//                answer.setText(user.email+ ", " + user.password);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e(TAG, "Failed to reade user", databaseError.toException());
//
//            }
//        });
//    }

}
