package com.example.inventarioapp.Model;

import java.util.ArrayList;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
//inicio de guardar en base de datos
@Entity(tableName = "Clientes")
public class Cliente {
    String nombre;
    String numeroTelefono;
    double saldo;
    private int id;
//constructores
    public Cliente(String nombre, String numeroTelefono, double saldo, int id) {
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.saldo = saldo;
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
