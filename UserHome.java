package com.example.incidentreporting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserHome extends AppCompatActivity {
    Button report,regulation,sign;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        report=findViewById(R.id.report);
        regulation=findViewById(R.id.regulation);
        sign=findViewById(R.id.sign);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this, ReportActivity.class));
            }
        });

        regulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this,RoadRegulations.class));
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this,RoadSigns.class));
            }
        });
    }
}

