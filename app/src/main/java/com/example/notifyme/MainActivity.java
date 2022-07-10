package com.example.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance ().getReferenceFromUrl ("https://notify-f6194-default-rtdb.firebaseio.com/");
    private EditText edtTxtUser,edtTxtPassword;
    private Button button;
    private TextView textView;
    private String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtTxtUser = findViewById(R.id.edtTxtUser);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernametxt = edtTxtUser.getText ().toString ();
                final String passwordtxt = edtTxtPassword.getText ().toString ();

                if(usernametxt.isEmpty () || passwordtxt.isEmpty ()){
                    Toast.makeText (MainActivity.this, "Please enter your username or password", Toast.LENGTH_SHORT).show ();
                    finish ();
                } else {
                    databaseReference.child ("users").addListenerForSingleValueEvent (new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild (usernametxt)) {
                                final String getPassword = snapshot.child (usernametxt).child ("Password").getValue(String.class);
                                if(getPassword.equals (passwordtxt)){
                                    Toast.makeText (MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show ();
                                   // startActivity (new Intent (MainActivity.this,PageActivity.class));
                                    finish ();
                                }else {
                                    Toast.makeText (MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show ();
                                    finish ();
                                }
                            }else {
                                Toast.makeText (MainActivity.this, "Wrong username", Toast.LENGTH_SHORT).show ();
                                finish ();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                sendData();

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }
    public void sendData(){
        Username = edtTxtUser.getText().toString().trim();
        Intent intent=new Intent(MainActivity.this,PageActivity.class);
        intent.putExtra(PageActivity.Username,Username);
        startActivity(intent);
    }
}