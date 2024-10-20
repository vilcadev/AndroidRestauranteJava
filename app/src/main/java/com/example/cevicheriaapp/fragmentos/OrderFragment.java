package com.example.cevicheriaapp.fragmentos;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.adapters.ProductoAdapter;
import com.example.cevicheriaapp.clases.Mesa;
import com.example.cevicheriaapp.clases.Platillo;
import com.example.cevicheriaapp.clases.Producto;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

class ProductoSeleccionado {
    private Platillo platillo;
    private int cantidad;
    private Double precioTotal;

    public ProductoSeleccionado(Platillo platillo, int cantidad, double precioTotal) {
        this.platillo = platillo;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    public Platillo getPlatillo() {
        return platillo;
    }
    public double getPrecioTotal() {
        return precioTotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
}

public class OrderFragment extends Fragment {

    private static final String ARG_MESA_NUMERO = "mesa_numero";  // Llave para pasar el número de mesa

    private int mesaNumero;  // Variable para guardar el número de mesa
    private BottomSheetDialog bottomSheetDialog;
    private Mesa  mesa;
    private Button searchProductButton;

    // Declarar el botón
    private Button botonSeleccionados;
    private List<Platillo> productosSeleccionados = new ArrayList<>();
    private LinearLayout layoutProductos;
    TextView textViewTotal;

    EditText edtMensajeCocina;
    String mensajeCocina;
    public OrderFragment() {
        // Constructor vacío requerido
    }

    // Método estático para instanciar el fragmento y recibir el número de mesa
    public static OrderFragment newInstance(int mesaNumero) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MESA_NUMERO, mesaNumero);  // Pasar el número de mesa
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Obtener el número de mesa del Bundle
            mesaNumero = getArguments().getInt(ARG_MESA_NUMERO);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar la vista para este fragmento

        View view = inflater.inflate(R.layout.fragment_order, container, false);


        searchProductButton = view.findViewById(R.id.searchProduct);
        searchProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductBottomSheet();
            }
        });

        // Obtener el objeto mesa desde el bundle
        if (getArguments() != null) {
            String mesaJson = getArguments().getString("mesa");
            Gson gson = new Gson();
            mesa = gson.fromJson(mesaJson, Mesa.class);
            // Puedes usar los datos de 'mesa' aquí
        }
        // Actualizar el botón con la cantidad de productos seleccionados
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_productos, null);
        botonSeleccionados= bottomSheetView.findViewById(R.id.bottom_productos);

        View layoutView = inflater.inflate(R.layout.fragment_order, container, false);
        layoutProductos = view.findViewById(R.id.layout_productos);


        textViewTotal = view.findViewById(R.id.textViewTotal);


        edtMensajeCocina= view.findViewById(R.id.edtMensajeCocina);

        // Llamar al método para obtener los platillos
        obtenerPlatillos(layoutProductos);

        return view;
    }

    private void obtenerPlatillos(LinearLayout layoutProductos) {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Orden/GetOrderMesa?id=" + mesa.getIdMesa(); ; // Reemplaza esto con la URL correcta

        // Crear una solicitud JSON
        JsonObjectRequest  jsonObjectRequest  = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Limpiar la lista de productos seleccionados
                            productosSeleccionados.clear();

                            // Obtener el array de platillos desde el objeto JSON
                            JSONArray platillosArray = response.getJSONArray("platillos");

                            // Comprobar la longitud del arreglo antes de iterar
                            if (platillosArray.length() > 0) {
                                for (int i = 0; i < platillosArray.length(); i++) {
                                    JSONObject platilloJson = platillosArray.getJSONObject(i);

                                    // Crear el platillo usando el constructor
                                    Platillo platillo = new Platillo(
                                            platilloJson.getString("idPlatillo"),
                                            platilloJson.getString("nombrePlatillo"), // Verifica el nombre correcto
                                            platilloJson.getDouble("precioTotal"),
                                            platilloJson.optString("imagenUrl", ""), // Usar optString para evitar excepciones si no existe
                                            platilloJson.getInt("cantidad")
                                    );
                                    // Agregar el platillo a la lista
                                    productosSeleccionados.add(platillo);
                                }

                                // Mostrar un mensaje de éxito
                                Toast.makeText(getContext(), "Productos Cargados", Toast.LENGTH_SHORT).show();
                                agregarProductosSeleccionados(layoutProductos);
                            } else {
                                Toast.makeText(getContext(), "No se encontraron platillos.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("JSONError", "Error al analizar la respuesta: " + e.getMessage());
                            Toast.makeText(getContext(), "Error al cargar los productos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage());
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView mesaNumeroTextView = view.findViewById(R.id.tableNumber);
        mesaNumeroTextView.setText(mesa.getNombreMesa());
//
//        Button buscarButton = view.findViewById(R.id.searchProduct);
//        buscarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                botonBuscarProductoPress(v);
//            }
//        });


        // Asignar evento click al botón btnSave
        Button guardarButton = view.findViewById(R.id.btnSave);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonGuardarPress(v);
            }
        });


        // Asignar evento click al botón btnSave
        Button pagosFragment = view.findViewById(R.id.btnPagos);
        pagosFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarPagos(v);
            }
        });
    }


    public void botonBuscarProductoPress(View view) {
        BuscarProducto buscarProductoFragment = new BuscarProducto();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, buscarProductoFragment)
                .addToBackStack(null)
                .commit();
    }

    public void navegarPagos(View view){

        // Convertir el objeto Mesa a JSON
        Gson gson = new Gson();
        String mesaJson = gson.toJson(mesa);

        // Enviar el JSON al PagosFragment a través de un Bundle
        Bundle bundle = new Bundle();
        bundle.putString("mesa", mesaJson);
        bundle.putDouble("totalGeneral", totalGeneral);

        PagosFragment pagosFragment = new PagosFragment();

        pagosFragment.setArguments(bundle);

        // Asignar el Bundle al fragmento
        pagosFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, pagosFragment)
                .addToBackStack(null)
                .commit();
    }


    // Método para manejar el evento del botón Guardar
    public void botonGuardarPress(View view) {

        MesasFragment mesasFragment = new MesasFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mesasFragment)
                .addToBackStack(null)
                .commit();
        // Aquí puedes agregar la lógica que se ejecutará cuando se haga clic en el botón Guardar


        // Obtener el mensaje de la cocina desde el EditText


        mostrarProductosConCantidadEnLog();

        // Lógica para enviar productos con Volley
        enviarProductosConVolley();

        Toast.makeText(getContext(), "Orden guardada", Toast.LENGTH_SHORT).show();




    }

    // Método para enviar los productos seleccionados al servidor con Volley
    private void enviarProductosConVolley() {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Orden";  // Cambia la URL al endpoint correcto

        // Crear el objeto JSON para enviar los detalles de la orden
        JSONObject jsonBody = new JSONObject();
        JSONArray ordenArray = new JSONArray();

        try {
            // Agregar fecha actual en el formato requerido
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());
            jsonBody.put("fecha", fechaActual);

            // Cambia esta ID por la ID de la mesa que estás usando
            jsonBody.put("mesaId", mesa.getIdMesa());


             String observacion = edtMensajeCocina.getText().toString();
            // Agregar una observación (puedes cambiar esto por la observación real si la tienes)
            jsonBody.put("observacion", observacion);

            // Crear el array de orden
            for (ProductoSeleccionado productoConCantidad : productosConCantidad) {
                Platillo platillo = productoConCantidad.getPlatillo();
                int cantidad = productoConCantidad.getCantidad();
                double precioTotal = platillo.getPrecioUnitario() * cantidad;

                // Crear el JSON de un platillo
                JSONObject platilloJson = new JSONObject();
                platilloJson.put("idPlatillo", platillo.getIdPlatillo());
                platilloJson.put("cantidad", cantidad);
                platilloJson.put("precioTotal", precioTotal);

                ordenArray.put(platilloJson);  // Agregar al array
            }

            // Agregar el array de orden al JSON body
            jsonBody.put("orden", ordenArray);

            // Log del objeto JSON antes de enviarlo
            Log.d("JSON Request", "Objeto JSON a enviar: " + jsonBody.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor
                        Log.d("Volley", "Respuesta: " + response.toString());
                        Toast.makeText(getContext(), "Orden enviada exitosamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error
                        Log.e("Volley", "Error en la solicitud: " + error.getMessage());
                        Toast.makeText(getContext(), "Error al enviar la orden", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }

    private void mostrarProductosConCantidadEnLog() {
        for (ProductoSeleccionado productoConCantidad : productosConCantidad) {
            Platillo platillo = productoConCantidad.getPlatillo();
            int cantidad = productoConCantidad.getCantidad();
            double precioTotal = platillo.getPrecioUnitario() * productoConCantidad.getCantidad();

            Log.d("ProductosConCantidad", "ID: " + platillo.getIdPlatillo() +
                    ", Nombre: " + platillo.getNombre() +
                    ", PrecioTotal: " + precioTotal +
                    ", Cantidad: " + cantidad);
        }
    }


    private void showProductBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_productos, null);
//
//        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.recyclerViewProductos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Cargar los platillos desde la API
//        cargarPlatillosDesdeAPI(recyclerView);
        GridLayout gridLayout = bottomSheetView.findViewById(R.id.grid_productos);
        cargarPlatillosDesdeAPI(gridLayout);
//
//        // Aquí deberías cargar tu lista de productos
//        List<Producto> productos = obtenerProductos();
//        ProductoAdapter adapter = new ProductoAdapter(productos, requireContext());
//        recyclerView.setAdapter(adapter);
//



        // Configurar el botón para cerrar el BottomSheet
        Button cerrarButton = bottomSheetView.findViewById(R.id.bottom_productos);
        cerrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                agregarProductosSeleccionados(layoutProductos);
            }
        });


        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();



        Toast.makeText(getContext(), "bottom sheet con productos", Toast.LENGTH_SHORT).show();
    }


    // Método para obtener los productos (puedes modificarlo según tu lógica)
    private List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto("Ceviche", 20.00));
        productos.add(new Producto("Inca Kola", 15.00));
        // Agrega más productos
        return productos;
    }



    private void cargarPlatillosDesdeAPI(GridLayout gridLayout) {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Menu?fecha=10-19-2024";  // Cambia la URL al endpoint correcto

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta y crear los platillos
                        List<Platillo> platillos = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject producto = response.getJSONObject(i);
                                String nombreProducto = producto.getString("nombre");
                                String precioProducto = producto.getString("precioUnitario");
                                String imagenUrl = producto.getString("imagenUrl");
                                String idPlatillo = producto.getString("idPlatillo");
                                int cantidad = producto.optInt("cantidad",1);

                                // Crear el objeto Producto
                                Platillo platillo = new Platillo(idPlatillo, nombreProducto, Double.parseDouble(precioProducto), imagenUrl, cantidad);
// Crear un FrameLayout para cada producto
                                FrameLayout frameLayout = new FrameLayout(getContext());
                                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                                params.width = 0;
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                                frameLayout.setLayoutParams(params);
                                frameLayout.setPadding(16, 16, 16, 16); // Ajustar el padding

// Crear un LinearLayout para organizar la imagen y el nombre verticalmente
                                LinearLayout verticalLayout = new LinearLayout(getContext());
                                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                                verticalLayout.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar elementos horizontalmente dentro del LinearLayout
                                FrameLayout.LayoutParams verticalLayoutParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                                verticalLayout.setLayoutParams(verticalLayoutParams);

// Crear el ImageButton para la imagen del producto
                                ImageButton imageButton = new ImageButton(getContext());
                                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(400, 300); // Cambiar tamaño de la imagen
                                imageButton.setLayoutParams(imageParams);
                                imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                Glide.with(getContext()).load(imagenUrl).into(imageButton);
                                imageButton.setBackgroundResource(R.drawable.rounded_image); // Borde redondeado

// Crear el TextView para el precio
                                TextView priceTextView = new TextView(getContext());
                                priceTextView.setText("S/. " + precioProducto);
                                priceTextView.setBackgroundColor(Color.parseColor("#808080")); // Cambiado a gris claro
                                priceTextView.setTextColor(Color.WHITE);
                                priceTextView.setTextSize(16); // Tamaño de texto
                                priceTextView.setTypeface(null, Typeface.BOLD); // Texto en negrita
                                FrameLayout.LayoutParams priceParams = new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.END);
                                priceParams.setMargins(16, 16, 16, 0); // Márgenes ajustados
                                priceTextView.setPadding(16, 8, 16, 8); // Padding ajustado

// Crear el TextView para el nombre del producto
                                TextView nameTextView = new TextView(getContext());
                                nameTextView.setText(nombreProducto);
                                nameTextView.setTextSize(18);
                                nameTextView.setTextColor(Color.BLACK);
                                nameTextView.setGravity(Gravity.CENTER); // Centrar texto
                                nameTextView.setTypeface(null, Typeface.BOLD); // Texto en negrita
                                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                nameParams.setMargins(0, 16, 0, 0); // Ajustar márgenes para que quede debajo de la imagen

// Agregar la imagen y el nombre al LinearLayout
                                verticalLayout.addView(imageButton);
                                verticalLayout.addView(nameTextView, nameParams);

// Agregar las vistas al FrameLayout
                                frameLayout.addView(verticalLayout);
                                frameLayout.addView(priceTextView, priceParams); // Precio flotando en la esquina superior derecha





                                // Agregar el evento de click al producto
                                imageButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Agregar el producto a la lista de seleccionados
                                        if (!productosSeleccionados.contains(platillo)) {
                                            productosSeleccionados.add(platillo);
                                        } else {
                                            productosSeleccionados.remove(platillo); // Si ya está, lo remueve
                                        }

                                        actualizarBotonSeleccionados(botonSeleccionados);

                                    }
                                });

                                // Agregar el FrameLayout al GridLayout
                                gridLayout.addView(frameLayout);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Configurar el adapter
//                        ProductoAdapter adapter = new ProductoAdapter(platillos, requireContext());
//                        recyclerView.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error
                        Toast.makeText(getContext(), "Error al cargar los platillos", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola
        queue.add(jsonArrayRequest);
    }





    private void actualizarBotonSeleccionados(Button botonSeleccionados) {
        int cantidadSeleccionados = productosSeleccionados.size();
        int longitud = productosSeleccionados.size();
        Log.d("ProductosSeleccionados", "Longitud del array: " + longitud);
        botonSeleccionados.setText("Ver " + cantidadSeleccionados + " seleccionados");
    }

    double totalGeneral = 0;
    // Lista para almacenar productos seleccionados y su cantidad
    List<ProductoSeleccionado> productosConCantidad = new ArrayList<>();
    private void agregarProductosSeleccionados(LinearLayout layoutProductos) {
        // Limpiar el layout en caso de que haya productos anteriores
        layoutProductos.removeAllViews();

        // Limpiar la lista de productos seleccionados
        productosConCantidad.clear();


        for (Platillo platillo : productosSeleccionados) {
            // Inflar la CardView
            CardView cardView = new CardView(getContext());
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(8); // Mayor elevación para el diseño
            cardView.setRadius(24); // Bordes más redondeados
            cardView.setCardBackgroundColor(Color.WHITE);

            // Crear el LinearLayout dentro de la CardView
            LinearLayout cardLayout = new LinearLayout(getContext());
            cardLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            cardLayout.setOrientation(LinearLayout.HORIZONTAL);
            cardLayout.setGravity(Gravity.CENTER_VERTICAL);
            cardLayout.setPadding(16, 16, 16, 16); // Aumenta el padding

            // Crear ImageView para la imagen del platillo
            ImageView imagenProducto = new ImageView(getContext());
            imagenProducto.setLayoutParams(new LinearLayout.LayoutParams(100, 100)); // Tamaño mayor
            imagenProducto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(getContext()).load(platillo.getImagenUrl()).into(imagenProducto);

            // Crear LinearLayout para texto
            LinearLayout textLayout = new LinearLayout(getContext());
            textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1));
            textLayout.setOrientation(LinearLayout.VERTICAL);
            textLayout.setPadding(16, 0, 0, 0);

            // Crear TextView para el nombre del platillo
            // Nombre del platillo
            TextView nombreProducto = new TextView(getContext());
            nombreProducto.setText(platillo.getNombre());
            nombreProducto.setTextSize(18);
            nombreProducto.setTypeface(null, Typeface.BOLD);

            // Crear TextView para el precio del platillo
            TextView precioProducto = new TextView(getContext());
            precioProducto.setText("S/ " + platillo.getPrecioUnitario());
            precioProducto.setTextSize(16);

            totalGeneral += platillo.getPrecioUnitario();

            // Agregar TextViews al LinearLayout de texto
            textLayout.addView(nombreProducto);
            textLayout.addView(precioProducto);



            // Inicializar cantidad
            final int[] cantidadActual = {platillo.getCantidad()};

            // Crear TextView para el total
            TextView total = new TextView(getContext());
//            total.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//            total.setText("S/ " + platillo.getPrecioUnitario()); // Inicializar con el precio unitario
//            total.setTextSize(20);
//            total.setTextColor(getResources().getColor(android.R.color.black));


            // Crear un contenedor horizontal para los botones y el contador
            LinearLayout layoutContador = new LinearLayout(getContext());
            layoutContador.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutContador.setOrientation(LinearLayout.HORIZONTAL);
            layoutContador.setGravity(Gravity.CENTER_HORIZONTAL); // Centrar elementos horizontalmente

            // TextView de cantidad
            TextView cantidad = new TextView(getContext());
            cantidad.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            cantidad.setText(String.valueOf(cantidadActual[0]));
            cantidad.setPadding(2, 10, 9, 6);
            cantidad.setTextSize(20);
            cantidad.setTextColor(getResources().getColor(android.R.color.black));

// Ajustar márgenes
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cantidad.getLayoutParams();
            params.setMargins(0, 10, 0, 0);  // Ajusta el valor si es necesario
            params.gravity = Gravity.CENTER_HORIZONTAL;  // Centrar el TextView de cantidad
            cantidad.setLayoutParams(params);

// Layout contenedor para los botones y el TextView de cantidad
            LinearLayout botonesLayout = new LinearLayout(getContext());
            botonesLayout.setOrientation(LinearLayout.HORIZONTAL);
            botonesLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            botonesLayout.setGravity(Gravity.CENTER_HORIZONTAL);  // Centrar horizontalmente

// Botón de restar
            Button btnRestar = new Button(getContext());
            btnRestar.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            btnRestar.setBackgroundResource(R.drawable.circular_button);
            btnRestar.setBackgroundResource(R.drawable.ic_minus);
            btnRestar.setText("-");
            btnRestar.setTextSize(24);
            btnRestar.setOnClickListener(v -> {
                if (cantidadActual[0] > 1) {
                    cantidadActual[0]--;
                    cantidad.setText(String.valueOf(cantidadActual[0]));
                    actualizarCantidadProducto(platillo, cantidadActual[0]);
                    actualizarTotal(precioProducto.getText().toString(), cantidadActual[0], total, 2);
                }
            });



// Botón de sumar
            Button btnSumar = new Button(getContext());
            btnSumar.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            btnSumar.setBackgroundResource(R.drawable.circular_button);
            btnSumar.setBackgroundResource(R.drawable.ic_plus);
            btnSumar.setText("+");
            btnSumar.setTextSize(24);
            btnSumar.setTextColor(getResources().getColor(android.R.color.black));
            btnSumar.setOnClickListener(v -> {
                cantidadActual[0]++;
                cantidad.setText(String.valueOf(cantidadActual[0]));
                actualizarCantidadProducto(platillo, cantidadActual[0]);
                actualizarTotal(precioProducto.getText().toString(), cantidadActual[0], total, 1);
            });

// Botón de eliminar
            ImageButton btnEliminar = new ImageButton(getContext());
            btnEliminar.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
            btnEliminar.setBackgroundResource(R.drawable.ic_trash);
            btnEliminar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnEliminar.setPadding(15, 0, 0, 0);
            btnEliminar.setOnClickListener(v -> {
                productosSeleccionados.remove(platillo);
                agregarProductosSeleccionados(layoutProductos);
            });

// Agregar botones al layout de botones
            botonesLayout.addView(btnRestar);
            botonesLayout.addView(cantidad);
            botonesLayout.addView(btnSumar);
            botonesLayout.addView(btnEliminar);


            // Actualizar el total al cargar el producto
//            totalGeneral += actualizarTotal(precioProducto.getText().toString(), cantidadActual[0], total,);

            // Agregar todos los elementos al layout de la CardView
            cardLayout.addView(imagenProducto);
            cardLayout.addView(textLayout);
            cardLayout.addView(botonesLayout);
//            cardLayout.addView(total); // Agregar el total a la CardView

            cardView.addView(cardLayout);

            // Agregar la CardView al layout de productos
            layoutProductos.addView(cardView);

            // Agregar el platillo con la cantidad inicial a la lista

            double precioTotal = platillo.getPrecioUnitario() * cantidadActual[0];
            productosConCantidad.add(new ProductoSeleccionado(platillo, cantidadActual[0],precioTotal));
        }


        textViewTotal.setText(""+totalGeneral);
    }

    private double actualizarTotal(String precioUnitarioString, int cantidad, TextView totalTextView, int modo) {
        double precioUnitario = Double.parseDouble(precioUnitarioString.replace("S/ ", ""));
        double total = precioUnitario * cantidad;

        if(modo ==1){
            totalGeneral += precioUnitario;
        }
        else if(modo ==2){
            totalGeneral -= precioUnitario;
        }

        Log.d("ProductosSeleccionados", "Longitud del array: " + totalGeneral);
        textViewTotal.setText(""+totalGeneral);
        totalTextView.setText("S/ " + total); // Actualizar el TextView con el nuevo total
        return total; // Retornar el total para sumarlo al totalGeneral
    }


    private void actualizarCantidadProducto(Platillo platillo, int nuevaCantidad) {
        for (ProductoSeleccionado producto : productosConCantidad) {
            if (producto.getPlatillo().equals(platillo)) {
                producto.setCantidad(nuevaCantidad);
                break;
            }
        }
    }




}
