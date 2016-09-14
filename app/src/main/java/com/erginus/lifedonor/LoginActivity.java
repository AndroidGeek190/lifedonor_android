package com.erginus.lifedonor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.GPSTracker;
import com.erginus.lifedonor.Common.Prefshelper;

import com.erginus.lifedonor.Common.VolleySingleton;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        LocationListener ,GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 9001;
    public static GoogleApiClient mGoogleApiClient;

    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    Shimmer shimmer;
    ShimmerTextView txt_shimmer;
    Prefshelper prefshelper;
    private ProfileTracker profileTracker;
    String gender,emailId, userSecurityHash,user_id,user_name,firstname,lastname,user_status,login_type,id;
    LinearLayout back,back2;
    int count=0;
    String stringLatitude,stringLongitude;
    Location locationAvailable;
    ProgressDialog progressDialog;
     GPSTracker gps;
    double latitude;
    double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        back=(LinearLayout)findViewById(R.id.imageView_back);
        back.setVisibility(View.GONE);
        back2=(LinearLayout)findViewById(R.id.imageView_back2);
        back2.setVisibility(View.GONE);
        TextView title=(TextView)findViewById(R.id.toolbar_title);
        title.setText("Login");


        txt_shimmer=(ShimmerTextView)findViewById(R.id.textView2);
        shimmer = new Shimmer();
        shimmer.start(txt_shimmer);
        shimmer.setDuration(2000);
        prefshelper=new Prefshelper(this);
        progressDialog=new ProgressDialog(LoginActivity.this);
              /* Getting location*/
      /*  MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                locationAvailable=location;
                count=1;
                System.out.println("Latitude: "+location.getLatitude());
                System.out.println("Longitude: " + location.getLongitude());

                stringLatitude = String.valueOf(location.getLatitude());
                stringLongitude = String.valueOf(location.getLongitude());
                prefshelper.storeLatitude(stringLatitude);
                prefshelper.storeLongitude(stringLongitude);
                Log.d(stringLatitude, stringLongitude);
             }

        };

        MyLocation.getLocation(LoginActivity.this, locationResult);*/
        gps = new GPSTracker(this);

        gps.canGetLocation();

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        stringLatitude = String.valueOf(latitude);
        stringLongitude = String.valueOf(longitude);
        prefshelper.storeLatitude(stringLatitude);
        prefshelper.storeLongitude(stringLongitude);
        Log.d("Gps latlong  "+ stringLatitude, stringLongitude);


       /* Gmail login*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        signInButton.setOnClickListener(this);

        /*facebook login button*/

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_about_me");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged( Profile oldProfile,
                                                            Profile currentProfile) {
                    }};

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {// Application code

                                Log.e(" First LoginActivity", response.toString());
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                   /* if((locationAvailable!=null))
                                    {
                                        progressDialog.hide();
                                      */  try {
                                        emailId = object.optString("email");
                                        user_id = object.getString("id");
                                        user_name = object.getString("name");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Log.e("facebook email ", emailId + " " + user_name);
                                    String[] fullname = user_name.split(" ");
                                    firstname=fullname[0];
                                    lastname=fullname[1];
                                    Log.e("first name",firstname);
                                    Log.e("last name", lastname);

                                     login_type="facebook";
                                      Login();
                                   /* }
                                    else
                                    {
                                        progressDialog.setMessage("Getting Location...");
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();
                                    }*/
                                }

                            }

                        }

                );
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email, id");
                request.setParameters(parameters);
                request.executeAsync();
                profileTracker.startTracking();
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("facebook - onError", exception.getMessage());
            }
        });
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                System.out.println("acesstoken trackercalled");
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.e("LoginActivity", response.toString());
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                         /*   if((locationAvailable!=null)) {
                                                progressDialog.hide();*/
                                                try {
                                                    final Profile profile = Profile.getCurrentProfile();
                                                    if (profile != null) {
                                                        Log.e("name", "" + profile.getName());
                                                        Log.e("id", "" + profile.getId());
                                                        Log.e("pic", "" + profile.getProfilePictureUri(400, 400));
                                                        emailId = object.optString("email");
                                                        id = object.getString("id");
                                                        user_name = object.getString("name");


                                                    } else {
                                                        emailId = object.optString("email");
                                                        id = object.getString("id");
                                                        user_name = object.getString("name");
                                                     }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.e("facebook email ", emailId + " " + user_name);
                                                String[] fullname = user_name.split(" ");
                                                firstname = fullname[0];
                                                lastname = fullname[1];
                                                Log.e("first name", firstname);
                                                Log.e("last name", lastname);

                                               login_type = "facebook";
                                                Login();
                                          /*  }
                                            else
                                            {
                                                progressDialog.setMessage("Getting Location...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                            }
*/
                                        }

                                    }

                                }

                        );
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "name, email, id");
                        request.setParameters(parameters);
                        request.executeAsync();

                        accessTokenTracker.startTracking();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {

                    }

                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.e("gmail request", "gmail");
            Log.e("data gmail",""+data.toString());
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            Log.e("data resultttttt", "" + result.toString());
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.e("handleSignInResult:", "" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                Log.e("Data",""+acct.toString());
                user_name=acct.getDisplayName();
                emailId=acct.getEmail();
                id=acct.getId();
             /*     if (acct.getPhotoUrl() != null) {
                    photo = acct.getPhotoUrl().toString();
                }*/
            }
           // prefshelper.setProfileImage(photo);

            Log.e("gmail", "" + user_name + " " + emailId + " ");
            login_type="google";
            Login();
        }
        else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("onConnectionFailed:", "" + connectionResult);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void Login()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "login");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"login", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                       // Toast.makeText(LoginActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
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

                                    String isDonor=object1.getString("user_is_donor");
                                    String lat=object1.getString("user_latitude");
                                    String longi=object1.getString("user_longitude");
                                    String eligible=object1.getString("user_donor_qualified");
                                    String profileImg=object1.getString("user_profile_image_url");
                                    prefshelper.setUserId(user_id);
                                    prefshelper.setUserSecurityHash(userSecurityHash);

                                    prefshelper.storeDOB(age);
                                    prefshelper.storeBloodGroup(bloodGroup);
                                    prefshelper.storeContact(contact);
                                    prefshelper.setGender(gender);
                                    prefshelper.isDonor(isDonor);
                                    prefshelper.storeEligiblity(eligible);
                                    prefshelper.setProfileImage(profileImg);
                                    prefshelper.setName(user_name);
                                    prefshelper.setEmail(emailId);
                                }

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

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
                        Toast.makeText(LoginActivity.this, "Timeout Error",
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

                    params.put("social_media_platform", login_type);
                    params.put("user_name", user_name);
                    params.put("user_email", emailId);
                    params.put("social_media_id", id);
                    params.put("user_latitude",prefshelper.getLatitude());
                    params.put("user_longitude",prefshelper.getLongitude());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(LoginActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

   /* public void getLocation(Context context) {
        // when you need location
        // if inside activity context = this;

        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                        Log.d("Location", "my location is " + location.toString());
                        locationAvailable=location;

                        System.out.println("Latitude: "+location.latitude);
                        System.out.println("Longitude: " + location.longitude);

                        stringLatitude = String.valueOf(location.latitude);
                        stringLongitude = String.valueOf(location.longitude);
                        Log.d(stringLatitude, stringLongitude);
                    }
                });
    }*/
}
