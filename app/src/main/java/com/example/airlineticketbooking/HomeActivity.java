package com.example.airlineticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airlineticketbooking.data.ATBContract;
import com.example.airlineticketbooking.data.ATBDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

//dashboard where user can access to the app features
public class HomeActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private EditText origin;
    private EditText destination;
    private EditText departureDate;
    private Button btnEditProfile;
    private Button btnViewFlights;
    private Button btnSearch;
    private ListView flightList;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> idList;
    private String email;
    int userId;

    Calendar myCal;
    DatePickerDialog.OnDateSetListener date;
    ATBDbHelper myHelper;
    SQLiteDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);
        origin = (EditText) findViewById(R.id.editOrigin);
        destination = (EditText) findViewById(R.id.editDestination);
        departureDate = (EditText) findViewById(R.id.editDepartureDate);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        btnViewFlights = (Button) findViewById(R.id.btnViewFlights);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        flightList = (ListView) findViewById(R.id.flightList);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);

        idList = new ArrayList<Integer>(); // stores each flight id of displayed flight in the list view

        //get user first and last name
        myHelper = new ATBDbHelper(HomeActivity.this);
        myDb = myHelper.getWritableDatabase();

        final Intent i = getIntent();
        email = i.getStringExtra("email");
        Cursor userInfo = null;
        userInfo = myDb.rawQuery("SELECT * FROM " + ATBContract.User.TABLE_NAME
                + " WHERE " + ATBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);

        if(userInfo.getCount() != 0){
            userInfo.moveToNext();
            userId = userInfo.getInt(0);
            txtWelcome.setText("Welcome " + userInfo.getString(1) + " " + userInfo.getString(2));
        }

        // date picker
        myCal = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();
            }

        };

        // when user clicks on departure date edit text date picker dialog box will be opened
        departureDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(HomeActivity.this, date, myCal
                        .get(Calendar.YEAR), myCal.get(Calendar.MONTH),
                        myCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag = false;
                // validates the user data
                if(origin.length()==0 ) {
                    origin.setError("Please enter valid origin of your trip");
                }
                else if(destination.length()==0 ) {
                    destination.setError("Please enter valid destination of your trip");
                }
                else if(departureDate.length()==0) {
                    departureDate.setError("Please enter valid departure date");
                }
                else
                {
                    flag = true;
                }

                if (flag){
                    Cursor cursor = null ;
                    // gets the flight details according to origin, destination and departure date entered by user
                    cursor = myDb.rawQuery("SELECT * FROM " + ATBContract.Flight.TABLE_NAME
                            + " WHERE " + ATBContract.Flight.COLUMN_NAME_ORIGIN + "='" + origin.getText() + "' AND "
                            + ATBContract.Flight.COLUMN_NAME_DESTINATION + "='" + destination.getText() + "' AND "
                            + ATBContract.Flight.COLUMN_NAME_DEPARTURE_DATE + "='" + departureDate.getText() + "' ORDER BY " + ATBContract.Flight.COLUMN_NAME_TOTAL_COST, null);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    flightList.setAdapter(adapter);
                    adapter.clear();

                    if(cursor.getCount() != 0){
                        while (cursor.moveToNext()) {
                            // adds data to listview
                            idList.add(cursor.getInt(0));
                            arrayList.add(cursor.getString(1) + " - Total Cost : $" + cursor.getString(7));
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this, "No results for given data", Toast.LENGTH_LONG).show();

                    }

                    // when user selects itinerary from the list, user will be redirected to BookItinerary activity
                    flightList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long i)
                        {
                            Intent intent = new Intent(HomeActivity.this, BookItineraryActivity.class);
                            intent.putExtra("flightId", idList.get(position));
                            intent.putExtra("userId", userId);
                            intent.putExtra("hideBook", "no");
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        btnViewFlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BookedFlightActivity.class);
                intent.putExtra("user_email", i.getStringExtra("email"));
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                intent.putExtra("user_email", i.getStringExtra("email"));
                startActivity(intent);
            }
        });
    }

    // adds the selected date from date picker to departure date editText
    private void setDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        departureDate.setText(sdf.format(myCal.getTime()));
    }
}