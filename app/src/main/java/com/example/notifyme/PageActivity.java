package com.example.notifyme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PageActivity extends AppCompatActivity {
    public static final String Username = "USERNAME";
    private TextView textView2,textView3;
    private Button button3,button4,button5;
    private FloatingActionButton floatingActionButton;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        Intent i = getIntent();
        username = i.getStringExtra(Username);
        textView2.setText(username);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageActivity.this,MedicalActivity.class);
                startActivity(intent);
            }
        });
       button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageActivity.this,CrimeActivity.class);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageActivity.this,FriendsActivity.class);
                startActivity(intent);
            }
        });
    }
}