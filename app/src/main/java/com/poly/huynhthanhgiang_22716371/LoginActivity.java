package com.poly.huynhthanhgiang_22716371;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.poly.huynhthanhgiang_22716371.entity.LoginRequest;
import com.poly.huynhthanhgiang_22716371.entity.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnLogin;
    DBHelper dbHelper;
    //DatabaseHelper dbsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new DBHelper(this);
        //dbsHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            //loginUser(username, password);

            if (dbHelper.checkLogin(username, password)) {
                String fullName = dbHelper.getFullName(username);
                Toast.makeText(this, "Xin chào " + fullName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, ProductListActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserSession(int userId, String username) {
        getSharedPreferences("user_session", MODE_PRIVATE)
                .edit()
                .putInt("user_id", userId)
                .putString("username", username)
                .apply();
    }
}

