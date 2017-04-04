package com.example.muna.toura.ServerTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by christopherarnold on 2017-04-03.
 */

public class PolylineAsyncTask extends AsyncTask<String, Void, List<LatLng>> {

    public static final String baseURL ="https://roads.googleapis.com/v1/snapToRoads?key=AIzaSyBsjfxWUybZ1QsI8EKROhMQSqxZVr0uWdY&interpolate=true&path=";
    private HttpURLConnection conn = null;
    public AsyncResponse delegate = null;
    private Activity callingActivity;
    private int responseCode = 0;

    public PolylineAsyncTask(Activity activity, AsyncResponse delegate) {
        this.callingActivity = activity;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(List<LatLng> serverResponse);
    }

    @Override
    protected List<LatLng> doInBackground(String... params) {
        String queryString = params[0];

        ArrayList<LatLng> finalArr = new ArrayList<>();

        try {
            URL url = new URL(baseURL + queryString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            responseCode = conn.getResponseCode();
            System.out.println("response: " + responseCode);

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));

                String json = "";
                String line;

                while ((line = br.readLine()) != null) {
                    json += line;
                }

                JSONObject jsonObj = new JSONObject(json);
                JSONArray jsonList = (JSONArray) jsonObj.get("snappedPoints");

                for (int i = 0; i < jsonList.length(); i++) {
                    double lat = (double) ((JSONObject)((JSONObject) jsonList.get(i)).get("location")).get("latitude");
                    double lng = (double) ((JSONObject)((JSONObject) jsonList.get(i)).get("location")).get("longitude");

                    finalArr.add(new LatLng(lat, lng));
                }

                br.close();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return finalArr;
    }
    @Override
    protected void onPostExecute(List<LatLng> result) {
        delegate.processFinish(result);
    }
}
