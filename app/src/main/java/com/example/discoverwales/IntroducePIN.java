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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class IntroducePIN extends AppCompatActivity {
    private static connectionPG con=new connectionPG();
    boolean fieldsChecked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.insert_pin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton backArrow=findViewById(R.id.back);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener( v -> {
            Intent i = new Intent(IntroducePIN.this, ResetPassword.class);
            startActivity(i);
        });
        ImageButton clearPin=findViewById((R.id.clearPin));
        EditText etPIN = findViewById(R.id.pin);

        clearPin.setOnClickListener( v -> {
            etPIN.setText("");
        });

        Button changeCode = findViewById(R.id.changeCode);
        Button insertPin = findViewById(R.id.insertPIN);
        Bundle extras = getIntent().getExtras();

        insertPin.setOnClickListener( v -> {
            fieldsChecked = CheckFields(etPIN);
            boolean correctPIN=false;
            if (fieldsChecked){
                correctPIN=checkPin(etPIN, extras.getString("email"));
            }
            if(correctPIN) {
                Intent i = new Intent(IntroducePIN.this, ChangePassword.class);
                i.putExtra("email", extras.getString("email"));
                i.putExtra("activity", "pin");
                startActivity(i);
            } else {
                AlertDialog.Builder builder=new AlertDialog.Builder(IntroducePIN.this);
                builder.setMessage("The PIN you introduced is incorrect").setPositiveButton("OK", (dialog, which) -> dialog.dismiss());;
                AlertDialog successfulSignUp= builder.create();
                successfulSignUp.show();
            }
        });
        changeCode.setOnClickListener( v -> {
            sendEmail(extras.getString("email"));
            AlertDialog.Builder builder=new AlertDialog.Builder(IntroducePIN.this);
            builder.setMessage("You have been sent a new PIN code to your email address.").setPositiveButton("OK", (dialog, which) -> dialog.dismiss());;
            AlertDialog successfulSignUp= builder.create();
            successfulSignUp.show();
        });

    }

    protected void sendEmail(String mail) {
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
            int r=0;
            r = pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    private boolean CheckFields(EditText etPassword) {
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("PIN is required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkPin (EditText etPin, String mail) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        conn = con.connectionDB();
        String name="SELECT \"recoverPassword\" FROM \"Users\" WHERE email = ?";
        String pin = null;
        try{
            pstmt = conn.prepareStatement(name);
            pstmt.setString(1,mail);
            rs = pstmt.executeQuery();
            rs.next();
            pin=rs.getString("recoverPassword");
            return pin.equals(etPin.getText().toString());
        }
        catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
}