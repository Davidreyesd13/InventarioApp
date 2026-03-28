package com.example.inventarioapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.inventarioapp.Model.DetallePedido;
import java.util.List;

@Dao
public interface DetallePedidoDao {

    @Insert
    void insertar(DetallePedido detalle);

    @Delete
    void eliminar(DetallePedido detalle);

    // Detalles de un pedido específico
    @Query("SELECT * FROM detalle_pedido WHERE pedidoId = :pedidoId")
    List<DetallePedido> obtenerPorPedido(int pedidoId);
}