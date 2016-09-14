package com.erginus.lifedonor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.erginus.lifedonor.BloodBanksActivity;
import com.erginus.lifedonor.Model.BloodBankModel;

import com.erginus.lifedonor.R;


import java.util.ArrayList;
import java.util.List;


public class BloodBankAdapter extends BaseAdapter {

        // Declare Variables
        private List<BloodBankModel> list2;
        private final Context Context;
        private AlphabetIndexer mIndexer;
        private List<BloodBankModel> glossariesListForSearch;
List<String> c_name;
        String a = "a", s;

    public BloodBankAdapter(Context context, List<BloodBankModel> list,List<String> c_name) {
                this.Context = context;
                this.list2 = list;
                this.c_name=c_name;
//                glossariesListForSearch = list;

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
//                return list2.indexOf(list2.get(position));
            return position;
        }

    private static class ViewHolder {
        TextView textName, textContact, textAddress, textWebsite, textSortKey;
        LinearLayout sortKeyLayout;
        TextView text;
    }
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

                if (convertView == null) {

                        LayoutInflater inflater;
                        inflater = (LayoutInflater)Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.bloodbank_list_item, parent, false);
                    holder = new ViewHolder();
                    holder.textName = (TextView) convertView.findViewById(R.id.textView_name);
                    holder.textContact = (TextView) convertView.findViewById(R.id.textView_contact);
                    holder.textAddress = (TextView) convertView.findViewById(R.id.textView_address);
                    holder.sortKeyLayout = (LinearLayout) convertView.findViewById(R.id.sort_key_layout);
                    holder.textSortKey = (TextView) convertView.findViewById(R.id.sort_key);

                    holder.textWebsite = (TextView) convertView.findViewById(R.id.textView_website);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position < list2.size()) {

//                      String  test1 = list2.get(position).get_city_name();
                        if (c_name.get(position).equalsIgnoreCase("0"))
                        {
                                Log.e("if position yes shown",""+position+" "+c_name.get(position));
//                            holder.sortKeyLayout.setVisibility(View.VISIBLE);
//                            holder.textSortKey.setVisibility(View.VISIBLE);
//                            holder.textSortKey.setText(list2.get(position).get_city_name());
                            holder.sortKeyLayout.setVisibility(View.VISIBLE);
                            holder.textSortKey.setVisibility(View.VISIBLE);
                            holder.textSortKey.setText(list2.get(position).get_city_name());
//
//                            holder.sortKeyLayout.setVisibility(View.GONE);
//                            holder.textSortKey.setVisibility(View.GONE);
                            holder.textName.setText(list2.get(position).getName() + ", " + list2.get(position).getDesc());
                            holder.textContact.setText(list2.get(position).getContact());
                            holder.textAddress.setText(list2.get(position).getAddress());

                            if(list2.get(position).getWebsite().equalsIgnoreCase(""))
                            {
                                holder.textWebsite.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.textWebsite.setVisibility(View.VISIBLE);
                                holder.textWebsite.setText(list2.get(position).getWebsite());
                            }
                        }
                        else {
                                Log.e("else position not show",""+position+"  "+c_name.get(position));
                            holder.sortKeyLayout.setVisibility(View.GONE);
                            holder.textSortKey.setVisibility(View.GONE);

                                holder.textName.setText(list2.get(position).getName() + ", " + list2.get(position).getDesc());
                                holder.textContact.setText(list2.get(position).getContact());

                                holder.textAddress.setText(list2.get(position).getAddress());
                            if(list2.get(position).getWebsite().equalsIgnoreCase(""))
                            {
                                holder.textWebsite.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.textWebsite.setVisibility(View.VISIBLE);
                                holder.textWebsite.setText(list2.get(position).getWebsite());
                            }

                        }
                }

                        return convertView;
                }

}