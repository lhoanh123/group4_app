package com.example.group4_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private SQLiteConnector databaseConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate SQLiteConnector
        databaseConnector = new SQLiteConnector(this);

        // Inflate the navigation drawer layout
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_main, null);

        TextView usernameTextView = findViewById(R.id.usernameTextView);

        Intent intent = getIntent();
        String username = intent.getStringExtra("UserName");
        String capitalizedUserName = username != null ? username.toUpperCase() : "";
        usernameTextView.setText(capitalizedUserName);

        // Set OnClickListener to handle logout confirmation
        ImageView logoutTextView = findViewById(R.id.logoutTextView);
        logoutTextView.setOnClickListener(view -> showLogoutConfirmationDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure you want to logout?");
        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            // Implement your logout logic here
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
            finish(); // Finish current activity to prevent going back to it after logging out
            dialogInterface.dismiss();
        });
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }
}
