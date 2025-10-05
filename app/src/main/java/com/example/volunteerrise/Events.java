package com.example.volunteerrise;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Events extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events); // Ensure the layout exists

        LinearLayout event1 = findViewById(R.id.event1);
        event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Events.this, Event1.class);
                startActivity(intent);
            }
        });

    }
}
