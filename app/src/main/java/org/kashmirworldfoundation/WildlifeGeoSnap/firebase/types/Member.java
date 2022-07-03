package org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types;

import android.app.Activity;

import com.google.gson.Gson;

import org.kashmirworldfoundation.WildlifeGeoSnap.utils.SharedPreferenceUtil;

public class Member {
    private String Email;
    private String Fullname;
    private String Job;
    private String Phone;
    private Boolean Admin;
    private String Org;
    private String Profile;

    public Member(){
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public String getOrg() {
        return Org;
    }

    public void setOrg(String org) {
        Org = org;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public void savePreference(String uid, Activity activity){
        SharedPreferenceUtil userPreferences = new SharedPreferenceUtil("user", activity);
        userPreferences.clearAll();
        Gson gson = new Gson();
        String json =gson.toJson(this);
        userPreferences.add("user", json);
        userPreferences.add("uid", json);
        userPreferences.write();
    }
}
