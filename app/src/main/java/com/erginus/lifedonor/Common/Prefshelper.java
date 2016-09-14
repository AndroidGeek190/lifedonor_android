package com.erginus.lifedonor.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by paramjeet on 29/9/15.
 */
public class Prefshelper {
    public static final String KEY_PREFS_USER_INFO = "user_info";
    private Context context;
    public static SharedPreferences preferences;

    public Prefshelper(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context getContext() {
        return context;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }


      public void setEmail(String email) {
        Editor edit = getPreferences().edit();
        edit.putString("user_email", email);
        edit.commit();

    }

    public String getEmail() {
        return getPreferences().getString("user_email", "null");
    }


    public void setProfileImage(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("user_profile_image_url", image);
        edit.commit();

    }

    public String getProfileImage() {
        return getPreferences().getString("user_profile_image_url", "");
    }

    public void setName(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("name", image);
        edit.commit();

    }

    public String getName() {
        return getPreferences().getString("name", "");
    }

    public void setUserId(String image) {
        Editor edit = getPreferences().edit();
        edit.putString("user_id", image);
        edit.commit();

    }

    public String getUserId() {
        return getPreferences().getString("user_id", "");
    }


    public void setUserSecurityHash(String hash) {
        Editor edit = getPreferences().edit();
        edit.putString("user_security_hash", hash);
        edit.commit();

    }

    public String getUserSecurityHash() {
        return getPreferences().getString("user_security_hash", "");
    }

    public void storeContact(String contact) {
        Editor edit = getPreferences().edit();
        edit.putString("user_contact", contact);
        edit.commit();

    }

    public String getContact() {
        return getPreferences().getString("user_contact", "");
    }

    public void storeAddress(String add) {
        Editor edit = getPreferences().edit();
        edit.putString("user_address", add);
        edit.commit();

    }

    public String getAddress() {
        return getPreferences().getString("user_address", "");
    }

    public void storeBloodGroup(String bg) {
        Editor edit = getPreferences().edit();
        edit.putString("blood_group", bg);
        edit.commit();

    }

    public String getBloodGroup() {
        return getPreferences().getString("blood_group", "");
    }

    public void storeDOB(String age) {
        Editor edit = getPreferences().edit();
        edit.putString("user_age", age);
        edit.commit();

    }

    public String getDOB() {
        return getPreferences().getString("user_age", "");
    }



    public void setGender(String gendr) {
        Editor edit = getPreferences().edit();
        edit.putString("user_gender", gendr);
        edit.commit();

    }

    public String getGender() {
        return getPreferences().getString("user_gender", "");
    }

    public void isDonor(String a) {
        Editor edit = getPreferences().edit();
        edit.putString("user_is_donor", a);
        edit.commit();

    }

    public String getIsDonor() {
        return getPreferences().getString("user_is_donor", "");
    }

    public void storeLatitude(String a) {
        Editor edit = getPreferences().edit();
        edit.putString("user_latitude", a);
        edit.commit();

    }

    public String getLatitude() {
        return getPreferences().getString("user_latitude", "");
    }


    public void storeLongitude(String a) {
        Editor edit = getPreferences().edit();
        edit.putString("user_longitude", a);
        edit.commit();

    }

    public String getLongitude() {
        return getPreferences().getString("user_longitude", "");
    }

    public void storeEligiblity(String a) {
        Editor edit = getPreferences().edit();
        edit.putString("user_donor_qualified", a);
        edit.commit();

    }

    public String getEligibilty() {
        return getPreferences().getString("user_donor_qualified", "");
    }



}
