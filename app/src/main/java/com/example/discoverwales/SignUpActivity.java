package com.example.discoverwales;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    String imageId=null;
    StorageReference storageReference;
    boolean fieldsChecked = false;
    Button uploadImage;
    private static connectionPG con=new connectionPG();
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[!@#$%^&*.()-+])(?=\\S+$).{9,}$";

    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private static final String NAME_PATTERN =
            "^[a-zA-Z\\s]+$";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final Pattern namePattern = Pattern.compile(NAME_PATTERN);
    ImageView imageView;
    Uri image;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
           if(result.getResultCode()==RESULT_OK) {
               System.out.println("Ok");
               if(result.getData()!=null){
                   image=result.getData().getData();
                   Glide.with(getApplicationContext()).load(image).into(imageView);
                   uploadImage.setEnabled(true);
                   uploadImage(image);
               }
           } else {
               Toast.makeText(SignUpActivity.this,"Please select an image", Toast.LENGTH_SHORT).show();
           }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton backArrow=findViewById(R.id.back);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener( v -> {
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
        });
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.profile_pic);
        ImageButton clearEmail=findViewById((R.id.clearEmail));
        ImageButton clearPassword=findViewById((R.id.clearPassword));
        ImageButton clearFirstName=findViewById((R.id.clearFirstName));
        ImageButton clearLastName=findViewById((R.id.clearLastName));
        ImageButton clearRepeatPassword=findViewById((R.id.clearRepeatPassword));
        TextView logIn = findViewById(R.id.logIn);
        Button signUp = findViewById(R.id.signUp);
        EditText etEmail = findViewById(R.id.email);
        EditText etPassword = findViewById(R.id.password);
        EditText etFirstName = findViewById(R.id.firstName);
        EditText etLastName = findViewById(R.id.lastName);
        EditText etRepeatPassword = findViewById(R.id.repeatPassword);
        clearEmail.setOnClickListener( v -> {
            etEmail.setText("");
        });
        clearPassword.setOnClickListener( v -> {
            etPassword.setText("");
        });
        clearFirstName.setOnClickListener( v -> {
            etFirstName.setText("");
        });
        clearLastName.setOnClickListener( v -> {
            etLastName.setText("");
        });
        clearRepeatPassword.setOnClickListener( v -> {
            etRepeatPassword.setText("");
        });

        FirebaseApp.initializeApp(SignUpActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();
        uploadImage= findViewById(R.id.choosePfpButton);
        System.out.println(storageReference.getBucket());

        uploadImage.setOnClickListener( v -> {
            //StorageReference imgRef = storageRef.child("IMG.jpg");
            //StorageReference locationImgRef = storageRef.child("C:/Users/USER/OneDrive/Imágenes");
            Intent intent= new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
            //uploadImage(image);

        });

        signUp.setOnClickListener(v -> {
            fieldsChecked = CheckFields(etEmail,etPassword,etFirstName,etLastName,etRepeatPassword);
            boolean signedUp=false;
            if (fieldsChecked){
                signedUp=signUp(etEmail.getText().toString(), etPassword.getText().toString(), etFirstName.getText().toString(), etLastName.getText().toString(),imageId);
            }
            if(signedUp&&fieldsChecked){
                //uploadImage(image);
                AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("You have signed up successfully");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok",(DialogInterface.OnClickListener)(dialog,which)->{
                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(i);
                });
                AlertDialog successfulSignUp= builder.create();
                successfulSignUp.show();
                //Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                //startActivity(i);
                }

        });

        logIn.setOnClickListener( v -> {
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
        });
    }

    private boolean CheckFields(EditText etEmail, EditText etPassword, EditText etFirstName, EditText etLastName, EditText etRepeatPassword) {
        if (etFirstName.getText().toString().isEmpty()) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return false;} else if (!isValidName(etFirstName.getText().toString())) {
            etFirstName.setError("Names must not contain numbers or special characters");
            etFirstName.requestFocus();
            return false;
        }

        if (etLastName.getText().toString().isEmpty()) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return false;} else if (!isValidName(etLastName.getText().toString())) {
            etLastName.setError("Names must not contain numbers or special characters");
            etLastName.requestFocus();
            return false;
        }

        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        } else if (!isValidEmail(etEmail.getText().toString())) {
            etEmail.setError("Incorrect email format");
            etEmail.requestFocus();
            return false;
        }

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

    private void uploadImage(Uri image) {
        imageId=UUID.randomUUID().toString();
        StorageReference reference= storageReference.child("images/"+ imageId);
        reference.putFile(image);
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        Matcher matcher = namePattern.matcher(name);
        return matcher.matches();
    }

    public boolean signUp(String email, String password, String firstName, String lastName, String profilePicture) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = con.connectionDB();

            if (conn != null) {
                String checkEmailSql = "SELECT COUNT(*) FROM \"Users\" WHERE email = ?";
                pstmt = conn.prepareStatement(checkEmailSql);
                pstmt.setString(1, email);
                rs = pstmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setCancelable(false)
                            .setMessage("Email is already registered.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }

                String insertSql = "INSERT INTO \"Users\" (email, password, \"firstName\", \"lastName\", \"profilePicture\") VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                pstmt.setString(3, firstName);
                pstmt.setString(4,lastName);
                pstmt.setString(5, profilePicture);

                int rowsInserted = pstmt.executeUpdate();

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setCancelable(false);

                if (rowsInserted == 0) {
                    builder.setMessage("Failed to create account. Please try again.")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
                return true;
            } else {
                System.err.println("Database connection failed!");
            }

        } catch (Exception e) {
            System.err.println("Error in signUp: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return false;
    }


}
