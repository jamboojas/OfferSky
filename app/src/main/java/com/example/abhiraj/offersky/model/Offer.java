package com.example.abhiraj.offersky.model;

/**
 * Created by Abhiraj on 17-04-2017.
 */

public class Offer {

    String offerId;
    String description;
    String validity;
    String tnc;
    String offerImageURL;

    public Offer() {
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public String getOfferImageURL() {
        return offerImageURL;
    }

    public void setOfferImageURL(String offerImageURL) {
        this.offerImageURL = offerImageURL;
    }
}