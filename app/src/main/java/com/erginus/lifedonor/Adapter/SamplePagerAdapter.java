package com.erginus.lifedonor.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.erginus.lifedonor.R;

import java.util.List;


public class SamplePagerAdapter extends PagerAdapter {

    private Context mContext;
    LayoutInflater mLayoutInflater;
//    int[] mResources = {R.drawable.first_pic,R.drawable.first_pic1};
    List<Integer> list;
    public SamplePagerAdapter(Context context, List<Integer> mResources) {
        mContext = context;
        list=mResources;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//        imageView.setImageResource(mResources[position]);
        Log.e("image",""+list.get(position));
        imageView.setImageResource(list.get(position).intValue());
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
