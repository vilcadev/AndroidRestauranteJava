package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cevicheriaapp.R;

public class ResetPasswordFragment extends Fragment {

    private Button btnRestablecer;
    private Button btnCancelPass;
    private EditText txtPassAct, txtNewPass, txtConfNewPass;
    private ImageView imgShowPassAct, imgShowNewPass, imgShowConfNewPass;
    private boolean isPassActVisible = false;
    private boolean isNewPassVisible = false;
    private boolean isConfNewPassVisible = false;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        // Encontrar las vistas en el layout
        btnRestablecer = view.findViewById(R.id.btnRestablecer);
        btnCancelPass = view.findViewById(R.id.btnCancelPass);
        txtPassAct = view.findViewById(R.id.txtPassAct);
        txtNewPass = view.findViewById(R.id.txtNewPass);
        txtConfNewPass = view.findViewById(R.id.txtConfNewPass);
        imgShowPassAct = view.findViewById(R.id.imgShowPassAct);
        imgShowNewPass = view.findViewById(R.id.imgShowNewPass);
        imgShowConfNewPass = view.findViewById(R.id.imgShowConfNewPass);

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

        // Configurar el listener para el botón "Cancelar"
        btnCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        // Listener para mostrar/ocultar contraseña actual
        imgShowPassAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassActVisible) {
                    txtPassAct.setInputType(129); // Ocultar contraseña
                    imgShowPassAct.setImageResource(R.drawable.ic_visibility_off); // Cambia a tu ícono
                } else {
                    txtPassAct.setInputType(144); // Mostrar contraseña
                    imgShowPassAct.setImageResource(R.drawable.ic_visibility); // Cambia a tu ícono
                }
                txtPassAct.setSelection(txtPassAct.length()); // Mantener el cursor al final
                isPassActVisible = !isPassActVisible;
            }
        });

        // Listener para mostrar/ocultar nueva contraseña
        imgShowNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewPassVisible) {
                    txtNewPass.setInputType(129); // Ocultar contraseña
                    imgShowNewPass.setImageResource(R.drawable.ic_visibility_off); // Cambia a tu ícono
                } else {
                    txtNewPass.setInputType(144); // Mostrar contraseña
                    imgShowNewPass.setImageResource(R.drawable.ic_visibility); // Cambia a tu ícono
                }
                txtNewPass.setSelection(txtNewPass.length()); // Mantener el cursor al final
                isNewPassVisible = !isNewPassVisible;
            }
        });

        // Listener para mostrar/ocultar confirmación de nueva contraseña
        imgShowConfNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfNewPassVisible) {
                    txtConfNewPass.setInputType(129); // Ocultar contraseña
                    imgShowConfNewPass.setImageResource(R.drawable.ic_visibility_off); // Cambia a tu ícono
                } else {
                    txtConfNewPass.setInputType(144); // Mostrar contraseña
                    imgShowConfNewPass.setImageResource(R.drawable.ic_visibility); // Cambia a tu ícono
                }
                txtConfNewPass.setSelection(txtConfNewPass.length()); // Mantener el cursor al final
                isConfNewPassVisible = !isConfNewPassVisible;
            }
        });

        return view;
    }
}
