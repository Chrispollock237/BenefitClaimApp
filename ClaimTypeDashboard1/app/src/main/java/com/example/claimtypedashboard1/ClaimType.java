package com.example.claimtypedashboard1;

public class ClaimType {

    private String type;
    private String info;
    private final int imageResource;

    /**
     * Constructor for the Claim data model
     * @param type The name of the claim type.
     * @param info Information about the claim type.
     */
    ClaimType(String type, String info, int imageResource) {
        this.type = type;
        this.info = info;
        this.imageResource = imageResource;
    }

    /**
     * Gets the type of claim.
     *
     * @return The type of the claim.
     */
    String getType() {
        return type;
    }

    /**
     * Gets the info about the claim.
     *
     * @return The info about the claim.
     */
    String getInfo() {
        return info;
    }

    /**
     * Gets the picture related to the claim type.
     *
     * @return The  picture related to the claim type.
     */
    public int getImageResource() {
        return imageResource;
    }
}
