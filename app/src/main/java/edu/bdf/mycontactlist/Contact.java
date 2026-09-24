package edu.bdf.mycontactlist;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class Contact {
    public static final String TAG = "Contact";
    public static final String DELIM = "|";

    // Private fields
    private int contactID;
    private String contactName;
    private String streetAddress;
    private String city;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String eMail;
    private Calendar birthday;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
    }

    // Public Accessors


    @NonNull
    public String toString()
    {
        String data = contactID
                + contactName + DELIM
                + streetAddress + DELIM
                + city + DELIM
                + state + DELIM
                + zipCode + DELIM
                + phoneNumber + DELIM
                + cellNumber + DELIM
                + eMail;
        //+ birthday;

        Log.d(TAG, "toString: " + data);
        return data;
    }
}
