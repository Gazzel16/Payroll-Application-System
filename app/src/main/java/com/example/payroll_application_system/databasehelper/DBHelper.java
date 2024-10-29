package com.example.payroll_application_system.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PayrollSystem.db";
    public static final String USERS_TABLE = "users";
    public static final String EMPLOYEES_TABLE = "employees";
    private static final int DATABASE_VERSION = 2; // Added database version

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Updated to use DATABASE_VERSION
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating users table
        db.execSQL("CREATE TABLE " + USERS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT)");

        // Creating employees table with 'paid' column
        db.execSQL("CREATE TABLE " + EMPLOYEES_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "department TEXT, " +
                "salary DOUBLE, " +
                "paid INTEGER DEFAULT 0" +  // Add paid column, default 0 (not paid)
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Check the old version and perform necessary upgrades
        if (oldVersion < 2) {
            // If upgrading from version 1 to 2, add the "paid" column
            db.execSQL("ALTER TABLE " + EMPLOYEES_TABLE + " ADD COLUMN paid INTEGER DEFAULT 0");
        }
    }

    // Insert User
    public boolean insertUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = db.insert(USERS_TABLE, null, contentValues);
        return result != -1; // returns false if insertion fails
    }

    // Insert Employee
    public boolean insertEmployee(String name, String department, double salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("department", department);
        contentValues.put("salary", salary);
        long result = db.insert(EMPLOYEES_TABLE, null, contentValues);
        return result != -1;
    }

    // Fetch Employee List
    public Cursor getEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EMPLOYEES_TABLE, null);
    }

    // Fetch User
    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE username = ? AND password = ?", new String[]{username, password});
    }

    // Method to add an employee
    public boolean addEmployee(String name, String department, double salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("department", department);
        contentValues.put("salary", salary);
        long result = db.insert(EMPLOYEES_TABLE, null, contentValues);
        return result != -1;  // Return true if insertion is successful
    }

    public Cursor getEmployeeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EMPLOYEES_TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});
    }

    public boolean updateEmployee(int id, String name, String department, double salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("department", department);
        contentValues.put("salary", salary);
        int result = db.update(EMPLOYEES_TABLE, contentValues, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;  // Return true if at least one row was updated
    }

    // Method to delete an employee by ID
    public boolean deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(EMPLOYEES_TABLE, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;  // Return true if at least one row was deleted
    }

    // Method to mark an employee as paid
    public boolean markEmployeeAsPaid(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("paid", 1);  // 1 means "paid"
        int result = db.update(EMPLOYEES_TABLE, contentValues, "id = ?", new String[]{String.valueOf(id)});
        return result > 0;  // Return true if at least one row was updated
    }

    // Method to check if an employee is paid
    public boolean checkIfEmployeePaid(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT paid FROM " + EMPLOYEES_TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) == 1;  // 1 means paid
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log it)
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();  // Close the cursor to prevent memory leaks
            }
        }
        return false; // Return false if not found or an error occurred
    }

    // Method to get employees who are paid
    public Cursor getPaidEmployees() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + EMPLOYEES_TABLE + " WHERE paid = 1", null);
    }
}
