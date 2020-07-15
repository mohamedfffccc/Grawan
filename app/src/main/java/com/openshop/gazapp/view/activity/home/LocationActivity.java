package com.openshop.gazapp.view.activity.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.helper.GPSTracker;
import com.openshop.gazapp.data.model.addresses.Addresses;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.bitmapDescriptorFromVector;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    @BindView(R.id.activity_location_rel_title)
    RelativeLayout activityLocationRelTitle;
    @BindView(R.id.activity_location_btn_save)
    Button activityLocationBtnSave;
    @BindView(R.id.activity_location_btn_back)
    ImageView activityLocationBtnBack;
    private GoogleMap mMap;
    public int geocoderMaxResults = 1;
    public static String ADDRESS, CITY, REGION;
    public static double LATITUDE, LONGITUDE;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this , R.raw.style));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        try {

            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

//            if (!provider.contains("gps")) { //if gps is disabled
//                final Intent poke = new Intent();
//                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//                poke.setData(Uri.parse("3"));
//                sendBroadcast(poke);
//            } else {

            GPSTracker gpsTracker = new GPSTracker(LocationActivity.this, LocationActivity.this);
            Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
            LATITUDE = gpsTracker.getLatitude();
            LONGITUDE = gpsTracker.getLongitude();
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, geocoderMaxResults);
            Address address = addresses.get(0);
            LocationActivity.ADDRESS = address.getAddressLine(0);
            LocationActivity.CITY = address.getAdminArea();
            LocationActivity.REGION = address.getSubAdminArea();

            LatLng you = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(you, 17f));

//            }


        } catch (Exception e) {
            //e.printStackTrace();


        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                markerOptions.icon(bitmapDescriptorFromVector(LocationActivity.this, R.drawable.pin));

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                LATITUDE = latLng.latitude;
                LONGITUDE = latLng.longitude;

                Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());

                try {
                    /**
                     * Geocoder.getFromLocation - Returns an array of Addresses
                     * that are known to describe the area immediately surrounding the given LATITUDE and LONGITUDE.
                     */
                    List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, geocoderMaxResults);
                    Address address = addresses.get(0);
//                    Toast.makeText(LocationActivity.this,  address.getAdminArea()+address.getSubAdminArea(), Toast.LENGTH_SHORT).show();
                    LocationActivity.ADDRESS = address.getAddressLine(0);
                    LocationActivity.CITY = address.getAdminArea();
                    LocationActivity.REGION = address.getSubAdminArea();

                } catch (Exception e) {
                    //e.printStackTrace();

                }

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

            }
        });
    }



    //TODO add address
    public void addAddress(String lat, String lang, String address) {
        try {
            token = LoadData(this, API_TOKEN);
        } catch (Exception e) {

        }
        getClient().addAddress(token, lat, lang, address).enqueue(new Callback<Addresses>() {
            @Override
            public void onResponse(Call<Addresses> call, Response<Addresses> response) {
//                Toast.makeText(LocationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                try {
                    if (response.body().getStatus() == 1) {
                        finish();
                    }
                } catch (Exception e) {
                    //             Toast.makeText(LocationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Addresses> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.activity_location_btn_back, R.id.activity_location_btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_location_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_location_btn_save:
                addAddress(String.valueOf(LocationActivity.LATITUDE), String.valueOf(LocationActivity.LONGITUDE), LocationActivity.ADDRESS);
                finish();

                break;
        }
    }
}