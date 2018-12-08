/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Quakes>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private QuakesCustomAdapter quakesCustomAdapter;
    private TextView emptyView;
    private ProgressBar loadingProgressBar;
    private static final String USGS_URL_RECENT_QUAKES = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        emptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        loadingProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new {@link ArrayAdapter} of earthquakes
        quakesCustomAdapter = new QuakesCustomAdapter(getApplicationContext()
                , 0, new ArrayList<Quakes>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(quakesCustomAdapter);

        /*
        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
        earthquakeAsyncTask.execute(USGS_URL_RECENT_QUAKES);
        */


        //this code checks if there is a network connection
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        if (isMobileConn || isWifiConn){

            //getting a reference to the loader manager for creating and instantiate the loader
            getLoaderManager().initLoader(0, null, this);
        }else {

            loadingProgressBar.setVisibility(View.GONE);
            emptyView.setText("no internet connection");
        }


        Log.i("loader steps", "iniLoader is called");

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Quakes quakes = quakesCustomAdapter.getItem(position);

                Uri quakeUrl = Uri.parse(quakes.getQuakeUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, quakeUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<ArrayList<Quakes>> onCreateLoader(int i, Bundle bundle) {

        Log.i("loader steps", "onCreateLoader is called");
        return new EarthquakeLoader(this, USGS_URL_RECENT_QUAKES);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Quakes>> loader, ArrayList<Quakes> quakes) {

        loadingProgressBar.setVisibility(View.GONE);
        emptyView.setText("no data to show");

        Log.i("loader steps", "onLoadFinished is called");
        quakesCustomAdapter.clear();
        if (quakes != null && !quakes.isEmpty()){
            quakesCustomAdapter.addAll(quakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Quakes>> loader) {
        quakesCustomAdapter.clear();
        Log.i("loader steps", "onLoadReset is called");
    }


    public class EarthquakeAsyncTask extends AsyncTask<String, Void, ArrayList<Quakes>>{


        @Override
        protected ArrayList<Quakes> doInBackground(String... strings) {

            URL url = createUrl(strings[0]);

            String JSONResponse = "";

            try {

                JSONResponse = httpMakeConnection(url);
            }catch (Exception e){
            }

            ArrayList<Quakes> quakes = QueryUtils.extractEarthquakes(JSONResponse);

            return quakes;
        }

        @Override
        protected void onPostExecute(ArrayList<Quakes> quakes) {

            quakesCustomAdapter.clear();
            if (quakes != null && !quakes.isEmpty()){
                quakesCustomAdapter.addAll(quakes);
            }
        }

        public URL createUrl(String response){

            URL resultUrl = null;

            try{
                resultUrl = new URL(response);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            }
            return resultUrl;
        }

        public String httpMakeConnection(URL url) throws IOException {

            HttpURLConnection connection = null;
            InputStream inputStream = null;
            String resultResponse = "";

            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() == 200){

                    inputStream = connection.getInputStream();
                    resultResponse = readFromStram(inputStream);
                }



            }catch (Exception e){

            }finally {

                connection.disconnect();
                if (inputStream == null){inputStream.close();}

            }

            return resultResponse;
        }

        private String readFromStram(InputStream inputStream){

            StringBuilder resultResponse = new StringBuilder();

            try {
                if (inputStream != null){

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line = bufferedReader.readLine();
                    while (line != null){
                        resultResponse.append(line);
                        //move to the next line
                        line = bufferedReader.readLine();
                    }
                }
            }catch (Exception e){

            }
            return resultResponse.toString();
        }
    }

}
