package com.example.volunteerrise;

import android.os.Bundle;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Event1_certificate extends AppCompatActivity {

    private TextView userNameTextView, uidTextView;
    private CircleImageView profileImageView;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event1_certificate);

        // Apply WindowInsetsListener
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        userNameTextView = findViewById(R.id.userNameTextView);
        uidTextView = findViewById(R.id.uidTextView);
        profileImageView = findViewById(R.id.profileImageView);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("user");  // Your users node in DB

        if (currentUser != null) {
            String uid = currentUser.getUid();
            uidTextView.setText("UID: " + uid);

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("userName").getValue(String.class);
                        String profilePicUrl = snapshot.child("profilepic").getValue(String.class);

                        if (name != null) {
                            userNameTextView.setText(name);
                        }

                        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                            Glide.with(Event1_certificate.this)
                                    .load(profilePicUrl)
                                    .placeholder(R.drawable.user_profile_img)  // your default profile pic
                                    .into(profileImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        }
    }
}
