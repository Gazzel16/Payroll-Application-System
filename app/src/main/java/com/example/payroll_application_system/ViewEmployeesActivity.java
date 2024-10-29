package com.example.payroll_application_system;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.payroll_application_system.databasehelper.DBHelper;

import java.util.ArrayList;

public class ViewEmployeesActivity extends AppCompatActivity {
    ListView employeeList;
    EditText employeeName, department, salary;
    Button updateEmployeeBtn, deleteEmployeeBtn;
    DBHelper db;
    ArrayList<String> employeeArrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Integer> employeeIdList = new ArrayList<>(); // To store employee IDs for editing
    int selectedEmployeeId = -1; // The currently selected employee's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employees);

        employeeList = findViewById(R.id.employeeList);
        employeeName = findViewById(R.id.editEmployeeName);
        department = findViewById(R.id.editDepartment);
        salary = findViewById(R.id.editSalary);
        updateEmployeeBtn = findViewById(R.id.updateEmployeeBtn);
        deleteEmployeeBtn = findViewById(R.id.deleteEmployeeBtn);
        db = new DBHelper(this);

        loadEmployeeList();

        // Set up item click listener to populate the edit fields
        employeeList.setOnItemClickListener((parent, view, position, id) -> {
            selectedEmployeeId = employeeIdList.get(position);
            populateEmployeeDetails(selectedEmployeeId);
        });

        // Update employee details on button click
        updateEmployeeBtn.setOnClickListener(v -> {
            if (selectedEmployeeId != -1) {
                String newName = employeeName.getText().toString();
                String newDepartment = department.getText().toString();
                double newSalary = Double.parseDouble(salary.getText().toString());

                boolean isUpdated = db.updateEmployee(selectedEmployeeId, newName, newDepartment, newSalary);

                if (isUpdated) {
                    Toast.makeText(ViewEmployeesActivity.this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
                    loadEmployeeList(); // Refresh the employee list
                    clearFields();
                } else {
                    Toast.makeText(ViewEmployeesActivity.this, "Failed to update employee", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ViewEmployeesActivity.this, "Please select an employee to update", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete employee on button click
        deleteEmployeeBtn.setOnClickListener(v -> {
            if (selectedEmployeeId != -1) {
                boolean isDeleted = db.deleteEmployee(selectedEmployeeId);
                if (isDeleted) {
                    Toast.makeText(ViewEmployeesActivity.this, "Employee deleted", Toast.LENGTH_SHORT).show();
                    loadEmployeeList(); // Refresh the employee list
                    clearFields();
                } else {
                    Toast.makeText(ViewEmployeesActivity.this, "Failed to delete employee", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ViewEmployeesActivity.this, "Please select an employee to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Load all employees into the list
    private void loadEmployeeList() {
        employeeArrayList.clear();
        employeeIdList.clear();
        Cursor cursor = db.getEmployees();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                employeeArrayList.add(cursor.getString(1) + " - " + cursor.getString(2) + " - $" + cursor.getString(3));
                employeeIdList.add(cursor.getInt(0)); // Add employee ID
            }
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, employeeArrayList);
        employeeList.setAdapter(arrayAdapter);
    }

    // Populate the fields with the selected employee's details
    private void populateEmployeeDetails(int employeeId) {
        Cursor cursor = db.getEmployeeById(employeeId);
        if (cursor.moveToFirst()) {
            employeeName.setText(cursor.getString(1));  // Employee Name
            department.setText(cursor.getString(2));   // Department
            salary.setText(cursor.getString(3));       // Salary
        }
    }

    // Clear the input fields after update or delete
    private void clearFields() {
        employeeName.setText("");
        department.setText("");
        salary.setText("");
        selectedEmployeeId = -1; // Reset the selected employee ID
    }
}
