package uk.ac.tees.scdt.mad.c2360539.flexchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav;
    private FrameLayout main_frame;
    FirebaseAuth auth;
    RecyclerView rv;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    String userName;
    List<String> list;
    UsersAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId())
                    {
                        case R.id.nav_home:
                            setFragement(new HomeFragment());
                            return true;

                        case R.id.nav_explore:
                            setFragement(new ExploreFragment());
                            return true;

                        case R.id.nav_setting:
                            setFragement(new SettingFragment());
                            return true;

                        case R.id.nav_profile:
                            setFragement(new AccountFragment());
                            return true;

                    }

                    return false;
                }
            };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        rv = findViewById(R.id.rv);
//        rv.setLayoutManager(new LinearLayoutManager(this));
//        list = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference();
//
//        reference.child("Users").child(user.getUid()).child("userName").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userName = snapshot.getValue().toString();
//                getUsers();
//                adapter = new UsersAdapter(list,userName,MainActivity.this);
//                rv.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.main_frame);

        nav.setOnItemSelectedListener(onNavigationItemSelectedListener);

        setFragement(new HomeFragment());
    }

    private void setFragement(Fragment fragement) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(),fragement);
        transaction.commit();
    }

//    public void getUsers(){
//        reference.child("Users").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String key = snapshot.getKey();
//
//                if (!key.equals(user.getUid()))
//                {
//                    list.add(key);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile_account){
            startActivity(new Intent(MainActivity.this, Profile_activity.class));
        }

        if (item.getItemId() == R.id.logout){
            auth.signOut();
            startActivity(new Intent(MainActivity.this, login_activity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}

