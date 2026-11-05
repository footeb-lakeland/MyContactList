package edu.bdf.mycontactlist;

import java.util.ArrayList;

public interface VolleyCallback {
    public <T> void onSuccess(ArrayList<T> result);
    public void onError(String error);
}

