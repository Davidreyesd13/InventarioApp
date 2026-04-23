package com.example.inventarioapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
//inicio de guardar en base de datos
@Entity(tableName = "clientes")
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String numeroTelefono;
    private double saldo;
//constructores
    public Cliente(String nombre, String numeroTelefono, double saldo) {
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;

    }

    public Cliente() {
    }
//Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
