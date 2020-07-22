package com.travel.travelinc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.travel.travelinc.activities.ProfileActivity;
import com.travel.travelinc.activities.UsersActivity;
import com.travel.travelinc.activities.WelcomeActivity;
import com.travel.travelinc.fragments.ChatFragment;
import com.travel.travelinc.fragments.FriendsFragment;
import com.travel.travelinc.fragments.HomeFragment;
import com.travel.travelinc.fragments.RequestsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.travel.travelinc.post.CreatePost;
import com.travel.travelinc.post.UserFeedActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action bar related

        Toolbar toolbar = findViewById(R.id.main_app_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Travel Inc.");

        // Fragments handler using SmartTabLayout

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Home", HomeFragment.class)
                .add("Requests", RequestsFragment.class)
                .add("Chat", ChatFragment.class)
                .add("Friends", FriendsFragment.class)
                .add("Maps", MapView.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            // If no logged in user send them to login/register
            Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(welcomeIntent);
            finish();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null)
        {
            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("online").setValue("true");
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null)
        {
            FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("online").setValue(ServerValue.TIMESTAMP);
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to close the application?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.menuLogout:
                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainActivity.this);
                logoutBuilder.setTitle("Logout");
                logoutBuilder.setMessage("Are you sure you want to logout?");
                logoutBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online").setValue(ServerValue.TIMESTAMP);

                        FirebaseAuth.getInstance().signOut();

                        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(welcomeIntent);
                        finish();
                    }
                });
                logoutBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog logoutDialog = logoutBuilder.create();
                logoutDialog.show();
                return true;
            case R.id.menuAbout:
                AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(MainActivity.this);
                aboutBuilder.setTitle("Travel Inc.");
                aboutBuilder.setMessage("Travel Companion App\n\nRaf, 2018. All Rights Reserved.");
                aboutBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog aboutDialog = aboutBuilder.create();
                aboutDialog.show();
                return true;
            case R.id.menuProfile:
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(profileIntent);
                return true;
            case R.id.menuSearch:
                Intent usersIntent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(usersIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}