package com.erginus.lifedonor.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.erginus.lifedonor.Common.MapAppConstant;
import com.erginus.lifedonor.Common.MultipartRequest;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.Common.VolleySingleton;
import com.erginus.lifedonor.HomeActivity;
import com.erginus.lifedonor.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditProfileFragment extends Fragment {
    EditText edtName, edtContact,edtDateOfBirth;
    public static String name1,number1,address1,age1,gender,genderVal;
    int gender_value,blood_id;
    Spinner  sprState, sprCity;
    Prefshelper prefHelper;
    File f;
    TextView btn_submit, txtSelectCity;
    private static final int PHOTO_PICKER_ID = 1;
    String filename,date, dateSet, parsedDate;
    ProgressDialog pDialog;
    ImageView imageView_profile;
    LinearLayout llProfilePic;
    int id;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 2500;
    String  u_name,contact,userAge, userAdd,bloodGroupId,userGender;
   /* private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;*/
    View rootview;
    /*String bloodGroup;*/
    List<String> stateName, stateId, cityName, cityId;
    int year, month, day,state_id, city_id;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat display = new SimpleDateFormat("dd MMM yyyy") ;
    
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragmnet_edit_profile, container, false);
        HomeActivity.txt_title.setText("Edit Profile");
        prefHelper=new Prefshelper(getActivity());
       // bloodGroup=prefHelper.getBloodGroup();
        edtName =(EditText)rootview.findViewById(R.id.editText_name);
        sprState = (Spinner) rootview.findViewById(R.id.spinner_state);
        sprCity = (Spinner)rootview.findViewById(R.id.spinner_city);
      //  edtAddress =(EditText)rootview.findViewById(R.id.editText_address);
        edtContact =(EditText)rootview.findViewById(R.id.editText_contact);
        txtSelectCity=(TextView)rootview.findViewById(R.id.textView_city);

        stateName =new ArrayList<>();
        stateId =new ArrayList<>();
        cityId =new ArrayList<>();
        cityName =new ArrayList<>();
        stateName.add("Select State");
        cityName.add("Select City");
        edtDateOfBirth=(EditText)rootview.findViewById(R.id.editText_date);
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        edtDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("clicking", "nothing");
          //      new DatePickerDialog(getActivity(), pickerListener, year, month,day).show();
                DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), pickerListener, year, month, day);
              //  pickerDialog.getDatePicker().setMaxDate(maxDate);
             //   pickerDialog.getDatePicker().setMinDate(minDate);
                pickerDialog.show();
            }
        });

        imageView_profile = (ImageView)rootview.findViewById(R.id.imageView_profile);
        llProfilePic=(LinearLayout)rootview.findViewById(R.id.ll_profilePic);

       llProfilePic.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog();
           }
       });
        if(!(prefHelper.getProfileImage().equalsIgnoreCase("")))
        {
            Picasso.with(getActivity()).load(prefHelper.getProfileImage()).into(imageView_profile);
        }

        edtName.setText(prefHelper.getName());
        edtContact.setText(prefHelper.getContact());
        String dob=prefHelper.getDOB();
        try {
            Date dt=display.parse(dob);
            String displaydt=display.format(dt);
            edtDateOfBirth.setText(displaydt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        stateList();
        btn_submit=(TextView)rootview.findViewById(R.id.button_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancelSignup = false;
                View focusView = null;

                name1 = edtName.getText().toString();
                number1 = edtContact.getText().toString();
                date =edtDateOfBirth.getText().toString();
                Date initDate = null;
                try {
                    initDate = display.parse(date);
                    String diaplayDate=display.format(initDate);
                    edtDateOfBirth.setText(diaplayDate);
                    prefHelper.storeDOB(diaplayDate);
                    parsedDate = output.format(initDate);
                    Log.e("parsed date", parsedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (TextUtils.isEmpty(name1)) {
                    edtName.setError("Name is required");
                    focusView = edtName;
                    cancelSignup = true;
                } 
                else if (TextUtils.isEmpty(number1)) {
                    edtContact.setError("Contact Number is Required");
                    focusView = edtContact;
                    cancelSignup = true;
                }

                else if (cancelSignup) {
                    // error in login
                    focusView.requestFocus();
                } else {
                    profile_update();
                }
            }
        });
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
                return  rootview;
    }



    public void profile_update()
    {
            try {
                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show();

                Log.e("", "Strings" + MapAppConstant.API + "edit_profile");
                StringRequest sr = new StringRequest(Request.Method.POST,MapAppConstant.API+"edit_profile", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.e("response",""+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String serverCode = object.getString("code");
                            String serverMessage = object.getString("message");
                            Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                            if (serverCode.equalsIgnoreCase("0")) {

                            }
                            if (serverCode.equalsIgnoreCase("1")) {
                                try {



                                    if ("1".equals(serverCode)) {
                                        JSONObject object1 = object.getJSONObject("data");

                                        u_name=object1.getString("user_name");
                                        contact=object1.getString("user_contact");
                                        userAge=object1.getString("user_dob");
                                        userGender=object1.getString("user_gender");
                                        String city=object1.getString("city_name");
                                        String state=object1.getString("state_name");
                                        if(city.equalsIgnoreCase("") || state.equalsIgnoreCase(""))
                                        {
                                            userAdd="";
                                        }
                                        else if(city.equalsIgnoreCase("null") || state.equalsIgnoreCase("null"))
                                        {
                                            userAdd="";
                                        }
                                        else
                                        {
                                            userAdd=city+", "+state;
                                        }
                                        }
                                    prefHelper.setName(u_name);
                                    prefHelper.storeContact(contact);
                                    prefHelper.setGender(userGender);
                                    prefHelper.storeAddress(userAdd);


                                }

                                catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent intent=new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();

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
                            Toast.makeText(getActivity(), "Timeout Error",
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

                        params.put("user_id", prefHelper.getUserId());
                        params.put("user_security_hash", prefHelper.getUserSecurityHash());
                        params.put("user_name", name1);
                        params.put("user_contact", number1);
                        params.put("state_id", state_id+"");
                        params.put("city_id", city_id+"");
                        params.put("user_dob",parsedDate);

                        return params;
                    }
                };
                sr.setRetryPolicy(new DefaultRetryPolicy(100000 * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                sr.setShouldCache(true);
                VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


            } catch (Exception e) {
                e.printStackTrace();
            }


    }
    private boolean isValidPhone(String pass) {
        return pass != null && pass.length() == 10;
    }
    public void dialog()
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle("Upload From");
        dialog.setContentView(R.layout.dialog_pop_up_gallery_camera);

        dialog.setTitle("Select an Option...");
        TextView txt_gallry=(TextView)dialog.findViewById(R.id.textView_gallery);
        TextView txt_camera=(TextView)dialog.findViewById(R.id.textView_camera);

        txt_gallry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICKER_ID);
            }
        });
        txt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File fil = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fil));
                startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        });
        dialog.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_IMAGE:

                    if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK ) {

                        File fil = new File(Environment.getExternalStorageDirectory().toString());
                        for (File temp : fil.listFiles()) {
                            if (temp.getName().equals("temp.jpg")) {
                                fil = temp;
                                break;
                            }
                        }
                        Bitmap bitmap = null;

                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        Bitmap bmp = BitmapFactory.decodeFile(fil.getAbsolutePath(),bitmapOptions);
//
                        Log.e("edit", "new image path is " + fil.getAbsolutePath());
                        Log.e("bitmap",""+bmp);
                        Log.e("Result Ok",""+data);

                        compressImage(fil.getAbsolutePath());
                        f= new File(filename);
                        uploadImage();
//
                    }

                    break;
                case PHOTO_PICKER_ID:
                    if (requestCode == PHOTO_PICKER_ID && resultCode == Activity.RESULT_OK && null != data) {
                        Log.e("Result Ok",""+data);
                        Uri selectedImage = data.getData();
                        Log.e("selected image", "" + selectedImage);
                        Log.e("selected image", "" + getPath(selectedImage));
                        compressImage(getPath(selectedImage));
                        f= new File(filename);
                        uploadImage();

                    }

                    break;
            }
        } catch (Exception e)
        {
            Log.d("krvrrusbviuritiribtr", e.getMessage());
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null)
            return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    public void uploadImage()
    {
        try {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.show();

            Log.e("", "LOGIN " + MapAppConstant.API + "profile_image");
            HashMap params = new HashMap<String, String>();
            params.put("user_id", prefHelper.getUserId());
            params.put("user_security_hash", prefHelper.getUserSecurityHash());
            MultipartRequest sr = new MultipartRequest(MapAppConstant.API +"profile_image", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Log.d("file", f + "");
                    Log.d("", ".......response====" + response.toString());

                    ////////
                    try {
                        JSONObject object = new JSONObject(response);
                        String serverCode = object.getString("code");
                        if (serverCode.equalsIgnoreCase("0")) {

                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {

                                if ("1".equals(serverCode)) {
                                    JSONObject object1 = object.getJSONObject("data");
                                    String  img=object1.getString("user_profile_image_url");
                                    prefHelper.setProfileImage(img);
                                    Log.d("response",img);

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
                    if ((pDialog != null) && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    //  VolleyLog.d("", "Error: " + error.getMessage());
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(getActivity(), "Timeout Error",
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

            }, f, params);
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    if(getFragmentManager().getBackStackEntryCount() > 0) {

                        getFragmentManager().popBackStack();
                        HomeActivity.txt_title.setText("Home");
                    }

                    return true;

                }

                return false;
            }
        });
    }

    public void stateList()
    {

        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
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

                            Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
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
                                    ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner,stateName){
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
                        Toast.makeText(getActivity(), "Timeout Error",
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

                    params.put("user_id", prefHelper.getUserId());
                    params.put("user_security_hash", prefHelper.getUserSecurityHash());
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cityList()
    {

        try {
            final ProgressDialog pDialog = new ProgressDialog(getActivity());
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

                            Toast.makeText(getActivity(), "" + serverMessage, Toast.LENGTH_SHORT).show();
                        }
                        if (serverCode.equalsIgnoreCase("1")) {
                            try {
                                cityId.clear();
                                cityName.clear();
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
                                    ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner,cityName){
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
                        Toast.makeText(getActivity(), "Timeout Error",
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

                    params.put("user_id", prefHelper.getUserId());
                    params.put("user_security_hash", prefHelper.getUserSecurityHash());
                    params.put("state_id", state_id+"");
                    return params;
                }
            };
            sr.setRetryPolicy(new DefaultRetryPolicy(100000*2,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            sr.setShouldCache(true);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(sr);


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

            year  = selectedYear;
            month = selectedMonth+1;
            day   = selectedDay;

            dateSet= String.valueOf(new StringBuilder().append(day)
                    .append("-").append(month).append("-").append(year)
                    .append(" "));


            Date initDate = null;
            try {
                initDate = sdf.parse(dateSet);
                String diaplayDate=display.format(initDate);
                edtDateOfBirth.setText(diaplayDate);
                prefHelper.storeDOB(diaplayDate);
                parsedDate = output.format(initDate);
                Log.e("parsed date", parsedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    };



}
