package com.example.cevicheriaapp.clases;

public class Mesa {
    private String idMesa;
    private String nombreMesa;
    private int estadoMesa;

    // Constructor
    public Mesa(String idMesa, String nombreMesa, int estadoMesa) {
        this.idMesa = idMesa;
        this.nombreMesa = nombreMesa;
        this.estadoMesa = estadoMesa;
    }

    // Getters
    public String getIdMesa() {
        return idMesa;
    }

    public String getNombreMesa() {
        return nombreMesa;
    }

    public int getEstadoMesa() {
        return estadoMesa;
    }
}
