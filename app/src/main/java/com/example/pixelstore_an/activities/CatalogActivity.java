package com.example.pixelstore_an.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.adapters.ProductAdapter;
import com.example.pixelstore_an.models.Product;
import com.example.pixelstore_an.models.User;
import com.google.android.material.navigation.NavigationView;
import com.example.pixelstore_an.data.ProductDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private ImageButton btnMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;
    private User currentUser; // ahora guardamos el usuario completo
    private ProductDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        dbHelper = new ProductDatabaseHelper(this);

        // --- 🔹 Recuperar el objeto User que llega desde el login ---
        currentUser = (User) getIntent().getSerializableExtra("user");

        // --- Inicializar vistas ---
        recyclerView = findViewById(R.id.recyclerProducts);
        etSearch = findViewById(R.id.etSearch);
        btnMenu = findViewById(R.id.btnMenu);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // --- 🔹 CONFIGURAR EL HEADER DEL MENÚ LATERAL ---
        android.view.View headerView = navigationView.getHeaderView(0);
        android.widget.ImageView imgProfile = headerView.findViewById(R.id.imgProfile);
        android.widget.TextView tvName = headerView.findViewById(R.id.tvName);
        android.widget.TextView tvEmail = headerView.findViewById(R.id.tvEmail);

        if (currentUser != null) {
            tvEmail.setText(currentUser.getEmail());
            tvName.setText(currentUser.getName());
        } else {
            tvEmail.setText("correo@ejemplo.com");
            tvName.setText("Usuario Gamer");
        }
        imgProfile.setImageResource(R.drawable.default_avatar);

        // --- Botón para abrir el menú lateral ---
        btnMenu.setOnClickListener(v -> drawerLayout.open());

// --- Opciones del menú lateral ---
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);

            } else if (id == R.id.nav_products) {
                Intent intent = new Intent(this, ProductManagerActivity.class);
                startActivity(intent);

            } else if (id == R.id.nav_logout) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            drawerLayout.close();
            return true;
        });


        // --- Lista de productos de ejemplo ---
        productList = dbHelper.getAllProducts(); // 🔹 Cargar desde la base de datos
        filteredList = new ArrayList<>(productList);
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        filteredList = new ArrayList<>(productList);
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // --- Filtro de búsqueda ---
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void filterProducts(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(productList);
        } else {
            filteredList.addAll(productList.stream()
                    .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper != null) {
            productList = dbHelper.getAllProducts();
            filteredList.clear();
            filteredList.addAll(productList);
            adapter.notifyDataSetChanged();
        }
    }


}
