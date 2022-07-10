package com.example.notifyme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MedicalActivity extends AppCompatActivity {
    //UI Views
    ListView l;
    EditText n;
    EditText m;
    Button b;
    Button getlocationBtn;
    TextView showLocationTxt;
    private static  final int REQUEST_LOCATION=1;
    LocationManager locationManager;
    String latitude,longitude;
    ArrayList<String> arr;  //length of array cannot be updated
    ArrayAdapter<String> adp;
    DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_medical);
        l = findViewById (R.id.lv);
        n = findViewById (R.id.name);
        m = findViewById (R.id.number);
        b = findViewById (R.id.add);

        mRef = FirebaseDatabase.getInstance ().getReference ();

        mRef.addChildEventListener (new ChildEventListener () {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               String value = snapshot.getValue(String.class).toString ();
               // String num = snapshot.child ("number")
              // String num = snapshot.PhoneNumberUtils.formatNumber (m.getText ().toString ());
                arr.add (value);
              //  arr.add (num);
                adp.notifyDataSetChanged ();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adp.notifyDataSetChanged ();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        showLocationTxt=findViewById(R.id.show_location);
        getlocationBtn=findViewById(R.id.getLocation);

        arr = new ArrayList<String> ();
        adp = new ArrayAdapter<String> (MedicalActivity.this, android.R.layout.simple_list_item_1, arr);
        l.setAdapter (adp);

        b.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                String name = n.getText ().toString ();
                String num = PhoneNumberUtils.formatNumber (m.getText ().toString ());
                arr.add (name);
                adp.notifyDataSetChanged ();
                arr.add (String.valueOf (num));
                adp.notifyDataSetChanged ();

            }
        });
        l.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText (MedicalActivity.this, arr.get (i), Toast.LENGTH_SHORT).show ();
            }
        });
        l.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener () {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder alert = new AlertDialog.Builder (MedicalActivity.this);
                View mView = getLayoutInflater ().inflate (R.layout.custom_dialog, null);
                Button btn_cancel = (Button) mView.findViewById (R.id.btn_cancel);
                Button btn_send = (Button) mView.findViewById (R.id.btn_send);
                alert.setView (mView);
                final AlertDialog alertDialog = alert.create ();

                alertDialog.setCanceledOnTouchOutside (false);

                btn_cancel.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss ();
                    }
                });

                btn_send.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        long num1 = Long.parseLong (m.getText ().toString ());
                        if (ContextCompat.checkSelfPermission (MedicalActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                            SmsManager smsManager = SmsManager.getDefault ();
                            StringBuffer smsBody = new StringBuffer();
                            smsBody.append("I'm in danger plz help me and my location is ");
                            smsBody.append("latitude:");
                            smsBody.append(latitude);
                            smsBody.append(", ");
                            smsBody.append("longitude");
                            smsBody.append(longitude);
                            smsManager.sendTextMessage (String.valueOf (num1), null, smsBody.toString(), null, null);
                            Toast.makeText (getApplicationContext (), "SMS sent successfully", Toast.LENGTH_LONG).show ();

                        } else {
                            ActivityCompat.requestPermissions (MedicalActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                        }

                    }
                });

                alertDialog.show ();
                return true;
            }


        });
        getlocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps

                    OnGPS();
                }
                else
                {
                    //GPS is already On then

                    getLocation();
                }
            }
        });

    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MedicalActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MedicalActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
};