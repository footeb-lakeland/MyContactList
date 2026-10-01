package edu.bdf.mycontactlist;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;

public class ContactEditActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {
    // Brian Foote
    public static final String TAG = "ContactEditActivity";
    Contact currentContact;
    Boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "onCreate: Start");

        initToggleButton();
        initChangeDateButton();
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);
        setForEditing(false);
        initSaveButton();

        initTextChanged(R.id.editName);
        initTextChanged(R.id.editAddress);
        initTextChanged(R.id.editCity);
        initTextChanged(R.id.editState);
        initTextChanged(R.id.editZipcode);
        initTextChanged(R.id.editHome);
        initTextChanged(R.id.editCell);
        initTextChangedEvents();



        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            Log.d(TAG, "onCreate: Extras Start");
            //currentContact.setContactName(extras.getString("contact"));
            //currentContact = (Contact)getIntent().getSerializableExtra("contact");
            //Log.d(TAG, "onCreate: " + currentContact.getContactName());
            //initContact();
            Log.d(TAG, "onCreate: end extras");
        }
        else {
            // Making a new contact
            currentContact = new Contact();    
        }
        

        Log.d(TAG, "onCreate: End");


    }

    private void initContact() {

        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView birthDay = findViewById(R.id.textBirthday);

        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editZipCode.setText(currentContact.getZipCode());
        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.geteMail());
        birthDay.setText(DateFormat.format("MM/dd/yyyy",
                currentContact.getBirthday().getTimeInMillis()).toString());
    }
    private void initToggleButton(){
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editName = findViewById(R.id.editName);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
        EditText editAddress = findViewById(R.id.editAddress);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
        EditText et = findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et = findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et = findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et = findViewById(R.id.editHome);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et = findViewById(R.id.editCell);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et = findViewById(R.id.editEMail);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private void initTextChanged(int controlId) {
        EditText editText = findViewById(controlId);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!loading) {
                    Log.d(TAG, "afterTextChanged: "+ editable.toString());
                    currentContact.setControlText(controlId, editable.toString());
                }
            }
        });
    }

    private void initTextChangedEvents(){

        final EditText etPhone = findViewById(R.id.editHome);
        final EditText etCell = findViewById(R.id.editCell);
        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etCell.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }
    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(ContactEditActivity.this);
                FileIOHelper fileIO = new FileIOHelper();
                try {
                    ds.open();

                    if (currentContact.getContactID() == -1) {
                        wasSuccessful = ds.insertContact(currentContact) > 0;
                        if (wasSuccessful) {
                            int newId = ds.getLastContactId();
                            currentContact.setContactID(newId);
                        }

                        // Also write to a file for fun.
                        ArrayList<String> contacts = new ArrayList<String>();
                        contacts.add(currentContact.toString());

                        Log.d(currentContact.TAG, "onClick: " + contacts.size());
                        fileIO.writeFile(ContactEditActivity.this, contacts);
                        Log.d(currentContact.TAG, currentContact.getContactName() + " written");
                        Toast.makeText(ContactEditActivity.this, currentContact.getContactName() + " written", Toast.LENGTH_LONG).show();

                    }
                    else {
                        wasSuccessful = ds.updateContact(currentContact) > 0;
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }
    private void initChangeDateButton(){
        Button changeDate = findViewById(R.id.btnBirthday);

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editHome = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);

        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        editHome.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);

        if(enabled)
            editName.requestFocus();
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        // Catch the selected date
        TextView tvSelectedDate = findViewById(R.id.textBirthday);
        tvSelectedDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
    }
}