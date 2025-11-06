package com.example.pixelstore_an.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.data.DatabaseHelper;
import com.example.pixelstore_an.models.User;
import com.example.pixelstore_an.utils.Validator;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    private static final String PREFS_NAME = "PixelStorePrefs";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);

        // 🔹 Si el usuario ya inició sesión antes, lo enviamos directo al catálogo
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedEmail = prefs.getString(KEY_EMAIL, null);
        if (savedEmail != null) {
            User savedUser = dbHelper.getUserByEmail(savedEmail);
            if (savedUser != null) {
                goToCatalog(savedUser);
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 🔹 Validaciones básicas
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Validator.isValidEmail(email)) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Validator.isValidPassword(password)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔹 Verificar credenciales
        if (dbHelper.checkUser(email, password)) {
            Toast.makeText(this, "Inicio de sesión exitoso 🎮", Toast.LENGTH_SHORT).show();

            // 🔹 Guardar correo para mantener la sesión
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putString(KEY_EMAIL, email).apply();

            // 🔹 Obtener usuario completo y enviarlo
            User user = dbHelper.getUserByEmail(email);
            goToCatalog(user);
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas ❌", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCatalog(User user) {
        Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
