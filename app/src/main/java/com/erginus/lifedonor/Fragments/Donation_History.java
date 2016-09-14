package com.erginus.lifedonor.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erginus.lifedonor.HomeActivity;
import com.erginus.lifedonor.R;

/**
 * Created by nazer on 6/15/2016.
 */
public class Donation_History  extends Fragment {
    View view;

    public Donation_History() {
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
        View rootview = inflater.inflate(R.layout.fragment_donation_history, container, false);
        HomeActivity.txt_title.setText("Donation History");
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

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    if(getFragmentManager().getBackStackEntryCount() > 0) {

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
