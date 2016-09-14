package com.erginus.lifedonor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erginus.lifedonor.HomeActivity;
import com.erginus.lifedonor.Model.DonorModel;
import com.erginus.lifedonor.R;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.List;


public class DonorAdapter extends BaseAdapter {
        TelephonyManager telephonyManager1;
        // Declare Variables
private List<DonorModel> list2;
private final Context context;
        String temp;
public DonorAdapter(Context context, List<DonorModel> list) {
        this.context = context;
        this.list2=list;
        }

@Override
public int getCount() {
        return list2.size();
        }

@Override
public Object getItem(int position) {
        return list2.get(position);
        }

@Override
public long getItemId(int position) {
        return list2.indexOf(list2.get(position));
        }

public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        telephonyManager1 = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        ImageView imageView;
        TextView textView_name, text_num,text_distance,text_available;
        LinearLayout call,sms,share;
        inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.donor_list_item, parent,false);
        imageView=(ImageView)itemView.findViewById(R.id.imageView3);
        textView_name=(TextView)itemView.findViewById(R.id.textView_name);
        text_num=(TextView)itemView.findViewById(R.id.textView_contact);
        text_distance=(TextView)itemView.findViewById(R.id.textView_distance);
        text_available=(TextView)itemView.findViewById(R.id.textView_available);
        textView_name.setText(list2.get(position).getName());
        text_num.setText(list2.get(position).getcontact());
        Log.e("adapter serch", "" + list2.get(position).getName());
        String str=list2.get(position).getDistance();
        double distance= Double.parseDouble(str);
        text_distance.setText(new DecimalFormat("##.##").format(distance)+ " km");
        text_available.setText(list2.get(position).getAddress());
        Picasso.with(context).load(list2.get(position).getImage()).into(imageView);
        call=(LinearLayout)itemView.findViewById(R.id.ll_call);
        sms=(LinearLayout)itemView.findViewById(R.id.ll_sms);
        share=(LinearLayout)itemView.findViewById(R.id.ll_share);

        call.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



                if(telephonyManager1.getPhoneType()== TelephonyManager.PHONE_TYPE_NONE)
                {
                        //coming here if Tablet
                        Toast.makeText(context, "Your device has no sim card available", Toast.LENGTH_LONG).show();
                }
                else{
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        temp = "tel:"+list2.get(position).getcontact() ;
                        intent.setData(Uri.parse(temp));
                        context.startActivity(intent);
                }



        }
        });
        sms.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                        if(telephonyManager1.getPhoneType()== TelephonyManager.PHONE_TYPE_NONE)
                        {
                                //coming here if Tablet
                                Toast.makeText(context, "Your device has no sim card available", Toast.LENGTH_LONG).show();
                        }
                        else{
                                try {
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(list2.get(position).getcontact(), null, "Please Help!!! Urgently required blood group" +
                                        list2.get(position).getblood_group() + "\nhttps://play.google.com/store/apps/details?id=com.erginus.lifedonor", null, null);

                                //Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                                Toast.makeText(context, "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                        }
                        }

                }
        });



        share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        String message = "Blood Donor Details- \nName: "+list2.get(position).getName()+", "+"Mobile :"+list2.get(position).getcontact() +", "+"Blood Group: "+ list2.get(position).getblood_group()+ "\nhttps://play.google.com/store/apps/details?id=com.erginus.lifedonor";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(share, "Share Via"));
        }
        });
        return itemView;
        }
        }