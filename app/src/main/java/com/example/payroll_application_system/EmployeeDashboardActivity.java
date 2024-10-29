package com.example.payroll_application_system;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmployeeDashboardActivity extends AppCompatActivity {
    Button addEmployee, viewEmployees, editEmployees, payEmployee, viewPaidEmployees, salaryCalculatorBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        addEmployee = findViewById(R.id.addEmployeeBtn);
        viewEmployees = findViewById(R.id.viewEmployeesBtn);
        payEmployee = findViewById(R.id.payEmployeeBtn);
        viewPaidEmployees = findViewById(R.id.viewPaidEmployeesBtn);
        salaryCalculatorBtn = findViewById(R.id.salaryCalculatorBtn);

        addEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        viewEmployees.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, ViewEmployeesActivity.class);
            startActivity(intent);
        });

        payEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, PayEmployeeActivity.class);
                    startActivity(intent);
        });

        viewPaidEmployees.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDashboardActivity.this, PaidEmployeesActivity.class);
            startActivity(intent);
        });


        salaryCalculatorBtn.setOnClickListener(v ->{
            Intent intent = new Intent(EmployeeDashboardActivity.this, SalaryCalculatorActivity.class);
            startActivity(intent);

        });
        // Define similar intents for Pay Employee and Generate Pay Slip activities
    }
}