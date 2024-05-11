package com.example.group4_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.group4_app.SQLiteConnector;
import com.example.group4_app.User;
import com.example.group4_app.databinding.ActivitySignupBinding;
import android.view.Window;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private SQLiteConnector sqLiteConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sqLiteConnector = new SQLiteConnector(this);

        binding.signupButton.setOnClickListener(v -> {
            String username = binding.signupUser.getText().toString();
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPassword)) {
                    if (!sqLiteConnector.checkUser(email)) {
                        User user = new User();
                        user.setName(username);
                        user.setEmail(email);
                        user.setPassword(password);
                        sqLiteConnector.addUser(user);

                        Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "User already exists. Please sign in", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.SignInRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}
