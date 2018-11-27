package com.example.abhiraj.offersky.model;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.io.Serializable;

/**
 * Created by Abhiraj on 24-04-2017.
 */

public class Coupon implements Serializable, SortedListAdapter.ViewModel{

    private String couponId;
    private String brand;
    private String couponImageURL;
    private String description;
    private String code;
    private int validity;
    private String tnc;
    private int price;
    private String brandImageURL;


    public Coupon() {
        super();
    }


    public String getCouponId() {
        return couponId;
    }


    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }


    public String getBrand() {
        return brand;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }


    public String getCouponImageURL() {
        return couponImageURL;
    }


    public void setCouponImageURL(String couponImageURL) {
        this.couponImageURL = couponImageURL;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public int getValidity() {
        return validity;
    }


    public void setValidity(int validity) {
        this.validity = validity;
    }


    public String getTnc() {
        return tnc;
    }


    public void setTnc(String tnc) {
        this.tnc = tnc;
    }


    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }


    public String getBrandImageURL() {
        return brandImageURL;
    }


    public void setBrandImageURL(String brandImageURL) {
        this.brandImageURL = brandImageURL;
    }


    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();
        sb.append(couponId + "  " + brand);
        return sb.toString();
    }
}