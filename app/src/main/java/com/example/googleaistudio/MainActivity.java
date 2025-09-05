package com.example.googleaistudio; // Make sure this matches your package name

// --- IMPORT STATEMENTS ---
// Make sure you have all of these
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.api.services.drive.DriveScopes;


public class MainActivity extends AppCompatActivity {

    // --- CONSTANTS ---
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    // --- UI COMPONENTS ---
    private DrawerLayout drawerLayout;
    // ... (other UI components)

    // --- GOOGLE SIGN IN ---
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mSignedInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Find Views by ID ---
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        // ... (find other views if you haven't declared them at the top)

        setSupportActionBar(toolbar);
        setupNavigationDrawer(toolbar);

        // --- Configure Google Sign-In ---
        // We request the user's basic profile and the permission to manage files they create with our app.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        mSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(mSignedInAccount);
    }

    private void setupNavigationDrawer(Toolbar toolbar) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_chat) {
                // Just close the drawer, we are already here
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (id == R.id.nav_sign_in) {
                signIn();
            } else if (id == R.id.nav_sign_out) {
                signOut();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // --- GOOGLE SIGN-IN METHODS ---

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
            updateUI(null);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            mSignedInAccount = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            updateUI(mSignedInAccount);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Sign in failed. Please try again.", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        if (account != null) {
            // User is signed in
            navMenu.findItem(R.id.nav_sign_in).setVisible(false);
            navMenu.findItem(R.id.nav_sign_out).setVisible(true);
            navHeaderName.setText(account.getDisplayName());
            navHeaderEmail.setText(account.getEmail());
        } else {
            // User is signed out
            navMenu.findItem(R.id.nav_sign_in).setVisible(true);
            navMenu.findItem(R.id.nav_sign_out).setVisible(false);
            navHeaderName.setText("Guest");
            navHeaderEmail.setText("Sign in to sync your chats");
        }
    }

    // --- OTHER METHODS ---

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}