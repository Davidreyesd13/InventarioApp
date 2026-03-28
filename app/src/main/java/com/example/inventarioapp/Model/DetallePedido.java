package com.example.inventarioapp.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "detalle_pedido",
        foreignKeys = {
                @ForeignKey(
                        entity = Pedido.class,
                        parentColumns = "id",
                        childColumns = "pedidoId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Producto.class,
                        parentColumns = "id",
                        childColumns = "productoId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class DetallePedido {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int pedidoId;
    private int productoId;
    private int cantidad;
    private double precioUnitario;

    // Constructor principal
    public DetallePedido(int pedidoId, int productoId,
                         int cantidad, double precioUnitario) {
        this.pedidoId       = pedidoId;
        this.productoId     = productoId;
        this.cantidad       = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Constructor vacío
    public DetallePedido() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}