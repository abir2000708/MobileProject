package com.example.formationstagefv;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menu;
    private EditText deviceName, deviceValue;
    private Button btnAddDevice;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ListView listDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        menu = findViewById(R.id.menu_home);
        deviceName = findViewById(R.id.device_name);
        deviceValue = findViewById(R.id.device_value);
        btnAddDevice = findViewById(R.id.btn_add_device);
        listDevices = findViewById(R.id.list_devices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Devices");

        ArrayList<String> deviceArrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, deviceArrayList);
        listDevices.setAdapter(arrayAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot devicesSnapshot) {
                deviceArrayList.clear();
                for (DataSnapshot deviceSnapshot : devicesSnapshot.getChildren()) {
                    String nameD =deviceSnapshot.child("name").getValue().toString();
                    String valueD = deviceSnapshot.child("value").getValue().toString();
                    deviceArrayList.add(nameD+" : "+valueD);
                }
                 arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        });

        btnAddDevice.setOnClickListener(v -> {
            HashMap<String, String> deviceMap = new HashMap<>();
            deviceMap.put("name", deviceName.getText().toString());
            deviceMap.put("value", deviceValue.getText().toString());
            databaseReference.push().setValue(deviceMap);

            deviceName.setText("");
            deviceValue.setText("");
            deviceName.clearFocus();
            deviceValue.clearFocus();
            Toast.makeText(this, "New device added successfully", Toast.LENGTH_SHORT).show();
        });


        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_item) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (item.getItemId() == R.id.profile_item) {
                startActivity(new Intent(this, ProfileActivity.class));
            }
            return true;
        });


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.home_item);
        navigationView.setNavigationItemSelectedListener(this);


        menu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}