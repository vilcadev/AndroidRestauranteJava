package com.example.cevicheriaapp.fragmentos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cevicheriaapp.R;

public class MesasFragment extends Fragment {

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

        // Configurar los botones de las mesas
        Button btnMesa1 = view.findViewById(R.id.mesa_1);
        Button btnMesa2 = view.findViewById(R.id.mesa_2);
        Button btnMesa3 = view.findViewById(R.id.mesa_3);
        Button btnMesa4 = view.findViewById(R.id.mesa_4);
        Button btnMesa5 = view.findViewById(R.id.mesa_5);
        Button btnMesa6 = view.findViewById(R.id.mesa_6);

        // Listener para mesa 1
        btnMesa1.setOnClickListener(v -> navigateToOrderFragment(1));
        // Listener para mesa 2
        btnMesa2.setOnClickListener(v -> navigateToOrderFragment(2));
        // Listener para mesa 3
        btnMesa3.setOnClickListener(v -> navigateToOrderFragment(3));
        // Listener para mesa 4
        btnMesa4.setOnClickListener(v -> navigateToOrderFragment(4));
        // Listener para mesa 5
        btnMesa5.setOnClickListener(v -> navigateToOrderFragment(5));
        // Listener para mesa 6
        btnMesa6.setOnClickListener(v -> navigateToOrderFragment(6));

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
}
