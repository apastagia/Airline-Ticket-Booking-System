package com.example.airlineticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.airlineticketbooking.data.ATBContract;
import com.example.airlineticketbooking.data.ATBDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText fname;
    private EditText lname;
    private EditText email;
    private EditText dob;
    private EditText passport;
    private EditText password;
    private RadioGroup radGender;
    private RadioButton radGenderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = (EditText) findViewById(R.id.editfname);
        lname = (EditText) findViewById(R.id.editlname);
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        dob = (EditText) findViewById(R.id.editDOB);
        passport = (EditText) findViewById(R.id.editPassport);
        radGender = (RadioGroup) findViewById(R.id.radGender);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checks validations
                int j=0;
                if(fname.length()==0 || fname.getText().toString().startsWith(" ")){
                    fname.setError("Please enter your first name");
                }else if(lname.length()==0 || lname.getText().toString().startsWith(" ")){
                    lname.setError("Please enter your last name");
                }else if(email.length()==0 || email.getText().toString().startsWith(" ")){
                    email.setError("Please enter a valid email address");
                }else if(password.length()<6 || password.getText().toString().startsWith(" ")){
                    password.setError("Please enter password atleast 6 characters long");
                }else{
                    j=1;
                }
                if(j==1) {
                    ContentValues values = new ContentValues();

                    int selectedId = radGender.getCheckedRadioButtonId();
                    radGenderBtn = (RadioButton) findViewById(selectedId);

                    //checks for gender
                    if(radGenderBtn.getText().equals("male")) {
                        values.put(ATBContract.User.COLUMN_NAME_GENDER, ATBContract.User.GENDER_MALE);
                    }else{
                        values.put(ATBContract.User.COLUMN_NAME_GENDER, ATBContract.User.GENDER_FEMALE);
                    }

                    ATBDbHelper mDbHelper = new ATBDbHelper(RegisterActivity.this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();


                    values.put(ATBContract.User.COLUMN_NAME_FIRST_NAME, fname.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_LAST_NAME, lname.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_EMAIL, email.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_PASSWORD, password.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_DOB, dob.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_PASSPORT_NUMBER, passport.getText().toString());

                    // inserts new item to the database
                    long newRowID;
                    newRowID = db.insert(ATBContract.User.TABLE_NAME, null, values);

                    Toast.makeText(RegisterActivity.this, "Registration Successful. You can login now.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                }

            }
        });




    }
}