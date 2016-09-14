package com.erginus.lifedonor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.erginus.lifedonor.Common.ConnectionDetector;
import com.erginus.lifedonor.Common.MapAppConstant;

import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2500;
    Prefshelper prefshelper;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    String gender,emailId, userSecurityHash,user_id,user_name, user_status,id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        prefshelper=new Prefshelper(this);
        Log.e("iam going to ", "");
        internet();

    }

    public void internet()
    {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {


            new Handler().postDelayed(new Runnable() {


                @Override
                public void run() {

                 final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            buildAlertMessageNoGps();
                        }
                        else{
                            if (!((prefshelper.getUserId()).equalsIgnoreCase("")) && !((prefshelper.getUserSecurityHash()).equalsIgnoreCase(""))) {
                                 sessionLogin();

                            }
                            else {
                                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                    }
                }
            }, SPLASH_TIME_OUT);

        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(SplashActivity.this, "No Internet Connection",
                    "Please Check Internet Connection.", false);
        }

    }
    private void buildAlertMessageNoGps() {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it? for Better Location.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        onStop();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                });

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Intent intent =new Intent(SplashActivity.this, SplashActivity.class);
        finish();
        startActivity(intent);
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    public void sessionLogin()
    {

        try {
            final ProgressDialog pDialog = new ProgressDialog(SplashActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"session_login" , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {



                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");

                                    userSecurityHash=object1.getString("user_security_hash");
                                    user_id= object1.getString("user_id");
                                    emailId=object1.getString("user_email");
                                    user_status=object1.getString("user_status");
                                    user_name=object1.getString("user_name");
                                    String contact=object1.getString("user_contact");
                                    String age=object1.getString("user_dob");
                                    String gender=object1.getString("user_gender");
                                    String bloodGroup=object1.getString("blood_group_name");
                                    String address=object1.getString("user_address");
                                    String isDonor=object1.getString("user_is_donor");
                                    prefshelper.setUserId(user_id);
                                    prefshelper.setUserSecurityHash(userSecurityHash);
                                    prefshelper.storeAddress(address);
                                    prefshelper.storeDOB(age);
                                    prefshelper.storeBloodGroup(bloodGroup);
                                    prefshelper.storeContact(contact);
                                    prefshelper.setGender(gender);
                                    prefshelper.isDonor(isDonor);
                                }

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    ;
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(SplashActivity.this, "Timeout Error",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ServerError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof NetworkError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    } else if (error instanceof ParseError) {
                        VolleyLog.d("", "" + error.getMessage() + "," + error.toString());
                    }
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", prefshelper.getUserId());
                    params.put("user_security_hash", prefshelper.getUserSecurityHash());
                    params.put("user_latitude", prefshelper.getLatitude());
                    params.put("user_longitude", prefshelper.getLongitude());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(SplashActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }
}
