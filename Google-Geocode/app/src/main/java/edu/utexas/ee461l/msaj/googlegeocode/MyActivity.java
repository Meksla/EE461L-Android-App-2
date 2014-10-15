package edu.utexas.ee461l.msaj.googlegeocode;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.location.Geocoder;
import android.location.Address;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MyActivity extends FragmentActivity {
    private GoogleMap mMap;

    public void geoLocator(View v) throws IOException{
        Geocoder gc = new Geocoder(this);

        EditText editText = (EditText) findViewById(R.id.editText);
        String location = editText.getText().toString();

        List<Address> list = gc.getFromLocationName(location, 1);


        Address address = list.get(0);

        double lat = address.getLatitude();
        double lng = address.getLongitude();

        String toastText = String.valueOf(lat) + String.valueOf(lng);

        Toast.makeText(this, toastText , Toast.LENGTH_LONG);

        setUpMapIfNeeded();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(location));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Button button = (Button) findViewById(R.id.goButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    geoLocator(v);
                }
                catch(IOException e){

                }
            }
        });
    }
}
