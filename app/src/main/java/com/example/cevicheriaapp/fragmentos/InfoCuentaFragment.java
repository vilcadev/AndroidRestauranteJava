package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.cevicheriaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoCuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoCuentaFragment extends Fragment {

    private Button btnVolver;

    public InfoCuentaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_cuenta, container, false);

        // Encontrar el botón en el layout
        btnVolver = view.findViewById(R.id.btnVolver);

        // Configurar el listener para el botón "Volver"
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retroceder al fragmento anterior (CuentaFragment)
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }
}