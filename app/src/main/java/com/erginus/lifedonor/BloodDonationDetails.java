/*
package com.erginus.lifedonor;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BloodDonationDetails extends AppCompatActivity {
    private String[] arrayQuantity ={"1 Bottle","2 Bottles", "3 Bottles"};
    private String[] arrayPupose ={"Individual","Blood Bank"};
    Spinner sprQuantity,sprPurpose;
    ImageView imgBack;
    TextView txt_submit;
    EditText edtDate,edtCity,edtVenue;
    String date, city, venue, quantity;
    int certificateValue, quantity_id,purposeValue;
    Prefshelper prefshelper;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Calendar calendar;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter, sdf2;
    String date1, date2;
    Date dateNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donation_details);
        prefshelper=new Prefshelper(BloodDonationDetails.this);
        imgBack=(ImageView)findViewById(R.id.imageView_back);
        sprQuantity = (Spinner) findViewById(R.id.spinner);
        sprPurpose = (Spinner) findViewById(R.id.spinner_purpose);
        edtDate=(EditText)findViewById(R.id.editText_name);
        edtCity=(EditText)findViewById(R.id.editText_contact);
        edtVenue=(EditText)findViewById(R.id.editText_address);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        sdf2  =new SimpleDateFormat("yyyy-MM-dd",Locale.US);

        calendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date1=dateFormatter.format(newDate.getTime());
                edtDate.setText(date1);
                try {
                    dateNew = dateFormatter.parse(date1);
                    date2=sdf2.format(dateNew);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayQuantity);
        sprQuantity.setAdapter(adapter);
        ArrayAdapter<String> adapterP = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arrayPupose);
        sprPurpose.setAdapter(adapterP);

        sprQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("quantity", "" + position);
                quantity_id = position + 1;
                Log.e("quantity", "" + quantity_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sprPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("quantity", "" + position);
                purposeValue = position + 1;
                Log.e("quantity", "" + quantity_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BloodDonationDetails.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        txt_submit=(TextView)findViewById(R.id.button_submit);

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent=new Intent(BloodDonationDetails.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
                boolean cancelSignup = false;
                View focusView = null;

                date = edtDate.getText().toString();
                city = edtCity.getText().toString();
                venue = edtVenue.getText().toString();

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(selectedId);
                quantity= String.valueOf(radioButton.getText());
                Log.e("gender",""+quantity);
                if(quantity.equalsIgnoreCase("Yes"))
                {
                    certificateValue =1;
                }else
                {
                    certificateValue =0;
                }

                if (TextUtils.isEmpty(date)) {
                    edtDate.setError("Date is required");
                    focusView = edtDate;
                    cancelSignup = true;
                } else if (TextUtils.isEmpty(city)) {
                    edtCity.setError("City is required");
                    focusView = edtCity;
                    cancelSignup = true;
                } else if (TextUtils.isEmpty(venue)) {
                    edtVenue.setError("Venue is required");
                    focusView = edtVenue;
                    cancelSignup = true;
                }

//                else if (TextUtils.isEmpty(gender)) {
//                    Toast.makeText(getActivity(), "Select Gender", Toast.LENGTH_SHORT).show();
//                    cancelSignup = true;
//                }

                else if (cancelSignup) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    bloodDonationDetail();
                }
            }
        });
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(BloodDonationDetails.this,HomeActivity.class);
        startActivity(intent);
    }

    public void bloodDonationDetail()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(BloodDonationDetails.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "feedback");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"feedback", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        Toast.makeText(BloodDonationDetails.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            finish();
                            Intent intent=new Intent(BloodDonationDetails.this, HomeActivity.class);
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
                        Toast.makeText(BloodDonationDetails.this, "Timeout Error",
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
                    params.put("feedback_date", date2);
                    params.put("feedback_city", city);
                    params.put("feedback_venue", venue);
                    params.put("feedback_get_certificate",""+certificateValue );
                    params.put("feedback_blood_quantity",""+ quantity_id);
                    params.put("feedback_purpose",""+ purposeValue);

                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BloodDonationDetails.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
*/
