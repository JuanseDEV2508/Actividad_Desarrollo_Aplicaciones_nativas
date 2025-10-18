package com.example.pixelstore_an.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.data.DatabaseHelper;
import com.example.pixelstore_an.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private EditText etName, etEmail, etPhone, etAge;
    private Button btnSaveProfile, btnBackCatalog;
    private DatabaseHelper dbHelper;
    private String userEmail; // Email del usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializar vistas
        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAge = findViewById(R.id.etAge);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnBackCatalog = findViewById(R.id.btnBackCatalog);

        dbHelper = new DatabaseHelper(this);

        // Recibir el correo del Intent (desde LoginActivity)
        userEmail = getIntent().getStringExtra("email");

        if (userEmail != null) {
            loadUserData(userEmail);
        } else {
            Toast.makeText(this, "Error: No se encontró el usuario", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Guardar cambios
        btnSaveProfile.setOnClickListener(v -> saveProfileChanges());

        // Volver al catálogo
        btnBackCatalog.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CatalogActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserData(String email) {
        User user = dbHelper.getUserByEmail(email);
        if (user != null) {
            etName.setText(user.getName());
            etEmail.setText(user.getEmail());
            etPhone.setText(String.valueOf(user.getPhone()));
            etAge.setText(String.valueOf(user.getAge()));

            // Cargar imagen si existe (por ahora sólo se usa drawable por defecto)
            imgProfile.setImageResource(user.getImage() != 0 ? user.getImage() : R.drawable.default_avatar);
        } else {
            Toast.makeText(this, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfileChanges() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean updated = dbHelper.updateUser(userEmail, name, phone, age);

        if (updated) {
            Toast.makeText(this, "Perfil actualizado con éxito ✅", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el perfil ❌", Toast.LENGTH_SHORT).show();
        }
    }
}
