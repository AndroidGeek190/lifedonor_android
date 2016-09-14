package com.erginus.lifedonor.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.erginus.lifedonor.Model.BloodBankModel;
import com.erginus.lifedonor.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements SectionIndexer {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<BloodBankModel>> expandableListDetail;
//    String sections[];
    String index_list[];
    ArrayList<String> sectionList;
    public CustomExpandableListAdapter(Context context, List<String> list_in,List<String> expandableListTitle, HashMap<String, List<BloodBankModel>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
//        if(sectionList!=null)
//        {
//            sectionList.clear();
//            sectionList = new ArrayList<String>(list_in);
//
//            Log.e("sectionList", sectionList.toString());
//            Collections.sort(sectionList);
//
//            index_list = new String[sectionList.size()];
//
//            sectionList.toArray(index_list);
////        index_list=list_in;
//            notifyDataSetInvalidated();
//        }
//        else {
            sectionList = new ArrayList<String>(list_in);

            Log.e("sectionList", sectionList.toString());
            Collections.sort(sectionList);

            index_list = new String[sectionList.size()];

            sectionList.toArray(index_list);
//        index_list=list_in;
            notifyDataSetInvalidated();
            Log.e("title size ", "" + expandableListTitle.size());
//        }
//sections=new String[expandableListTitle.size()];
////        mapIndex = new LinkedHashMap<String, Integer>();
//        Log.e("size adapter ",""+expandableListTitle.size());
//        for (int x = 0; x < expandableListTitle.size(); x++) {
//            String name = expandableListTitle.get(x);
//            Log.e("name ",""+name);
//            String ch = String.valueOf(name.charAt(1));
////            ch = ch.toUpperCase(Locale.US);
//            Log.e("upper case",""+ch);
//            // HashMap will prevent duplicates
////            mapIndex.put(ch, x);
//            sections[x]=ch;
//        }
    }
//
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textName, textContact, textAddress, textWebsite, textSortKey;
        LinearLayout sortKeyLayout;
        TextView text;
//        final String expandedListText = (String)getChild(listPosition, expandedListPosition);
        final String expandedListText=this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).get_city_name();
        Log.e("Child data of row "," "+expandedListText.toString());

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.bloodbank_list_item, null);
        }

        textName = (TextView) convertView.findViewById(R.id.textView_name);
        textContact = (TextView) convertView.findViewById(R.id.textView_contact);
        textAddress = (TextView) convertView.findViewById(R.id.textView_address);
        sortKeyLayout = (LinearLayout) convertView.findViewById(R.id.sort_key_layout);
        textSortKey = (TextView) convertView.findViewById(R.id.sort_key);
        textWebsite = (TextView) convertView.findViewById(R.id.textView_website);

//        TextView expandedListTextView = (TextView) convertView
//                .findViewById(R.id.expandedListItem);
//        expandedListTextView.setText(expandedListText.toString());
        textName.setText(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).getName());
        textContact.setText(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).getContact());
        textAddress.setText(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).getAddress());
        if(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).getWebsite().equalsIgnoreCase(""))
        {
            textWebsite.setVisibility(View.GONE);
        }
        else
        {
            textWebsite.setVisibility(View.VISIBLE);
         textWebsite.setText(this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition).getWebsite());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        Log.e("child size :",""+expandableListDetail.get(this.expandableListTitle.get(listPosition)).size());
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expand_header, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.sort_key);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    @Override
    public Object[] getSections() {
        return index_list;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
            return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }
}