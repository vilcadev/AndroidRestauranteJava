package com.example.cevicheriaapp.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.MultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class InfoCuentaFragment extends Fragment {

    private Button  btnEditar, btnGuardar, btnCancelar;
    private EditText etNombreUsuario, etNombre, etApellido, etCorreo;

    // Variables para almacenar los valores iniciales antes de cualquier edición
    private String originalNombreUsuario, originalNombre, originalApellido, originalCorreo;

    private String idUsuario;


    private ImageView imvCircular;
    private Bitmap selectedBitmap;
    private String imagenBase64;
    ImageView imgEditPhoto;

    public InfoCuentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_cuenta, container, false);

        // Encontrar los botones y EditTexts en el layout
//        ImageView imgCamera = view.findViewById(R.id.imgCamera);
//        btnEditar = view.findViewById(R.id.btnEditar);
//        btnGuardar = view.findViewById(R.id.btnGuardar);
//        btnCancelar = view.findViewById(R.id.btnCancelar);
//        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etNombre = view.findViewById(R.id.etNombre);
        imvCircular = view.findViewById(R.id.imvCircular);

        btnEditar = view.findViewById(R.id.btnEditar);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        imgEditPhoto = view.findViewById(R.id.imgEditPhoto);

        // Configurar el clic para editar la foto
        imgEditPhoto.setOnClickListener(v -> {
            // Abrir el selector de imágenes
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1); // 1 es el código de solicitud para la imagen
        });

//        etApellido = view.findViewById(R.id.etApellido);
//        etCorreo = view.findViewById(R.id.etCorreo);

        // Deshabilitar los EditTexts inicialmente
//        etNombreUsuario.setEnabled(false);
//        etNombre.setEnabled(false);
//        etApellido.setEnabled(false);
//        etCorreo.setEnabled(false);

        // Ocultar botones Guardar y Cancelar inicialmente
//        btnGuardar.setVisibility(View.GONE);
//        btnCancelar.setVisibility(View.GONE);

        // Configurar el listener para el ImageView
//        imgCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Retroceder a la actividad anterior
//                requireActivity().onBackPressed();
//            }
//        });

        // Configurar el listener para el botón "Editar"
//        btnEditar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Guardar los valores actuales antes de editar
//                originalNombreUsuario = etNombreUsuario.getText().toString();
//                originalNombre = etNombre.getText().toString();
//                originalApellido = etApellido.getText().toString();
//                originalCorreo = etCorreo.getText().toString();
//
//                // Habilitar los campos de EditText para edición
//                etNombreUsuario.setEnabled(true);
//                etNombre.setEnabled(true);
//                etApellido.setEnabled(true);
//                etCorreo.setEnabled(true);
//
//                // Cambiar el color del texto para dar feedback visual
//                etNombreUsuario.setTextColor(getResources().getColor(android.R.color.black));
//                etNombre.setTextColor(getResources().getColor(android.R.color.black));
//                etApellido.setTextColor(getResources().getColor(android.R.color.black));
//                etCorreo.setTextColor(getResources().getColor(android.R.color.black));
//
//                // Ocultar el botón "Editar" y mostrar los botones "Guardar" y "Cancelar"
//                btnEditar.setVisibility(View.GONE);
//                btnGuardar.setVisibility(View.VISIBLE);
//                btnCancelar.setVisibility(View.VISIBLE);
//            }
//        });

        // Habilitar la edición del nombre cuando se presione el botón "Editar"
        btnEditar.setOnClickListener(v -> {
            etNombre.setEnabled(true); // Habilitar el EditText
            btnGuardar.setVisibility(View.VISIBLE); // Mostrar el botón "Guardar"
            btnEditar.setVisibility(View.GONE); // Ocultar el botón "Editar"
        });


//
//        // Configurar el listener para el botón "Guardar"
//        btnGuardar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Aquí puedes agregar lógica para guardar los datos en una base de datos o similar
//
//                // Deshabilitar los campos de EditText de nuevo
//                etNombreUsuario.setEnabled(false);
//                etNombre.setEnabled(false);
//                etApellido.setEnabled(false);
//                etCorreo.setEnabled(false);
//
//                // Ocultar botones Guardar y Cancelar, y mostrar el botón Editar
//                btnGuardar.setVisibility(View.GONE);
//                btnCancelar.setVisibility(View.GONE);
//                btnEditar.setVisibility(View.VISIBLE);
//            }
//        });
//
//        // Configurar el listener para el botón "Cancelar"
//        btnCancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Revertir los cambios y restaurar los valores originales
//                etNombreUsuario.setText(originalNombreUsuario);
//                etNombre.setText(originalNombre);
//                etApellido.setText(originalApellido);
//                etCorreo.setText(originalCorreo);
//
//                // Deshabilitar los campos de EditText de nuevo
//                etNombreUsuario.setEnabled(false);
//                etNombre.setEnabled(false);
//                etApellido.setEnabled(false);
//                etCorreo.setEnabled(false);
//
//                // Ocultar botones Guardar y Cancelar, y mostrar el botón Editar
//                btnGuardar.setVisibility(View.GONE);
//                btnCancelar.setVisibility(View.GONE);
//                btnEditar.setVisibility(View.VISIBLE);
//            }
//        });

        // Recuperar el idUsuario pasado desde el fragmento anterior
        if (getArguments() != null) {
            idUsuario = getArguments().getString("idUsuario");

        }
        cargarDatosUsuario(idUsuario);
        // Log para verificar que el idUsuario se recuperó correctamente
        Log.d("DEBUG", "El idUsuario recibido en InfoCuentaFragment es: " + idUsuario);


        // Guardar los cambios en nombre y foto
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = etNombre.getText().toString();
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            guardarDatosUsuario(nuevoNombre, selectedBitmap,idUsuario);
        });
        return view;
    }

    // Método para abrir la galería o cámara
    private void abrirGaleriaDeImagenes() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Para seleccionar cualquier tipo de imagen
        startActivityForResult(intent, 1); // Abrir la galería, código 1
    }

    // Manejar el resultado de la selección de la imagen
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                // Obtener la imagen seleccionada
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Obtener el bitmap de la imagen seleccionada
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                    // Mostrar la imagen en el ShapeableImageView (imvCircular)
                    imvCircular.setImageBitmap(bitmap); // Actualizar la imagen de perfil

                    imagenBase64 = convertirImagenAStringBase64(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para convertir la imagen a Base64
    private String convertirImagenAStringBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void guardarDatosUsuario(String nuevoNombre, Bitmap nuevaFoto, String usuarioId) {
        // Convertir la imagen a Base64

        if (nuevaFoto != null) {
            imagenBase64 = convertirImagenAStringBase64(nuevaFoto);
        }

        // Crear un mapa de parámetros para enviar en el formulario
        Map<String, String> params = new HashMap<>();
        params.put("Nombre", nuevoNombre);  // Agregar el nombre
        params.put("Imagen", imagenBase64); // Agregar la imagen en Base64

        // Crear la solicitud PATCH con Volley usando MultipartRequest
        MultipartRequest multipartRequest = new MultipartRequest(
                "https://www.cevicheriaappapitest.somee.com/api/Usuario?id=" + usuarioId,  // URL de tu API con el ID del usuario
                params,
                response -> {
                    // Manejar la respuesta exitosa
                    Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Manejar el error de la solicitud
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                }
        );

        // Crear la cola de solicitudes y agregar la solicitud
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(multipartRequest);
    }



    private void cargarDatosUsuario(String userId) {
        String url = "https://www.cevicheriaappapitest.somee.com/api/Usuario?id=" + userId; // Ajusta la URL según tu API
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Crear solicitud GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        // Extraer datos del JSON
                        String nombre = response.getString("nombre");
                        imagenBase64 = response.getString("imagen");

                        // Decodificar imagen Base64
                        byte[] decodedString = Base64.decode(imagenBase64, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        // Actualizar la UI
                        etNombre.setText(nombre);
                        imvCircular.setImageBitmap(decodedBitmap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
        );

        // Agregar la solicitud a la cola de Volley
        requestQueue.add(jsonObjectRequest);
    }

}
