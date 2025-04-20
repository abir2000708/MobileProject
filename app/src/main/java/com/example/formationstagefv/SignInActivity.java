package com.example.formationstagefv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private TextView goToSignUp, goToForgotPassword;
    private Button btnSignIn;
    private EditText emailSignIn, passwordSignIn;
    private String inputEmailSignIn;
    private String inputPasswordSignIn;
    private static final String REGEX_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final String REGEX_PASSWORD = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        goToSignUp = findViewById(R.id.go_to_sign_up);
        goToForgotPassword = findViewById(R.id.go_to_forgot_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        emailSignIn = findViewById(R.id.email_sign_in);
        passwordSignIn = findViewById(R.id.password_sign_in);
        rememberMe = findViewById(R.id.remember_me);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean rememberIsChecked = sharedPreferences.getBoolean("rememberMe",false);
        if (rememberIsChecked){
         startActivity( new Intent(this,ProfileActivity.class));
        }

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                editor.putBoolean("rememberMe", true);
                editor.apply();
            } else {
                editor.putBoolean("rememberMe", false);
                editor.apply();
            }

        });


        goToForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPassword.class));
        });

        goToSignUp.setOnClickListener(V -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        btnSignIn.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(inputEmailSignIn, inputPasswordSignIn).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        checkEmailVerification();

                    } else {
                        Toast.makeText(this, "Sign in failed ! ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser loggedUser = firebaseAuth.getCurrentUser();
        if (loggedUser != null) {
            if (loggedUser.isEmailVerified()) {
                startActivity(new Intent(this,ProfileActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please verify your email ", Toast.LENGTH_SHORT).show();
                loggedUser.sendEmailVerification();
                firebaseAuth.signOut();
                progressDialog.dismiss();
            }
        }
    }

    private boolean validate() {
        boolean result = false;
        inputEmailSignIn = emailSignIn.getText().toString().trim();
        inputPasswordSignIn = passwordSignIn.getText().toString().trim();

        if (!isValidString(inputEmailSignIn, REGEX_EMAIL)) {
            emailSignIn.setError("Email is invalid !");
        } else if (!isValidString(inputPasswordSignIn, REGEX_PASSWORD)) {
            passwordSignIn.setError("Password is invalid !");
        } else {
            result = true;
        }
        return result;
    }


    private boolean isValidString(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher mtacher = pattern.matcher(input);
        ;
        return mtacher.matches();


    }


}
