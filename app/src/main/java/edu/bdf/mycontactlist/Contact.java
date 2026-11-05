package edu.bdf.mycontactlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Calendar;

public class Contact implements Serializable {

    private static final String TAG = "ContactEntity";
    private static final String DELIM = "|";

    // Private Fields
    private int contactID;
    private String contactName;

    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String eMail;
    private Calendar birthday;
    private String birthdayLong;
    private Bitmap photo;
    private String photoJson;


    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;

        if(photo != null)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bitmap = Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            photoJson = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }
        else {
            Log.d(TAG, "setPhoto: Photo is null in setPhoto");
        }
    }

    // Constructor
    public Contact() {
        // Default initial values
        contactID = -1;
        birthday = Calendar.getInstance();
    }

    public Contact(String contactName,
                   String streetAddress,
                   String city,
                   String state,
                   String zipCode,
                   String phoneNumber,
                   String cellNumber,
                   String email,
                   Calendar birthday) {
        this.setContactID(-1);
        this.setContactName(contactName);
        this.setStreetAddress(streetAddress);
        this.setCity(city);
        this.setState(state);
        this.setZipCode(zipCode);
        this.setPhoneNumber(phoneNumber);
        this.setCellNumber(cellNumber);
        this.setEMail(email);
        this.setBirthday(birthday);
    }
    public void fromJSON(JSONObject item)
    {
        try {
            this.setContactID(item.getInt("id"));
            this.setContactName(item.getString("contactName"));
            this.setStreetAddress(item.getString("streetAddress"));
            this.setCity(item.getString("city"));
            this.setState(item.getString("state"));
            this.setZipCode(item.getString("zipCode"));
            this.setPhoneNumber(item.getString("phoneNumber"));
            this.setCellNumber(item.getString("cellNumber"));
            this.setEMail(item.getString("email"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(item.getString("birthday")));
            this.setBirthday(calendar);

            String jsonPhoto = item.getString("photo");

            if(jsonPhoto != null)
            {
                Log.d(TAG, "fromJSON: Photo not null");
                byte[] bytePhoto = null;
                bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                setPhoto(bmp);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public Contact(JSONObject item) {

        try
        {
            this.setContactID(item.getInt("id"));
            this.setContactName(item.getString("contactName"));
            this.setStreetAddress(item.getString("streetAddress"));
            this.setCity(item.getString("city"));
            this.setState(item.getString("state"));
            this.setZipCode(item.getString("zipCode"));
            this.setPhoneNumber(item.getString("phoneNumber"));
            this.setCellNumber(item.getString("cellNumber"));
            this.setEMail(item.getString("email"));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(item.getString("birthday")));
            this.setBirthday(calendar);

            String jsonPhoto = item.getString("photo");

            if(jsonPhoto != null)
            {
                Log.d(TAG, "fromJSON: Photo not null");
                byte[] bytePhoto = null;
                bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                setPhoto(bmp);
            }
        }
        catch(JSONException e)
        {
            Log.d(TAG, "Contact: " + e.getMessage());
        }
    }

    public JSONObject toJSON() {
        JSONObject item = new JSONObject();
        try {
            item.put("id", getContactID());
            item.put("name", contactName);
            item.put("streetAddress", streetAddress);
            item.put("city", city);
            item.put("state", state);
            item.put("zipCode", zipCode);
            item.put("phoneNumber", phoneNumber);
            item.put("email", eMail);
            item.put("birthday", String.valueOf(birthday.getTimeInMillis()));
            item.put("photo", photoJson);
            return item;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public int getContactID() {
        return contactID;
    }

    public void setContactID(int i) {
        contactID = i;
    }

    // Public property
    // Getter
    public String getContactName() {
        // Return the private field
        return contactName;
    }

    // Public property
    // Setter
    public void setContactName(String newvalue) {
        // Change the private field value to the value of the parameter
        contactName = newvalue;
    }


    public Calendar getBirthday() {
        return birthday;
    }

    public void setBirthday(Calendar c) {
        birthday = c;
        birthdayLong = String.valueOf(c.getTimeInMillis());
    }


    public String getCity() {
        return city;
    }

    public void setCity(String s) {
        city = s;
    }

    public String getState() {
        return state;
    }

    public void setState(String s) {
        state = s;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String s) {
        zipCode = s;
    }

    public void setPhoneNumber(String s) {
        phoneNumber = s;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCellNumber(String s) {
        cellNumber = s;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setEMail(String s) {
        eMail = s;
    }

    public String getEMail() {
        return eMail;
    }

    // Override
    public String toString() {
        String data = contactID + DELIM
                + contactName + DELIM
                + streetAddress + DELIM
                + city + DELIM
                + state + DELIM
                + zipCode + DELIM
                + phoneNumber + DELIM
                + cellNumber + DELIM
                + eMail + DELIM
                + birthday;

        //Log.d(TAG, "toString: " + data);
        return data;
    }

    public void setControlText(int controlId, String value) {
        if(controlId == R.id.editName)
        {
            Log.d("Team", "setControlText: " + value);
            this.setContactName(value);
        } else if (controlId == R.id.editAddress) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editCity) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editState) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editZipcode) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editHome) {
            this.setStreetAddress(value);
        }else if (controlId == R.id.editCell) {
            this.setStreetAddress(value);
        }
    }
}
