package com.example.cevicheriaapp.clases;

public class Pedido {
    private String idPlatillo;
    private String nombre;
    private String imagen;
    private double precioTotal;
    private int cantidad;

    // Constructor, getters y setters

    public Pedido(String idPlatillo, String nombre, String imagen, double precioTotal, int cantidad) {
        this.idPlatillo = idPlatillo;
        this.nombre = nombre;
        this.imagen = imagen;
        this.precioTotal = precioTotal;
        this.cantidad = cantidad;
    }

    public String getIdPlatillo() {
        return idPlatillo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public int getCantidad() {
        return cantidad;
    }
}
