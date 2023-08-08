package com.example.incidentreporting;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewReportActivity extends AppCompatActivity {

    private TextView descriptionTextView;
    private TextView phoneNumberTextView;
    private TextView vehicleTypeTextView;
    private TextView incidentTypeTextView;
    private ImageView imageView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        // Get the report key passed from ReportActivity
        String reportKey = getIntent().getStringExtra("reportKey");

        // Initialize the Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Find views
        descriptionTextView = findViewById(R.id.textViewDescription);
        phoneNumberTextView = findViewById(R.id.textViewPhoneNumber);
        vehicleTypeTextView = findViewById(R.id.textViewVehicleType);
        incidentTypeTextView = findViewById(R.id.textViewIncidentType);
        imageView = findViewById(R.id.imageView);

        // Retrieve report data from Firebase Database
        DatabaseReference reportRef = databaseReference.child("reports").child(reportKey);
        reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Report report = snapshot.getValue(Report.class);
                    if (report != null) {
                        // Display the report data in the appropriate views
                        descriptionTextView.setText(report.getDescription());
                        phoneNumberTextView.setText(report.getPhoneNumber());
                        vehicleTypeTextView.setText(report.getVehicleType());
                        incidentTypeTextView.setText(report.getIncidentType());

                        // Load the image using Glide or Picasso
                        if (report.getImageUrl() != null) {
                            Glide.with(ViewReportActivity.this)
                                    .load(report.getImageUrl())
                                    .into(imageView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the database error if needed
            }
        });
    }
}
