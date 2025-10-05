package com.example.volunteerrise;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;  // To load images from URLs

import java.util.ArrayList;
import java.util.List;

public class Verification extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProofAdapter proofAdapter;
    private List<Proof> proofList = new ArrayList<>();
    private DatabaseReference proofRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        proofAdapter = new ProofAdapter(proofList);
        recyclerView.setAdapter(proofAdapter);

        // Firebase reference for proofs
        proofRef = FirebaseDatabase.getInstance().getReference("event1_proofs");

        loadProofsFromFirebase();
    }

    private void loadProofsFromFirebase() {
        proofRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                proofList.clear();
                for (DataSnapshot proofSnapshot : dataSnapshot.getChildren()) {
                    Proof proof = proofSnapshot.getValue(Proof.class);
                    proofList.add(proof);
                }
                proofAdapter.notifyDataSetChanged();  // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("VerificationActivity", "Firebase error: " + databaseError.getMessage());
            }
        });
    }
}

//
//
//public class Verification extends AppCompatActivity {
//
//    private ImageView profileImageView, proofImageView;
//    private TextView userNameTextView, uidTextView, statusTextView;
//    private Button verifyButton;
//
//    private DatabaseReference usersRef, proofsRef;
//    private String userId; // This would be the UID of the logged-in user
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_verification);
//
//        // Initialize views
//        profileImageView = findViewById(R.id.profileImageView);
//        proofImageView = findViewById(R.id.proofImageView);
//        userNameTextView = findViewById(R.id.userNameTextView);
//        uidTextView = findViewById(R.id.uidTextView);
//        statusTextView = findViewById(R.id.statusTextView);
//        verifyButton = findViewById(R.id.verifyButton);
//
//        // Get the userId (you can fetch this from Firebase Authentication if needed)
//        userId = "UID1"; // Replace with dynamic UID
//
//        // Initialize Firebase Database references
//        usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        proofsRef = FirebaseDatabase.getInstance().getReference("event1_proofs").child(userId);
//
//        // Fetch user data (name, profile picture)
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get user data
//                Users user = dataSnapshot.getValue(Users.class);
//
//                if (user != null) {
//                    userNameTextView.setText(user.getUserName());
//                    uidTextView.setText("UID: " + user.getUserId());
//                    Picasso.get().load(user.getProfilepic()).into(profileImageView);  // Load profile image using Picasso
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//
//        // Fetch proof data (proof image, verification status)
//        proofsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Assuming proof data is under a random filename node (filename_timestamp_random)
//                for (DataSnapshot proofSnapshot : dataSnapshot.getChildren()) {
//                    Proof proof = proofSnapshot.getValue(Proof.class);
//
//                    if (proof != null) {
//                        // Set proof image and status
//                        Picasso.get().load(proof.getProofImageUrl()).into(proofImageView);  // Load proof image using Picasso
//                        if (proof.isVerified()) {
//                            statusTextView.setText("Verification Status: Verified");
//                            statusTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
//                        } else {
//                            statusTextView.setText("Verification Status: Pending");
//                            statusTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//
//        // Button to verify the proof (update verification status in Firebase)
//        verifyButton.setOnClickListener(v -> {
//            proofsRef.child("filename_timestamp_random").child("verified").setValue(true);  // Set verified status to true
//        });
//    }
//}
