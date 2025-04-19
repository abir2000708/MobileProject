package com.example.formationstagefv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.formationstagefv.models.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextView gotosignIn;
    private EditText fullname, email, phone, cin, address, password, confirmepasswrd;
    private Button btnsignup;
    private String inputFullname, inputEmail, inputPhone, inputCin, inputAddress, inputPassword, inputConfirmePasswrd;
    private static final String REGEX_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final String REGEX_PASSWORD = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        gotosignIn = findViewById(R.id.go_to_sign_in);
        fullname = findViewById(R.id.full_name_sign_up);
        email = findViewById(R.id.email_sign_up);
        phone = findViewById(R.id.phone_sign_up);
        cin = findViewById(R.id.cin_sign_up);
        address = findViewById(R.id.address_sign_up);
        password = findViewById(R.id.password_sign_up);
        confirmepasswrd = findViewById(R.id.confirme_password_sign_up);
        btnsignup = findViewById(R.id.btn_sign_up);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        gotosignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        btnsignup.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                    } else {
                        Toast.makeText(this, "Creation failed !", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser != null) {
            loggedUser.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendUserData();
                    Toast.makeText(this, "Registration done ! please check your email address !", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(this, SignInActivity.class));
                    firebaseAuth.signOut();
                    finish();
                } else {
                    Toast.makeText(this, "Creation failed !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        User newUser = new User(inputFullname, inputEmail, inputPhone, inputCin, inputAddress);
        databaseReference.child("Users").child("" + firebaseAuth.getUid()).setValue(newUser);
    }

    private boolean validate() {
        boolean result = false;
        inputFullname = fullname.getText().toString().trim();
        inputEmail = email.getText().toString().trim();
        inputPhone = phone.getText().toString().trim();
        inputCin = cin.getText().toString().trim();
        inputAddress = address.getText().toString().trim();
        inputPassword = password.getText().toString().trim();
        inputConfirmePasswrd = confirmepasswrd.getText().toString().trim();

        if (inputFullname.length() < 6) {
            fullname.setError("full name is invalid !");
        } else if (!isValidString(inputEmail, REGEX_EMAIL)) {
            email.setError("Email is invalid !");
        } else if (inputCin.length() != 8) {
            cin.setError("Cin is invalid !");
        } else if (inputPhone.length() != 8) {
            phone.setError("phone is invalid !");
        } else if (inputAddress.length() < 5) {
            address.setError("Address is invalid !");
        } else if (!isValidString(inputPassword, REGEX_PASSWORD)) {
            password.setError("Password is invalid !");
        } else if (!inputConfirmePasswrd.equals(inputPassword)) {
            confirmepasswrd.setError("password not match");
        } else {
            result = true;
        }

        return result;
    }

    private boolean isValidString(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher mtacher = pattern.matcher(input);
        return mtacher.matches();
    }
}


