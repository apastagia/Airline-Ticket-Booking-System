package com.example.airlineticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airlineticketbooking.data.ATBContract;
import com.example.airlineticketbooking.data.ATBDbHelper;

//allows user to edit the profile
public class EditProfileActivity extends AppCompatActivity {

    private Button btnSave;
    private EditText fname;
    private EditText lname;
    private EditText email;
    private EditText dob;
    private EditText passport;
    private EditText password;
    private String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_email = extras.getString("user_email");
            Toast.makeText(EditProfileActivity.this, user_email, Toast.LENGTH_SHORT).show();
        }

        fname = (EditText) findViewById(R.id.editfname);
        lname = (EditText) findViewById(R.id.editlname);
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        dob = (EditText) findViewById(R.id.editDOB);
        passport = (EditText) findViewById(R.id.editPassport);
        btnSave = (Button) findViewById(R.id.btnSave);

        ATBDbHelper mDbHelper = new ATBDbHelper(EditProfileActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = {
                ATBContract.User._ID,
                ATBContract.User.COLUMN_NAME_FIRST_NAME,
                ATBContract.User.COLUMN_NAME_LAST_NAME,
                ATBContract.User.COLUMN_NAME_EMAIL,
                ATBContract.User.COLUMN_NAME_PASSPORT_NUMBER,
                ATBContract.User.COLUMN_NAME_DOB,
                ATBContract.User.COLUMN_NAME_GENDER,
        };

        String selection = ATBContract.User.COLUMN_NAME_EMAIL + " = ?";
        String i = user_email;
        String[] selectionArgs = { i };

        Cursor cursor = db.query(ATBContract.User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()) {
            email.setText(cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_EMAIL)));
            fname.setText(cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_FIRST_NAME)));
            lname.setText(cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_LAST_NAME)));
            dob.setText(cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_DOB)));
            passport.setText(cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_PASSPORT_NUMBER)));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
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
                    ATBDbHelper mDbHelper = new ATBDbHelper(EditProfileActivity.this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();

                    values.put(ATBContract.User.COLUMN_NAME_FIRST_NAME, fname.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_LAST_NAME, lname.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_EMAIL, email.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_PASSWORD, password.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_DOB, dob.getText().toString());
                    values.put(ATBContract.User.COLUMN_NAME_PASSPORT_NUMBER, passport.getText().toString());
                    String[] selectionArgs = { user_email };
                    int row = db.update(ATBContract.User.TABLE_NAME, values, ATBContract.User.COLUMN_NAME_EMAIL + "=?", selectionArgs);
                    Toast.makeText(EditProfileActivity.this, "effect row: " + row, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}