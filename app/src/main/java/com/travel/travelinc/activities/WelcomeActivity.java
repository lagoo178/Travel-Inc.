package com.travel.travelinc.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.somesh.permissionmadeeasy.enums.Permission;
import com.somesh.permissionmadeeasy.helper.PermissionHelper;
import com.somesh.permissionmadeeasy.intefaces.PermissionListener;
import com.travel.travelinc.MainActivity;
import com.travel.travelinc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import top.wefor.circularanim.CircularAnim;

public class WelcomeActivity extends AppCompatActivity {
    private final String TAG = "CA/WelcomeActivity";
    FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_SINGLE_PERMISSION = 999;
    public String address = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // activity_welcome views

        final EditText loginEmail = findViewById(R.id.login_email_text);
        final EditText loginPassword = findViewById(R.id.login_password_text);

        final EditText registerName = findViewById(R.id.register_name_text);
        final EditText registerEmail = findViewById(R.id.register_email_text);
        final EditText registerPassword = findViewById(R.id.register_password_text);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }else {
            getCity();
        }

        // Setting up login button work

        final ActionProcessButton loginButton = findViewById(R.id.login_button);
        loginButton.setProgress(0);
        loginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.setClickable(false);

                loginEmail.clearFocus();
                loginPassword.clearFocus();

                // Hiding the soft keyboard

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginPassword.getWindowToken(), 0);

                if (loginEmail.getText().length() == 0 || loginPassword.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty.", Toast.LENGTH_SHORT).show();

                    loginButton.setProgress(-1);
                    loginButton.setClickable(true);
                } else {
                    loginButton.setProgress(1);

                    // Loggin user with data he gave us

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String token = FirebaseInstanceId.getInstance().getToken();
                                final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                // Updating user device token

                                FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("token").setValue(token).addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful())
                                    {

                                        loginButton.setProgress(100);

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("city").setValue(address).addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()){
                                                Toast.makeText(getApplicationContext(), "Berhasil",Toast.LENGTH_LONG).show();
                                            }
                                        });

                                        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                                        {
                                            // Show animation and start activity

                                            new Handler().postDelayed(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    CircularAnim.fullActivity(WelcomeActivity.this, loginButton)
                                                            .colorOrImageRes(R.color.colorGreen)
                                                            .go(() -> {
                                                                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                                                finish();
                                                            });
                                                }
                                            }, 1000);
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Your email is not verified, we have sent you a new one.", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();

                                            loginButton.setProgress(-1);
                                            loginButton.setClickable(true);
                                        }
                                    }
                                    else
                                    {
                                        Log.d(TAG, "uploadToken failed: " + task1.getException().getMessage());
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                Log.d(TAG, "signIn failed: " + task.getException().getMessage());

                                loginButton.setProgress(-1);
                                loginButton.setClickable(true);
                            }
                        }
                    });
                }
            }
        });

        // Will handle "enter key" login

        loginPassword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    loginButton.performClick();
                    return true;
                }
                return false;
            }
        });

        // Setting up register button

        final ActionProcessButton registerButton = findViewById(R.id.register_button);
        registerButton.setProgress(0);
        registerButton.setMode(ActionProcessButton.Mode.ENDLESS);
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                registerButton.setClickable(false);

                loginButton.setClickable(false);

                registerName.clearFocus();
                registerEmail.clearFocus();
                registerPassword.clearFocus();

                // Hiding soft keyboard

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(registerButton.getWindowToken(), 0);

                if(registerEmail.getText().toString().length() == 0 || registerPassword.getText().toString().length() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty.", Toast.LENGTH_SHORT).show();

                    registerButton.setProgress(-1);
                    registerButton.setClickable(true);

                    loginButton.setClickable(true);
                }
                else
                {
                    registerButton.setProgress(1);

                    // Registering user with data he gave us

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                if(firebaseUser != null)
                                {
                                    String userid = firebaseUser.getUid();

                                    // "Packing" user data

                                    Map map = new HashMap<>();
                                    map.put("token", FirebaseInstanceId.getInstance().getToken());
                                    map.put("name", registerName.getText().toString());
                                    map.put("email", registerEmail.getText().toString());
                                    map.put("status", "Welcome to my Profile!");
                                    map.put("image", "default");
                                    map.put("cover", "default");
                                    map.put("date", ServerValue.TIMESTAMP);

                                    // Uploading user data

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(userid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                registerButton.setProgress(100);

                                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                                Toast.makeText(getApplicationContext(), "We have sent you a verification email to activate your account.", Toast.LENGTH_LONG).show();
                                                FirebaseAuth.getInstance().signOut();

                                                loginButton.setClickable(true);
                                            }
                                            else
                                            {
                                                Log.d(TAG, "registerData failed: " + task.getException().getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                Log.d(TAG, "createUser failed: " + task.getException().getMessage());

                                registerButton.setProgress(-1);
                                registerButton.setClickable(true);

                                loginButton.setClickable(true);
                            }
                        }
                    });
                }
            }
        });

        // Will handle "enter key" register

        registerPassword.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    registerButton.performClick();
                    return true;
                }
                return false;
            }
        });

        // Will handle Terms and Condition text click

        final TextView registerTerms = findViewById(R.id.register_terms);
        registerTerms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/h01d/ChatApp/blob/master/TERMS_AND_CONDITIONS.md")));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        SlidingUpPanelLayout slidingUpPanelLayout = findViewById(R.id.welcome_sliding);

        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED)
        {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        else
        {
            super.onBackPressed();
        }
    }

    void checkPermission(){
       PermissionHelper permissionHelper = PermissionHelper.Builder()
                .with(this)
                .requestCode(REQUEST_SINGLE_PERMISSION)
                .setPermissionResultCallback(new PermissionListener() {
                    @Override
                    public void onPermissionsGranted(int requestCode, ArrayList<String> acceptedPermissionList) {
                        getCity();
                    }

                    @Override
                    public void onPermissionsDenied(int requestCode, ArrayList<String> deniedPermissionList) {

                    }
                })
                .askFor(Permission.LOCATION)
                .rationalMessage("Permissions are required for app to work properly") //Optional
                .build();

        permissionHelper.requestPermissions();
    }

    void getCity(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addresses = null;
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                address = addresses.get(0).getSubAdminArea()+", "+addresses.get(0).getAdminArea();
                Log.d("datum", address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
