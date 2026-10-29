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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestClient {

    public static final String TAG = "RestClient";

    public static void executeGetRequest(String url,
                                         Context context,
                                         VolleyCallback volleyCallback) {
        Log.d(TAG, "executeRequest: Start");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Contact> contacts = new ArrayList<>();
        Log.d(TAG, "executeRequest: " + url);
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONArray items = new JSONArray(response);

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject object = items.getJSONObject(i);
                                    Contact contact = new Contact();
                                    contact.setContactID(object.getInt("id"));
                                    contact.setContactName(object.getString("contactName"));
                                    contact.setStreetAddress(object.getString("streetAddress"));
                                    contact.setCity(object.getString("city"));
                                    contact.setState(object.getString("state"));
                                    contact.setZipCode(object.getString("zipCode"));
                                    contact.setPhoneNumber(object.getString("phoneNumber"));
                                    contact.setCellNumber(object.getString("cellNumber"));
                                    contact.seteMail(object.getString("email"));

                                    String jsonPhoto = object.getString("photo");
                                    if(jsonPhoto != null) {
                                        Log.d(TAG, "onResponse: jsonPhoto: " + jsonPhoto);
                                        byte[] bytePhoto = null; //need to initialize it
                                        bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                        contact.setPicture(bmp);
                                    }

                                    Log.d(TAG, "onResponse: " + jsonPhoto);

                                    contacts.add(contact);
                                }
                                Log.d(TAG, "onResponse: Items: " + contacts.size());
                                for (Contact c : contacts) {
                                    Log.d(TAG, "onResponse: " + c.getContactName());
                                }
                                volleyCallback.onSuccess(contacts);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());
                    volleyCallback.onError(error.getMessage());
                }
            });
            // IMPORTANT - NOTHING WILL WORK WITHOUT THIS LINE
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
