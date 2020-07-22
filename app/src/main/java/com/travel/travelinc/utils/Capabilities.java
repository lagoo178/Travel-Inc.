package com.travel.travelinc.utils;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class Capabilities extends Application
{
    private final String TAG = "CA/Capabilities";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // For offline use

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // If user disconnect

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            final DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userDatabase.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    userDatabase.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Log.d(TAG, "usersDatabase failed: " + databaseError.getMessage());
                }
            });
        }
    }
}