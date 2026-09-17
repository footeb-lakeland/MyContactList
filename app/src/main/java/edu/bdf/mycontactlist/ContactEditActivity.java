package edu.bdf.mycontactlist;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class ContactEditActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {
    // Brian Foote
    public static final String TAG = "ContactEditActivity";
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

        initToggleButton();
        initChangeDateButton();
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);
        setForEditing(false);
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