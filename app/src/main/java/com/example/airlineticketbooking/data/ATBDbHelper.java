package com.example.airlineticketbooking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class ATBDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "atb.db";
    private static final int DATABASE_VERSION =1;

    public ATBDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_USER_TABLE = "CREATE TABLE " + ATBContract.User.TABLE_NAME + "("
                + ATBContract.User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATBContract.User.COLUMN_NAME_FIRST_NAME + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_LAST_NAME + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_EMAIL + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_DOB + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_PASSPORT_NUMBER + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_PASSWORD + " TEXT NOT NULL, "
                + ATBContract.User.COLUMN_NAME_GENDER + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_USER_TABLE);

        // create statement for Booking info table
        String SQL_CREATE_BOOKING_INFO_TABLE = "CREATE TABLE " + ATBContract.BookingInfo.TABLE_NAME + "("
                + ATBContract.BookingInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATBContract.BookingInfo.COLUMN_NAME_USER_ID + " INTEGER NOT NULL, "
                + ATBContract.BookingInfo.COLUMN_NAME_FLIGHT_ID + " INTEGER NOT NULL); ";
        db.execSQL(SQL_CREATE_BOOKING_INFO_TABLE);

        // create statement for flight table
        String SQL_CREATE_FLIGHT_TABLE = "CREATE TABLE " + ATBContract.Flight.TABLE_NAME + "("
                + ATBContract.Flight._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ATBContract.Flight.COLUMN_NAME_FLIGHT_NAME + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_FLIGHT_NUMBER + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_ORIGIN + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_DESTINATION + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_DEPARTURE_DATE + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_ARRIVAL_DATE + " TEXT NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_TOTAL_COST + " REAL NOT NULL, "
                + ATBContract.Flight.COLUMN_NAME_TOTAL_TIME + " TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_FLIGHT_TABLE);

        // insert statements for Flight table
        String SQL_INSERT_FLIGHT_RECORDS = "INSERT INTO " + ATBContract.Flight.TABLE_NAME + "("
                + ATBContract.Flight.COLUMN_NAME_FLIGHT_NAME + ", "
                + ATBContract.Flight.COLUMN_NAME_FLIGHT_NUMBER + ", "
                + ATBContract.Flight.COLUMN_NAME_ORIGIN + ", "
                + ATBContract.Flight.COLUMN_NAME_DESTINATION + ", "
                + ATBContract.Flight.COLUMN_NAME_DEPARTURE_DATE + ", "
                + ATBContract.Flight.COLUMN_NAME_ARRIVAL_DATE + ", "
                + ATBContract.Flight.COLUMN_NAME_TOTAL_COST + ", "
                + ATBContract.Flight.COLUMN_NAME_TOTAL_TIME + ") " +
                "VALUES ('Jet Airways', '233', 'Toronto', 'Ahmedabad', '25/12/2017', '26/12/2017', 2800.0, '24h 40m'), " +
                "('Air Canada', '2001', 'Toronto', 'London', '25/12/2017', '26/12/2017', 1100.0, '12h 30m'), " +
                "('Air India', '121', 'Toronto', 'Ahmedabad', '25/12/2017', '26/12/2017', 2200.0, '27h 30m');";

        db.execSQL(SQL_INSERT_FLIGHT_RECORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
