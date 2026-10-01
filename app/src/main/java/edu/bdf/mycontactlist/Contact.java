package edu.bdf.mycontactlist;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

public class Contact implements Serializable {
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

    public Contact(String contactName,
                   String streetAddress,
                   String city,
                   String state,
                   String zipCode,
                   String phoneNumber,
                   String cellNumber,
                   String email,
                   Calendar birthday) {
        contactID = -1;
        this.birthday = Calendar.getInstance();
        this.contactName = contactName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.cellNumber = cellNumber;
        this.eMail = email;
        this.setBirthday(birthday);
    }
    public Contact(String contactName,
                   String streetAddress,
                   String city,
                   String state,
                   String zipCode,
                   String phoneNumber,
                   String cellNumber,
                   String email) {
        contactID = -1;
        this.birthday = Calendar.getInstance();
        this.contactName = contactName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.cellNumber = cellNumber;
        this.eMail = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Contact(){
        contactID = -1;
        birthday = Calendar.getInstance();
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

    public void setControlText(int controlId, String value) {
        if(controlId == R.id.editName)
        {
            Log.d("Team", "setControlText: " + value);
            this.setContactName(value);
        } else if (controlId == R.id.editAddress) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editCity) {
            this.setCity(value);
        }else if (controlId == R.id.editState) {
            this.setState(value);
        }else if (controlId == R.id.editZipcode) {
            this.setZipCode(value);
        }else if (controlId == R.id.editHome) {
            this.setPhoneNumber(value);
        }else if (controlId == R.id.editCell) {
            this.setCellNumber(value);
        }
    }
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
