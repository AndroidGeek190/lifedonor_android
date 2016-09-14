package com.erginus.lifedonor;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.erginus.lifedonor.Common.CircularImageView;
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BecomeADonorActivity extends AppCompatActivity {
    private String[] arrayBloodGropu={"Select Blood Group","A+","A-", "B+","B-", "AB+", "AB-", "O+","O-"};
    Spinner sprBloodGroups, sprState, sprCity;
    ImageView imgBack;
    TextView txt_submit, txtSelectCity;
    EditText name,number,edtDateOfBirth;
    String name1,number1,gender;
    int gender_value,blood_id,id;
    CircularImageView img;
    Prefshelper prefshelper;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    String contact, donorAge,donorGender,bloodGroup,donorAddress, user_name,lat,longi, date, dateSet, parsedDate;
    List<String> stateName, stateId, cityName, cityId;
    int year, month, day,state_id, city_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_adonor);
        prefshelper=new Prefshelper(BecomeADonorActivity.this);
        sprBloodGroups = (Spinner) findViewById(R.id.spinner);
        sprState = (Spinner) findViewById(R.id.spinner_state);
        sprCity = (Spinner) findViewById(R.id.spinner_city);
        img=(CircularImageView)findViewById(R.id.imageView_profile);
        txtSelectCity=(TextView)findViewById(R.id.textView_city);
        stateName =new ArrayList<>();
        stateId =new ArrayList<>();
        cityId =new ArrayList<>();
        cityName =new ArrayList<>();
        stateName.add("Select State");
        cityName.add("Select City");
        String pic=prefshelper.getProfileImage();
        if(pic.equals(""))
        {
            //img.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }
        else {
            Picasso.with(BecomeADonorActivity.this).load(pic).into(img);
        }

        name=(EditText)findViewById(R.id.editText_name);
        number=(EditText)findViewById(R.id.editText_contact);
        edtDateOfBirth=(EditText)findViewById(R.id.editText_date);
        name.setText(prefshelper.getName());
        radioSexGroup = (RadioGroup)findViewById(R.id.radio_group);
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicking", "nothing");
                new DatePickerDialog(BecomeADonorActivity.this, pickerListener, year, month,day).show();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
               R.layout.layout_spinner, arrayBloodGropu);
        sprBloodGroups.setAdapter(adapter);

        sprBloodGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", "" + position);
                if(position==0)
                {
                    Log.e("blood group", "" + blood_id);
                }
                else
                {
                    blood_id = position;
                    Log.e("blood group", "" + blood_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgBack=(ImageView)findViewById(R.id.imageView_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(BecomeADonorActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        txt_submit=(TextView)findViewById(R.id.button_submit);

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean cancelSignup = false;
                View focusView = null;

                name1 = name.getText().toString();
                number1 = number.getText().toString();
               // dateSet=edtDateOfBirth.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat display = new SimpleDateFormat("dd MMM yyyy") ;
                SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                Date initDate = null;
                try {
                    initDate = sdf.parse(dateSet);
                    String diaplayDate=display.format(dateSet);
                    prefshelper.storeDOB(diaplayDate);
                    parsedDate = output.format(initDate);
                    Log.e("parsed date", parsedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton)findViewById(selectedId);
                gender= String.valueOf(radioSexButton.getText());
                Log.e("gender",""+gender);
                if(gender.equalsIgnoreCase("Male"))
                {
                    gender_value=1;
                }else
                {
                    gender_value=2;
                }

                if (TextUtils.isEmpty(name1)) {
                    name.setError("Name is required");
                    focusView = name;
                    cancelSignup = true;
                }
                else if (TextUtils.isEmpty(number1)) {
                    number.setError("Contact Number is Required");
                    focusView = number;
                    cancelSignup = true;
                }


                else if (cancelSignup) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    donor_sign();                }
            }
        });
        stateList();
          sprState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    Log.e("name and id",""+stateName+" "+state_id);
                    state_id= Integer.parseInt(stateId.get(i));
                }
                else
                {
                    state_id= Integer.parseInt(stateId.get(i))-1;
                    Log.e("name and id",""+state_id);
                    txtSelectCity.setVisibility(View.GONE);
                    sprCity.setVisibility(View.VISIBLE);
                    cityList();
                  }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sprCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    Log.e("name and id",""+cityName+" "+city_id);
                    city_id= Integer.parseInt(cityId.get(i));
                }
                else
                {
                    city_id= Integer.parseInt(cityId.get(i))-1;
                    Log.e("name and id",""+city_id);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(BecomeADonorActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void donor_sign()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(BecomeADonorActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "signup");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"signup", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                      //  Toast.makeText(BecomeADonorActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {



                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    user_name=object1.getString("user_name");
                                    contact=object1.getString("user_contact");
                                    donorAge=object1.getString("user_age");
                                    donorGender=object1.getString("user_gender");
                                    bloodGroup=object1.getString("blood_group_name");
                                    String city=object1.getString("city_name");
                                    String state=object1.getString("state_name");
                                    if(city.equalsIgnoreCase("") || state.equalsIgnoreCase(""))
                                    {
                                        donorAddress="";
                                    }
                                    else if(city.equalsIgnoreCase("null") || state.equalsIgnoreCase("null"))
                                    {
                                        donorAddress="";
                                    }
                                    else
                                    {
                                        donorAddress=city+", "+state;

                                    }
                                    lat=object1.getString("user_latitude");
                                    longi=object1.getString("user_longitude");

                                }

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            prefshelper.isDonor("1");
                            prefshelper.setName(user_name);
                            prefshelper.storeAddress(donorAddress);
                            prefshelper.storeBloodGroup(bloodGroup);
                            prefshelper.storeContact(contact);
                            prefshelper.setGender(donorGender);
                            prefshelper.storeLatitude(lat);
                            prefshelper.storeLongitude(longi);

                            Intent intent=new Intent(BecomeADonorActivity.this, HomeActivity.class);
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
                        Toast.makeText(BecomeADonorActivity.this, "Timeout Error",
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
                    params.put("user_name", name1);
                    params.put("user_contact", number1);
                    params.put("state_id", state_id+"");
                    params.put("city_id", city_id+"");
                    params.put("user_dob",parsedDate);
                    params.put("blood_group_id",""+blood_id);
                    params.put("user_gender",""+gender_value);
                    params.put("user_notification",""+0);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BecomeADonorActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void stateList()
    {

        try {
            final ProgressDialog pDialog = new ProgressDialog(BecomeADonorActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.e("", "Strings" + MapAppConstant.API + "blood_banks");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"states", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {

                            Toast.makeText(BecomeADonorActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray arr1 = object.getJSONArray("data");
                                    for(int i=0;i<arr1.length();i++)
                                    {
                                        JSONObject jsonObject=arr1.getJSONObject(i);

                                        String stat_id =jsonObject.getString("state_id");
                                        String stat_name =jsonObject.getString("state_name");

                                        stateId.add(stat_id);
                                        stateName.add(stat_name);

                                    }

                                }


                                if(sprState.getAdapter()==null){
                                    ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(BecomeADonorActivity.this,R.layout.layout_spinner,stateName){
                                        @Override
                                        public boolean isEnabled(int position){
                                            if(position == 0)
                                            {
                                                // Disable the first item from Spinner
                                                // First item will be use for hint
                                                return false;
                                            }
                                            else
                                            {
                                                return true;
                                            }
                                        }
                                        @Override
                                        public View getDropDownView(int position, View convertView,ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;
                                            if(position == 0){
                                                // Set the hint text color gray
                                                tv.setTextColor(Color.GRAY);
                                                tv.setPadding(18,18,18,18);
                                            }
                                            else {
                                                tv.setTextColor(Color.BLACK);
                                                tv.setPadding(17,17,17,17);
                                            }
                                            return view;
                                        }
                                    };

                                    sprState.setAdapter(stringArrayAdapter);
//                                    sp_state.setSelection(stringArrayAdapter.getCount());


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
                        Toast.makeText(BecomeADonorActivity.this, "Timeout Error",
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
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BecomeADonorActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cityList()
    {

        try {
            final ProgressDialog pDialog = new ProgressDialog(BecomeADonorActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.e("", "Strings" + MapAppConstant.API + "cities");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"cities", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {

                            Toast.makeText(BecomeADonorActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                cityName.clear();
                                cityId.clear();
                                cityName.add("Select City");
                                if ("1".equals(serverCode)) {
                                    JSONArray arr1 = object.getJSONArray("data");
                                    for(int i=0;i<arr1.length();i++)
                                    {
                                        JSONObject jsonObject=arr1.getJSONObject(i);

                                        String city_id =jsonObject.getString("city_id");
                                        String city_name =jsonObject.getString("city_name");

                                        cityId.add(city_id);
                                        cityName.add(city_name);

                                    }

                                }


                                if(sprCity.getAdapter()==null){
                                    ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(BecomeADonorActivity.this,R.layout.layout_spinner,cityName){
                                        @Override
                                        public boolean isEnabled(int position){
                                            if(position == 0)
                                            {
                                                // Disable the first item from Spinner
                                                // First item will be use for hint
                                                return false;
                                            }
                                            else
                                            {
                                                return true;
                                            }
                                        }
                                        @Override
                                        public View getDropDownView(int position, View convertView,ViewGroup parent) {
                                            View view = super.getDropDownView(position, convertView, parent);
                                            TextView tv = (TextView) view;
                                            if(position == 0){
                                                // Set the hint text color gray
                                                tv.setTextColor(Color.GRAY);
                                                tv.setPadding(18,18,18,18);
                                            }
                                            else {
                                                tv.setTextColor(Color.BLACK);
                                                tv.setPadding(17,17,17,17);
                                            }
                                            return view;
                                        }
                                    };

                                    sprCity.setAdapter(stringArrayAdapter);
//                                    sp_state.setSelection(stringArrayAdapter.getCount());


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
                        Toast.makeText(BecomeADonorActivity.this, "Timeout Error",
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
                    params.put("state_id", state_id+"");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BecomeADonorActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            edtDateOfBirth.setFocusable(true);
            String monthName = "";
            year  = selectedYear;
            month = selectedMonth+1;
            day   = selectedDay;
            if(month==1)
            {
                monthName="January";
            }
            if(month==2)
            {
                monthName="February";
            }
            if(month==3)
            {
                monthName="March";
            }
            if(month==4)
            {
                monthName="April";
            }
            if(month==5)
            {
                monthName="May";
            }
            if(month==6)
            {
                monthName="June";
            }
            if(month==7)
            {
                monthName="July";
            }
            if(month==8)
            {
                monthName="August";
            }
            if(month==9)
            {
                monthName="September";
            }
            if(month==10)
            {
                monthName="October";
            }
            if(month==11)
            {
                monthName="November";
            }
            if(month==12)
            {
                monthName="December";
            }

            // Show selected date
           date= String.valueOf(new StringBuilder().append(day)
                    .append(" ").append(monthName).append(" ").append(year)
                    .append(" "));
            dateSet= String.valueOf(new StringBuilder().append(day)
                    .append("-").append(month).append("-").append(year)
                    .append(" "));
            edtDateOfBirth.setText(date);

        }
    };


}
