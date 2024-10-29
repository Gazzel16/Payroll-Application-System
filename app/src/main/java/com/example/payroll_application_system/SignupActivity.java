package com.example.payroll_application_system;

import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity {
    EditText username, email, password;
    Button signupBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.loginTextBtn).setOnClickListener(view ->{
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        db = new DBHelper(this);

        signupBtn.setOnClickListener(v -> {
            String user = username.getText().toString();
            String mail = email.getText().toString();
            String pass = password.getText().toString();

            if (db.insertUser(user, mail, pass)) {
                Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                // Redirect to LoginActivity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
