package com.example.androiddeveloper.faredriver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class Drawer extends AppCompatActivity {

    private AccountHeader headerResult = null;
    private com.mikepenz.materialdrawer.Drawer result = null;
    Button home;
    double lat,lng;
    Context ctx;
    String address;
    EditText txtaddress=null;
    AlertDialog alertDialog;
    TextView countdown;
    SharedPreferences prefs;
    private GPSTracker gps;
    private Location mLocation;
    boolean isGPSEnabled = false;
    private LatLng latLng;
    Double latitude,longitude;
    private LocationManager lm;
    ProgressDialog progressDialog;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        home = (Button) findViewById(R.id.home);
        gps = new GPSTracker(getApplicationContext());
        gps = new GPSTracker(Drawer.this);
        mLocation = gps.getLocation();
        if(mLocation!=null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }else{
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);

        }
        AccountHeader headerResult = null;
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerbg)
                .addProfiles(
                        new ProfileDrawerItem().withName("Sajjad").withTextColor(getResources()
                                .getColor(R.color.colorBlack)).withEmail("sajjadahmed33@gmal.com")
                                .withIcon(R.drawable.logo)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile,
                                                    boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(250)
                .withHasStableIds(true)
                .withShowDrawerOnFirstLaunch(false)
                .withAccountHeader(headerResult)
                .addDrawerItems(


                        new PrimaryDrawerItem().withName(R.string.drawer_items1).withIcon(R.drawable.home)
                                .withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_items2).withIcon(R.drawable.guid)
                                .withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_items3).withIcon(R.drawable.logout)
                                .withIdentifier(3).withSelectable(false))


                .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                Intent i = new Intent(ctx, Drawer.class);
                                startActivity(i);
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 2) {
                                result.closeDrawer();
                                Intent i = new Intent(Drawer.this, MapsActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.push_left_in,
                                        R.anim.push_left_out);
                            } else if (drawerItem.getIdentifier() == 3) {
                                Intent i = new Intent(Drawer.this, Updateprofile.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                result.closeDrawer();
                                finish();
                            }
                            if (intent != null) {
                                Drawer.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.openDrawer();
            }
        });


    }
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        moveTaskToBack(true);
    }


}
