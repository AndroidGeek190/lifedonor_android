/*
package com.erginus.lifedonor.Common;

import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


public class MyLocation {
    public static Timer timer1;
    public static LocationManager lm;
    public static LocationResult locationResult;
    public static boolean gps_enabled = false;
    public static boolean network_enabled = false;
    public static Prefshelper prefshelper;
    public static Long duration = Long.valueOf(18000000);
    public static String stringLatitude, stringLongitude;
    // public static Context context;

    public static boolean getLocation(Context context, LocationResult result) {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result;
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        prefshelper = new Prefshelper(context);

        // duration = Long.parseLong(prefshelper.getPollingFromPrefrence()) * 1000;

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false;

        if (gps_enabled)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return true;
            }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, duration, 0, locationListenerGps);
        if (network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, duration, 0, locationListenerNetwork);
        timer1 = new Timer();

        timer1.schedule(new GetLastLocation(), duration);

        return true;
    }

    public static LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();

            locationResult.gotLocation(location);
            stringLatitude = String.valueOf(location.getLatitude());
            stringLongitude = String.valueOf(location.getLongitude());
            //    allocate_res = "{\"command\": \"location\"," + "\"user_id\":\"" + prefshelper.getuseridFromPreference() + "\"," + "\"user_security_hash\":\"" + prefshelper.gethashcodeFromPreference() + "\"," + "\"latitude\":\"" + stringLatitude + "\"," + "\"longitude\":\"" + stringLongitude + "\"}";

              */
/*  new CountDownTimer(duration, 1000) {

                    public void onTick(long millisUntilFinished) {
                        Log.e("seconds remaining: ", "" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        if (prefshelper.getSlugFromPreference().equals("user")) {
                            HomeActivity.mWebSocketClient.send(allocate_res);
                        } else {
                            PoliceHomeActivity.mWebSocketClient.send(allocate_res);
                        }
                        Log.e("snsjjdkwjdk", allocate_res);
                    }
                }.start();*//*



          */
/*  lm.removeUpdates(this);
            lm.removeUpdates(locationListenerNetwork);*//*

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public static LocationListener locationListenerNetwork = new LocationListener() {

        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            stringLatitude = String.valueOf(location.getLatitude());
            stringLongitude = String.valueOf(location.getLongitude());
            //allocate_res = "{\"command\": \"location\"," + "\"user_id\":\"" + prefshelper.getuseridFromPreference() + "\"," + "\"user_security_hash\":\"" + prefshelper.gethashcodeFromPreference() + "\"," + "\"latitude\":\"" + stringLatitude + "\"," + "\"longitude\":\"" + stringLongitude + "\"}";

       */
/*     new CountDownTimer(duration, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.e("seconds remaining: ", "" + millisUntilFinished / 1000);
                }

                public void onFinish() {
              *//*
*/
/*      if (prefshelper.getSlugFromPreference().equals("user")) {
                        HomeActivity.mWebSocketClient.send(allocate_res);
                    } else if(prefshelper.getSlugFromPreference().equals("police")) {
                        PoliceHomeActivity.mWebSocketClient.send(allocate_res);
                    }*//*
*/
/*

                }
            }.start();*//*


        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public static class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            //lm.removeUpdates(locationListenerGps);
            //lm.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;
           gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if (network_enabled)
               net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

           //if there are both values use the latest one
           if (gps_loc != null && net_loc != null) {
               if (gps_loc.getTime() > net_loc.getTime())
                   locationResult.gotLocation(gps_loc);
               else
                   locationResult.gotLocation(net_loc);
               return;
           }

           if (gps_loc != null) {
               locationResult.gotLocation(gps_loc);
               return;
           }
           if (net_loc != null) {
               locationResult.gotLocation(net_loc);
               return;
           }
           locationResult.gotLocation(null);
       }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    public void stopLoaction(Context context) {
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.removeUpdates(locationListenerNetwork);
        lm.removeUpdates(locationListenerGps);
    }
 */
/*   public void setCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        provider = locationManager.getBestProvider(criteria, true);
    }*//*

}*/
