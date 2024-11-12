package com.example.cevicheriaapp.fragmentos;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.clases.Mesa;
import com.google.gson.Gson;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PagosFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PagosFragment extends Fragment {

    private Mesa mesa;
    EditText seleccionarFecha;

    ImageButton mostrarCalendario, btnEmail, btnNumero;
    Button btnFinalizar;

    EditText etCorreo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PagosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PagosFragment newInstance(String param1, String param2) {
        PagosFragment fragment = new PagosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PagosFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pagos, container, false);

        seleccionarFecha = view.findViewById(R.id.etSeleccionarFecha);
        mostrarCalendario = view.findViewById(R.id.mostrarCalendario);

        btnFinalizar = view.findViewById(R.id.btnFinalizar);


        btnEmail = view.findViewById(R.id.mostrarEmail);
        btnNumero = view.findViewById(R.id.mostrarNumero);

        etCorreo = view.findViewById(R.id.etCorreoonumber);


        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensajeWhatsApp("941773032");
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCorreo.setHint("Ingrese su correo");  // Cambia el hint a correo
                etCorreo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); // Cambia a correo
            }
        });

        btnNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCorreo.setHint("Ingrese su número");  // Cambia el hint a número
                etCorreo.setInputType(InputType.TYPE_CLASS_NUMBER); // Cambia a número
            }
        });


        // Establecer el OnClickListener para el ImageButton
        mostrarCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarCalendario();  // Llamar al método para mostrar el calendario
            }
        });

        // Obtener el objeto Mesa desde el Bundle
        if (getArguments() != null) {
            String mesaJson = getArguments().getString("mesa");
            String totalGeneral = getArguments().getString("totalGeneral");


            double totalGeneralNumber = Double.parseDouble(totalGeneral);

            // Formatear como moneda
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "PE")); // Para Perú
            String totalFormatted = format.format(totalGeneralNumber);

            Gson gson = new Gson();
            mesa = gson.fromJson(mesaJson, Mesa.class);

            // Aquí puedes utilizar los datos de 'mesa', por ejemplo mostrar el nombre de la mesa
            TextView mesaTextView = view.findViewById(R.id.nombreMesaTextView);
            mesaTextView.setText(mesa.getNombreMesa());

            TextView totalId = view.findViewById(R.id.totalId);
            totalId.setText(totalFormatted);

            Log.d("ProductosSeleccionados", "Longitud del array: " + totalId);
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // Asignar evento click al botón btnSave
        Button productosButton = view.findViewById(R.id.btnProductos);
        productosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navegarOrder(v);
            }
        });

    }


    public void navegarOrder(View view){

        Gson gson = new Gson();
        String mesaJson = gson.toJson(mesa);

        // Enviar el JSON al PagosFragment a través de un Bundle
        Bundle bundle = new Bundle();
        bundle.putString("mesa", mesaJson);


        OrderFragment orderFragment = new OrderFragment();

        orderFragment.setArguments(bundle);


        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, orderFragment)
                .addToBackStack(null)
                .commit();
    }

    public void mostrarCalendario(){
        DatePickerDialog d = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                seleccionarFecha.setText(dayOfMonth+"/"+ month+1 +"/"+year);
            }
        }, 2024,0,1);
        d.show();
    }


    public void generarYCompartirPDF(String email) {
        try {
            // 1. Ruta y archivo para el PDF
            File pdfDir = new File(Environment.getExternalStorageDirectory(), "MisPDFs");
            if (!pdfDir.exists()) {
                pdfDir.mkdir();  // Crea la carpeta si no existe
            }
            File pdfFile = new File(pdfDir, "Formulario.pdf");

            // 2. Genera el PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Agrega contenido al PDF
            document.add(new Paragraph("Formulario de Contacto"));
            document.add(new Paragraph("Correo: " + email));

            // Cierra el documento
            document.close();
            writer.close();

            // 3. Prepara el archivo para compartir
            Uri pdfUri = Uri.fromFile(pdfFile); // Usa Uri.parse si necesitas compatibilidad con versiones más recientes de Android
            Intent compartirIntent = new Intent();
            compartirIntent.setAction(Intent.ACTION_SEND);
            compartirIntent.setType("application/pdf");
            compartirIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);

            // 4. Enviar por WhatsApp
            compartirIntent.setPackage("com.whatsapp"); // Especifica WhatsApp
            startActivity(compartirIntent);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void enviarMensajeWhatsApp(String numeroTelefono) {
        try {
            // Define el mensaje de texto que deseas enviar
            String mensaje = "Gracias por su compra";

            // Construye el URI para abrir el chat de WhatsApp con el mensaje
            Uri uri = Uri.parse("https://wa.me/" + numeroTelefono + "?text=" + Uri.encode(mensaje));

            // Crea el Intent con ACTION_VIEW para abrir el enlace
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            // Verifica que hay una aplicación para manejar el Intent
            if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}