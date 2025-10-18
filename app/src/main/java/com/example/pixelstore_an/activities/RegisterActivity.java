package com.example.pixelstore_an.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.data.DatabaseHelper;
import com.example.pixelstore_an.utils.Validator;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAge, etPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phoneStr = etPhone.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || phoneStr.isEmpty() || ageStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
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

// Validar edad
            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age < 13 || age > 120) {
                    Toast.makeText(this, "Ingrese una edad válida (13–120)", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La edad debe ser un número", Toast.LENGTH_SHORT).show();
                return;
            }

            String phone = phoneStr;


            boolean success = dbHelper.registerUser(name, email, phone, age, password);
            if (success) {
                Toast.makeText(this, "Usuario registrado con éxito ✅", Toast.LENGTH_SHORT).show();
                finish(); // cerrar pantalla
            } else {
                Toast.makeText(this, "Error: el correo ya existe ⚠️", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
