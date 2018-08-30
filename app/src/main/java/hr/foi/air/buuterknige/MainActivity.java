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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hr.foi.air.buuterknige.Adapter.ViewPagerAdapter;
import hr.foi.air.buuterknige.Fragments.RequestFriendsFragment;
import hr.foi.air.buuterknige.Fragments.FragmentAllUsers;
import hr.foi.air.buuterknige.Fragments.FriendsFragment;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String onlineUserId;
    private FirebaseUser currentUser;
    private DatabaseReference onlinUsersReferemce;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CurrentActivity.setActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbarid);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        onlinUsersReferemce = FirebaseDatabase.getInstance().getReference().child("users").child(onlineUserId);

        if (currentUser == null) {
            logOut();
        }
        else if (currentUser != null) {
            onlinUsersReferemce.child("online").setValue(true);
        }

 //       ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        CurrentFirebaseAuth.setFirebaseAuth(firebaseAuth);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");


        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpagerid);

        // Adding fragments
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAllUsers(), "All users" );
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new RequestFriendsFragment(), "Requests");

        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();



        switch (id) {
            case R.id.activity_join:
                onlinUsersReferemce.child("online").setValue(true);
                break;

            case R.id.activity_unjoin:
                onlinUsersReferemce.child("online").setValue(false);
                break;
            case R.id.activity_logout:
                onlinUsersReferemce.child("online").setValue(false);
                logOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logOut() {
        mAuth.signOut();

        Intent i = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
