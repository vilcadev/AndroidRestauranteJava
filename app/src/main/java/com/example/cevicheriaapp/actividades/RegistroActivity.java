package com.example.cevicheriaapp.actividades;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.cevicheriaapp.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText,confirmPasswordEditText;
    private Button registerButton, cancelButton,takePhotoButton;
    private FirebaseAuth mAuth;
    private ImageView photoPreviewImageView;


    private Bitmap userPhoto;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        photoPreviewImageView = findViewById(R.id.photoPreviewImageView);


        takePhotoButton.setOnClickListener(v -> openCamera());

        // Aquí falta inicializar los botones
        registerButton = findViewById(R.id.registerButton); // Asegúrate de que el ID coincide
        cancelButton = findViewById(R.id.cancelButton);

        // Acción para el botón Registrar
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserBackend();
            }
        });

        // Acción para el botón Cancelar
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Cierra la actividad
            }
        });

    }

    // Solicitar permiso de cámara si no ha sido concedido
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no ha sido concedido, lo pedimos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // El permiso ya está concedido, se puede abrir la cámara
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso fue concedido
                openCamera();
            } else {
                // El permiso fue denegado, puedes mostrar un mensaje de error
                Toast.makeText(this, "El permiso para usar la cámara es necesario", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        //}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                userPhoto = (Bitmap) extras.get("data");
                photoPreviewImageView.setImageBitmap(userPhoto);
            }
        }
    }


    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validar los campos
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Correo electrónico es requerido");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor, introduce un correo válido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Contraseña es requerida");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }


        // Crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso, actualiza la interfaz de usuario con la información del usuario
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistroActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Aquí podrías redirigir a otra actividad o hacer lo que necesites
                            finish();  // Cierra la actividad
                        } else {
                            // Si el registro falla, muestra un mensaje
                            Toast.makeText(RegistroActivity.this, "Fallo en el registro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void registerUserBackend() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPasswordStrong(password)) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, un número y un carácter especial", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPhoto == null) {
            Toast.makeText(this, "Por favor toma una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enviar los datos al servidor
        sendDataToServer(name, email, password, convertBitmapToBase64(userPhoto));
    }


    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private boolean isPasswordStrong(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
    }

    private void sendDataToServer(String name, String email, String password, String photoBase64) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://www.cevicheriaappapitest.somee.com/api/Usuario";

        // Log para verificar los datos antes de enviarlos
        Log.d("DEBUG", "Datos a enviar:");
        Log.d("DEBUG", "Nombre: " + name);
        Log.d("DEBUG", "Correo: " + email);
        Log.d("DEBUG", "Contraseña: " + password);
        Log.d("DEBUG", "Foto (Base64): " + photoBase64.substring(0, Math.min(100, photoBase64.length())) + "..."); // Muestra solo los primeros 100 caracteres

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Manejar respuesta exitosa
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                },
                error -> {
                    // Manejar errores
                    Toast.makeText(this, "Error en el registro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Preparar parámetros para enviar al servidor
                Map<String, String> params = new HashMap<>();
                params.put("Nombre", name);
                params.put("Correo", email);
                params.put("Contrasena", password);
                params.put("Imagen", photoBase64);


                // Log para mostrar los parámetros enviados
                Log.d("DEBUG", "Parámetros enviados: " + params.toString());

                return params;
            }



        };

        // Agregar la solicitud a la cola
        requestQueue.add(stringRequest);
    }

}