package com.example.volunteerrise;

public class Proof {
    private String proofImageUrl;
    private boolean verified;

    // Default constructor for Firebase
    public Proof() {}

    // Constructor
    public Proof(String proofImageUrl, boolean verified) {
        this.proofImageUrl = proofImageUrl;
        this.verified = verified;
    }

    // Getters and setters
    public String getProofImageUrl() {
        return proofImageUrl;
    }

    public void setProofImageUrl(String proofImageUrl) {
        this.proofImageUrl = proofImageUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
