package com.example.pixelstore_an.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.data.DatabaseHelper;
import com.example.pixelstore_an.utils.Validator;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!Validator.isValidEmail(email)) {
                Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Validator.isValidPassword(password)) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(this, "Inicio de sesión exitoso 🎮", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, CatalogActivity.class);
                intent.putExtra("email", email); // enviar correo del usuario logueado
                startActivity(intent); // Evita volver atrás al splash o login
            } else {
                Toast.makeText(this, "Credenciales incorrectas ❌", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }
}
