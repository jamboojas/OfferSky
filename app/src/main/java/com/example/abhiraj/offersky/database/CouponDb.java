package com.example.abhiraj.offersky.database;

/**
 * Created by Abhiraj on 19-06-2017.
 */

public class CouponDb {

    private String mallId;
    private String couponId;
    private String allotmentDate;
    private String allotmentTime;

    public CouponDb(){}

    public CouponDb(String mallId, String couponId, String allotmentDate, String allotmentTime) {
        this.mallId = mallId;
        this.couponId = couponId;
        this.allotmentDate = allotmentDate;
        this.allotmentTime = allotmentTime;
    }

    public String getMallId() {
        return mallId;
    }

    public void setMallId(String mallId) {
        this.mallId = mallId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getAllotmentDate() {
        return allotmentDate;
    }

    public void setAllotmentDate(String allotmentDate) {
        this.allotmentDate = allotmentDate;
    }

    public String getAllotmentTime() {
        return allotmentTime;
    }

    public void setAllotmentTime(String allotmentTime) {
        this.allotmentTime = allotmentTime;
    }
}

