package com.example.inventarioapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

//conexion de tablas
@Entity(tableName = "Productos",
foreignKeys = @ForeignKey(
        entity = Cliente.class,
        parentColumns = "Id",
        childColumns = "IdCliente",
        onDelete = ForeignKey.SET_NULL)
)
public class Producto {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private Integer IdCliente;
    private String Nombre;
    private double Precio;
    private int Cantidad;
    private String Marca;
//constructores
    public Producto() {

    }

    public Producto(String nombre, double precio, int cantidad, String marca) {
        Nombre = nombre;
        Precio = precio;
        Cantidad = cantidad;
        Marca = marca;
        this.IdCliente = null;

    }
//Getters y Setters
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public Integer getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(Integer idCliente) {
        IdCliente = idCliente;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
