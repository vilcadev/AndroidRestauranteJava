package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.cevicheriaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarProducto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarProducto extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuscarProducto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuscarProducto.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscarProducto newInstance(String param1, String param2) {
        BuscarProducto fragment = new BuscarProducto();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_buscar_producto, container, false);

        // Encontrar el FrameLayout
        FrameLayout frameVerSeleccionados = view.findViewById(R.id.frame_ver_seleccionados);

        // Asignar el OnClickListener para navegar a otro fragmento
        frameVerSeleccionados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del nuevo fragmento al que quieres navegar
                OrderFragment nuevoFragmento = new OrderFragment();

                // Usar FragmentTransaction para cambiar de fragmento
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, nuevoFragmento);  // Reemplazar el fragmento actual
                transaction.addToBackStack(null);  // Agregar la transacción a la pila para permitir volver atrás
                transaction.commit();  // Confirmar la transacción
            }
        });

        return view;
    }
}