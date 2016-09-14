package com.erginus.lifedonor.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidpagecontrol.PageControl;
import com.erginus.lifedonor.Adapter.SamplePagerAdapter;
import com.erginus.lifedonor.Common.Prefshelper;
import com.erginus.lifedonor.HomeActivity;
import com.erginus.lifedonor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazer on 6/14/2016.
 */
public class Compatibility extends Fragment {


    List<Integer> mResources;
    public Compatibility() {
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
        View rootview = inflater.inflate(R.layout.fragment_compatibility, container, false);
        mResources=new ArrayList<>();
        HomeActivity.txt_title.setText("Compatibility");
        mResources.add(R.drawable.redcells);
        mResources.add(R.drawable.blood);
        SamplePagerAdapter adapter = new SamplePagerAdapter(getActivity(),mResources);
        ViewPager viewPager = (ViewPager)rootview.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        final PageControl pageControl = (PageControl)rootview.findViewById(R.id.page_control);
        pageControl.setViewPager(viewPager);

        pageControl.setPosition(0);
return rootview;
    }
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    if (getFragmentManager().getBackStackEntryCount() > 0) {

                        HomeActivity.txt_title.setText("Home");
                        getFragmentManager().popBackStack();

                    }

                    return true;

                }

                return false;
            }
        });
    }
}