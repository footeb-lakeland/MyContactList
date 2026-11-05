package edu.bdf.mycontactlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

public class RestClient {

    public static final String TAG = "RestClient";

    public static <T> void executeGetRequest(String url,
                                             Context context,
                                             VolleyCallback volleyCallback,
                                             Class<T> entityType)
    {
        Log.d(TAG, "execGetRequest: Start");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<T> entities = new ArrayList<T>();
        Log.d(TAG, "execGetRequest: " + url);

        try
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONArray items = new JSONArray(response);
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);
                                    T entity = entityType.getDeclaredConstructor(JSONObject.class).newInstance(object);
                                    entities.add(entity);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "execGetRequest: ", e);
                                throw new RuntimeException(e);
                            }
                            volleyCallback.onSuccess(entities);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    });
            // IMPORTANT
            Log.d(TAG, "execGetRequest: Get all Request");
            requestQueue.add(stringRequest);
        }
        catch(Exception e)
        {
            Log.e(TAG, "execGetRequest: ", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> void executeGetOneRequest(String url,
                                                Context context,
                                                VolleyCallback volleyCallback,
                                                Class<T> entityType)
    {
        Log.d(TAG, "execGeOneRequest: Start");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<T> entities = new ArrayList<T>();
        Log.d(TAG, "execGeOneRequest: " + url);

        try
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                T team = entityType.getDeclaredConstructor(JSONObject.class).newInstance(object);
                                entities.add(team);
                            } catch (Exception e) {
                                Log.e(TAG, "execGetRequest: ", e);
                                throw new RuntimeException(e);
                            }
                            volleyCallback.onSuccess(entities);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    });
            // IMPORTANT
            Log.d(TAG, "execGetRequest: Add Request");
            requestQueue.add(stringRequest);
        }
        catch(Exception e)
        {
            Log.e(TAG, "execGetRequest: ", e);
            throw new RuntimeException(e);
        }
    }
    //com.google.code.gson
    private static <T> void executeRequest(T entity,
                                           String url,
                                           Context context,
                                           VolleyCallback volleyCallback,
                                           int method,
                                           Class<T> entityType)

    {

        Log.d(TAG, "executeRequest: Start : " + method + " : " + url);
        ArrayList<T> entities = new ArrayList<T>();

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            // Add the com.google.code.gson library
            Gson gson = new Gson();
            JSONObject item = new JSONObject(gson.toJson(entity));
            Log.d(TAG, "executeRequest: Create JSON" + item);

            item.put("id", item.get("contactID"));
            item.put("contactId", item.get("contactID"));
            item.put("photo", null);
            item.put("photo", item.get("photoJson"));
            item.put("birthday", item.get("birthdayLong"));

            final String requestBody = item.toString();
            Log.d(TAG, "executeRequest: " + requestBody);

            JsonObjectRequest request = new JsonObjectRequest(method,
                    url,
                    item,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            entities.add(entity);
                            volleyCallback.onSuccess(entities);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    })
            {
                @Override
                public byte[] getBody(){
                    Log.d(TAG, "getBody: " + item.toString());
                    return item.toString().getBytes(StandardCharsets.UTF_8);
                }
            };

            requestQueue.add(request);
        }
        catch(Exception e)
        {
            Log.d(TAG, "executeRequest: Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public static <T> void executePostRequest(T entity,
                                              String url,
                                              Context context,
                                              VolleyCallback volleyCallback,
                                              Class<T> entityType)
    {
        try
        {
            executeRequest(entity, url, context, volleyCallback, Request.Method.POST, entityType);
        }
        catch(Exception e)
        {
            throw e;
        }
    }
    public static <T> void executePutRequest(T entity,
                                          String url,
                                          Context context,
                                          VolleyCallback volleyCallback,
                                          Class<T> entityType)
    {
        try {
            executeRequest(entity, url, context, volleyCallback, Request.Method.PUT, entityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static <T> void executeDeleteRequest(T entity,
                                                String url,
                                                Context context,
                                                VolleyCallback volleyCallback,
                                                Class<T> entityType)
    {
        try {
            executeRequest(entity, url, context, volleyCallback, Request.Method.DELETE, entityType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
