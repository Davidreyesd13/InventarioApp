package com.example.inventarioapp.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidos",
        foreignKeys = @ForeignKey(
                entity = Cliente.class,
                parentColumns = "id",
                childColumns = "clienteId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Pedido {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int clienteId;
    private double total;
    private boolean pagado;
    private String fecha;

    // Constructor principal
    public Pedido(int clienteId, double total, boolean pagado, String fecha) {
        this.clienteId = clienteId;
        this.total     = total;
        this.pagado    = pagado;
        this.fecha     = fecha;
    }

    // Constructor vacío obligatorio
    public Pedido() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}