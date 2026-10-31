package edu.bdf.mycontactlist;

import java.util.ArrayList;

public interface VolleyCallback {
    public void onSuccess(ArrayList<Contact> result);
    public void onError(String error);
}

