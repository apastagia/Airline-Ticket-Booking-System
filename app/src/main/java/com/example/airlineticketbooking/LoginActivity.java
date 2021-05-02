package com.example.airlineticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airlineticketbooking.data.ATBContract;
import com.example.airlineticketbooking.data.ATBDbHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editPassword = (EditText) findViewById(R.id.editPassword);
        editUsername = (EditText) findViewById(R.id.editUsername);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                int j=0;
                //validation for username and password
                if(editUsername.length()==0 || editUsername.getText().toString().startsWith(" ")){
                    editUsername.setError("Please enter email");
                }else if(editPassword.length()==0 || editPassword.getText().toString().startsWith(" ")){
                    editPassword.setError("Please enter a password");
                }else{
                    j=1;
                }
                if(j==1) {
                    ATBDbHelper mDbHelper = new ATBDbHelper(LoginActivity.this);
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();

                    Cursor cursor;
                    String[] projection = {
                            ATBContract.User._ID,
                            ATBContract.User.COLUMN_NAME_EMAIL,
                            ATBContract.User.COLUMN_NAME_PASSWORD
                    };

                    String selection = ATBContract.User.COLUMN_NAME_EMAIL + " = ?";
                    String[] selectionArgs = { editUsername.getText().toString() };
                    cursor = db.query(ATBContract.User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);



                    //if match found, then allows users to login else shows error message
                    if(cursor.getCount()!=0){
                        //String dbuname = cursor.getString(cursor.getColumnIndexOrThrow(HospitappContract.Doctor.COLUMN_NAME_USERNAME));
                        cursor.moveToFirst();
                        String dbpass = cursor.getString(cursor.getColumnIndexOrThrow(ATBContract.User.COLUMN_NAME_PASSWORD));
                        if(dbpass.equals(editPassword.getText().toString())){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            i.putExtra("email", editUsername.getText().toString());
                            startActivity(i);
                        }else{
                            Toast.makeText(LoginActivity.this, "Login Failed. Email/Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Failed. Email/Password is incorrect", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}