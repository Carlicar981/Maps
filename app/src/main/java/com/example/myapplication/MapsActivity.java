package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,
        GoogleMap.OnMapClickListener , GoogleMap.OnMapLongClickListener ,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    Marker marker;
    private ActivityMapsBinding binding;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);


        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng julian = new LatLng(-70, 260);
        LatLng otro = new LatLng(-50, 200);
        mMap.addMarker(new MarkerOptions().position(julian)
                .title("Julian")
                .draggable(true)
                .snippet("Tlf: 243252532")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Syndey")
                .draggable(true)
                .snippet("Tlf: 243252532")
        );
        mMap.addMarker(new MarkerOptions().position(otro));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(julian));
        //Cambia el tipo de visibilidad
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);


        //tenemos que implementar GoogleMap.OnMapClickListener
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        //definicion de circulo
        CircleOptions circleOptions = new CircleOptions()
                .center(julian)
                .radius(500000)
                .strokeColor(Color.GREEN)
                .strokeWidth(30);
        Circle CIRCLE = mMap.addCircle(circleOptions);

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(julian, sydney, otro)
                .color(Color.RED));

        SharedPreferences.Editor edit = settings.edit();
        edit.putString("julian_lat", String.valueOf(julian.latitude));
        edit.putString("julian_long", String.valueOf(julian.longitude));

        edit.putString("sydney_lat", String.valueOf(sydney.latitude));
        edit.putString("sydney_long", String.valueOf(sydney.longitude));
        edit.commit();
        System.out.println(settings.getString("julian_lat",""));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, "Has hecho click en: " + latLng.latitude +
                ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
        //Para mover el marcador
        marker.setPosition(latLng);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(this, "Has hecho click en: " + latLng.latitude +
                ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, "Has hecho click en: " + marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(this, "Se empieza a arrastar: "
                + marker.getTitle() + "  " + marker.getPosition().latitude + ","
                + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.i("prueba", "prueba: " + marker.getPosition().latitude + ","
                + marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(this, "has soltado en: "
                + marker.getTitle() + " " + marker.getPosition().latitude + ","
                + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //muestro por pantalla mi nombre si clicamos en el menu
        int id = item.getItemId();
        switch (id) {
            case R.id.julian:
                Toast.makeText(getApplicationContext(), "julian", Toast.LENGTH_LONG).show();
                double julianlat=Double.parseDouble(settings.getString("julian_lat",""));
                double julianlong=Double.parseDouble(settings.getString("julian_long",""));
                LatLng julian = new LatLng(julianlat, julianlong);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(julian));
                return true;
            case R.id.syndey:
                Toast.makeText(getApplicationContext(), "syndey", Toast.LENGTH_LONG).show();
                double sydneylat=Double.parseDouble(settings.getString("sydney_lat",""));
                double sydneylong=Double.parseDouble(settings.getString("sydney_long",""));
                LatLng sydney = new LatLng(sydneylat, sydneylong);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}