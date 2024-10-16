package com.example.cevicheriaapp.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cevicheriaapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SesionActivity extends AppCompatActivity implements View.OnClickListener{
    EditText    txtUsuario, txtContra;
    Button  btnIngresar;
    CheckBox chkRecordar;

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Button googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtUsuario = findViewById(R.id.logTxtUsuario);
        txtContra = findViewById(R.id.logTxtContrasena);
        btnIngresar = findViewById(R.id.logBtnIngresar);

        chkRecordar = findViewById(R.id.logChkRecordar);

        TextView registerTextView = findViewById(R.id.registerTextView);

        // Configura el OnClickListener
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la nueva Activity (RegisterActivity, por ejemplo)
                //llamar a la otra actividad
                Intent sesion = new Intent(SesionActivity.this,RegistroActivity.class);
                startActivity(sesion);
            }
        });


        // Inicializa SharedPreferences
       SharedPreferences sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE);

        // Verificar si la sesión sigue activa
        if (sharedPreferences.getBoolean("sesionIniciada", false)) {
            // Si la sesión está activa, redirigir a MainActivity inmediatamente
            Intent main = new Intent(SesionActivity.this, MainActivity.class);
            Toast.makeText(this, "Sesión guardada", Toast.LENGTH_SHORT).show();
            startActivity(main);
            finish(); // Finaliza SesionActivity para evitar volver a esta pantalla
            return; // Termina la ejecución de onCreate
        }

        btnIngresar.setOnClickListener(this);

        cargarCredenciales();

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Usa tu client_id de la consola de Firebase
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginButton = findViewById(R.id.googleLoginButton);
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Resultado devuelto tras la interacción con Google Sign-In
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Inicio de sesión de Google fue exitoso, ahora autentica con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Si el inicio de sesión falla, muestra un mensaje
                Log.w("GoogleSignIn", "Error en el inicio de sesión con Google", e);
                Toast.makeText(SesionActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, actualiza la interfaz de usuario con la información del usuario
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(SesionActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        // Redirige a la actividad principal
                        startActivity(new Intent(SesionActivity.this, MainActivity.class));
                        finish();
                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje
                        Toast.makeText(SesionActivity.this, "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.logBtnIngresar){
            boolean recordarSesion = chkRecordar.isChecked();
            iniciarSesion(txtUsuario.getText().toString(), txtContra.getText().toString(), recordarSesion);

        }

    }

//    private void iniciarSesion(String string, String string1, boolean recordar) {
//        if(string.equals("omar@upn.pe") && string1.equals("Abc123$")){
//
//            if (recordar) {
//                // Guardar credenciales en SharedPreferences
//                guardarCredenciales(string, string1);
//            }
//
//            Intent main = new Intent(this, MainActivity.class);
//            main.putExtra("nombre", "Omar");
//            startActivity(main);
//            finish();
//        }
//        else{
//            Toast.makeText(this,"Verificar cuenta",Toast.LENGTH_LONG).show();
//        }
//    }

    private void iniciarSesion(String correo, String contrasena, boolean recordar) {

        // Verificar si los campos están vacíos
        if (correo.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese su correo", Toast.LENGTH_LONG).show();
            return;  // No continuar si el correo está vacío
        }

        if (contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese su contraseña", Toast.LENGTH_LONG).show();
            return;  // No continuar si la contraseña está vacía
        }

        // Lógica de inicio de sesión con FirebaseAuth
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // El inicio de sesión fue exitoso, obtener el usuario actual
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Si se selecciona "Recordar sesión", guardar las credenciales
                            if (recordar) {
                                guardarCredenciales(correo, contrasena);
                            }

                            // Redirigir a la MainActivity
                            Intent main = new Intent(SesionActivity.this, MainActivity.class);
                            main.putExtra("nombre", user.getEmail());  // Usar el correo como nombre
                            startActivity(main);
                            finish();

                        } else {
                            // Si el inicio de sesión falla, mostrar un mensaje
                            Toast.makeText(SesionActivity.this, "Correo o Contraseña Incorrecta, inténtelo de nuevo: " , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void guardarCredenciales(String usuario, String contrasena) {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", usuario);
        editor.putString("contrasena", contrasena);
        editor.putBoolean("sesionIniciada", true);
        editor.apply();
    }


    private void cargarCredenciales() {
        SharedPreferences preferences = getSharedPreferences("Sesion", MODE_PRIVATE);
        String usuario = preferences.getString("usuario", null);
        String contrasena = preferences.getString("contrasena", null);

        if (usuario != null && contrasena != null) {
            txtUsuario.setText(usuario);
            txtContra.setText(contrasena);
            chkRecordar.setChecked(true);  // Marcamos el checkbox si se cargaron las credenciales
        }
    }

}