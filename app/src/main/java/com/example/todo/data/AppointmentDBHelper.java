package com.example.todo.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class AppointmentDBHelper  extends SQLiteOpenHelper {

    //  database name and version
    public static final String DATABASE_NAME = "events.db";
    public static final int DATABASE_VERSION = 1;

    //constants for the table names and column names

    public static final String TABLE_DEPENDENCY = "dependency";

    public static final String DEPENDENCY_ID = "dependency_id";
    private static final String DEPENDENCY_NAME ="dependency_name" ;
    private static final String DEPENDENCY_CATEGORY ="dependency_category" ;
    public static final String TABLE_AGENT = "agent";


    private static final String AGENT_ID = "agent_id";
    private static final String AGENT_NAME = "agent_name";
    private static final String AGENT_CATEGORY = "agent_category";
    private static final String AGENT_PHONE_NUMBER = "agent_phone_number";


    public static final String TABLE_APPOINTMENT = "appointment";

    private static final String APPOINTMENT_ID = "appointment_id";
    public static final String APPOINTMENT_START_DATETIME = "start_datetime";
    public static final String APPOINTMENT_REMINDER_DATETIME = "reminder_datetime";
    public static final String APPOINTMENT_STAT = "stat";
    public static final String APPOINTMENT_DEPENDENCY_ID = "dependency_id";
    public static final String APPOINTMENT_AGENT_ID = "agent_id";

    // Declare a private constructor to prevent direct instantiation
    public AppointmentDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Override the onCreate method to create database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the dependency table
        String CREATE_TABLE_DEPENDENCY = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPENDENCY + " (" +
                DEPENDENCY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DEPENDENCY_NAME + " TEXT NOT NULL, " +
                DEPENDENCY_CATEGORY + " TEXT" +
                ");";

        // Create the agent table
        String CREATE_TABLE_AGENT = "CREATE TABLE IF NOT EXISTS " + TABLE_AGENT + " (" +
                AGENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AGENT_NAME + " TEXT NOT NULL, " +
                AGENT_CATEGORY + " TEXT, " +
                AGENT_PHONE_NUMBER + " TEXT" +
                ");";

        // Create the appointment table
        String CREATE_TABLE_APPOINTMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_APPOINTMENT + " (" +
                APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                APPOINTMENT_START_DATETIME + " TEXT NOT NULL, " +
                APPOINTMENT_REMINDER_DATETIME + " TEXT, " +
                APPOINTMENT_STAT + " TEXT NOT NULL, " +
                APPOINTMENT_DEPENDENCY_ID + " INTEGER NOT NULL, " +
                APPOINTMENT_AGENT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + APPOINTMENT_DEPENDENCY_ID + ") REFERENCES " + TABLE_DEPENDENCY + " (" + DEPENDENCY_ID + "), " +
                "FOREIGN KEY (" + APPOINTMENT_AGENT_ID + ") REFERENCES " + TABLE_AGENT + " (" + DEPENDENCY_ID + ")" +
                ");";

        // Execute the SQL statements
        db.execSQL(CREATE_TABLE_DEPENDENCY);
        db.execSQL(CREATE_TABLE_AGENT);
        db.execSQL(CREATE_TABLE_APPOINTMENT);
    }

    // Override the onUpgrade method to drop and recreate the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPENDENCY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);

        // Recreate the tables
        onCreate(db);
    }
}
