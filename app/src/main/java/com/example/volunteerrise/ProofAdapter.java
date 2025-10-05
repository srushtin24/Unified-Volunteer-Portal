package com.example.volunteerrise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProofAdapter extends RecyclerView.Adapter<ProofAdapter.ProofViewHolder> {

    private List<Proof> proofList;
    private DatabaseReference proofRef; // Firebase reference

    public ProofAdapter(List<Proof> proofList) {
        this.proofList = proofList;
        this.proofRef = proofRef;
    }

    @NonNull
    @Override
    public ProofViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proof, parent, false);
        return new ProofViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProofViewHolder holder, int position) {
        Proof proof = proofList.get(position);
        holder.statusTextView.setText(proof.isVerified() ? "Verified" : "Pending");

        // Load the proof image using Picasso
        Picasso.get().load(proof.getProofImageUrl()).into(holder.proofImageView);

        // Handle the verification button click
        holder.verifyButton.setOnClickListener(v -> {
            // Mark proof as verified
            proof.setVerified(true);
            proofRef.child(proof.getProofImageUrl()).setValue(proof);  // Update Firebase database
            notifyItemChanged(position); // Refresh the item in the list
        });
    }

    @Override
    public int getItemCount() {
        return proofList.size();
    }

    public static class ProofViewHolder extends RecyclerView.ViewHolder {
        ImageView proofImageView;
        TextView statusTextView;
        Button verifyButton;

        public ProofViewHolder(View itemView) {
            super(itemView);
            proofImageView = itemView.findViewById(R.id.proofImageView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            verifyButton = itemView.findViewById(R.id.verifyButton);
        }
    }
}
