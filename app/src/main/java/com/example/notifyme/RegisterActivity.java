package com.example.notifyme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance ().getReferenceFromUrl ("https://notify-f6194-default-rtdb.firebaseio.com/");

    private EditText editTextPersonName,editTextPersonName2,editTextEmailAddress,editTextPassword,editTextPassword2;
    private Button button2, loginNow;
    private String Username;
    //FirebaseDatabase rootNode;
    //DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextPersonName2 = findViewById(R.id.editTextPersonName2);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPassword2 = findViewById(R.id.editTextPassword2);
        button2 = findViewById(R.id.button2);
        loginNow = findViewById (R.id.loginNow);
        button2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                final String regname = editTextPersonName.getText ().toString ();
                final String reguser = editTextPersonName2.getText ().toString ();
                final String regemail = editTextEmailAddress.getText ().toString ();
                final String regpassword = editTextPassword.getText ().toString ();
                final String regconfirmpass = editTextPassword2.getText ().toString ();

                if(regname.isEmpty () || reguser.isEmpty () || regemail.isEmpty () || regpassword.isEmpty () || regconfirmpass.isEmpty ()) {
                    Toast.makeText (RegisterActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show ();
                }else if (!regpassword.equals (regconfirmpass)){
                    Toast.makeText (RegisterActivity.this, "Passwords are not matching", Toast.LENGTH_SHORT).show ();
                }else {
                    databaseReference.child ("users").addListenerForSingleValueEvent (new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild (reguser)){
                                Toast.makeText (RegisterActivity.this, "Username already exist", Toast.LENGTH_SHORT).show ();
                            }else {
                                databaseReference.child ("users").child (reguser).child ("Name").setValue (regname);
                                databaseReference.child ("users").child (reguser).child ("Username").setValue (reguser);
                                databaseReference.child ("users").child (reguser).child ("Email").setValue (regemail);
                                databaseReference.child ("users").child (reguser).child ("Password").setValue (regpassword);
                                databaseReference.child ("users").child (reguser).child ("ConfirmPassword").setValue (regconfirmpass);
                                Toast.makeText (RegisterActivity.this, "User register success for you", Toast.LENGTH_SHORT).show ();
                                finish ();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
     //           sendData();
                finish ();

            }
        });

    }
 /*   public void sendData(){
       Username = editTextPersonName2.getText().toString().trim();
       Intent intent=new Intent(RegisterActivity.this,PageActivity.class);
       intent.putExtra(PageActivity.Username,Username);
       startActivity(intent);
    }*/
    }
