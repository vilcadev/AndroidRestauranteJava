package com.example.cevicheriaapp.clases;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {


    public static String hashPassword(String password) {
        try {
            // Crear una instancia de MessageDigest con SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generar el hash de la contraseña
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Codificar el hash en Base64
            return Base64.encodeToString(hashBytes, Base64.NO_WRAP);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Retorna null si ocurre un error
        }
    }
}
