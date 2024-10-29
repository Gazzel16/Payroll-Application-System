package com.example.payroll_application_system;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.payroll_application_system.databasehelper.DBHelper;

import java.util.ArrayList;

public class PaidEmployeesActivity extends AppCompatActivity {

    ListView paidEmployeesList;
    ArrayList<String> paidEmployeesArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Button editEmployeeButton;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_employees);

        paidEmployeesList = findViewById(R.id.paidEmployeesList);
        editEmployeeButton = findViewById(R.id.editEmployeeButton);
        db = new DBHelper(this);

        editEmployeeButton.setOnClickListener(view -> {

            Intent intent = new Intent(PaidEmployeesActivity.this, ViewEmployeesActivity.class);
            startActivity(intent);
            finish();

        });

        loadPaidEmployees();
    }



    // Load paid employees from the database
    private void loadPaidEmployees() {
        paidEmployeesArrayList.clear();
        Cursor cursor = db.getPaidEmployees(); // Fetch paid employees

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String employeeDetails = cursor.getString(1) + " - " + cursor.getString(2) + " - $" + cursor.getString(3);
                paidEmployeesArrayList.add(employeeDetails);
            }
        } else {
            paidEmployeesArrayList.add("No paid employees found.");
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paidEmployeesArrayList);
        paidEmployeesList.setAdapter(arrayAdapter);
    }
}
