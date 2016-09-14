package com.erginus.lifedonor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import com.erginus.lifedonor.Adapter.BloodBankAdapter;
import com.erginus.lifedonor.Adapter.CustomExpandableListAdapter;
import com.erginus.lifedonor.Common.EndlessListView;
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.erginus.lifedonor.Model.BloodBankModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloodBanksActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CustomExpandableListAdapter bloodBankAdapter;
    Prefshelper prefshelper;
    List<BloodBankModel> bloodBankList;
    String bloodBankName, bloodBankContact, websiteLink,bloodBankAddress, bloodBankDesc;
    LinearLayout back;
    private SearchView mSearchView;
    int state_id_one;
    List<String> state_name,state_id,c_name;
    String aa="ch",citya;
    Spinner sp_state;
    ExpandableListView expListView;
    List<String> listDataHeader,list_index;
    HashMap<String, List<BloodBankModel>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_banks);
        state_name=new ArrayList<>();
        state_id=new ArrayList<>();
        c_name=new ArrayList<>();
        state_id.add("id");
        state_name.add("Select State For Blood Banks");

        listDataHeader = new ArrayList<String>();

        listDataChild = new HashMap<>();
        prefshelper=new Prefshelper(BloodBanksActivity.this);
//        listView=(EndlessListView)findViewById(R.id.listView);
        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        sp_state=(Spinner)findViewById(R.id.state1);
        sp_state.setOnItemSelectedListener(this);
//        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String st_name= stateName.get(position);
//                state_id_one= Integer.parseInt(stateId.get(position));
//                Log.e("name and id",""+st_name+" "+state_id_one);
//                if(bloodBankList!=null) {
//                    Log.e("clear data","clear");
//                    listDataChild.clear();
//                    listDataHeader.clear();
//                }
//
//                expListView.setVisibility(View.GONE);
//                donor_sign();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition)+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();

            }
        });
        back=(LinearLayout)findViewById(R.id.imageView_back);
        TextView title=(TextView)findViewById(R.id.toolbar_title);
//        mIndexerLayout = (LinearLayout) findViewById(R.id.indexer_layout);
        title.setText("Blood Banks List");
//        listView.setOnLoadMoreListener(new EndlessListView.OnLoadMoreListener() {
//            @Override
//            public boolean onLoadMore() {
//                if (mIsLoading) {
//                    loadMoreData();
//                } else {
//                    Toast.makeText(BloodBanksActivity.this, "No more data to load",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//                return mIsLoading;
//
//            }
//        });
//        mTitleLayout = (FrameLayout) findViewById(R.id.title_layout);
//        mTitleText = (TextView) findViewById(R.id.title_text);
//        mSectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);
//        mSectionToastText = (TextView) findViewById(R.id.section_toast_text);
//        for(int i = 0; i < alphabet.length(); i++) {
//            TextView letterTextView = new TextView(this);
//            letterTextView.setText(alphabet.charAt(i)+"");
//            letterTextView.setTextSize(14f);
//            letterTextView.setGravity(Gravity.CENTER);
//            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(28, 0, 1.0f);
//            letterTextView.setLayoutParams(params);
//            letterTextView.setPadding(4, 0, 2, 0);
//            mIndexerLayout.addView(letterTextView);
//            mIndexerLayout.setBackgroundResource(R.drawable.letterslist_bg);
//        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(BloodBanksActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
//        donor_sign(page);
        donor_state_list();

    }
    public List<BloodBankModel> getlist() {
        return bloodBankList;
    }
    public void setlist(List<BloodBankModel> d_list) {
        this.bloodBankList = d_list;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(BloodBanksActivity.this, HomeActivity.class);
        startActivity(intent);
    }

//    private void loadMoreData() {
//
//        page++;
//        donor_sign(page);
//    }

    public void donor_sign()
    {
        c_name.clear();
//        list_index.clear();
        list_index=new ArrayList<>();
        try {
            final ProgressDialog pDialog = new ProgressDialog(BloodBanksActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            Log.e("", "Strings" + MapAppConstant.API + "blood_banks");
            StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"blood_banks", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.dismiss();
                    Log.e("response",""+response);
                    try {
                        expListView.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        String serverMessage = object.getString("message");

                        if (serverCode.equalsIgnoreCase("0")) {
                            expListView.setFastScrollAlwaysVisible(false);
                            Toast.makeText(BloodBanksActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
                            bloodBankAdapter.notifyDataSetChanged();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            expListView.setFastScrollAlwaysVisible(true);
                            try {
                                if ("1".equals(serverCode)) {
                                    JSONArray arr1 = object.getJSONArray("data");
                                    for(int i=0;i<arr1.length();i++)
                                    {
                                        JSONObject jsonObject=arr1.getJSONObject(i);
                                        bloodBankName =jsonObject.getString("blood_bank_name");
                                        bloodBankContact =jsonObject.getString("blood_bank_contact");
                                        bloodBankAddress=jsonObject.getString("blood_bank_address");
                                        bloodBankDesc =jsonObject.getString("blood_bank_description");
                                        websiteLink =jsonObject.getString("blood_bank_website");
                                        String city_n =jsonObject.getString("city_name");
                                        if(!aa.equalsIgnoreCase(city_n)) {
                                            listDataHeader.add(city_n);
                                            String ch = String.valueOf(city_n.charAt(1));
                                            list_index.add(ch);
                                            citya=city_n;
                                            aa=city_n;
                                            Log.e("not match","not");
                                            bloodBankList=new ArrayList<>();
                                            bloodBankList.add(model(bloodBankName, bloodBankContact, bloodBankAddress, bloodBankDesc, websiteLink,city_n));
                                        }
                                        else
                                        {
                                            Log.e("yes match","yes");
                                            aa=city_n;
                                            bloodBankList.add(model(bloodBankName, bloodBankContact, bloodBankAddress, bloodBankDesc, websiteLink,city_n));
                                        }
                                        listDataChild.put(citya, bloodBankList); // Header, Child data
                                    }
                                    setlist(bloodBankList);
                                }

//                                if(expListView.getAdapter()==null){

                                    bloodBankAdapter =new CustomExpandableListAdapter(BloodBanksActivity.this, list_index,listDataHeader,listDataChild);
                                    expListView.setAdapter(bloodBankAdapter);
//                                }
//                                else {
//                                    bloodBankAdapter.notifyDataSetChanged();
//                                }


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
                        Toast.makeText(BloodBanksActivity.this, "Timeout Error",
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
//                    params.put("page",""+ page);
                    params.put("state_id",""+state_id_one);

//                    Log.e("page",""+page);
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(BloodBanksActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BloodBankModel model(String name, String cont,String add, String desc,String website,String name1)
    {
        BloodBankModel model=new BloodBankModel();
        model.setName(name);
        model.setAddress(add);
        model.setContact(cont);
        model.setDesc(desc);
        model.setWebsite(website);
        model.set_city_name(name1);
        return  model;
    }
    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        return true;
    }

    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_search:
                // search action
                // Associate searchable configuration with the SearchView
//            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
//                    .getActionView();
//            searchView.setSearchableInfo(searchManager
//                    .getSearchableInfo(getComponentName()));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupSearchView(MenuItem searchItem) {


    }



    public boolean onClose() {
        // mStatusView.setText("Closed!");
        Log.i("Action Search Query", "^%^%^%^%^");
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    public void donor_state_list()
    {
//        stateName.clear();
//        stateId.clear();
        try {
            final ProgressDialog pDialog = new ProgressDialog(BloodBanksActivity.this);
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

                            Toast.makeText(BloodBanksActivity.this, "" + serverMessage, Toast.LENGTH_SHORT).show();
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

                                        state_id.add(stat_id);
                                     state_name.add(stat_name);

                                    }

                                }


                                if(sp_state.getAdapter()==null){
                                    ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(BloodBanksActivity.this,android.R.layout.simple_spinner_dropdown_item,state_name){
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
                                            }
                                            else {
                                                tv.setTextColor(Color.BLACK);
                                            }
                                            return view;
                                        }
                                    };

                                    sp_state.setAdapter(stringArrayAdapter);
//                                    sp_state.setSelection(stringArrayAdapter.getCount());


                                }
                                else {
                                    bloodBankAdapter.notifyDataSetChanged();
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
                        Toast.makeText(BloodBanksActivity.this, "Timeout Error",
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
            VolleySingleton.getInstance(BloodBanksActivity.this.getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String st_name= state_name.get(position);
                if(position==0)
                {

                }
        else
                {
                    state_id_one= Integer.parseInt(state_id.get(position));
                    Log.e("name and id",""+st_name+" "+state_id_one);
                    if(bloodBankList!=null) {
                        Log.e("clear data","clear");
                        listDataChild.clear();
                        listDataHeader.clear();
                    }

                    expListView.setVisibility(View.GONE);
                    aa="ch";
                    donor_sign();
                }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
