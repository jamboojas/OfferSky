package com.example.abhiraj.offersky.model;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Abhiraj on 14-04-2017.
 */

public class Shop implements SortedListAdapter.ViewModel{

    private String shopId;
    private String phone;
    private String email;
    private String location;
    private HashMap<String, Offer> mOffers;
    private HashMap<String, String> mCategories;
    private String name;
    boolean hasMenu;
    String gender;
    private String shopImageURL;
    private String brandImageURL;

    private List<String> shopTourImageURLs;
    // TODO: Add list of search tags


    public Shop(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Offer> getOffers() {
        return mOffers;
    }

    public void setOffers(HashMap<String, Offer> offers) {
        mOffers = offers;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HashMap<String, String> getCategories() {
        return mCategories;
    }

    public void setCategories(HashMap<String, String> categories) {
        mCategories = categories;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShopImageURL() {
        return shopImageURL;
    }

    public void setShopImageURL(String shopImageURL) {
        this.shopImageURL = shopImageURL;
    }

    public String getBrandImageURL() {
        return brandImageURL;
    }

    public void setBrandImageURL(String brandImageURL) {
        this.brandImageURL = brandImageURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setHasMenu(boolean hasMenu) {
        this.hasMenu = hasMenu;
    }

    public List<String> getShopTourImageURLs() {
        return shopTourImageURLs;
    }

    public void setShopTourImageURLs(List<String> shopTourImageURLs) {
        this.shopTourImageURLs = shopTourImageURLs;
    }
}