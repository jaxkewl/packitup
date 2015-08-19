package com.marshong.packitup.ui.storage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

//GOTCHA: if you want to switch out the actual fragments that are being displayed,
//you need to avoid FragmentPagerAdapter and use FragmentStatePagerAdapter.
//
//Using FragmentPagerAdapter won't work because FragmentPagerAdapter never destroys
//a fragment after it's been displayed the first time.
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = SectionsPagerAdapter.class.getSimpleName();

    private int mLocationId;
    private int mContainerCount;
    private String[] mContainerNames;
    private String[] mContainerIds;
    private int mSelectedContainerId;

    public SectionsPagerAdapter(FragmentManager fm, int locationId, int containerCount, String[] containerNames, String[] containerIds) {
        super(fm);
        Log.d(TAG, "SectionsPagerAdapter passing in locationId: " + locationId + " count: " + containerCount);

        String names = "";
        String ids = "";
        for (int i = 0; i < containerCount; i++) {
            names += containerNames[i] + " ";
            ids += containerIds[i] + " ";
        }
        Log.d(TAG, "names: " + names + " ids: " + ids);

        mLocationId = locationId;
        mContainerCount = containerCount;
        mContainerNames = containerNames;
        mContainerIds = containerIds;

    }


    //we need to pass to each fragment of the viewpager, what location ID and what container ID it came from
    //in case any item in the container list wants to be updated.
    @Override
    public Fragment getItem(int i) {
        Log.d(TAG, "========SectionsPagerAdapter - getItem: " + i);
        Fragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        mSelectedContainerId = Integer.parseInt(mContainerIds[i]);   //need to set the current container Id to the one that was just selected
        args.putInt(SectionFragment.CONTAINER_ID, mSelectedContainerId);
        args.putInt(SectionFragment.LOCATION_ID, mLocationId);
        args.putStringArray(SectionFragment.containerArrayKey, mContainerIds);
        args.putInt(SectionFragment.selectedIndexKey, i);


        fragment.setArguments(args);
        Log.d(TAG, " selectedContainerId: " + mSelectedContainerId + " with locationId: " + mLocationId + " to SectionFragment: " + fragment.toString());
        return fragment;
    }

    @Override
    public int getCount() {
        //Log.d(TAG, "SectionsPagerAdapter - count: " + mContainerCount + " " + this.toString());
        return mContainerCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d(TAG, "getPageTitle: " + position);
        mSelectedContainerId = Integer.parseInt(mContainerIds[position]);   //need to set the current container Id to the one that was just selected
        Log.d(TAG, "setting selectedContainerId to: " + mSelectedContainerId);



        return mContainerNames[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem position: " + position);
        return super.instantiateItem(container, position);
    }
}