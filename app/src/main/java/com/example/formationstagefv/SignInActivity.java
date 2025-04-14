package com.example.formationstagefv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {
    private TextView GoToSignUp, GoToForgotPassword;
    private Button  BtnSignIn ;
    private EditText EmailSignIn , PasswordSignIn  ;
    private String inputEmailSignIn;
    private String inputPasswordSignIn;
    private static final   String REGEX_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final String REGEX_PASSWORD = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$";

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

        GoToSignUp = findViewById(R.id.go_to_sign_up);
        GoToForgotPassword = findViewById(R.id.go_to_forgot_password);
        BtnSignIn= findViewById(R.id.btn_sign_in);
        EmailSignIn = findViewById(R.id.email_sign_in);
        PasswordSignIn = findViewById(R.id.password_sign_in);


        GoToForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, forgotPassword.class));


        });


        GoToSignUp.setOnClickListener(V -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        BtnSignIn.setOnClickListener(v ->{
            if (validate()) {
                Toast.makeText(this, "hello welcome back", Toast.LENGTH_SHORT).show();

            }
        });






    }

    private boolean validate() {
        boolean result = false;
        inputEmailSignIn = EmailSignIn.getText().toString().trim();
        inputPasswordSignIn= PasswordSignIn.getText().toString().trim();

        if (!isValidString(inputEmailSignIn , REGEX_EMAIL)) {
            EmailSignIn.setError("Email is invalid !");
        }else if (!isValidString(inputPasswordSignIn , REGEX_PASSWORD)){
            PasswordSignIn.setError("Password is invalid !");
        }else {
            result = true;
        }


        return result;
    }






    private boolean isValidString(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher mtacher = pattern.matcher(input);;
        return mtacher.matches();




    }



}
