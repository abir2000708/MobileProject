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

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private TextView GoToSignIn;
    private EditText FullName, Email, Phone, Cin, Address, Password, ConfirmePasswrd;
    private Button BtnSignUp;
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

        GoToSignIn = findViewById(R.id.go_to_sign_in);
        FullName = findViewById(R.id.full_name_sign_up);
        Email = findViewById(R.id.email_sign_up);
        Phone = findViewById(R.id.phone_sign_up);
        Cin = findViewById(R.id.cin_sign_up);
        Address = findViewById(R.id.address_sign_up);
        Password = findViewById(R.id.password_sign_up);
        ConfirmePasswrd = findViewById(R.id.confirme_password_sign_up);
        BtnSignUp = findViewById(R.id.btn_sign_up);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        GoToSignIn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        BtnSignUp.setOnClickListener(v -> {
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
         if (loggedUser != null){
             loggedUser.sendEmailVerification().addOnCompleteListener(task -> {
              if (task.isSuccessful()){
                  Toast.makeText(this, "Registration done ! please check your email address !", Toast.LENGTH_LONG).show();
                  progressDialog.dismiss();
                  startActivity(new Intent(this,SignInActivity.class));
                  firebaseAuth.signOut();
                  finish();
              }else {
                  Toast.makeText(this, "Creation failed !", Toast.LENGTH_SHORT).show();
                  progressDialog.dismiss();
              }
             });
         }
    }

    private boolean validate() {
        boolean result = false;
        inputFullname = FullName.getText().toString().trim();
        inputEmail = Email.getText().toString().trim();
        inputPhone = Phone.getText().toString().trim();
        inputCin = Cin.getText().toString().trim();
        inputAddress = Address.getText().toString().trim();
        inputPassword = Password.getText().toString().trim();
        inputConfirmePasswrd = ConfirmePasswrd.getText().toString().trim();

        if (inputFullname.length() < 6) {
            FullName.setError("full name is invalid !");
        } else if (!isValidString(inputEmail, REGEX_EMAIL)) {
            Email.setError("Email is invalid !");
        } else if (inputCin.length() != 8) {
            Cin.setError("Cin is invalid !");
        } else if (inputPhone.length() != 8) {
            Phone.setError("phone is invalid !");
        } else if (inputAddress.length() < 5) {
            Address.setError("Address is invalid !");
        } else if (!isValidString(inputPassword, REGEX_PASSWORD)) {
            Password.setError("Password is invalid !");
        } else if (!inputConfirmePasswrd.equals(inputPassword)) {
            ConfirmePasswrd.setError("password not match");
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


