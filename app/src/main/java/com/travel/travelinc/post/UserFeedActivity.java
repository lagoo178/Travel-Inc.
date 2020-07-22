package com.travel.travelinc.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import jahirfiquitiva.libs.fabsmenu.FABsMenu;
import jahirfiquitiva.libs.fabsmenu.TitleFAB;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.travel.travelinc.R;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    String email;
    static String uid, username;

    public static List<PostingSupport> arrayList = new ArrayList<>();


    private FABsMenu menu;
    private TitleFAB button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        Intent i = getIntent();
        email = i.getStringExtra("email");


    }
    public void initMyProfile()
    {
        if(button1 != null)
        {
            menu.removeButton(button1);
        }

        if(button2 != null)
        {
            menu.removeButton(button2);
        }

        button1 = new TitleFAB(UserFeedActivity.this);
        button1.setTitle("Search Location");
        button1.setBackgroundColor(getResources().getColor(R.color.colorPurple));
        button1.setRippleColor(getResources().getColor(R.color.colorPurpleDark));
        button1.setImageResource(R.drawable.ic_action_search);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //MapView.arrayList.clear();
                SearchDialog searchDialog = new SearchDialog();
                searchDialog.show(getSupportFragmentManager(), "search dialog");

                menu.collapse();
            }
        });
        menu.addButton(button1);

        button2 = new TitleFAB(UserFeedActivity.this);
        button2.setTitle("Create Post");
        button2.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        button2.setRippleColor(getResources().getColor(R.color.colorBlueDark));
        button2.setImageResource(R.drawable.ic_action_create);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(UserFeedActivity.this, CreatePost.class));
                Toast.makeText(UserFeedActivity.this, "Create a Post", Toast.LENGTH_SHORT).show();

                menu.collapse();
            }
        });
        menu.addButton(button2);
    }
}