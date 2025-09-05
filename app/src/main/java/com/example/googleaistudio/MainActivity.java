package com.example.googleaistudio; // Make sure this matches your package name

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView chatRecyclerView;
    private EditText promptInput;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This links the Java file to our XML layout file.
        setContentView(R.layout.activity_main);

        // --- Initialize UI Components ---
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        promptInput = findViewById(R.id.prompt_input);
        sendButton = findViewById(R.id.send_button);

        // --- Setup Toolbar ---
        setSupportActionBar(toolbar);

        // --- Setup Navigation Drawer ---
        // This creates the "hamburger" icon and handles opening/closing the drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // --- Setup Navigation Item Click Listener ---
        // This is where you'll handle clicks on "Chat", "Settings", etc.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_chat) {
                    Toast.makeText(MainActivity.this, "Chat Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_settings) {
                    // Start the SettingsActivity
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }

                // Close the navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // --- Setup Send Button Click Listener ---
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = promptInput.getText().toString();
                if (!prompt.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Sending: " + prompt, Toast.LENGTH_SHORT).show();
                    // We will call the AI here in the next step
                    promptInput.setText(""); // Clear the input field
                }
            }
        });
    }

    // This method handles the back button press to close the drawer if it's open.
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}