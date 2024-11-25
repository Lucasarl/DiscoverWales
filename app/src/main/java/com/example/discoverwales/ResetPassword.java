package com.example.discoverwales;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPassword extends AppCompatActivity {
    private static connectionPG con=new connectionPG();
    boolean fieldsChecked = false;
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.recover_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton backArrow=findViewById(R.id.back);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener( v -> {
            Intent i = new Intent(ResetPassword.this, MainActivity.class);
            startActivity(i);
        });
        Button resetPassword = findViewById(R.id.resetPassword);
        EditText etEmail = findViewById(R.id.email);
        ImageButton clearEmail=findViewById((R.id.clearEmail));

        clearEmail.setOnClickListener( v -> {
            etEmail.setText("");
        });

        resetPassword.setOnClickListener(v -> {
            fieldsChecked = CheckFields(etEmail);
            boolean emailExists=false;
            if (fieldsChecked){
                emailExists=checkEmailRegistered(etEmail.getText().toString());
            }
            if(emailExists&&fieldsChecked){
                sendEmail(etEmail);
                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPassword.this);
                builder.setMessage("You have been sent an email to reset your password.");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok",(DialogInterface.OnClickListener)(dialog, which)->{
                    Intent i = new Intent(ResetPassword.this, IntroducePIN.class);
                    i.putExtra("email", etEmail.getText().toString());
                    startActivity(i);
                });
                AlertDialog successfulSignUp= builder.create();
                successfulSignUp.show();
                //Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                //startActivity(i);
            }

        });


    }

    protected void sendEmail(EditText etEmail) {
        String mail = etEmail.getText().toString();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000);
        conn = con.connectionDB();
        String name="SELECT \"firstName\" FROM \"Users\" WHERE email = ?";
        String firstName = null;
        try{
        pstmt = conn.prepareStatement(name);
        pstmt.setString(1,mail);
        rs = pstmt.executeQuery();
        rs.next();
        firstName=rs.getString("firstName");
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,"PIN for password recovery", "Dear "+firstName+",\n Please introduce the following PIN in the discovery wales app to reset your account's password: "+pin);
        javaMailAPI.execute();
        String query = "UPDATE \"Users\" SET \"recoverPassword\" = ? WHERE email = ?";
        pstmt = conn.prepareStatement(query);
        pstmt.setString(1, String.valueOf(pin)); // New password
            pstmt.setString(2, mail);// setString(2, email);
            rs = pstmt.executeQuery();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    private boolean CheckFields(EditText etEmail) {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        } else if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Incorrect email format");
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkEmailRegistered(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try{
            conn=con.connectionDB();
            if (conn != null) {
                String checkEmailSql = "SELECT COUNT(*) FROM \"Users\" WHERE email = ?";
                pstmt = conn.prepareStatement(checkEmailSql);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
                builder.setCancelable(false)
                        .setMessage("Email isn't registered.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
            return false;} catch (Exception e) {
            System.err.println("Database fail");
            return false;
        }
    }
}