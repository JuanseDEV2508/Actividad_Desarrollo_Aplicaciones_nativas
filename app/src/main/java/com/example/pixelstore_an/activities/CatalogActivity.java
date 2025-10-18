package com.example.pixelstore_an.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.adapters.ProductAdapter;
import com.example.pixelstore_an.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private Button btnGoProfile;
    private ProductAdapter adapter;
    private List<Product> productList;
    private List<Product> filteredList;
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        userEmail = getIntent().getStringExtra("email");
        recyclerView = findViewById(R.id.recyclerProducts);
        etSearch = findViewById(R.id.etSearch);
        btnGoProfile = findViewById(R.id.btnGoProfile);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔥 Productos de ejemplo
        productList = new ArrayList<>();
        productList.add(new Product("Mouse Gamer RGB", "Accesorios", 15999, R.drawable.mouse));
        productList.add(new Product("Teclado Mecánico Redragon", "Accesorios", 24999, R.drawable.keyboard));
        productList.add(new Product("Monitor 144Hz", "Pantallas", 89999, R.drawable.monitor));
        productList.add(new Product("Audífonos HyperX", "Audio", 34999, R.drawable.headset));
        productList.add(new Product("Silla Ergonómica", "Muebles", 59999, R.drawable.chair));

        filteredList = new ArrayList<>(productList);
        adapter = new ProductAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // 🔍 Buscar productos
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 👤 Ir al perfil
        btnGoProfile.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogActivity.this, ProfileActivity.class);
            intent.putExtra("email", userEmail); // Envía el correo del usuario
            startActivity(intent);
        });

    }

    private void filterProducts(String query) {
        if (query.isEmpty()) {
            filteredList.clear();
            filteredList.addAll(productList);
        } else {
            filteredList.clear();
            filteredList.addAll(productList.stream()
                    .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        adapter.notifyDataSetChanged();
    }
}
