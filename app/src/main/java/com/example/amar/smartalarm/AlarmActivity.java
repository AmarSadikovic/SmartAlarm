package com.example.amar.smartalarm;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private Button btnLocaiton;
    private Location myLocation;

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;

    private GoogleApiClient mClient;
    private GeofencingClient mGeofencingClient;
    private Geofencing mGeofencing;

    private ArrayList mGeofenceList;

    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        checkPermissions();

        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(200);
        btnLocaiton = findViewById(R.id.btnLocaiton);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        btnLocaiton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myLocation != null){
                    System.out.println("My Location: "+myLocation.toString());
                }
                System.out.println("My Location: = null");


            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mClient != null){
            mClient.connect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Do stuff
        mGeofencing = new Geofencing(this, mClient);
        mGeofencing.registerAllGeofences();
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("Connection failed!");
    }

    public void refreshPlacesData() {
        //TODO: SkaneTrafiken API places Refresh
    }


    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            //Already checked
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "New Location: "+location.toString(), Toast.LENGTH_LONG).show();
        myLocation = location;
    }
}
