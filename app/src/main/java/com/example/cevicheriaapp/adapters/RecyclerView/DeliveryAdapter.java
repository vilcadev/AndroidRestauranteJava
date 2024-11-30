package com.example.cevicheriaapp.adapters.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.Delivery;
import com.example.cevicheriaapp.fragmentos.DetallePedidoFragment;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {

    private List<Delivery> deliveryList;
    private Context context;

    public static class DeliveryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtClienteNombre, txtPedidoEstado, txtPedidoHora, txtDireccion;

        public DeliveryViewHolder(View itemView) {
            super(itemView);
            txtClienteNombre = itemView.findViewById(R.id.txtClienteNombre);
            txtPedidoEstado = itemView.findViewById(R.id.txtPedidoEstado);
            txtPedidoHora = itemView.findViewById(R.id.txtPedidoHora);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
        }
    }

    public DeliveryAdapter(Context context, List<Delivery> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
    }

    @Override
    public DeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryViewHolder holder, int position) {
        Delivery delivery = deliveryList.get(position);
        holder.txtClienteNombre.setText(delivery.getNombre());



//        GradientDrawable drawable = new GradientDrawable();
//        drawable.setCornerRadius(8);
//        drawable.setShape(GradientDrawable.RECTANGLE);

        // Establecer el texto del estado
        if (delivery.getEstadoDelivery() == 1) {
            holder.txtPedidoEstado.setText("Recibido");
            holder.txtPedidoEstado.setTextColor(Color.WHITE);
        } else {
            holder.txtPedidoEstado.setText("Completado");

            holder.txtPedidoEstado.setBackgroundColor(Color.GREEN);  // Color verde
        }
//        holder.txtPedidoEstado.setBackground(drawable);

        String formattedTime = formatTime(delivery.getFecha());
        holder.txtPedidoHora.setText(formattedTime);
        String address = getAddressFromCoordinates(delivery.getLatitud(), delivery.getLongitud());
        holder.txtDireccion.setText(address);

        // Agregar un evento de clic
        holder.itemView.setOnClickListener(v -> {
            // Crear un Bundle y pasar los datos
            Bundle bundle = new Bundle();
            bundle.putString("idDelivery", delivery.getIdDelivery());
            bundle.putDouble("latitud", delivery.getLatitud());
            bundle.putDouble("longitud", delivery.getLongitud());
            bundle.putString("direccion",address);

            // Crear el Fragment y pasar el Bundle
            DetallePedidoFragment detallePedidoFragment = new DetallePedidoFragment();
            detallePedidoFragment.setArguments(bundle);

//            requireActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, detallePedidoFragment)
//                    .addToBackStack(null)
//                    .commit();

            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, detallePedidoFragment); // Reemplaza con el ID del contenedor de fragmentos
            transaction.addToBackStack(null);  // Si deseas permitir regresar al fragment anterior
            transaction.commit();

            // Realizar la transacción del Fragment
//            FragmentTransaction transaction = context.getPackageManager().queryIntentActivities;
//            transaction.replace(R.id.fragment_container, detallePedidoFragment);  // Reemplaza con tu contenedor de fragmentos
//            transaction.addToBackStack(null);  // Si deseas permitir regresar al fragment anterior
//            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }



    private String formatTime(String fecha) {
        try {
            // Convertir la fecha de String a Date
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = originalFormat.parse(fecha);

            // Formatear la fecha para mostrar solo la hora
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");  // 24-hour format
            return timeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";  // En caso de error, devolver una cadena vacía
        }
    }

    // Método para obtener la dirección a partir de las coordenadas
    private String getAddressFromCoordinates(double lat, double lon) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // Obtener la lista de direcciones desde las coordenadas
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Retornar la dirección completa
                return address.getAddressLine(0); // Dirección completa
            } else {
                return "Dirección no encontrada";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al obtener la dirección";
        }
    }
}
