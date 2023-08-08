package com.example.incidentreporting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Angalia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angalia);

        TextView linkTextView = findViewById(R.id.linkTextView); // Move this line here

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://console.firebase.google.com/u/0/project/incidentreporting-5608b/database/incidentreporting-5608b-default-rtdb/data";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}
