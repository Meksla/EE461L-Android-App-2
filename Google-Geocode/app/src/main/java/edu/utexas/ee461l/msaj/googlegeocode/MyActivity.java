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

import java.io.IOException;
import java.util.List;

public class MyActivity extends FragmentActivity {

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
