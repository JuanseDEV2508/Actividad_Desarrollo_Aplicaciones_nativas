package com.example.pixelstore_an.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixelstore_an.R;
import com.example.pixelstore_an.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final OnProductClickListener listener;

    // 👇 Interfaz para manejar el clic
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Constructor alternativo sin listener (para vistas de solo lectura)
    public ProductAdapter(List<Product> products) {
        this.products = products;
        this.listener = product -> {}; // listener vacío que no hace nada
    }

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);
        holder.tvName.setText(p.getName());
        holder.tvCategory.setText(p.getCategory());
        holder.tvPrice.setText("$" + p.getPrice());
        holder.imgProduct.setImageResource(p.getImage());

        // 👇 Evento de clic: llama al listener
        holder.itemView.setOnClickListener(v -> listener.onProductClick(p));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }
}
