package com.example.inventarioapp.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventarioapp.Model.Pedido;
import java.util.List;

@Dao
public interface PedidoDao {

    @Insert
    long insertar(Pedido pedido); // ← long devuelve el id generado

    @Update
    void actualizar(Pedido pedido);

    @Delete
    void eliminar(Pedido pedido);

    // Todos los pedidos de un cliente
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId")
    List<Pedido> obtenerPorCliente(int clienteId);

    // Pedidos pendientes de un cliente
    @Query("SELECT * FROM pedidos WHERE clienteId = :clienteId AND pagado = 0")
    List<Pedido> obtenerPendientesPorCliente(int clienteId);

    // Saldo total pendiente de un cliente
    @Query("SELECT SUM(total) FROM pedidos WHERE clienteId = :clienteId AND pagado = 0")
    double obtenerSaldoPendiente(int clienteId);
}