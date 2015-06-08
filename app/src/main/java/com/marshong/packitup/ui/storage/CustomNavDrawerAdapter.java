package com.marshong.packitup.ui.storage;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by martin on 6/7/2015.
 */
public class CustomNavDrawerAdapter extends ArrayAdapter<CharSequence> {
    public static final String TAG = CustomNavDrawerAdapter.class.getSimpleName();

    private Context mContext;
    private int mLayoutResourceId;
    private CharSequence mData[] = null;
    private Typeface mTf;

    public CustomNavDrawerAdapter(Context context, int resource, CharSequence[] objects, String font) {
        super(context, resource, objects);
        Log.d(TAG, "customNavDrawer constructor... " + font);
        mLayoutResourceId = resource;
        mContext = context;
        mData = objects;
        mTf = Typeface.createFromAsset(context.getAssets(), font);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setTypeface(mTf);
        return view;
    }
}
