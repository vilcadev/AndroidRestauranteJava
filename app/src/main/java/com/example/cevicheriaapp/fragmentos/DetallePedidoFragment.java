package com.example.cevicheriaapp.fragmentos;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.adapters.RecyclerView.DeliveryAdapter;
import com.example.cevicheriaapp.adapters.RecyclerView.PedidoAdapter;
import com.example.cevicheriaapp.clases.Delivery;
import com.example.cevicheriaapp.clases.Pedido;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetallePedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetallePedidoFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap gMap;

    private RequestQueue requestQueue;

    private String idDelivery;
    private double latitud;
    private double longitud;
    private String direccion;
    private Button btnMarkDelivered;

    private PedidoAdapter pedidoAdapter;
    private List<Pedido> pedidosList;

    private RecyclerView recyclerView;
    public DetallePedidoFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetallePedidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetallePedidoFragment newInstance(String param1, String param2) {
        DetallePedidoFragment fragment = new DetallePedidoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        // Oculta el Toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Muestra el Toolbar de nuevo al salir del fragmento
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);


        Button btnBack = view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous fragment (DeliveryFragment)
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnMarkDelivered = view.findViewById(R.id.btn_mark_delivered);
        btnMarkDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsDelivered();
                getActivity().getSupportFragmentManager().popBackStack();

                // Recargar los datos en el fragmento anterior
                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentByTag("DeliveryFragment");
                if (currentFragment != null) {

                    ((DeliveryFragment) currentFragment).loadDeliveries();
                }
            }
        });


        recyclerView = view.findViewById(R.id.food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pedidosList = new ArrayList<>();





        // Inicializa Volley
        requestQueue = Volley.newRequestQueue(requireContext());


        // Obtén el fragmento del mapa y configura el callback
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        if (getArguments() != null) {
            idDelivery = getArguments().getString("idDelivery");
            latitud = getArguments().getDouble("latitud");
            longitud = getArguments().getDouble("longitud");
            direccion = getArguments().getString("direccion");

            TextView direccionTextView = view.findViewById(R.id.address_text);
            direccionTextView.setText(direccion);

            fetchPedidos(idDelivery);

            // Inicializar el Adapter
            pedidoAdapter = new PedidoAdapter(pedidosList,getContext());
            recyclerView.setAdapter(pedidoAdapter);

        }


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        gMap = googleMap;

        // Define el punto de inicio y de destino
        LatLng start = new LatLng(-11.943746, -76.989977); // Lima, Perú
        LatLng end = new LatLng(latitud, longitud); // Otro punto en Lima

        // Agrega marcadores en ambos puntos
        gMap.addMarker(new MarkerOptions().position(start).title("Inicio"));
        gMap.addMarker(new MarkerOptions().position(end).title("Destino"));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 12));

        // Llama a la función para obtener la ruta
        fetchRoute(start, end);
    }

    private void fetchRoute(LatLng origin, LatLng dest) {
        String url = getDirectionsUrl(origin, dest);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        drawRoute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String key = "AIzaSyBHpQltVFexYgFVSt4AkNf7BspdZg2OSjM";  // Reemplaza con tu clave de API de Google
        String parameters = str_origin + "&" + str_dest + "&key=" + key;
        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
    }

    private void drawRoute(JSONObject response) {
        try {
            JSONArray routes = response.getJSONArray("routes");
            JSONObject route = routes.getJSONObject(0);
            JSONArray legs = route.getJSONArray("legs");
            JSONObject leg = legs.getJSONObject(0);
            JSONArray steps = leg.getJSONArray("steps");

            List<LatLng> path = new ArrayList<>();
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                JSONObject polyline = step.getJSONObject("polyline");
                String points = polyline.getString("points");
                path.addAll(decodePolyline(points));
            }

            // Dibuja la ruta en el mapa
            gMap.addPolyline(new PolylineOptions().addAll(path).color(Color.BLUE).width(10));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            poly.add(new LatLng((lat / 1E5), (lng / 1E5)));
        }
        return poly;
    }

    public void fetchPedidos(String idDelivery) {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Delivery/" + idDelivery;  // Ajusta la URL de la API

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Crear la lista de pedidos
                        List<Pedido> pedidos = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject pedidoJson = response.getJSONObject(i);
                                Pedido pedido = new Pedido(
                                        pedidoJson.getString("idPlatillo"),
                                        pedidoJson.getString("nombre"),
                                        pedidoJson.getString("imagen"),
                                        pedidoJson.getDouble("precioTotal"),
                                        pedidoJson.getInt("cantidad")
                                );
                                pedidos.add(pedido);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Configurar el RecyclerView

                        PedidoAdapter adapter = new PedidoAdapter(pedidos, getContext());

                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error aquí
                        Toast.makeText(getContext(), "Error al cargar los pedidos", Toast.LENGTH_SHORT).show();
                    }
                });

        // Añadir la solicitud a la cola
        requestQueue.add(request);
    }


    private void markAsDelivered() {

        String url = "https://www.cevicheriaappapitest.somee.com/api/Delivery/" + idDelivery;
        // Crear una solicitud PUT
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                response -> {
                    // Manejo de la respuesta exitosa
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Entrega marcada como entregada", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Manejo del error
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error al marcar como entregado", Toast.LENGTH_SHORT).show();
                    }
                    if (error instanceof VolleyError) {
                        // Si la solicitud falla por algún motivo (por ejemplo, error de red)
                        error.printStackTrace();
                    }
                });

        // Crear una instancia de la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        // Agregar la solicitud a la cola
        requestQueue.add(putRequest);
    }

}