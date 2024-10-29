package com.example.payroll_application_system;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SalaryCalculatorActivity extends AppCompatActivity {

    private EditText edtNetSalary, edtSSSTax, edtPagibigTax;
    private TextView textViewNetSalary;
    private Button calculateButton;
    Button updateEmployeeBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_calculator);

        edtNetSalary = findViewById(R.id.edtNetSalary);
        edtSSSTax = findViewById(R.id.edtSSSTax);
        edtPagibigTax = findViewById(R.id.edtPagibigTax);
        textViewNetSalary = findViewById(R.id.textViewNetSalary);
        calculateButton = findViewById(R.id.button);
        updateEmployeeBtn = findViewById(R.id.updateEmployeeBtn);

        updateEmployeeBtn.setOnClickListener(v ->{
            Intent intent = new Intent(SalaryCalculatorActivity.this, ViewEmployeesActivity.class);
            startActivity(intent);
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateNetSalary();
            }
        });
    }

    private void calculateNetSalary() {
        String netSalaryStr = edtNetSalary.getText().toString();
        String sssTaxStr = edtSSSTax.getText().toString();
        String pagibigTaxStr = edtPagibigTax.getText().toString();

        if (netSalaryStr.isEmpty() || sssTaxStr.isEmpty() || pagibigTaxStr.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double netSalary = Double.parseDouble(netSalaryStr);
        double sssTaxPercentage = Double.parseDouble(sssTaxStr);
        double pagibigTaxPercentage = Double.parseDouble(pagibigTaxStr);

        // Calculate total deductions as percentages
        double sssTax = (sssTaxPercentage / 100) * netSalary;
        double pagibigTax = (pagibigTaxPercentage / 100) * netSalary;

        // Calculate final net salary after deductions
        double finalNetSalary = netSalary - (sssTax + pagibigTax);

        // Display the final net salary
        textViewNetSalary.setText("Net Salary: " + finalNetSalary);
    }
}
