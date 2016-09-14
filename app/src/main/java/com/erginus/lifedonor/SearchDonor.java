package com.erginus.lifedonor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.erginus.lifedonor.Adapter.DonorAdapter;
import com.erginus.lifedonor.Common.EndlessListView;
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.erginus.lifedonor.Model.DonorModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchDonor extends AppCompatActivity {
    EndlessListView listView;
    DonorAdapter donorAdapter;
    Prefshelper prefshelper;
    List<DonorModel> don_list;
    String u_name, u_contact, u_blood_group_name,name_b, distance;
    int page=0;
    private boolean mIsLoading=true;
    String bloodGroup;
    LinearLayout back;
    int blood_id;
    TextView txtBloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        LinearLayout ll=(LinearLayout)findViewById(R.id.llshow);
        ll.setVisibility(View.VISIBLE);
        prefshelper=new Prefshelper(this);
        back=(LinearLayout)findViewById(R.id.imageView_back);
        txtBloodGroup=(TextView) findViewById(R.id.text_blood_group);
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        Intent intent = new Intent(SearchDonor.this, BloodGroupSelectionActivity.class);
                                        startActivity(intent);
                                        BloodGroupSelectionActivity.reset(BloodGroupSelectionActivity.root);
                                        BloodGroupSelectionActivity.addListener(BloodGroupSelectionActivity.root);
                                        BloodGroupSelectionActivity.mExplosionField.clear();
                                    }
                                });
        TextView title=(TextView)findViewById(R.id.toolbar_title);
        title.setText("Donors List");
        don_list=new ArrayList<>();
        listView=(EndlessListView)findViewById(R.id.listView);
        bloodGroup=getIntent().getStringExtra("blood_group");
        listView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
            @Override
            public boolean onLoadMore() {
                if (mIsLoading) {
                    page++;
                    donorList(page);
                } else {

                    Toast.makeText(SearchDonor.this, "No more data to load",
                            Toast.LENGTH_SHORT).show();
                }

                return mIsLoading;

            }
        });
        if(bloodGroup.equalsIgnoreCase("A+"))
        {
            blood_id=1;
            txtBloodGroup.setText("A+");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("B+"))
        {
            blood_id=2;
            txtBloodGroup.setText("B+");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("AB+"))
        {
            blood_id=3;
            txtBloodGroup.setText("AB+");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("O+"))
        {
            blood_id=4;
            txtBloodGroup.setText("O+");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("A-"))
        {
            blood_id=5;
            txtBloodGroup.setText("A-");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("B-"))
        {
            blood_id=6;
            txtBloodGroup.setText("B-");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("AB-"))
        {
            blood_id=7;
            txtBloodGroup.setText("AB-");
            donorList(page);
        }
        if(bloodGroup.equalsIgnoreCase("O-"))
        {
            blood_id=8;
            txtBloodGroup.setText("O-");
            donorList(page);
        }


    }

    public List<DonorModel> getlist() {
        return don_list;
    }
    public void setlist(List<DonorModel> d_list) {
        this.don_list = d_list;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(SearchDonor.this, BloodGroupSelectionActivity.class);
        startActivity(intent);
        BloodGroupSelectionActivity.reset(BloodGroupSelectionActivity.root);
        BloodGroupSelectionActivity.addListener(BloodGroupSelectionActivity.root);
        BloodGroupSelectionActivity.mExplosionField.clear();
    }


    public void donorList(final int page)
    {
        try {
            final ProgressDialog pDialog = new ProgressDialog(SearchDonor.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.e("", "Strings" + MapAppConstant.API + "donors");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"donors", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {

                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {

                            Toast.makeText(SearchDonor.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            getlist().clear();
                            try {
                                if ("1".equals(serverCode)) {

                                    JSONArray arr1 = object.getJSONArray("data");
                                    for(int i=0;i<arr1.length();i++)
                                    {
                                        JSONObject jsonObject=arr1.getJSONObject(i);
                                        u_name=jsonObject.getString("user_name");
                                        u_contact=jsonObject.getString("user_contact");
                                        String city=jsonObject.getString("city_name");
                                        String state=jsonObject.getString("state_name");
                                        u_blood_group_name=jsonObject.getString("blood_group_name");
                                        distance=jsonObject.getString("distance_in_km");
                                        String image =jsonObject.getString("user_profile_image_url");
                                        String u_address;
                                        if(city.equalsIgnoreCase("") && state.equalsIgnoreCase(""))
                                        {
                                            u_address="NA";
                                        }
                                        else if(city.equalsIgnoreCase("null") && state.equalsIgnoreCase("null"))
                                        {
                                            u_address="NA";
                                        }
                                        else
                                        {
                                            u_address=city+", "+state;

                                        }
                                        don_list.add(model(u_name, u_contact, u_blood_group_name, u_address, distance, image));
                                    }
                                    setlist(don_list);
                                }
                                if(listView.getAdapter()==null){
                                    donorAdapter=new DonorAdapter(SearchDonor.this,don_list);
                                    listView.setAdapter(donorAdapter);
                                }
                                else {
                                    listView.loadMoreCompleat();
                                    donorAdapter.notifyDataSetChanged();

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
                        Toast.makeText(SearchDonor.this, "Timeout Error",
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
                    params.put("blood_group_id",blood_id+"");
                    params.put("page",""+ page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(SearchDonor.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DonorModel model(String name, String cont, String u_blood_group_name, String add, String distance, String image)
    {
        DonorModel model=new DonorModel();
        model.setName(name);
        model.setcontact(cont);
        model.setAddress(add);
        model.setblood_group(u_blood_group_name);
        model.setDistance(distance);
        model.setImage(image);
        return  model;
    }

}
