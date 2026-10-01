package edu.bdf.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;
    public static final String TAG = "ContactDataSource";

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException
    {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void refreshData()
    {
        Log.d(TAG, "refreshData: Start");
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        database.delete("contact", null, null);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2002);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 25);

        contacts.add(new Contact("Brian Foote",
                "123 Main St.",
                "Oshkosh",
                "WI",
                "54901",
                "9201112222",
                "9202223333",
                "footeb@lakeland.edu"));

        calendar.set(Calendar.YEAR, 1996);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 15);

        contacts.add(new Contact("Han Solo",
                "234 Jones St.",
                "Sheboygan",
                "WI",
                "53081",
                "9203334444",
                "9204445555",
                "soloh@lakeland.edu"));

        calendar.set(Calendar.YEAR, 1941);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 7);

        contacts.add(new Contact("Leia Organa",
                "234 Jones St.",
                "Sheboygan",
                "WI",
                "53081",
                "9203334545",
                "9204445656",
                "organal@lakeland.edu"));

        // Delete and reinsert all the teams
        long results = 0;
        for(Contact contact : contacts){
            results += insertContact(contact);
        }
        Log.d(TAG, "refreshData: End: " + results + " rows...");
    }


    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<>();
        try {
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contactNames = new ArrayList<String>();
        }
        return contactNames;
    }

    public long insertContact(Contact c) {
        long results = 0;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.geteMail());
            initialValues.put("birthday",String.valueOf(c.getBirthday().getTimeInMillis()));

            results = database.insert("contact", null, initialValues);
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return results;
    }

    public long updateContact(Contact c) {
        long results = 0;
        try {
            Long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.geteMail());
            updateValues.put("birthday",
                    String.valueOf(c.getBirthday().getTimeInMillis()));

            results = database.update("contact", updateValues, "_id=" + rowId, null);
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return results;
    }

    public int getLastContactId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

}