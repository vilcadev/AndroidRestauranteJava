package com.example.cevicheriaapp.clases;
import java.util.UUID;

public class Platillo {
    private String idPlatillo;  // UUID para el identificador único
    private String nombre;     // Nombre del platillo
    private double precioUnitario; // Precio del platillo
    private String imagenUrl;  // URL de la imagen del platillo
    private Integer cantidad;


    // Constructor con parámetros
    public Platillo(String idPlatillo, String nombre, double precioUnitario, String imagenUrl, Integer  cantidad) {
        this.idPlatillo = idPlatillo;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.imagenUrl = imagenUrl;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public String getIdPlatillo() {
        return idPlatillo;
    }

    public void setIdPlatillo(String idPlatillo) {
        this.idPlatillo = idPlatillo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer  getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer  cantidad) {
        this.cantidad = cantidad;
    }
}
