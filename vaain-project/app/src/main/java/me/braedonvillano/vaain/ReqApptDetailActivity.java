package me.braedonvillano.vaain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import org.parceler.Parcels;

import me.braedonvillano.vaain.models.Appointments2;
import me.braedonvillano.vaain.models.Location;


public class ReqApptDetailActivity extends AppCompatActivity implements OnMapReadyCallback{

    Appointments2 appt2;
    MapView mapView;
    GoogleMap map;
    Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_appt_detail);

        TextView tvDate;
        TextView tvLocation;
        TextView tvDescription;
        TextView tvProName;
        TextView tvBeautName;
        TextView tvPrice;





        ImageView ivBeautImage;
        ImageView ivProductImage;

        appt2 = (Appointments2) Parcels.unwrap(getIntent().getParcelableExtra("appt"));

        tvDate = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        tvDescription = findViewById(R.id.tvDescription);
        tvProName = findViewById(R.id.tvProName);
        tvBeautName = findViewById(R.id.tvBeautName);
        tvPrice = findViewById(R.id.tvPrice);

        tvDate.setText(appt2.getDate());
        if(appt2.getDescription()!= null)tvDescription.setText(appt2.getDescription());
        tvProName.setText(appt2.getProName());
        tvPrice.setText(appt2.getPrice());


        ivBeautImage = findViewById(R.id.ivBeautImage);
        ivProductImage = findViewById(R.id.ivProImage);
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        location = appt2.getLocation();
        tvLocation.setText(location.getString("address"));

       if(appt2.getProImage() != null) Glide.with(this).load(appt2.getProImage().getUrl()).apply(RequestOptions.circleCropTransform()).into(ivProductImage);

       if(appt2.getBeautImage() != null && ParseUser.getCurrentUser().getBoolean("isClient")){
           tvBeautName.setText(appt2.getBeautName());
           Glide.with(this).load(appt2.getBeautImage().getUrl()).apply(RequestOptions.circleCropTransform()).into(ivBeautImage);
       }else{
           tvBeautName.setText(appt2.getClientName());
           Glide.with(this).load(appt2.getClientImage().getUrl()).apply(RequestOptions.circleCropTransform()).into(ivBeautImage);
       }

        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
       //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        LatLng loc = new LatLng((double) location.getNumber("lat"), (double) location.getNumber("long"));
        MapsInitializer.initialize(this);
        map.addMarker(new MarkerOptions().position(loc)
                .title(location.getString("address")));
        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 15);
        map.animateCamera(cameraUpdate);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
