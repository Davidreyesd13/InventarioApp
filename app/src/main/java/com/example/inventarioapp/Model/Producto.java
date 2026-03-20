package com.example.inventarioapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

//conexion de tablas
@Entity(tableName = "productos",
foreignKeys = @ForeignKey(
        entity = Cliente.class,
        parentColumns = "id",
        childColumns = "idCliente",
        onDelete = ForeignKey.SET_NULL)
)
public class Producto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer idCliente;
    private String nombre;
    private double precio;
    private int cantidad;
    private String marca;
//constructores
    public Producto() {

    }

    public Producto(String nombre, double precio, int cantidad, String marca) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.marca = marca;
        this.idCliente = null;

    }
//Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Nombre) {
        nombre = Nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double Precio) {
        precio = Precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int Cantidad) {
        cantidad = Cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String Marca) {
        marca = Marca;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer IdCliente) {
        this.idCliente = IdCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        id = Id;
    }
}
