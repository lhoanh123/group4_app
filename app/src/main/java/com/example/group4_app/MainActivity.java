package com.example.group4_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Kích hoạt chế độ toàn màn hình
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        // Đặt layout cho activity
        setContentView(R.layout.activity_main);

        // Tạo kết nối đến cơ sở dữ liệu SQLite
        SQLiteConnector databaseConnector = new SQLiteConnector(this);

        // Inflate layout của navigation drawer
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.activity_main, null);

        // Lấy TextView để hiển thị tên người dùng
        TextView usernameTextView = findViewById(R.id.usernameTextView);

        // Lấy Intent để lấy tên người dùng từ activity trước
        Intent intent = getIntent();
        String username = intent.getStringExtra("UserName");
        // Chuyển tên người dùng thành chữ hoa
        String capitalizedUserName = username != null ? username.toUpperCase() : "";
        // Hiển thị tên người dùng
        usernameTextView.setText(capitalizedUserName);

        // Lấy ImageView để xử lý sự kiện logout
        ImageView logoutTextView = findViewById(R.id.logoutTextView);
        // Xử lý sự kiện logout
        logoutTextView.setOnClickListener(view -> showLogoutConfirmationDialog());

        // Xử lý sự kiện thay đổi kích thước màn hình
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Lấy kích thước của các thanh hệ thống
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Đặt kích thước padding cho view
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showLogoutConfirmationDialog() {
        // Tạo dialog xác nhận logout
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Logout");
        alertDialog.setMessage("Are you sure you want to logout?");
        // Xử lý sự kiện nhấn nút Yes
        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            // Thực hiện logout logic
            Intent intent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(intent);
            finish(); // Đóng activity hiện tại để không quay lại sau khi logout
            dialogInterface.dismiss();
        });
        // Xử lý sự kiện nhấn nút No
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        // Hiển thị dialog
        alertDialog.show();
    }
}