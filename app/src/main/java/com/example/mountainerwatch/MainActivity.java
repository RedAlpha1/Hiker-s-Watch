package com.example.mountainerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;

    public void updateLocation(Location location)
    {
        Log.e("LocationInfo","location"+location.toString());

        TextView latTextview = findViewById(R.id.latitude);
        TextView lonTextview = findViewById(R.id.longitude);
        TextView accuracyTextview = findViewById(R.id.acuracy);
        TextView altitudeTextview = findViewById(R.id.altitude);
        TextView addressTextview = findViewById(R.id.address);
        latTextview.setText("Latitude "+location.getLatitude());
        lonTextview.setText("Longitude "+location.getLongitude());
        accuracyTextview.setText("Accuracy "+location.getAccuracy());
        altitudeTextview.setText("Altitude "+location.getAltitude());

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find Address";
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            address ="";
            if(addressList!=null&&addressList.size()>0)
                if(addressList.get(0).getSubThoroughfare()!=null){
                    address+=addressList.get(0).getSubThoroughfare()+ " ";
                }

            if(addressList.get(0).getThoroughfare()!=null) {
                address += addressList.get(0).getThoroughfare()+ "\n";
            }

            if(addressList.get(0).getLocality()!=null) {
                address += addressList.get(0).getLocality()+ "\n";
            }
            if(addressList.get(0).getPostalCode()!=null) {
                address += addressList.get(0).getPostalCode()+ "\n";
            }
            if(addressList.get(0).getCountryName()!=null) {
                address += addressList.get(0).getCountryName()+ "\n";
            }
            if(addressList.get(0).getSubAdminArea()!=null)
            {
                address += addressList.get(0).getSubAdminArea()+ "\n";
            }
            addressTextview.setText("Address "+address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            startListning();
        }


    }
    public void startListning()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };
        if(Build.VERSION.SDK_INT<23)
        {
           startListning();
        }
        else
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastLocation!=null)
            updateLocation(lastLocation);

        }
    }
}

