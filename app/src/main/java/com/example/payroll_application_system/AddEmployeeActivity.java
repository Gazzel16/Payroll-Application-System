package com.example.payroll_application_system;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.payroll_application_system.databasehelper.DBHelper;

public class AddEmployeeActivity extends AppCompatActivity {
    EditText name, department, salary;
    Button addEmployeeBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        name = findViewById(R.id.employeeName);
        department = findViewById(R.id.department);
        salary = findViewById(R.id.salary);
        addEmployeeBtn = findViewById(R.id.addEmployeeBtn);
        db = new DBHelper(this);

        addEmployeeBtn.setOnClickListener(v -> {
            String empName = name.getText().toString();
            String empDept = department.getText().toString();
            double empSalary = Double.parseDouble(salary.getText().toString());

            if (db.insertEmployee(empName, empDept, empSalary)) {
                Toast.makeText(AddEmployeeActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();
                // Optionally, go back to the dashboard
            } else {
                Toast.makeText(AddEmployeeActivity.this, "Failed to add employee", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
