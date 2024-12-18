package com.example.discoverwales;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MainActivity extends AppCompatActivity {
    private static connectionPG con=new connectionPG();
    boolean fieldsChecked = false;
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[!@#$%^&*.()-+])(?=\\S+$).{9,}$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etEmail = findViewById(R.id.email);
        EditText etPassword = findViewById(R.id.password);
        Button logIn = findViewById(R.id.logIn);
        Button signUp = findViewById(R.id.signUp);
        TextView recoverPassword = findViewById(R.id.forgottenPassword);
        ImageButton clearEmail=findViewById((R.id.clearEmail));
        ImageButton clearPassword=findViewById((R.id.clearPassword));
        logIn.setOnClickListener(v -> {
        fieldsChecked = CheckFields(etEmail,etPassword);
        if (fieldsChecked) {
            logIn(etEmail.getText().toString(), etPassword.getText().toString());
        }
        });
        clearEmail.setOnClickListener( v -> {
            etEmail.setText("");
        });
        clearPassword.setOnClickListener( v -> {
            etPassword.setText("");
        });
        recoverPassword.setOnClickListener( v -> {
            Intent i = new Intent(MainActivity.this, ResetPassword.class);
            startActivity(i);
        });
        signUp.setOnClickListener( v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });
    }

    private boolean CheckFields(EditText etEmail, EditText etPassword) {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    public void logIn(String email, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = con.connectionDB();

            if (conn != null) {
                String sql = "SELECT * FROM \"Users\" WHERE email = ? AND password = ?";
                pstmt = conn.prepareStatement(sql);

                pstmt.setString(1, email);
                pstmt.setString(2, password);

                rs = pstmt.executeQuery();
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                if (rs.next()) {
                    builder.setMessage("You have signed in successfully").setPositiveButton("OK", (dialog, which) -> {Intent i = new Intent(MainActivity.this, MuseumsActivity.class);
                        i.putExtra("email", email);
                        startActivity(i);
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    builder.setMessage("Email or password incorrect").setPositiveButton("OK", (dialog, which) -> dialog.dismiss());;
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            } else {
                System.err.println("Database connection failed!");
            }

        } catch (Exception e) {
            System.err.println("Error in logIn: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}