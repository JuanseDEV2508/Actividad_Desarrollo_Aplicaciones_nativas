package com.example.pixelstore_an.activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.adapters.ProductAdapter;
import com.example.pixelstore_an.data.ProductDatabaseHelper;
import com.example.pixelstore_an.models.Product;

import java.util.List;

public class ProductManagerActivity extends AppCompatActivity {

    private EditText etName, etCategory, etPrice, etImage;
    private Button btnAdd, btnUpdate, btnDelete;
    private RecyclerView recyclerView;
    private ProductDatabaseHelper dbHelper;
    private List<Product> productList;
    private ProductAdapter adapter;
    private int selectedProductId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);

        // Referencias actualizadas a los IDs correctos del XML
        etName = findViewById(R.id.etProductName);
        etCategory = findViewById(R.id.etProductCategory);
        etPrice = findViewById(R.id.etProductPrice);
        etImage = findViewById(R.id.etProductImage);

        btnAdd = findViewById(R.id.btnAddProduct);
        btnUpdate = findViewById(R.id.btnUpdateProduct);
        btnDelete = findViewById(R.id.btnDeleteProduct);
        recyclerView = findViewById(R.id.rvProducts);

        dbHelper = new ProductDatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();

        // ➕ Agregar producto
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String priceText = etPrice.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            float price = Float.parseFloat(priceText);
            int image = R.drawable.default_avatar; // Imagen por defecto

            dbHelper.addProduct(new Product(name, category, price, image));
            Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
            clearFields();
            loadProducts();
        });

        // ✏️ Actualizar producto
        btnUpdate.setOnClickListener(v -> {
            if (selectedProductId == -1) {
                Toast.makeText(this, "Selecciona un producto de la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            String priceText = etPrice.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            float price = Float.parseFloat(priceText);
            int image = R.drawable.default_avatar;

            Product updatedProduct = new Product(selectedProductId, name, price, image, category);
            dbHelper.updateProduct(updatedProduct);

            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
            clearFields();
            loadProducts();
        });

        // 🗑️ Eliminar producto
        btnDelete.setOnClickListener(v -> {
            if (selectedProductId == -1) {
                Toast.makeText(this, "Selecciona un producto para eliminar", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.deleteProduct(selectedProductId);
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
            clearFields();
            loadProducts();
        });
    }

    // Cargar lista de productos
    private void loadProducts() {
        productList = dbHelper.getAllProducts();
        adapter = new ProductAdapter(productList, this::onProductClick); // ✅ usa el listener
        recyclerView.setAdapter(adapter);
    }


    // Cuando haces clic en un producto del RecyclerView
    private void onProductClick(Product product) {
        selectedProductId = product.getId();
        etName.setText(product.getName());
        etCategory.setText(product.getCategory());
        etPrice.setText(String.valueOf(product.getPrice()));
        Toast.makeText(this, "Producto seleccionado: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    // Limpiar formulario
    private void clearFields() {
        etName.setText("");
        etCategory.setText("");
        etPrice.setText("");
        etImage.setText("");
        selectedProductId = -1;
    }
}
