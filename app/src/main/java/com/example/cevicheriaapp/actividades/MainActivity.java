package com.example.cevicheriaapp.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.cevicheriaapp.R;
import com.example.cevicheriaapp.fragmentos.ConfiguracionFragment;
import com.example.cevicheriaapp.fragmentos.CuentaFragment;
import com.example.cevicheriaapp.fragmentos.DeliveryFragment;
import com.example.cevicheriaapp.fragmentos.MesasFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MesasFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_mesas);
        }





        getSupportActionBar().setTitle("");



        handleUserAuthentication();
    }

    private void handleUserAuthentication() {
        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        ImageView toolbarProfileImage = findViewById(R.id.toolbar_profile_image);

        if (currentUser!= null) {
            String photoUrl = currentUser.getPhotoUrl().toString();
            Glide.with(this)
                    .load(photoUrl)
                    .into(toolbarProfileImage);
        } else {

            toolbarProfileImage.setImageResource(R.drawable.logoinicio);

        }

        String idUsuario = getIntent().getStringExtra("idUsuario");

        if (idUsuario != null) {

            Log.d("DEBUG", "El idUsuario es: " + idUsuario);
        } else {

            Log.d("DEBUG", "No se recibió un idUsuario");
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_mesas) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MesasFragment()).commit();
        } else if (id == R.id.nav_delivery) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeliveryFragment()).commit();
        } else if (id == R.id.nav_cuenta) {

            CuentaFragment cuentaFragment = new CuentaFragment();


            Bundle bundle = new Bundle();
            bundle.putString("idUsuario", getIntent().getStringExtra("idUsuario"));


            cuentaFragment.setArguments(bundle);


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, cuentaFragment)
                    .commit();


        }
          else if (id == R.id.nav_configuracion) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfiguracionFragment()).commit();
        }
        else if (id == R.id.out_logout){


            FirebaseAuth.getInstance().signOut();


            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
            googleSignInClient.signOut().addOnCompleteListener(this, task -> {

                SharedPreferences sharedPreferences = getSharedPreferences("Sesion", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();


                Intent intent = new Intent(this, SesionActivity.class);
                startActivity(intent);
                finish();
            });

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}