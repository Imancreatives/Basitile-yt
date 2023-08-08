package com.example.incidentreporting;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ReportActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private EditText editTextDescription;
    private EditText editTextPhoneNumber;
    private Spinner spinnerVehicleType;
    private Spinner spinnerIncidentType;
    private Button buttonAttachPhoto;
    private Button buttonSendReport;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        databaseReference = FirebaseDatabase.getInstance().getReference("reports");
        storageReference = FirebaseStorage.getInstance().getReference();

        editTextDescription = findViewById(R.id.editTextDescription);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        spinnerVehicleType = findViewById(R.id.spinnerVehicleType);
        spinnerIncidentType = findViewById(R.id.spinnerIncidentType);
        buttonAttachPhoto = findViewById(R.id.buttonAttachPhoto);
        buttonSendReport = findViewById(R.id.buttonSendReport);

        ArrayAdapter<CharSequence> vehicleTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.vehicle_types,
                android.R.layout.simple_spinner_item
        );
        vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleType.setAdapter(vehicleTypeAdapter);

        ArrayAdapter<CharSequence> incidentTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.incident_types,
                android.R.layout.simple_spinner_item
        );
        incidentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncidentType.setAdapter(incidentTypeAdapter);

        buttonAttachPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // TODO: Display the selected image if needed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendReport() {
        final String description = editTextDescription.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        final String vehicleType = spinnerVehicleType.getSelectedItem().toString();
        final String incidentType = spinnerIncidentType.getSelectedItem().toString();

        if (description.isEmpty() || phoneNumber.isEmpty() ) {
            Toast.makeText(this, "Tafadhali jaza taarifa zote kwa usahihi", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if either of the spinners is not selected
        if (vehicleType.isEmpty() || incidentType.isEmpty()) {
            Toast.makeText(this, "Tafadhari chagua aina ya chombo na tukio", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verify if the phone number is in the Tanzania format (+255 followed by 9 digits)
        String phoneNumberPattern = "^\\+255(75|76|71|73|78|68|69|65|62|61|67|74)\\d{7}$";
        if (!phoneNumber.matches(phoneNumberPattern)) {
            Toast.makeText(this, "Please enter a valid Tanzania phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (filePath != null) {
            // Code to handle image attachment
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            imageRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            Report report = new Report(description, phoneNumber, vehicleType, incidentType, imageUrl);
                            String reportId = databaseReference.push().getKey();
                            databaseReference.child(reportId).setValue(report);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReportActivity.this, "Asante kwa ushirikiano, taarifa zako zimepokelewa na zinafanyiwa kazi na jeshi la polisi", Toast.LENGTH_LONG).show();
                                }
                            },10000);
                            finish();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ReportActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Code to handle report without image attachment
            Report report = new Report(description, phoneNumber, vehicleType, incidentType, null);
            String reportId = databaseReference.push().getKey();
            databaseReference.child(reportId).setValue(report);
            Toast.makeText(ReportActivity.this, "Asante kwa ushirikiano, taarifa zako zimepokelewa na zinafanyiwa kazi na jeshi la polisi", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
