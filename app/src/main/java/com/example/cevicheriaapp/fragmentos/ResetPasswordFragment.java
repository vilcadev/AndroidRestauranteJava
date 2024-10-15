package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.cevicheriaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    private Button btnVolver2;
    private Button btnRestablecer;
    private EditText txtPassAct, txtNewPass, txtConfNewPass;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        // Encontrar las vistas en el layout
        btnVolver2 = view.findViewById(R.id.btnVolver2);
        btnRestablecer = view.findViewById(R.id.btnRestablecer);
        txtPassAct = view.findViewById(R.id.txtPassAct);
        txtNewPass = view.findViewById(R.id.txtNewPass);
        txtConfNewPass = view.findViewById(R.id.txtConfNewPass);

        // Configurar el listener para el botón "Volver"
        btnVolver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retroceder al fragmento anterior (CuentaFragment o cualquier otro)
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        // Configurar el listener para el botón "Restablecer Contraseña"
        btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener las contraseñas ingresadas
                String passActual = txtPassAct.getText().toString();
                String nuevaPass = txtNewPass.getText().toString();
                String confNuevaPass = txtConfNewPass.getText().toString();

                // Verificar que los campos no estén vacíos y que las contraseñas coincidan
                if (passActual.isEmpty() || nuevaPass.isEmpty() || confNuevaPass.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!nuevaPass.equals(confNuevaPass)) {
                    Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para restablecer la contraseña
                    // Aquí puedes agregar el código para actualizar la contraseña en la base de datos o el servidor
                    Toast.makeText(getContext(), "Contraseña restablecida exitosamente", Toast.LENGTH_SHORT).show();

                    // Regresar al fragmento anterior
                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack();
                    }
                }
            }
        });

        return view;
    }
}