package com.example.airlineticketbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.airlineticketbooking.data.ATBContract;
import com.example.airlineticketbooking.data.ATBDbHelper;

import java.util.ArrayList;

//list all the booked flight by user
public class BookedFlightActivity extends AppCompatActivity {

    private String email;
    ATBDbHelper myHelper;
    SQLiteDatabase myDb;
    private ListView bookedFlightList;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_flight);

        bookedFlightList = (ListView) findViewById(R.id.booked_flight_booked_flight_lv);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayList);

        idList = new ArrayList<Integer>(); // stores each flight id of displayed flight in the list view

        myHelper = new ATBDbHelper(BookedFlightActivity.this);
        myDb = myHelper.getWritableDatabase();

        Intent i = getIntent();
        email = i.getStringExtra("user_email");

        Cursor userInfo = null;
        userInfo = myDb.rawQuery("SELECT * FROM " + ATBContract.User.TABLE_NAME
                + " WHERE " + ATBContract.User.COLUMN_NAME_EMAIL + "='" + email + "'", null);
        if (userInfo.getCount() != 0) {
            userInfo.moveToNext();
            final int userid = userInfo.getInt(0);

//            Toast.makeText(BookedFlightActivity.this, userid, Toast.LENGTH_LONG).show();
            Cursor userBookedFlight = null;
            userBookedFlight = myDb.rawQuery("SELECT * FROM " + ATBContract.BookingInfo.TABLE_NAME
                    + " WHERE " + ATBContract.BookingInfo.COLUMN_NAME_USER_ID + "='" + userid + "'", null);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            bookedFlightList.setAdapter(adapter);
            adapter.clear();

            //gets the username ands flight details of the logged in user and shows in the listview
            if (userBookedFlight.getCount() != 0) {
                while (userBookedFlight.moveToNext()) {
                    Cursor userFlightDetail = null;
                    userFlightDetail = myDb.rawQuery("SELECT * FROM " + ATBContract.Flight.TABLE_NAME
                            + " WHERE " + ATBContract.Flight._ID+ "='" + userBookedFlight.getInt(2) + "'", null);
                    if(userFlightDetail.getCount() != 0){
                        while ((userFlightDetail.moveToNext())){
                            // adds data to listview
                            idList.add(userFlightDetail.getInt(0));
                            arrayList.add(userFlightDetail.getString(1) + userFlightDetail.getString(2)
                                    + " : " + userFlightDetail.getString(3) + " - " + userFlightDetail.getString(4)
                                    + " - " + userFlightDetail.getString(5));
                            adapter.notifyDataSetChanged();
                        }
                    }

                }
            } else {
                Toast.makeText(BookedFlightActivity.this, "No results for given data", Toast.LENGTH_LONG).show();

            }

            //on listview click, opens the ticket menu activity
            bookedFlightList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(view.getContext(), BookItineraryActivity.class);
                    intent.putExtra("flightId", idList.get(i));
                    intent.putExtra("userId", userid);
                    intent.putExtra("hideBook", "yes");
                    startActivity(intent);
                }
            });
        }
    }
}