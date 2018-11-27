package com.example.abhiraj.offersky.model;

import java.util.HashMap;

/**
 * Created by Abhiraj on 17-04-2017.
 */

public class Mall {

    String mallId;
    String name;
    String phone;
    String email;
    String address;
    double latitude;
    double longitude;
    float radius;
    int m1;
    int m2;
    int m3;
    int m4;
    int m5;

    HashMap<String, Shop> shops;
    HashMap<String, Coupon> coupons;
    HashMap<String, Event> events;

    public String getMallId() {
        return mallId;
    }
    public void setMallId(String mallId) {
        this.mallId = mallId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public float getRadius() {
        return radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public int getM1() {
        return m1;
    }
    public void setM1(int m1) {
        this.m1 = m1;
    }
    public int getM2() {
        return m2;
    }
    public void setM2(int m2) {
        this.m2 = m2;
    }
    public int getM3() {
        return m3;
    }
    public void setM3(int m3) {
        this.m3 = m3;
    }
    public int getM4() {
        return m4;
    }
    public void setM4(int m4) {
        this.m4 = m4;
    }
    public int getM5() {
        return m5;
    }
    public void setM5(int m5) {
        this.m5 = m5;
    }
    public HashMap<String, Shop> getShops() {
        return shops;
    }
    public void setShops(HashMap<String, Shop> shops) {
        this.shops = shops;
    }
    public HashMap<String, Coupon> getCoupons() {
        return coupons;
    }
    public void setCoupons(HashMap<String, Coupon> coupons) {
        this.coupons = coupons;
    }
    public HashMap<String, Event> getEvents() {
        return events;
    }
    public void setEvents(HashMap<String, Event> events) {
        this.events = events;
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "MallId = " + mallId + " name = "+ name +
                " shops = \n");
        for(Shop shop : shops.values())
        {

            sb.append("shopid = " + shop.getShopId());
            sb.append(" shop name = " + shop.getName());
            sb.append("\n");

        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(obj instanceof Mall) {
            if (this.getMallId().equals(((Mall) obj).getMallId())){
                return true;
            }
        }
        return false;
    }

}
