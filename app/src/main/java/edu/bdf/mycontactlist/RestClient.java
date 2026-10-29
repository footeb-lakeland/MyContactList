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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;

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
    public static void executeGetOneRequest(String request,
                                            Context context,
                                            VolleyCallback volleyCallback) {
        Log.d(TAG, "executeRequest: Start");
        RequestQueue queue = Volley.newRequestQueue(context);
        ArrayList<Contact> contacts = new ArrayList<>();
        Log.d(TAG, "executeRequest: " + request);
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);
                            try {
                                JSONObject object = new JSONObject(response);

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
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Long.valueOf(object.getString("birthday")));
                                contact.setBirthday(calendar);

                                String jsonPhoto = object.getString("photo");
                                Log.d(TAG, "onResponse: jsonPhoto: " + jsonPhoto);
                                byte[] bytePhoto=null; //need to initialize it

                                bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);

                                Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                contact.setPicture(bmp);

                                Log.d(TAG, "onResponse: " + (bmp == null));

                                contacts.add(contact);

                                Log.d(TAG, "onResponse: " + contact.getContactName() + " ("  + contact.getContactID() + ")");
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

            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void executeRequest(Contact contact,
                                       String url,
                                       Context context,
                                       VolleyCallback volleyCallback,
                                       int method)

    {
        try {
            RequestQueue queue = Volley.newRequestQueue(context);
            JSONObject object = new JSONObject();

            object.put("id", contact.getContactID());
            object.put("contactName", contact.getContactName());
            object.put("streetAddress", contact.getStreetAddress());
            object.put("city", contact.getCity());
            object.put("state", contact.getState());
            object.put("zipCode", contact.getZipCode());
            object.put("phoneNumber", contact.getPhoneNumber());
            object.put("cellNumber", contact.getCellNumber());
            object.put("email", contact.geteMail());
            object.put("photo", null);
            object.put("birthday", String.valueOf(contact.getBirthday().getTimeInMillis()));

            //Update the picture
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bmp = contact.getPicture();
            if(bmp != null)
            {
                Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 200, 200, false);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String jsonPhoto = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                Log.d(TAG, "executeRequest: Ended the Photo Processing" );
                object.put("photo", jsonPhoto);
            }

            final String requestBody = object.toString();

            JsonObjectRequest request = new JsonObjectRequest(method, url, object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse: " + response);
                            volleyCallback.onSuccess(null);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    volleyCallback.onError(error.getMessage());
                }
            })
            {
                @Override
                public byte[] getBody() {
                    Log.d(TAG, "getBody: " + object.toString());
                    return object.toString().getBytes(StandardCharsets.UTF_8);
                }
            };
            queue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void executePostRequest(Contact contact,
                                          String url,
                                          Context context,
                                          VolleyCallback volleyCallback)
    {
        try {
            executeRequest(contact, url, context, volleyCallback, Request.Method.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void executePutRequest(Contact contact,
                                          String url,
                                          Context context,
                                          VolleyCallback volleyCallback)
    {
        try {
            executeRequest(contact, url, context, volleyCallback, Request.Method.PUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void executeDeleteRequest(Contact contact,
                                         String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        try {
            executeRequest(contact, url, context, volleyCallback, Request.Method.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
