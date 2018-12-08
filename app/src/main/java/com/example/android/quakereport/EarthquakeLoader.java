package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Quakes>> {

    String mUrl = "";
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();

        Log.i("loader steps", "onStartLoader is called");
    }

    @Override
    public ArrayList<Quakes> loadInBackground() {

        if (mUrl == null){
            return null;
        }

        Log.i(" loader steps", "loadInBackground is called");

        ArrayList<Quakes> quakes = QueryUtils.fetchEarthquakeData(mUrl);
        return quakes;
    }
}
