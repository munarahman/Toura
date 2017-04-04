package com.example.muna.toura.ServerTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.muna.toura.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by christopherarnold on 2017-04-03.
 */

public class PolylineAsyncTask extends AsyncTask<String, Void, String> {

    public static final String baseURL = "https://roads.googleapis.com/v1/snapToRoads?key=AIzaSyBsjfxWUybZ1QsI8EKROhMQSqxZVr0uWdY";
    private HttpURLConnection conn = null;
    public AsyncResponse delegate = null;
    private Activity callingActivity;
    private int responseCode = 0;

    public PolylineAsyncTask(Activity activity, AsyncResponse delegate) {
        this.callingActivity = activity;
        this.delegate = delegate;
    }

    public interface AsyncResponse {
        void processFinish(String serverResponse);
    }

    @Override
    protected String doInBackground(String... params) {
        String queryString = params[0];

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

                try {
                    while ((line = br.readLine()) != null) {
                        json += line;
                    }
                    System.out.println("******");
                    JSONObject jsonObj = new JSONObject(json);
                    System.out.println(jsonObj);
                    System.out.println(jsonObj.get("snappedPoints"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                br.close();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "test";
    }
    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
