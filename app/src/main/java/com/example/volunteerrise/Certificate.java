package com.example.volunteerrise;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Certificate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certificate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        LinearLayout btnEvent1 = findViewById(R.id.clkevents1_certificate);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        btnEvent1.setOnClickListener(v -> {
            DatabaseReference event1Ref = FirebaseDatabase.getInstance()
                    .getReference("event1_proofs")
                    .child(uid);

            event1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        boolean isVerified = false;
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Boolean verified = child.child("verified").getValue(Boolean.class);
                            if (verified != null && verified) {
                                isVerified = true;
                                break;
                            }
                        }

                        if (isVerified) {
                            Intent intent = new Intent(Certificate.this, Event1_certificate.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Certificate.this, "Your proof is not yet verified.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Certificate.this, "You haven't uploaded proof yet.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Certificate.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            });
        });







        //bottom bar click listeners
        ImageView home_icon = findViewById(R.id.home_icon);
        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Certificate.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView profile_icon = findViewById(R.id.profile_icon);
        profile_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Certificate.this, Profile.class);
                startActivity(intent);
            }
        });







    }
}