package com.example.cevicheriaapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.Platillo;
import com.example.cevicheriaapp.clases.Producto;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Platillo> platillos;
    private Context context;

    public ProductoAdapter(List<Platillo> platillos, Context context) {
        this.platillos = platillos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Platillo platillo = platillos.get(position);
        holder.nombreTextView.setText(platillo.getNombre());
        holder.precioTextView.setText("S/ " + platillo.getPrecioUnitario());
        // Aquí puedes cargar la imagen usando Glide, Picasso, etc.
    }

    @Override
    public int getItemCount() {
        return platillos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView precioTextView;
        ImageView imagenProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreProducto);
            precioTextView = itemView.findViewById(R.id.precioProducto);
            imagenProducto = itemView.findViewById(R.id.imagenProducto);
        }
    }
}
