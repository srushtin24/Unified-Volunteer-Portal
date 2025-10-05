package com.example.volunteerrise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.volunteerrise.databinding.ActivityEvent1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Event1 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityEvent1Binding binding;





    private static final int IMAGE_PICK_CODE = 101;

    private ImageView eve1proofImg;
    private androidx.appcompat.widget.AppCompatButton selectProofBtn, uploadProofBtn;

    private Uri proofUri;
    String fileName = System.currentTimeMillis() + ".jpg";
    private FirebaseAuth auth;
    private DatabaseReference dbRef;
    private StorageReference storageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEvent1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);










        // Initialize UI elements
        eve1proofImg = findViewById(R.id.event1_imgproof);
        selectProofBtn = findViewById(R.id.selectproof1_button);
        uploadProofBtn = findViewById(R.id.uploadproof1_button);

        // Firebase
        auth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("event1_proofs");
        storageRef = FirebaseStorage.getInstance().getReference("event1_proofs/" + auth.getUid() + "/" + fileName);

        // Select image from gallery
        selectProofBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Proof Image"), IMAGE_PICK_CODE);
        });

        // Upload selected image
        uploadProofBtn.setOnClickListener(v -> {
            if (proofUri != null) {
                storageRef.putFile(proofUri).addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Map<String, Object> proofData = new HashMap<>();
                        proofData.put("proofImage", imageUrl);
                        proofData.put("verified", false);

                        dbRef.child(auth.getUid()).child(fileName.replace(".jpg", "")).setValue(proofData)
                                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Proof uploaded. Awaiting verification.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Failed to upload proof.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
            }
        });







    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            proofUri = data.getData();
            eve1proofImg.setImageURI(proofUri);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng event1 = new LatLng(19.00768285043494, 73.01944906694088);
        mMap.addMarker(new MarkerOptions().position(event1).title("Flamingo Point"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(event1));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(event1, 16f));
    }
}