package com.example.amar.smartalarm;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class Geofencing {


    private float geofenceRadius = 250;
    private long geofenceTimeout = 24 * 60 * 60 * 1000; //24 hours

    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private AlarmActivity alarmActivity;
    private GoogleApiClient mClient;

    private GeofencingClient mGeofencingClient;

    public Geofencing(AlarmActivity alarmActivity, GoogleApiClient mClient) {
        this.alarmActivity = alarmActivity;
        this.mClient = mClient;
        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
        mGeofencingClient = LocationServices.getGeofencingClient(alarmActivity);

    }

    public void registerAllGeofences() {
        createGeofence();
        System.out.println("List size: "+mGeofenceList.size()+" mClient: "+mClient.isConnected());
        if (mClient == null || !mClient.isConnected() || mGeofenceList == null || mGeofenceList.size() == 0) {

            return;
        }
        addGeofences();

    }

    public void createGeofence() {
        try {
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId("1")
                    .setCircularRegion(
                            55.432368,
                            13.711914,
                            geofenceRadius
                    )
                    .setExpirationDuration(geofenceTimeout)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        } catch (SecurityException securityException) {
            System.out.println("Security exception: " + securityException.getMessage());
        }
    }

    public void addGeofences() {
        if (ActivityCompat.checkSelfPermission(alarmActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(mGeofenceList.size() != 0) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), getmGeofencePendingIntent());
        }else{
            System.out.println("List is 0");
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getmGeofencePendingIntent() {
        if (mGeofencePendingIntent != null) {

            return mGeofencePendingIntent;
        }

        Intent intent = new Intent(alarmActivity, GeofenceTransitionsIntentService.class);
        mGeofencePendingIntent = PendingIntent.getService(alarmActivity, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
}
