package com.example.cevicheriaapp.adapters.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.Pedido;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder> {

    private List<Pedido> pedidoList;
    private Context context;

    public PedidoAdapter(List<Pedido> pedidoList, Context context) {
        this.pedidoList = pedidoList;
        this.context = context;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidoList.get(position);
        holder.nombreTextView.setText(pedido.getNombre());
        holder.precioTextView.setText(String.format("S/%.2f", pedido.getPrecioTotal()));
        holder.cantidadTextView.setText("Cantidad: " + pedido.getCantidad());

        // Usar una librería como Glide o Picasso para cargar la imagen
        Glide.with(context)
                .load(pedido.getImagen())
                .into(holder.imagenImageView);
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView, precioTextView, cantidadTextView;
        ImageView imagenImageView;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombre_pedido);
            precioTextView = itemView.findViewById(R.id.precio_pedido);
            cantidadTextView = itemView.findViewById(R.id.cantidad_pedido);
            imagenImageView = itemView.findViewById(R.id.imagen_pedido);
        }
    }
}
