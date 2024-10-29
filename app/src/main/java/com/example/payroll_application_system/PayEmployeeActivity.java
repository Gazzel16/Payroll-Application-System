package com.example.payroll_application_system;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.payroll_application_system.databasehelper.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PayEmployeeActivity extends AppCompatActivity {

    ListView employeeList;
    TextView selectedEmployeeDetails, paymentStatus;
    Button payEmployeeBtn, generatePayslipBtn;
    DBHelper db;
    ArrayList<String> employeeArrayList = new ArrayList<>();
    ArrayList<Integer> employeeIdList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    int selectedEmployeeId = -1;
    boolean isPaid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_employee);

        employeeList = findViewById(R.id.employeeList);
        selectedEmployeeDetails = findViewById(R.id.selectedEmployeeDetails);
        paymentStatus = findViewById(R.id.paymentStatus);
        payEmployeeBtn = findViewById(R.id.payEmployeeBtn);
        generatePayslipBtn = findViewById(R.id.generatePayslipBtn);
        db = new DBHelper(this);

        loadEmployeeList();

        // When an employee is selected, load their details
        employeeList.setOnItemClickListener((parent, view, position, id) -> {
            selectedEmployeeId = employeeIdList.get(position);
            displayEmployeeDetails(selectedEmployeeId);
        });

        // Mark employee as paid
        payEmployeeBtn.setOnClickListener(v -> {
            if (selectedEmployeeId != -1) {
                isPaid = db.markEmployeeAsPaid(selectedEmployeeId);
                if (isPaid) {
                    paymentStatus.setText("Payment Status: Paid");
                    Toast.makeText(PayEmployeeActivity.this, "Employee paid successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PayEmployeeActivity.this, "Failed to mark employee as paid", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PayEmployeeActivity.this, "Please select an employee", Toast.LENGTH_SHORT).show();
            }
        });

        // Generate the payslip
        generatePayslipBtn.setOnClickListener(v -> {
            if (selectedEmployeeId != -1 && isPaid) {
                generatePayslip(selectedEmployeeId);
            } else {
                Toast.makeText(PayEmployeeActivity.this, "Please select an employee and mark as paid first", Toast.LENGTH_SHORT).show();
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

    // Display selected employee's details
    private void displayEmployeeDetails(int employeeId) {
        Cursor cursor = db.getEmployeeById(employeeId);
        if (cursor.moveToFirst()) {
            String details = "Name: " + cursor.getString(1) + "\n" +
                    "Department: " + cursor.getString(2) + "\n" +
                    "Salary: $" + cursor.getString(3);
            selectedEmployeeDetails.setText(details);

            // Check if the employee has already been paid
            boolean hasBeenPaid = db.checkIfEmployeePaid(employeeId);
            if (hasBeenPaid) {
                isPaid = true;
                paymentStatus.setText("Payment Status: Paid");
            } else {
                isPaid = false;
                paymentStatus.setText("Payment Status: Not Paid");
            }
        }
    }

    // Generate payslip for the selected employee
    private void generatePayslip(int employeeId) {
        Cursor cursor = db.getEmployeeById(employeeId);
        if (cursor.moveToFirst()) {
            // Sample logic to generate a payslip
            String payslip = "PAYSLIP\n" +
                    "Name: " + cursor.getString(1) + "\n" +
                    "Department: " + cursor.getString(2) + "\n" +
                    "Salary: $" + cursor.getString(3) + "\n" +
                    "Payment Status: Paid\n" +
                    "Date: " + getCurrentDate() + "\n" +
                    "Time: " + getCurrentTime();

            // Display the payslip in a dialog
            new AlertDialog.Builder(this)
                    .setTitle("Generated Payslip")
                    .setMessage(payslip)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    // Method to get current date
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    // Method to get current time
    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila")); // Set the timezone to PHT
        return timeFormat.format(new Date());
    }
}