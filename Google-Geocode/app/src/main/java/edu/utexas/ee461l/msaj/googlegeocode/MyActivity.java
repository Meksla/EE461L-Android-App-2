package edu.utexas.ee461l.msaj.googlegeocode;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.location.Geocoder;
import android.location.Address;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


public class MyActivity extends FragmentActivity {
    private GoogleMap mMap;
    final private String ACCESS_TOKEN = "1530582008.5b9e1e6.1d885f32fb6e4685b63cb2366c24a893";   //Instagram API Access Token

    public void geoLocator(View v) throws IOException{
        Geocoder gc = new Geocoder(this);

        hideSoftKeyboard(v);

        EditText editText = (EditText) findViewById(R.id.editText);
        String location = editText.getText().toString();

        List<Address> list = gc.getFromLocationName(location, 1);

        //check if the Geocododer found a location
        if(list.size() == 0){
            Toast noLocationToast = Toast.makeText(this, "Location Not Found", Toast.LENGTH_LONG);
            noLocationToast.show();
            return;
        }

        Address address = list.get(0);

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        String locality = address.getLocality();

        String toastText = "Locality: " + locality;

        Toast locationToast = Toast.makeText(this, toastText , Toast.LENGTH_LONG);
        locationToast.show();


        setUpMapIfNeeded();
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(location));

        String instagramAPIString = "https://api.instagram.com/v1/media/search?";
        //add url parameters
        instagramAPIString += "lat=" + String.valueOf(lat) + "&lng=" + String.valueOf(lng);
        //add ACCESS TOKEN
        instagramAPIString += "&access_token=" + ACCESS_TOKEN;
        new HttpAsyncTask().execute(instagramAPIString);
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override

        protected void onPostExecute(String result) {
            try{
                JSONObject json = new JSONObject(result);
                JSONArray images = json.getJSONArray("data"); // get articles array
                String links = new String();
                TextView t2 = (TextView) findViewById(R.id.textView);
                for(int i = 0; i < images.length(); i++) {
                    String myImage = images.getJSONObject(i).getString("link"); // return an article url
                    links += myImage + "\n";
                }
                t2.setText(links);
            }
            catch(JSONException e){

            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {

        }
        return result;
    }

    private void hideSoftKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        final Button instaButton = (Button) findViewById(R.id.instagramButton);

        instaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instaButton.getText() == "Show Instagram Pics") {
                    TextView tv = (TextView) findViewById(R.id.textView);
                    MapFragment mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
                    mMapFragment.getView().setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    instaButton.setText("Show Map");
                }
                else{
                    TextView tv = (TextView) findViewById(R.id.textView);
                    MapFragment mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
                    mMapFragment.getView().setVisibility(View.VISIBLE);
                    tv.setVisibility(View.INVISIBLE);
                    instaButton.setText("Show Instagram Pics");
                }
            }
        });

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
