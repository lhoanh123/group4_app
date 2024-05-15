package com.example.group4_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group4_app.databinding.ActivitySignupBinding;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SignupActivity extends AppCompatActivity {

    // Biến binding để truy cập các thành phần trong layout
    private ActivitySignupBinding binding;

    // Biến connector để thao tác với cơ sở dữ liệu SQLite
    private MySQLConnector mySQLConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Kích hoạt chế độ toàn màn hình
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        // Tạo binding để truy cập các thành phần trong layout
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Tạo connector để thao tác với cơ sở dữ liệu SQLite
        mySQLConnector = new MySQLConnector();

        // Đặt sự kiện click cho nút đăng ký
        binding.signupButton.setOnClickListener(v -> {
            String username = binding.signupUser.getText().toString();
            String email = binding.signupEmail.getText().toString();
            String password = binding.signupPassword.getText().toString();
            String confirmPassword = binding.signupConfirm.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPassword)) {
                    try {
                        Future<Boolean> futureCheck = mySQLConnector.checkUserSignUp(email);
                        boolean userExists = futureCheck.get();

                        if (!userExists) {
                            User user = new User();
                            user.setName(username);
                            user.setEmail(email);
                            user.setPassword(password);

                            mySQLConnector.addUser(user);

                            Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "User already exists. Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Đặt sự kiện click cho nút chuyển sang màn hình đăng nhập
        binding.SignInRedirectText.setOnClickListener(v -> {
            // Chuyển sang màn hình đăng nhập
            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
            startActivity(intent);
        });

        // Đặt sự kiện áp dụng các khoảng cách cho màn hình
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Lấy các khoảng cách của màn hình
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Áp dụng các khoảng cách cho màn hình
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}