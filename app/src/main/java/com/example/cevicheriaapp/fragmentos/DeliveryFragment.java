package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.adapters.RecyclerView.DeliveryAdapter;
import com.example.cevicheriaapp.clases.Delivery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeliveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout deliveryListLayout;

    private RecyclerView recyclerView;
    private DeliveryAdapter deliveryAdapter;
    private List<Delivery> deliveryList;

    public DeliveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryFragment newInstance(String param1, String param2) {
        DeliveryFragment fragment = new DeliveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        loadDeliveries();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
//        View cardView = view.findViewById(R.id.pedido1);
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navega al OrderDetailsFragment
//                Fragment orderDetailsFragment = new DetallePedidoFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, orderDetailsFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });

        recyclerView = view.findViewById(R.id.recycler_view_deliveries);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Cargar la lista de entregas
        deliveryList = new ArrayList<>();
        loadDeliveries();

        // Inicializar el Adapter
        deliveryAdapter = new DeliveryAdapter(getContext(),deliveryList);
        recyclerView.setAdapter(deliveryAdapter);

        return view;
    }

//    private void fetchDeliveries() {
//        String url = "https://www.cevicheriaappapitest.somee.com/api/Delivery"; // Cambia esto con la URL de tu API
//
//        // Crear la solicitud GET
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        List<Delivery> deliveries = new ArrayList<>();
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                // Parsear el objeto JSON y mapearlo a Delivery
//                                JSONObject deliveryJson = response.getJSONObject(i);
//                                Delivery delivery = new Delivery();
//                                delivery.setNombre(deliveryJson.getString("nombre"));
//                                delivery.setEstadoDelivery(deliveryJson.getInt("estadoDelivery"));
//                                delivery.setFecha(deliveryJson.getString("fecha"));
//                                delivery.setLatitud(deliveryJson.getDouble("latitud"));
//                                delivery.setLongitud(deliveryJson.getDouble("longitud"));
//
//                                deliveries.add(delivery);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        displayDeliveries(deliveries);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Manejar el error
//                        Toast.makeText(getContext(), "Error al cargar los deliveries", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        // Añadir la solicitud a la cola de Volley
//        Volley.newRequestQueue(getContext()).add(request);
//    }


    public void loadDeliveries() {
        // URL de tu API
        String url = "https://www.cevicheriaappapitest.somee.com/api/Delivery";

        // Crear una solicitud de tipo GET usando Volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Crear una lista para almacenar las entregas
                            List<Delivery> deliveries = new ArrayList<>();

                            // Convertir la respuesta JSON en un JSONArray
                            JSONArray jsonArray = new JSONArray(response);

                            // Recorrer el array JSON y parsear cada entrega
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject deliveryObject = jsonArray.getJSONObject(i);

                                // Obtener los datos de cada entrega
                                String idDelivery = deliveryObject.getString("idDelivery");
                                String nombre = deliveryObject.getString("nombre");
                                int estadoDelivery = deliveryObject.getInt("estadoDelivery");
                                String fecha = deliveryObject.getString("fecha");
                                double latitud = deliveryObject.getDouble("latitud");
                                double longitud = deliveryObject.getDouble("longitud");

                                // Crear un nuevo objeto Delivery y agregarlo a la lista
                                deliveries.add(new Delivery(idDelivery,nombre, estadoDelivery, fecha, latitud, longitud));
                            }

                            // Actualizar el Adapter con la lista de entregas
                            DeliveryAdapter adapter = new DeliveryAdapter(getContext(),deliveries);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error
                        Log.e("Volley", "Error en la solicitud: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


//    private void displayDeliveries(List<Delivery> deliveries) {
//        // Limpiar el layout antes de agregar nuevos elementos
//        deliveryListLayout.removeAllViews();
//
//        // Llenar el LinearLayout con las vistas correspondientes
//        for (Delivery delivery : deliveries) {
//            View deliveryView = getLayoutInflater().inflate(R.layout.item_delivery, deliveryListLayout, false);
//
//            // Referencias a las vistas dentro de cada item
//            TextView nombreTextView = deliveryView.findViewById(R.id.txtClienteNombre);
//            TextView estadoTextView = deliveryView.findViewById(R.id.txtPedidoEstado);
//            TextView fechaTextView = deliveryView.findViewById(R.id.txtPedidoHora);
//            TextView direccionTextView = deliveryView.findViewById(R.id.txtDireccion);
//
//            // Asignar los valores a las vistas
//            nombreTextView.setText(delivery.getNombre());
//            estadoTextView.setText(delivery.getEstadoDelivery() == 1 ? "Recibido" : "Pendiente");
//            fechaTextView.setText(delivery.getFecha());
//            direccionTextView.setText("Lat: " + delivery.getLatitud() + ", Long: " + delivery.getLongitud());
//
//            // Agregar la vista del delivery al layout principal
//            deliveryListLayout.addView(deliveryView);
//        }
//    }
}