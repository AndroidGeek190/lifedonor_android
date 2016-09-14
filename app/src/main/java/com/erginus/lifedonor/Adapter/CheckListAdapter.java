package com.erginus.lifedonor.Adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.erginus.lifedonor.EligibilityActivity;
import com.erginus.lifedonor.Model.EligibilityModel;
import com.erginus.lifedonor.R;

import java.util.List;

import kotlin.data;


public class CheckListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    Context context;
    List<EligibilityModel> list;
    public SparseBooleanArray mCheckStates;
    //    String checklist_data[]={"Have you recently come into contact with an infectious disease?","Have you had complicated dental work in the past 24 hours?",
//    "Are you pregnant or have given birth in the last six months?","Have you had hepatitis A or jaundice in the last 12 months?",
//    "Have you had a tattoo, semi-permanent make up or any sort of body piercing in the last four months?","Are you less than 16 years of age?",
//    "Are you more than 70 years of age?","Have you worked as a commercial sex worker in the past 12 months?"};
    public  CheckListAdapter(Context context,List<EligibilityModel> list)
    {

        this.list=list;
        this.context = context;
        mCheckStates = new SparseBooleanArray(list.size());
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View holder=inflater.inflate(R.layout.checklist_layout,null);

        TextView check_test=(TextView)holder.findViewById(R.id.text_check);
        final CheckBox checkbox=(CheckBox)holder.findViewById(R.id.checkbox_id);
        checkbox.setTag(position);
        checkbox.setChecked(mCheckStates.get(position, false));
        checkbox.setOnCheckedChangeListener(this);
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked())
                {
                    new EligibilityActivity(true);
                }
                if(!checkbox.isChecked())
                {
                    new EligibilityActivity(false);
                }
            }
        });
        check_test.setText(list.get(position).getName());


        return holder;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);

    }
    public void toggle(int position) {
        setChecked(position, !isChecked(position));

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }
}
