package com.example.group4_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.group4_app.databinding.ActivitySigninBinding;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// Lớp hoạt động đăng nhập
public class SigninActivity extends AppCompatActivity {

    // Biến binding cho layout signin
    private ActivitySigninBinding binding;

    // Biến connector để kết nối với cơ sở dữ liệu SQLite
    private MySQLConnector mySQLConnector;

    // Phương thức onCreate được gọi khi hoạt động được tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Kích hoạt chế độ toàn màn hình
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        // Inflate layout signin và set nội dung cho hoạt động
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Tạo đối tượng connector để kết nối với cơ sở dữ liệu SQLite
        mySQLConnector = new MySQLConnector();

        // Đặt sự kiện click cho nút đăng nhập
        binding.signinButton.setOnClickListener(view -> {
            String username = binding.signinUser.getText().toString();
            String password = binding.signinPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                // Execute the network operation in a separate thread
                new Thread(() -> {
                    try {
                        Future<Boolean> futureCheck = mySQLConnector.checkUserSignIn(username, password);
                        boolean checkCredentials = futureCheck.get(); // Wait and get the result

                        runOnUiThread(() -> {
                            if (checkCredentials) {
                                Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("UserName", username);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });

        // Đặt sự kiện click cho văn bản đăng ký
        binding.SignUpRedirectText.setOnClickListener(view -> {
            // Tạo intent để chuyển đến hoạt động đăng ký
            Intent intent = new Intent(this, SignupActivity.class);
            // Khởi động hoạt động đăng ký
            startActivity(intent);
        });

        // Đặt sự kiện áp dụng các khoảng cách cho layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Lấy các khoảng cách của hệ thống
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Áp dụng các khoảng cách cho layout
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}