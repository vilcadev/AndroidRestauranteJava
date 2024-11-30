package com.example.cevicheriaapp.clases;

public class Delivery {

    private String idDelivery;
    private String nombre;
    private int estadoDelivery;
    private String fecha;
    private double latitud;
    private double longitud;

    public Delivery(String idDelivery, String nombre, int estadoDelivery, String fecha, double latitud, double longitud) {
        this.idDelivery = idDelivery;
        this.nombre = nombre;
        this.estadoDelivery = estadoDelivery;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }



    // Getters and setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstadoDelivery() {
        return estadoDelivery;
    }

    public void setEstadoDelivery(int estadoDelivery) {
        this.estadoDelivery = estadoDelivery;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }
}

