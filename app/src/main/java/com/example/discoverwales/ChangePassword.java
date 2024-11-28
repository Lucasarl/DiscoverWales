package com.example.discoverwales;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    boolean fieldsChecked = false;
    private static connectionPG con=new connectionPG();
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[!@#$%^&*.()-+])(?=\\S+$).{9,}$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle extras = getIntent().getExtras();
        ImageButton backArrow=findViewById(R.id.back);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener( v -> {
            System.out.println("HI "+extras.getString("activity"));
            if(extras.getString("activity").equals("pin")){
            Intent i = new Intent(ChangePassword.this, ResetPassword.class);
            startActivity(i);} else if (extras.getString("activity").equals("settings")) {
                Intent i = new Intent(ChangePassword.this, ProfileSettings.class);
                String email=extras.getString("email");
                i.putExtra("email",email);
                i.putExtra("activity1", extras.getString("activity1"));
                startActivity(i);
            }
        });

        ImageButton clearPassword = findViewById(R.id.clearPassword);
        ImageButton clearRepeatPassword=findViewById(R.id.clearRepeatPassword);
        EditText etPassword = findViewById(R.id.password);
        EditText etRepeatPassword = findViewById(R.id.repeatPassword);

        clearPassword.setOnClickListener( v -> {
            etPassword.setText("");
        });

        clearRepeatPassword.setOnClickListener( v -> {
            etRepeatPassword.setText("");
        });

        Button changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(v->{
            if(checkFields(etPassword,etRepeatPassword)) {
                Connection conn = null;
                PreparedStatement pstmt = null;
                //ResultSet rs = null;
                int rs= 0;
                conn = con.connectionDB();
                try{
                    String query = "UPDATE \"Users\" SET \"password\" = ? WHERE email = ?";
                    pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, etPassword.getText().toString()); // New password
                    pstmt.setString(2, extras.getString("email"));// setString(2, email);
                    rs = pstmt.executeUpdate();
                    System.out.println("About to show the dialog");
                    AlertDialog.Builder builder=new AlertDialog.Builder(ChangePassword.this);
                    builder.setMessage("You have successfully changed your account's password").setPositiveButton("OK", (dialog, which) -> {
                        if(extras.getString("activity").equals("pin")){
                            Intent i = new Intent(ChangePassword.this, MainActivity.class);
                            startActivity(i);} else if (extras.getString("activity").equals("settings")) {
                            Intent i = new Intent(ChangePassword.this, ProfileSettings.class);
                            String email=extras.getString("email");
                            i.putExtra("email",email);
                            i.putExtra("activity1", extras.getString("activity1"));
                            startActivity(i);
                        }});;
                    AlertDialog successfulChange= builder.create();
                    successfulChange.show();
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        });
    }

    private boolean checkFields(EditText etPassword, EditText etRepeatPassword) {
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        } else if (!isValidPassword(etPassword.getText().toString())) {
            etPassword.setError("Password must at least 9 characters, a number, and a special character");
            etPassword.requestFocus();
            return false;
        }

        if (etRepeatPassword.getText().toString().isEmpty()) {
            etRepeatPassword.setError("Please repeat the password");
            etRepeatPassword.requestFocus();
            return false;} else if (!etPassword.getText().toString().equals(etRepeatPassword.getText().toString())) {
            etPassword.setError("Passwords do not match");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }
}