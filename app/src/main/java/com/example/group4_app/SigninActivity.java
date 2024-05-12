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

// Lớp hoạt động đăng nhập
public class SigninActivity extends AppCompatActivity {

    // Biến binding cho layout signin
    private ActivitySigninBinding binding;

    // Biến connector để kết nối với cơ sở dữ liệu SQLite
    private SQLiteConnector sqLiteConnector;

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
        sqLiteConnector = new SQLiteConnector(this);

        // Đặt sự kiện click cho nút đăng nhập
        binding.signinButton.setOnClickListener(view -> {
            // Lấy giá trị của trường tên đăng nhập và mật khẩu
            String username = binding.signinUser.getText().toString();
            String password = binding.signinPassword.getText().toString();

            // Kiểm tra xem cả hai trường đều có giá trị hay không
            if (username.isEmpty() || password.isEmpty()) {
                // Hiển thị thông báo lỗi nếu có trường nào đó trống
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            } else {
                // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu
                boolean checkCredentials = sqLiteConnector.checkUser(username, password);

                // Nếu thông tin đăng nhập hợp lệ
                if (checkCredentials) {
                    // Hiển thị thông báo thành công
                    Toast.makeText(this, "Sign in successfully", Toast.LENGTH_SHORT).show();
                    // Tạo intent để chuyển đến MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    // Đưa tên đăng nhập vào intent
                    intent.putExtra("UserName", username);
                    // Khởi động hoạt động chính
                    startActivity(intent);
                } else {
                    // Hiển thị thông báo lỗi nếu thông tin đăng nhập không hợp lệ
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
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