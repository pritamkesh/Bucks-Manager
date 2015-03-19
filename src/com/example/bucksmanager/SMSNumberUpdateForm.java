/* This is source code of Bank Account Registration.
* Source Code written by Pritam Kesh.

* E-mail : pritamkesh.summercode@gmail.com
*
* To report any bugs please send me an e-mail.
* Tips are welcome.
*
*/
package com.example.bucksmanager;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
 
public class SMSNumberUpdateForm extends Activity implements
        OnItemSelectedListener {
 
    // Spinner element
    Spinner spinner;
 
    // Add button
    Button btnAdd;
 
    // Input text
    EditText inputLabel;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_smsnumber_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinnerSMSnumber);
 
        // add button
        btnAdd = (Button) findViewById(R.id.addSMSnumber);
 
        // new label input field
        inputLabel = (EditText) findViewById(R.id.smsNumber);
 
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
 
        // Loading spinner data from database
        loadSpinnerData();
 
        /**
         * Add new label button click listener
         * */
        btnAdd.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                String label = inputLabel.getText().toString();
 
                if (label.trim().length() > 0) {
                    // database handler
                    DatabaseHandler db = new DatabaseHandler(
                            getApplicationContext());
 
                    // inserting new label into database
                    db.insertLabel(label);
 
                    // making input filed text to blank
                    inputLabel.setText("");
 
                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
 
                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter SMS Number",
                            Toast.LENGTH_SHORT).show();
                }
 
            }
        });
    }
 
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
 
        // Spinner Drop down elements
        List<String> lables = db.getAllLabels();
 
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lables);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
 
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
 
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "SMS Number Added",
                Toast.LENGTH_LONG).show();
 
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
 
    }
}

