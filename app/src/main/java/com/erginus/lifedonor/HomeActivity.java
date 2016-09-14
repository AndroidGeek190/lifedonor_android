package com.erginus.lifedonor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.androidpagecontrol.PageControl;
import com.erginus.lifedonor.Adapter.SamplePagerAdapter;
import com.erginus.lifedonor.Common.CircularImageView;
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.erginus.lifedonor.Fragments.AboutUS;
import com.erginus.lifedonor.Fragments.Compatibility;
import com.erginus.lifedonor.Fragments.Donation_History;
import com.erginus.lifedonor.Fragments.EditProfileFragment;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    public  static TextView txt_title,text_name, email_name;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout linearLayout, llBecomeADonor,llRequestADonor,llSearchADonor,llDonorProfile, llIAmDonor,llNotEligibleDonor;
    private CharSequence mDrawerTitle;
    FrameLayout f;
    ActionBarDrawerToggle mDrawerToggle;
    Prefshelper prefshelper;
    String pic,name,email;
    List<Integer> mResources;
    public static CircularImageView pimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(HomeActivity.this);
        setContentView(R.layout.activity_home);
        mResources=new ArrayList<Integer>();
        mResources.add(R.drawable.first_pic);
        mResources.add(R.drawable.first_pic1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txt_title=(TextView)toolbar.findViewById(R.id.toolbar_title);
        txt_title.setText("Home");
        LinearLayout back=(LinearLayout)findViewById(R.id.imageView_back);
        back.setVisibility(View.GONE);
        prefshelper=new Prefshelper(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation);
        linearLayout=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        navigationView.addHeaderView(linearLayout);
        pimage=(CircularImageView)linearLayout.findViewById(R.id.profile_img);
        text_name=(TextView)linearLayout.findViewById(R.id.txt_usrName);
        email_name=(TextView)linearLayout.findViewById(R.id.txt_userEmail);

        pic=prefshelper.getProfileImage();
        name=prefshelper.getName();
        email=prefshelper.getEmail();

        text_name.setText(name);
        email_name.setText(email);

        if(pic.equals(""))
        {
            pimage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }
        else {
            Picasso.with(HomeActivity.this).load(pic).into(pimage);
        }
        if(!(prefshelper.getLatitude().equalsIgnoreCase("")) && !(prefshelper.getLatitude().equalsIgnoreCase("")))
        {
            getLocation();
        }

        f=(FrameLayout)findViewById(R.id.content_frame);
        llBecomeADonor=(LinearLayout)findViewById(R.id.ll_becomeDonor);
        llSearchADonor=(LinearLayout)findViewById(R.id.ll_searchDonor);
        llRequestADonor=(LinearLayout)findViewById(R.id.ll_requestDonor);
        llDonorProfile=(LinearLayout)findViewById(R.id.ll_profileDonor);
        llIAmDonor =(LinearLayout)findViewById(R.id.ll_becameDonor);
        llNotEligibleDonor =(LinearLayout)findViewById(R.id.ll_eligibleDonor);
        Log.e("eligiblity",prefshelper.getEligibilty());
        if((prefshelper.getIsDonor().equals("1")) && (prefshelper.getEligibilty().equals("1")))
        {
            llBecomeADonor.setVisibility(View.GONE);
            llIAmDonor.setVisibility(View.VISIBLE);
            llNotEligibleDonor.setVisibility(View.GONE);
        }
        else  if((prefshelper.getIsDonor().equals("1")) && (prefshelper.getEligibilty().equals("0")))
        {
            llBecomeADonor.setVisibility(View.GONE);
            llNotEligibleDonor.setVisibility(View.VISIBLE);
            llIAmDonor.setVisibility(View.GONE);
        }
        else
        {
            llBecomeADonor.setVisibility(View.VISIBLE);
            llIAmDonor.setVisibility(View.GONE);
            llNotEligibleDonor.setVisibility(View.GONE);
        }
        Log.d("gender", prefshelper.getGender());

        SamplePagerAdapter adapter = new SamplePagerAdapter(this,mResources);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        final PageControl pageControl = (PageControl) findViewById(R.id.page_control);
        pageControl.setViewPager(viewPager);
        pageControl.setPosition(0);
        if (drawerLayout != null) {
            drawerLayout.setDrawerShadow(R.drawable.list_back, GravityCompat.START);

            mDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout,
                    toolbar, R.string.drawer_open, R.string.drawer_close) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();

                }
            };
            drawerLayout.setDrawerListener(mDrawerToggle);

        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawer_home:
                        txt_title.setText("Home");
                        Intent intent=new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.drawer_edit:


                        if(prefshelper.getIsDonor().equals("1"))
                        {
                            txt_title.setText("Edit Profile");
                            android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                            android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, new EditProfileFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                        else
                        {
                            String a="Please First Become a Donor.";
                            message(a);

//                            Toast.makeText(HomeActivity.this, "First Become a donor", Toast.LENGTH_SHORT).show();

                        }

                        return true;

                  /*  case R.id.drawer_history:

                        txt_title.setText("Donation History");
                        android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                        fragmentTransaction1.replace(R.id.content_frame, new Donation_History());
                        fragmentTransaction1.addToBackStack(null);
                        fragmentTransaction1.commit();
                        return true;*/

                    case R.id.about_us:

                        txt_title.setText("About Us");
                        android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.content_frame, new AboutUS());
                        fragmentTransaction2.addToBackStack(null);
                        fragmentTransaction2.commit();
                        return true;
                    case R.id.drawer_compatible:

                        txt_title.setText("Compatibility");
                        android.support.v4.app.FragmentManager fragmentManager3 = getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                        fragmentTransaction3.replace(R.id.content_frame, new Compatibility());
                        fragmentTransaction3.addToBackStack(null);
                        fragmentTransaction3.commit();
                        return true;

                    case R.id.drawer_share:

                        shareapp();
                        return true;
                    case R.id.drawer_logout:

                        prefshelper.setUserId("");
                        prefshelper.setUserSecurityHash("");
                        LoginManager.getInstance().logOut();

                        finish();

                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        llBecomeADonor.setOnClickListener(this);
        llDonorProfile.setOnClickListener(this);
        llRequestADonor.setOnClickListener(this);
        llSearchADonor.setOnClickListener(this);
        llIAmDonor.setOnClickListener(this);
        llNotEligibleDonor.setOnClickListener(this);
    }
  /*  private void signOut() {
        if(LoginActivity.mGoogleApiClient!=null)
        {
            Auth.GoogleSignInApi.signOut(LoginActivity.mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
        }

    }*/
    public void shareapp(){
        String message = "https://play.google.com/store/apps/details?id=com.erginus.lifedonor";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Via"));
    }
    @Override

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.

        mDrawerToggle.syncState();

    }


    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        mDrawerToggle.onConfigurationChanged(newConfig);

    }
//    public void signOut() {
//        Auth.GoogleSignInApi.signOut(LoginActivity.mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Toast.makeText(HomeActivity.this, "Signout............", Toast.LENGTH_LONG).show();
//                    }
//                });
//    }



    @Override
    public void onClick(View v) {
        if(v==llBecomeADonor)
        {

            Intent intent=new Intent(HomeActivity.this, EligibilityActivity.class);
            intent.putExtra("not_eligible","0");
            startActivity(intent);
            finish();

        }
        if(v== llIAmDonor)
        {
          /*  Intent intent=new Intent(HomeActivity.this, BloodDonationDetails.class);
            startActivity(intent);
            finish();*/

        }
        if(v==llDonorProfile)
        {
            if(prefshelper.getIsDonor().equals("1"))
            {
                Intent intent = new Intent(HomeActivity.this, ProfileDonorActivity.class);
                startActivity(intent);
                finish();

            }
            else {
                String a="Please First Become a Donor";
                message(a);
//                Toast.makeText(HomeActivity.this, "First Become a donor", Toast.LENGTH_SHORT).show();
            }

        }
        if(v==llRequestADonor)
        {
            Intent intent=new Intent(HomeActivity.this, BloodBanksActivity.class);
            startActivity(intent);
            finish();
        }
        if(v==llSearchADonor)
        {
            Intent intent=new Intent(HomeActivity.this, BloodGroupSelectionActivity.class);
            startActivity(intent);
            finish();
        }
        if(v==llNotEligibleDonor)
        {

            Intent intent=new Intent(HomeActivity.this, EligibilityActivity.class);
            intent.putExtra("not_eligible", "1");
            startActivity(intent);
            finish();

        }
    }

    public void dialogpassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.back_layout);
        ImageView enter = (ImageView) dialog.findViewById(R.id.mark);

        Animation mLoadAnimation = AnimationUtils.loadAnimation(HomeActivity.this, android.R.anim.slide_in_left);
        mLoadAnimation.setDuration(1700);
        enter.startAnimation(mLoadAnimation);

        Button yes = (Button) dialog.findViewById(R.id.bt_yes);
        Button no = (Button) dialog.findViewById(R.id.bt_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);


            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

// Show the dialog
        dialog.show();
    }
    public void getLocation()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "get_location");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"get_location", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        // Toast.makeText(HomeActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {



                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");


                                }

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }


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
                        Toast.makeText(HomeActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(HomeActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void message(String msg) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customTitleView = inflater.inflate(R.layout.message, null);
        TextView msgtitle=(TextView)customTitleView.findViewById(R.id.msgtitle);
        msgtitle.setText("Alert!");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(customTitleView);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();


            }
        });
        builder.setMessage("" + msg);

        AlertDialog dialog = builder.show();
        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);

        dialog.show();
    }
    @Override
    public void onBackPressed() {

        dialogpassword();
    }
}
