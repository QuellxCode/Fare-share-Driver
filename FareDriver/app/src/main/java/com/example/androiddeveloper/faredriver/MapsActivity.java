package com.example.androiddeveloper.faredriver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private GPSTracker gpsTracker;
    private Location mLocation;
    double latitude, dest_latitude;
    double longitude, dest_longitude;
    double chemist_lat, chemist_long, order_lat, order_long;
    String return_odr;
    double lng;
    Button btnder_chem;
    ProgressDialog pDialog;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String d_id, cust1, descrption, chem_add, cust_name, cust_lat, cust_mobile,ride_type,ride_type1,ride_type2,cust_long, des_lat, des_lng, chem_lat, chem_long, odr_id,cust_mobile1,cust_mobile2;
    TextView txtviewdes, txtviewchemaddress, txtviewcustaddresss, txtviewcustname;
    Context c;
    private AccountHeader headerResult = null;
    private com.mikepenz.materialdrawer.Drawer result = null;
    public int status;
    //final Handler handler = new Handler();
    double lat;
    Button home;
    int i, odr;
    int odrcount = 0;
    String cust_lat1, cust_long1, des_lat1, des_lng1, odr_id1, cust_lat2, cust_long2, des_lat2, des_lng2;
    int odr1;
    int i1, i2, i3;
    int cuscount;
    Handler handler = null;
    Timer timer;
    TimerTask doAsynchronousTask;

    public static List<String> arrayList = new ArrayList<String>();
    static ListView clients;
    NotificationManager manager;
    Notification myNotication;
    ArrayList markerPoints = new ArrayList();
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    /*  LocationRequest mLocationRequest;
      GoogleApiClient mGoogleApiClient;*/
    LinearLayout llBottomSheet;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


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

        clients = (ListView)findViewById(R.id.listView);
        i=0;
        Button cust1 =(Button)findViewById(R.id.btncust1);
        Button cust2 =(Button)findViewById(R.id.btncust2);
        Button cust3 =(Button)findViewById(R.id.btncust3);

        cust1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()==1) {
                    CalculationByDistance();
                }else if(arrayList.size()==2){
                    CalculationByDistance1();
                }else if(arrayList.size()==3){
                    CalculationByDistanc2();
                }

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MapsActivity.this);

// Setting Dialog Title
                alertDialog2.setTitle("Yours Distance is :\n"+ com.example.androiddeveloper.faredriver.Handler.hdistance+" KM");

// Setting Dialog Message
                alertDialog2.setMessage("Yours Fare is: RS "+ com.example.androiddeveloper.faredriver.Handler.hrupees);

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();

                /*final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Context);
                } else {
                    builder = new AlertDialog.Builder(c);
                }
                builder.setTitle("Yours Distance is :"+ com.example.androiddeveloper.faredriver.Handler.hdistance+" KM")
                        .setMessage("Yours Fare is: RS "+ com.example.androiddeveloper.faredriver.Handler.hrupees)
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/


            }
        });
        cust2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()==1) {
                    CalculationByDistance();
                }else if(arrayList.size()==2){
                    CalculationByDistance1();
                }else if(arrayList.size()==3){
                    CalculationByDistanc2();
                }

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MapsActivity.this);

// Setting Dialog Title
                alertDialog2.setTitle("Yours Distance is :\n"+ com.example.androiddeveloper.faredriver.Handler.hdistance1+" KM");

// Setting Dialog Message
                alertDialog2.setMessage("Yours Fare is: RS "+ com.example.androiddeveloper.faredriver.Handler.hrupees1);

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();
            }
        });
        cust3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrayList.size()==1) {
                    CalculationByDistance();
                }else if(arrayList.size()==2){
                    CalculationByDistance1();
                }else if(arrayList.size()==3){
                    CalculationByDistanc2();
                }

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MapsActivity.this);

// Setting Dialog Title
                alertDialog2.setTitle("Yours Distance is :\n"+ com.example.androiddeveloper.faredriver.Handler.hdistance2+" KM");

// Setting Dialog Message
                alertDialog2.setMessage("Yours Fare is: RS "+ com.example.androiddeveloper.faredriver.Handler.hrupees2);

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();

            }
        });


        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        txtviewcustaddresss=(TextView)findViewById(R.id.btmcustaddress);
        //btnder_chem=(Button)findViewById(R.id.btnchedirecmtion);
        home = (Button) findViewById(R.id.home);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        d_id=prefs.getString("id",d_id);
        gpsTracker = new GPSTracker(getApplicationContext());
        llBottomSheet = (LinearLayout) findViewById(R.id.include2);
        AccountHeader headerResult = null;
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawerbg)
                .addProfiles(
                        new ProfileDrawerItem().withName("").withTextColor(getResources()
                                .getColor(R.color.colorBlack)).withEmail("")
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
                .withDrawerWidthDp(230)
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
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 2) {
                                result.closeDrawer();
                            } else if (drawerItem.getIdentifier() == 3) {
                                logout();
                                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                                result.closeDrawer();
                            }
                            if (intent != null) {
                                MapsActivity.this.startActivity(intent);
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
        callAsynchronousTask();

        if(mLocation!=null) {
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }

        Log.e("latlong", "" + latitude + "" + longitude);
        mLocation = gpsTracker.getLocation();
        if(mLocation!=null) {
            latitude = mLocation.getLatitude();
            Log.e("latitude", String.valueOf(latitude));
            longitude = mLocation.getLongitude();
            Log.e("longitude", String.valueOf(longitude));
        }
        else {
            Toast.makeText(c, "Enable yours location", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        configureCameraIdle();



    }
    public void googleintent1(View v){

        if(odrcount==1){
            String uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+cust_lat+","+ cust_long+"+to:"+des_lat+","+des_lng;
           // String uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+cust_lat+","+cust_long+"+to:"+cust_lat1+","+ cust_long1+"+to:"+des_lat+","+ des_lng;



            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }
        else if(odrcount==2){
           // uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+des_lat+","+des_lng+"+to:"+cust_lat+","+ cust_long;
            String uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+cust_lat+","+cust_long+"+to:"+cust_lat1+","+ cust_long1+"+to:"+des_lat+","+ des_lng;



            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);

        }else if(odrcount==3){
           // uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+des_lat+","+des_lng+"+to:"+cust_lat+","+ cust_long;
            String uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+cust_lat+","+cust_long+"+to:"+cust_lat1+","+ cust_long1+"+to:"+cust_lat2+","+ cust_long2+"+to:"+des_lat+","+ des_lng;



            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);

        }
/*
        String uri = "http://maps.google.com/maps?saddr="+latitude+","+longitude+"&daddr="+cust_lat+","+cust_long+"+to:"+cust_lat1+","+ cust_long1+"+to:"+des_lat+","+ des_lng;

        

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);*/

    }
    public void callAsynchronousTask() {
         handler = new Handler();
        timer = new Timer();
        doAsynchronousTask = new TimerTask() {
            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if(odrcount<3) {
                                if (odrcount == 0) {

                                    chemlocrider("23");
                                    notifi();

                                } else if (odrcount == 1) {

                                    chemlocrider1("23");


                                } else if (odrcount == 2) {

                                    chemlocrider2("23");



                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // TODO Auto-generated catch block
                        }
                    }
                });

            }


        };
        timer.schedule(doAsynchronousTask, 0, 20000);
        //execute in every 50000 ms

    }


    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;

                Geocoder geocoder = new Geocoder(MapsActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude,
                            latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        if (!locality.isEmpty() && !country.isEmpty())
                            lat=latLng.latitude;
                        lng=latLng.longitude;


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 16));

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_FINE_LOCATION);
            }
        }


    }

    public void notifi() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",i1);

        client.get(c,
                "http://gochem.com.pk/apis/rider_order_notification.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {

                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String odrid=obj1.getString("order_id");
                                return_odr=odrid;
                                Alertbox();
                                notification();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    }
                });
    }

    public void notifi1() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",i2);

        client.get(c,
                "http://gochem.com.pk/apis/rider_order_notification.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {

                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String odrid=obj1.getString("order_id");
                                return_odr=odrid;
                                Alertbox();
                                notification();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    }
                });
    }

    public void notifi2() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",i3);

        client.get(c,
                "http://gochem.com.pk/apis/rider_order_notification.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {

                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String odrid=obj1.getString("order_id");
                                return_odr=odrid;
                                Alertbox();
                                notification();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    }
                });
    }


    public void chemlocrider(String rid) {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",rid);
        i1= Integer.parseInt(rid);
        client.get(this,
                "http://gochem.com.pk/apis/customer_chem_loc_to_rider1.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String chem_name=obj1.getString("chemist_name");
                                String des=obj1.getString("description");
                                String custname=obj1.getString("customer_name");//

                                String custadd=obj1.getString("customer_address"); //
                                cust_lat=obj1.getString("lattitude"); //
                                des_lat=obj1.getString("des_lat");
                                des_lng=obj1.getString("des_lng");
                                cust_long=obj1.getString("longitude");//
                                cust_mobile=obj1.getString("cust_mobile");//

                                ride_type=obj1.getString("ride_type");

                                String odrid=obj1.getString("order_id");
                                odr_id =odrid;
                                odr= Integer.parseInt(odr_id);
                                txtviewcustaddresss.setText(custadd);
                                if(!(com.example.androiddeveloper.faredriver.Handler.ordid == Integer.parseInt(odrid))) {
                                    com.example.androiddeveloper.faredriver.Handler.ordid = Integer.parseInt(odrid);
                                        if(arrayList.size() < 3) {
                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                                getApplicationContext(),
                                                R.layout.activity_bottom_sheet_content,
                                                arrayList);

                                        clients.setAdapter(arrayAdapter);
                                        arrayList.add("Customer name    "+custname +"\nCustomer Mobile    "+cust_mobile + "\n customer Address \n   " + custadd);
                                            if(ride_type.equalsIgnoreCase("1")){
                                                timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
                                                timer.purge();
                                            }

                                    }
                                    else{
                                            i1++;
                                            chemlocrider(String.valueOf(i1));
                                        }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        i1++;
                        chemlocrider(String.valueOf(i1));
                    }
                });


    }

    public void chemlocrider1(String rid) {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",rid);
        i2= Integer.parseInt(rid);
        client.get(c,
                "http://gochem.com.pk/apis/customer_chem_loc_to_rider1.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String chem_name=obj1.getString("chemist_name");
                                String des=obj1.getString("description");
                                String custname=obj1.getString("customer_name");//

                                String custadd=obj1.getString("customer_address"); //
                                cust_lat1=obj1.getString("lattitude"); //
                                cust_long1=obj1.getString("longitude");//
                                des_lat1=obj1.getString("des_lat");
                                des_lng1=obj1.getString("des_lng");
                                cust_mobile1=obj1.getString("cust_mobile");
                                ride_type1=obj1.getString("ride_type");
                                if(ride_type1.equalsIgnoreCase("1")){
                                    timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
                                    timer.purge();
                                    callAsynchronousTask();
                                }
                                else {
                                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                                    temp.setLatitude(Double.parseDouble(des_lat1));
                                    temp.setLongitude(Double.parseDouble(des_lng1));
                                    Location temp1 = new Location(LocationManager.GPS_PROVIDER);
                                    temp1.setLatitude(Double.parseDouble(des_lat));
                                    temp1.setLongitude(Double.parseDouble(des_lng));
                                    String odrid = obj1.getString("order_id");
                                    double radius = 1000.0;
                                    float distance = temp.distanceTo(temp1);

                                    if (!(com.example.androiddeveloper.faredriver.Handler.ordid == Integer.parseInt(odrid))) {
                                        com.example.androiddeveloper.faredriver.Handler.ordid = Integer.parseInt(odrid);
                                        if (distance < radius) {
                                            if (arrayList.size() < 3) {
                                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                                        getApplicationContext(),
                                                        R.layout.activity_bottom_sheet_content,
                                                        arrayList);

                                                clients.setAdapter(arrayAdapter);
                                                arrayList.add("Customer name    " + custname + "\nCustomer Mobile    " + cust_mobile1 + "\n customer Address    " + custadd);

                                            }
                                        } else {
                                            i2++;
                                            chemlocrider1(String.valueOf(i2));
                                        }
                                    }
                                    odr_id1 = odrid;
                                    odr1 = Integer.parseInt(odr_id1);
                                    txtviewcustaddresss.setText(custadd);
                                    notifi1();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        i2++;
                        chemlocrider(String.valueOf(i2));
                    }
                });


    }
    public void chemlocrider2(String rid) {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("rider_id",rid);
        i3= Integer.parseInt(rid);
        client.get(c,
                "http://gochem.com.pk/apis/customer_chem_loc_to_rider1.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {
                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {
                                String chem_name=obj1.getString("chemist_name");
                                String des=obj1.getString("description");
                                String custname=obj1.getString("customer_name");//

                                String custadd=obj1.getString("customer_address"); //
                                cust_lat2=obj1.getString("lattitude"); //
                                cust_long2=obj1.getString("longitude");//
                                des_lat2=obj1.getString("des_lat");
                                String odrid=obj1.getString("order_id");
                                des_lng2=obj1.getString("des_lng");
                                cust_mobile2=obj1.getString("cust_mobile");
                                ride_type2=obj1.getString("ride_type");
                                if(ride_type2.equalsIgnoreCase("1")){
                                    timer.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
                                    timer.purge();
                                    callAsynchronousTask();
                                }
                                else{


                                Location temp = new Location(LocationManager.GPS_PROVIDER);
                                temp.setLatitude(Double.parseDouble(des_lat2));
                                temp.setLongitude(Double.parseDouble(des_lng2));
                                Location temp1 = new Location(LocationManager.GPS_PROVIDER);
                                temp1.setLatitude(Double.parseDouble(des_lat));
                                temp1.setLongitude(Double.parseDouble(des_lng));
                                double radius = 1000.0;
                                float distance = temp.distanceTo(temp1);
                                if(!(com.example.androiddeveloper.faredriver.Handler.ordid == Integer.parseInt(odrid))) {
                                    com.example.androiddeveloper.faredriver.Handler.ordid = Integer.parseInt(odrid);
                                    if (distance < radius) {
                                        if (arrayList.size() < 3) {
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                                    getApplicationContext(),
                                                    R.layout.activity_bottom_sheet_content,
                                                    arrayList);
                                            clients.setAdapter(arrayAdapter);
                                            arrayList.add("Customer name:" + custname + "\nCustomer Mobile    "+cust_mobile2 +"\n customer Address:\n   " + custadd);

                                        }
                                    }
                                    else{
                                        i3++;
                                        chemlocrider2(String.valueOf(i3));
                                    }
                                }
                                odr_id1 =odrid;
                                odr1= Integer.parseInt(odr_id1);
                                txtviewcustaddresss.setText(custadd);
                                notifi2();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        i3++;
                        chemlocrider(String.valueOf(i3));
                    }
                });


    }
    public void CalculationByDistance() {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(Double.parseDouble(cust_lat));
        temp.setLongitude(Double.parseDouble(cust_long));
        Location temp1 = new Location(LocationManager.GPS_PROVIDER);
        temp1.setLatitude(Double.parseDouble(des_lat));
        temp1.setLongitude(Double.parseDouble(des_lng));
cuscount++;

        float distance = temp.distanceTo(temp1);
        float km = distance/1000;
        Log.e("hsdshd", String.valueOf(km));
        Log.e("hsdshd", String.valueOf(distance));

        double a= km*15;
        String rupees = String.valueOf(a);
        Log.e("hsdshd", String.valueOf(rupees));

        com.example.androiddeveloper.faredriver.Handler.hdistance = String.valueOf(km);
        com.example.androiddeveloper.faredriver.Handler.hrupees = rupees;


    }

    public void CalculationByDistance1() {
        CalculationByDistance();
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(Double.parseDouble(cust_lat1));
        temp.setLongitude(Double.parseDouble(cust_long1));
        Location temp1 = new Location(LocationManager.GPS_PROVIDER);
        temp1.setLatitude(Double.parseDouble(des_lat1));
        temp1.setLongitude(Double.parseDouble(des_lng1));
        cuscount++;
        float distance = temp.distanceTo(temp1);
        float km = distance/1000;
        Log.e("hsdshd", String.valueOf(km));
        Log.e("hsdshd", String.valueOf(distance));


        double a= (km*15)-((km*15*30)/100);
        String rupees = String.valueOf(a);
        com.example.androiddeveloper.faredriver.Handler.hdistance1 = String.valueOf(km);
        com.example.androiddeveloper.faredriver.Handler.hrupees1 = rupees;

        double pre= Double.parseDouble(com.example.androiddeveloper.faredriver.Handler.hrupees);
        double b= pre - ((pre * 30)/100);
        com.example.androiddeveloper.faredriver.Handler.hrupees= String.valueOf(b);
    }


    public void CalculationByDistanc2() {
        CalculationByDistance();
        CalculationByDistance1();
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(Double.parseDouble(cust_lat2));
        temp.setLongitude(Double.parseDouble(cust_long2));
        Location temp1 = new Location(LocationManager.GPS_PROVIDER);
        temp1.setLatitude(Double.parseDouble(des_lat2));
        temp1.setLongitude(Double.parseDouble(des_lng2));
        cuscount++;
        float distance = temp.distanceTo(temp1);
        float km = distance/1000;

        double a= (km*15)-((km*15*40)/100);
        String rupees = String.valueOf(a);
        Log.e("hsdshd", String.valueOf(km));
        Log.e("hsdshd", String.valueOf(distance));
        Log.e("hsdshd", String.valueOf(rupees));
        com.example.androiddeveloper.faredriver.Handler.hdistance2 = String.valueOf(km);
        com.example.androiddeveloper.faredriver.Handler.hrupees2 = rupees;

        double pre= Double.parseDouble(com.example.androiddeveloper.faredriver.Handler.hrupees);
        double b= pre - ((pre * 40)/100);
        com.example.androiddeveloper.faredriver.Handler.hrupees= String.valueOf(b);

        double pre2= Double.parseDouble(com.example.androiddeveloper.faredriver.Handler.hrupees1);
        double b2= pre2 - ((pre2 * 40)/100);
        com.example.androiddeveloper.faredriver.Handler.hrupees1= String.valueOf(b2);

    }
    

    public  void Alertbox(){
        llBottomSheet.setVisibility(llBottomSheet.VISIBLE);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setPeekHeight(90);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        llBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        new FancyGifDialog.Builder(this)
                .setTitle("Fare share")
                .setMessage("New Order from Fare share")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#32CD32")
                .setPositiveBtnText("Accept")
                .setNegativeBtnBackground("#DC143C")
                .setGifResource(R.drawable.git3)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        accept();
                        final ProgressDialog dialog = ProgressDialog
                                .show(MapsActivity.this, "",
                                        "Fetching data..." ,
                                        true);
                        dialog.show();
                        odrcount++;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                dialog.dismiss();
                            }
                        }, 5000);

                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        Toast.makeText(MapsActivity.this,"Order Rejected",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }
    public void accept() {
        final AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("order_id",return_odr);
        params.put("rider_id",d_id);

        client.get(c,
                "http://gochem.com.pk/apis/rider_order_status_update1.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, byte[] responseBody) {
                        try {

                            JSONArray array = new JSONArray(new String(responseBody));
                            JSONObject obj1 = array.getJSONObject(0);
                            Boolean bool = obj1.getBoolean("boolean");
                            if(bool) {

                                Toast.makeText(MapsActivity.this,
                                        "order Accepted",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                    }
                });
    }
    public void notification(){
        Intent resultIntent = new Intent(this, MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification.Builder builder = new Notification.Builder(MapsActivity.this);
        builder.setAutoCancel(true);
        builder.setTicker("You have new order from Fare share");
        builder.setContentTitle("Fare share Notification");
        builder.setContentText("would you like to accept");
        builder.setSmallIcon(R.drawable.logo48);
        builder.setContentIntent(resultPendingIntent);
        builder.setOngoing(false);
        builder.setOnlyAlertOnce(true);
        builder.build();
        myNotication = builder.getNotification();
        myNotication.defaults |= Notification.DEFAULT_SOUND;
        manager.notify(Integer.parseInt(d_id), myNotication);
    }
    public void logout(){
        prefs = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        moveTaskToBack(true);
    }
}