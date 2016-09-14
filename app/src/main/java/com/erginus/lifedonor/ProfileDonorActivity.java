package com.erginus.lifedonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erginus.lifedonor.Common.Prefshelper;
import com.squareup.picasso.Picasso;

public class ProfileDonorActivity extends AppCompatActivity {
    public static ImageView imgBack, imgProfile;
    public EditText edtName, edtAge, edtcontact, edtAddress;
    TextView txtBloodGroup, txtSex;
    Prefshelper prefshelper;
    int id;
    String genderVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_donor);

        imgBack = (ImageView) findViewById(R.id.imageView_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(ProfileDonorActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        edtName=(EditText)findViewById(R.id.editText_name);
        edtAddress=(EditText)findViewById(R.id.editText_address);
        edtAge=(EditText)findViewById(R.id.editText_age);
        edtcontact=(EditText)findViewById(R.id.editText_contact);
        imgProfile=(ImageView)findViewById(R.id.imageView_profile);
        txtBloodGroup=(TextView)findViewById(R.id.editText_bloodGroup);
        txtSex=(TextView)findViewById(R.id.editText_sex);
        prefshelper=new Prefshelper(this);
        edtName.setText(prefshelper.getName());
        edtcontact.setText(prefshelper.getContact());
        edtAge.setText(prefshelper.getDOB());
        edtAddress.setText(prefshelper.getAddress());
        genderVal=prefshelper.getGender();
        if(!(prefshelper.getProfileImage().equalsIgnoreCase("")))
        {
            Picasso.with(this).load(prefshelper.getProfileImage()).into(imgProfile);
        }
        if(genderVal.equalsIgnoreCase("1"))
        {
            id=1;
            txtSex.setText("Male");
        }else
        {
            id=2;
           txtSex.setText("Female");
        }

            txtBloodGroup.setText(prefshelper.getBloodGroup());
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(ProfileDonorActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
