package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.Mesa;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MesasFragment extends Fragment {
    private LinearLayout mesasContainer;
    // Constructor vacío (requerido)
    public MesasFragment() {}

    // Método estático para instanciar el fragmento (no es necesario aquí, pero lo mantengo)
    public static MesasFragment newInstance() {
        return new MesasFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_mesas, container, false);




        // Inicializar el contenedor donde se agregarán las mesas dinámicamente
        mesasContainer = view.findViewById(R.id.mesasContainer);

        cargarMesasDesdeAPI();

        return view;
    }

    // Método para navegar al OrderFragment y pasar el número de mesa
    private void navigateToOrderFragment(int mesaNumero) {
        // Crear una instancia de OrderFragment
        OrderFragment orderFragment = OrderFragment.newInstance(mesaNumero);

        // Reemplazar el fragmento actual con OrderFragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, orderFragment)
                .addToBackStack(null) // Permitir volver atrás
                .commit();
    }

    private void cargarMesasDesdeAPI() {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Mesa";  // URL del endpoint

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta y crear los botones dinámicamente
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject mesa = response.getJSONObject(i);
                                String nombreMesa = mesa.getString("nombreMesa");
                                int estadoMesa = mesa.getInt("estadoMesa");
                                String mesaId = mesa.getString("idMesa");

                                // Crear un botón por cada mesa
                                Button btnMesa = new Button(getContext());
                                btnMesa.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                                btnMesa.setText(nombreMesa);
                                btnMesa.setGravity(Gravity.CENTER);
                                btnMesa.setPadding(16, 16, 16, 16);
                                btnMesa.setTextSize(18);
                                btnMesa.setId(View.generateViewId());
                                btnMesa.setTextColor(getResources().getColor(android.R.color.black));

                                // Aquí puedes cambiar el color según el estado de la mesa
                                if (estadoMesa == 1) {
                                    btnMesa.setBackgroundResource(R.drawable.boton);
                                } else if (estadoMesa==2) {
                                    btnMesa.setBackgroundResource(R.drawable.boton2);
                                }
                                else if (estadoMesa==3) {
                                    btnMesa.setBackgroundResource(R.drawable.boton3);
                                }


                                // Configurar qué pasa cuando se presiona el botón
                                btnMesa.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Mesa mesaObj = new Mesa(mesaId, nombreMesa, estadoMesa);

                                        Gson gson = new Gson();
                                        String mesaJson = gson.toJson(mesaObj);

                                        // Convertir el objeto mesa a String JSON para enviarlo
                                        Bundle bundle = new Bundle();
                                        bundle.putString("mesa", mesaJson);

                                        // Crear instancia del fragmento de destino
                                        OrderFragment orderFragment = new OrderFragment();
                                        orderFragment.setArguments(bundle);

                                        // Reemplazar el fragmento actual por el nuevo
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, orderFragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });

                                // Agregar el botón al contenedor de mesas
                                mesasContainer.addView(btnMesa);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Error al cargar las mesas: " + error.toString();
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("API Error", errorMessage);
            }
        });

        // Añadir la solicitud a la cola de peticiones
        queue.add(jsonArrayRequest);
    }
}
