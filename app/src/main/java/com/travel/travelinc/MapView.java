package com.travel.travelinc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapView extends Fragment implements OnMapReadyCallback {

    private int ID_LOCATION_PERMISSION = 100;
    private static GoogleMap mMap;
    static DatabaseReference mDatabase;
    private FloatingActionButton fabSearchLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_map_view, container, false);

        fabSearchLocation = view.findViewById(R.id.fabSearchLocation);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissionLocation();
        fabSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_dialog, null);

                new AlertDialog.Builder(getContext())
                        .setView(view)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etSearchLocation = view.findViewById(R.id.searchLoc_searchdlg_edittext);
                                String locationName = etSearchLocation.getText().toString();

                                searchLocationFromName(locationName);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void checkPermissionLocation() {
        if (checkPermission()){
            if (!isLocationEnabled()){
                turnOnLocationGoToSettings();
            }
        }else {
            requestPermission();
        }
    }

    private void searchLocationFromName(String locationName) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 5);

            if (addresses != null){
                LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(addresses.get(0).getFeatureName()));
                mMap.setMinZoomPreference(19.0f);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(20)
                        .strokeColor(Color.parseColor("#FF0000"))
                        .fillColor(Color.argb(70, 150, 50, 50)));
            }else {
                Toast.makeText(getContext(), "Location null", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkPermission()){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMinZoomPreference(18.0f);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ID_LOCATION_PERMISSION){
            if (grantResults.length > 0){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    if (checkPermission()){
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.setMinZoomPreference(18.0f);
                    }
                    if (!isLocationEnabled()){
                        turnOnLocationGoToSettings();
                    }else {
                        getCurrentLocation();
                    }
                }else {
                    Toast.makeText(getContext(), "Permission failed granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission()){
            if (isLocationEnabled()){
                getCurrentLocation();
            }
        }
    }

    private Boolean checkPermission(){
        if (getContext() != null){
            return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermission(){
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                ID_LOCATION_PERMISSION);
    }

    private Boolean isLocationEnabled(){
        if (getContext() != null){
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }

    private void getCurrentLocation(){
        if (checkPermission()){
            if (getContext() != null){
                LocationServices.getFusedLocationProviderClient(getContext()).getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()){
                            Location location = task.getResult();
                            if (location != null){
                                LatLng lastLocationLang = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(lastLocationLang));
                                mMap.setMinZoomPreference(16.0f);
                                mMap.setMaxZoomPreference(20.0f);
                            }
                        }
                    }
                });
            }
        }else {
            requestPermission();
        }
    }

    private void turnOnLocationGoToSettings(){
        new AlertDialog.Builder(getContext())
                .setTitle("Location Permission")
                .setMessage("The app needs location. please enable your location.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
