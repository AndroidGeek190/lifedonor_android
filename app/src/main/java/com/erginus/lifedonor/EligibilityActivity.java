package com.erginus.lifedonor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.erginus.lifedonor.Adapter.CheckListAdapter;
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.erginus.lifedonor.Model.EligibilityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EligibilityActivity extends AppCompatActivity {
    ListView mychecklist;
    CheckListAdapter custom_adapter;
    static Boolean state=false;
    Button checkbutton,never_donate;
    ImageView imgBack;
    Prefshelper prefshelper;
    List<EligibilityModel> list;
    JSONObject jsonObj = null;
    String strEligible;

    public EligibilityActivity()
    {
    }

    public EligibilityActivity(Boolean state)
    {
        Log.e("elgible cons excuted","yes");
        this.state=state;
        Log.e("state of boolen in cons",""+this.state);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefshelper=new Prefshelper(EligibilityActivity.this);
        setContentView(R.layout.activity_eligblity);

        list=new ArrayList<>();
        strEligible=getIntent().getStringExtra("not_eligible");
        mychecklist=(ListView)findViewById(R.id.eligible_id);
        checkbutton=(Button)findViewById(R.id.check_button);
        never_donate=(Button)findViewById(R.id.never_donate);

        check_user();


        checkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder result = new StringBuilder();
                result.append("{");
                if(custom_adapter.mCheckStates.size()>0)
                {
                    for (int i = 0; i < custom_adapter.mCheckStates.size(); i++) {

                    if (custom_adapter.mCheckStates.get(i)) {
                        result.append("\"");
                        result.append(list.get(i).getId());
                        result.append("\":");
                        result.append("\"1\"");
                        result.append(",");
                    }
                    if (!(custom_adapter.mCheckStates.get(i))) {

                        result.append("\"");
                        result.append(list.get(i).getId());
                        result.append("\":");
                        result.append("\"0\"");
                        result.append(",");
                    }

                  }
                }
                else  if(custom_adapter.mCheckStates.size()==0)
                {
                    for (int j = 0; j < (list.size() - custom_adapter.mCheckStates.size()); j++) {
                        result.append("\"");
                        result.append(list.get(j).getId());
                        result.append("\":");
                        result.append("\"0\"");
                        result.append(",");
                    }
                }
                result.append("}");
                try {
                    result.deleteCharAt(result.length()-2);
                    String resultString=String.valueOf(result);

                    jsonObj = new JSONObject(resultString);
                    Log.d("jsonnnnnnnnnnnnnnn", jsonObj +"");
                    answers();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        never_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aaa = "1. Had HIV" + "\n2. Had hepatitis C" + " \n3. Had syphilis" + "\n4. Had human t-lymphotropic virus (HTLV)" + "\n5. Injected yourself with drugs" + "\n6. If you have a serious heart condition";
                message(aaa);
            }
        });
        imgBack = (ImageView) findViewById(R.id.imageView_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(EligibilityActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(EligibilityActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    public void message(String msg) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customTitleView = inflater.inflate(R.layout.message, null);
        TextView msgtitle=(TextView)customTitleView.findViewById(R.id.msgtitle);
        //msgtitle.setText("You Should Never Donate If You Have Ever");
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
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void check_user()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(EligibilityActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "questions");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"questions", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray arr = object.getJSONArray("data");
                                    for(int i=0;i<arr.length();i++) {
                                        JSONObject obj = arr.getJSONObject(i);
                                        String quesId=obj.getString("question_id");
                                        String questionNm=obj.getString("question_name");

                                        list.add(eligibilityModel(quesId, questionNm));

                                    }
                                }

                                if(mychecklist.getAdapter()==null){
                                    custom_adapter=new CheckListAdapter(EligibilityActivity.this,list);
                                    mychecklist.setAdapter(custom_adapter);

                                }
                                else {
                                    custom_adapter.notifyDataSetChanged();

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
                        Toast.makeText(EligibilityActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(EligibilityActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    private EligibilityModel eligibilityModel(String id, String name)
    {
        EligibilityModel model=new EligibilityModel();
        model.setId(id);
        model.setName(name);
        return  model;
    }

    public void answers()
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(EligibilityActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "Strings" + MapAppConstant.API + "answers");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"answers", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONObject jsonObject = object.getJSONObject("data");
                                    String eligible=jsonObject.getString("user_donor_qualified");
                                    prefshelper.storeEligiblity(eligible);
                                    Log.e("eligiblity", eligible);

                                }
                            }

                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(strEligible.equals("0"))
                            {
                                if((prefshelper.getEligibilty()).equals("0")) {

                                    Toast.makeText(EligibilityActivity.this, "Sorry,You Can't Donate Yet", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EligibilityActivity.this, BecomeADonorActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(EligibilityActivity.this, "CONGRATULATIONS. You are eligible to Donate Blood, Every donation can save 3 lives", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EligibilityActivity.this, BecomeADonorActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            else if(strEligible.equals("1")) {
                                if (prefshelper.getEligibilty().equals("1")) {
                                    Toast.makeText(EligibilityActivity.this, "CONGRATULATIONS. You are eligible to Donate Blood, Every donation can save 3 lives", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EligibilityActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (prefshelper.getEligibilty().equals("0")) {
                                    Toast.makeText(EligibilityActivity.this, "Sorry,You Can't Donate Yet", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(EligibilityActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
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
                        Toast.makeText(EligibilityActivity.this, "Timeout Error",
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
                    params.put("answer_details_array", ""+jsonObj);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(EligibilityActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}

