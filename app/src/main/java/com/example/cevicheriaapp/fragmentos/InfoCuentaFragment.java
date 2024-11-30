package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.cevicheriaapp.R;

public class InfoCuentaFragment extends Fragment {

    private Button  btnEditar, btnGuardar, btnCancelar;
    private EditText etNombreUsuario, etNombre, etApellido, etCorreo;

    // Variables para almacenar los valores iniciales antes de cualquier edición
    private String originalNombreUsuario, originalNombre, originalApellido, originalCorreo;

    private String idUsuario;

    public InfoCuentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_cuenta, container, false);

        // Encontrar los botones y EditTexts en el layout
        ImageView imgCamera = view.findViewById(R.id.imgCamera);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etCorreo = view.findViewById(R.id.etCorreo);

        // Deshabilitar los EditTexts inicialmente
        etNombreUsuario.setEnabled(false);
        etNombre.setEnabled(false);
        etApellido.setEnabled(false);
        etCorreo.setEnabled(false);

        // Ocultar botones Guardar y Cancelar inicialmente
        btnGuardar.setVisibility(View.GONE);
        btnCancelar.setVisibility(View.GONE);

        // Configurar el listener para el ImageView
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retroceder a la actividad anterior
                requireActivity().onBackPressed();
            }
        });

        // Configurar el listener para el botón "Editar"
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Guardar los valores actuales antes de editar
                originalNombreUsuario = etNombreUsuario.getText().toString();
                originalNombre = etNombre.getText().toString();
                originalApellido = etApellido.getText().toString();
                originalCorreo = etCorreo.getText().toString();

                // Habilitar los campos de EditText para edición
                etNombreUsuario.setEnabled(true);
                etNombre.setEnabled(true);
                etApellido.setEnabled(true);
                etCorreo.setEnabled(true);

                // Cambiar el color del texto para dar feedback visual
                etNombreUsuario.setTextColor(getResources().getColor(android.R.color.black));
                etNombre.setTextColor(getResources().getColor(android.R.color.black));
                etApellido.setTextColor(getResources().getColor(android.R.color.black));
                etCorreo.setTextColor(getResources().getColor(android.R.color.black));

                // Ocultar el botón "Editar" y mostrar los botones "Guardar" y "Cancelar"
                btnEditar.setVisibility(View.GONE);
                btnGuardar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
            }
        });

        // Configurar el listener para el botón "Guardar"
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar lógica para guardar los datos en una base de datos o similar

                // Deshabilitar los campos de EditText de nuevo
                etNombreUsuario.setEnabled(false);
                etNombre.setEnabled(false);
                etApellido.setEnabled(false);
                etCorreo.setEnabled(false);

                // Ocultar botones Guardar y Cancelar, y mostrar el botón Editar
                btnGuardar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
                btnEditar.setVisibility(View.VISIBLE);
            }
        });

        // Configurar el listener para el botón "Cancelar"
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Revertir los cambios y restaurar los valores originales
                etNombreUsuario.setText(originalNombreUsuario);
                etNombre.setText(originalNombre);
                etApellido.setText(originalApellido);
                etCorreo.setText(originalCorreo);

                // Deshabilitar los campos de EditText de nuevo
                etNombreUsuario.setEnabled(false);
                etNombre.setEnabled(false);
                etApellido.setEnabled(false);
                etCorreo.setEnabled(false);

                // Ocultar botones Guardar y Cancelar, y mostrar el botón Editar
                btnGuardar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
                btnEditar.setVisibility(View.VISIBLE);
            }
        });

        // Recuperar el idUsuario pasado desde el fragmento anterior
        if (getArguments() != null) {
            idUsuario = getArguments().getString("idUsuario");
        }

        // Log para verificar que el idUsuario se recuperó correctamente
        Log.d("DEBUG", "El idUsuario recibido en InfoCuentaFragment es: " + idUsuario);



        return view;
    }
}
