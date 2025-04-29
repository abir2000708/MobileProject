package com.example.formationstagefv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity   {
    private EditText fullName ,email, phoneNumber, cin, address;
    private Button btnEditProfile, btnLogOut ;
     private FirebaseAuth firebaseAuth;
     private FirebaseDatabase firebaseDatabase;
     private DatabaseReference databaseReference;
     private FirebaseUser loggedUser ;

     private ImageView menuProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fullName= findViewById(R.id.full_name_profile);
        email= findViewById(R.id.email_profile);
        phoneNumber= findViewById(R.id.phone_profile);
        cin= findViewById(R.id.cin_profile);
        address= findViewById(R.id.address_profile);
        btnEditProfile= findViewById(R.id.btn_edit_profile);
        btnLogOut= findViewById(R.id.btn_log_out);
        menuProfile = findViewById(R.id.menu_profile);




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loggedUser = firebaseAuth.getCurrentUser();
        databaseReference =firebaseDatabase.getReference().child("Users").child(""+loggedUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             String fullNameString = snapshot.child("fullName").getValue().toString();
             String emailString= snapshot.child("email").getValue().toString();
             String phoneNumberString = snapshot.child("phoneNumber").getValue().toString();
             String cinString = snapshot.child("cin").getValue().toString();
             String  addressString = snapshot.child("address").getValue().toString();

             fullName.setText(fullNameString);
             email.setText(emailString);
             phoneNumber.setText(phoneNumberString);
             cin.setText(cinString);
             address.setText(addressString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();

            }
        });

        btnLogOut.setOnClickListener(v ->{
            SharedPreferences sharedPreferences = getSharedPreferences("checkBox", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", false);
            editor.apply();
            firebaseAuth.signOut();
            startActivity(new Intent(this,SignInActivity.class));
            Toast.makeText(this, "Log out successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnEditProfile.setOnClickListener(v ->{
             String editedFullName= fullName.getText().toString();
             String editedPhone= phoneNumber.getText().toString();
             String editedCin= cin.getText().toString();
             String editedAddress= address.getText().toString();


             databaseReference.child("fullName").setValue(editedFullName);
             databaseReference.child("phoneNumber").setValue(editedPhone);
             databaseReference.child("cin").setValue(editedCin);
             databaseReference.child("address").setValue(editedAddress);
            Toast.makeText(this, "Your data has been changed successfully  ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,ProfileActivity.class));

        });





    }

    private void navigationDrawer() {
    }


}