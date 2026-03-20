package com.example.inventarioapp.Database;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.inventarioapp.Model.Cliente;
import java.util.List;
@Dao
public interface ClientDao {
    //Insertar cliente
@Insert
void insert(Cliente cliente);
//Actualizar cliente
@Update
void update(Cliente cliente);
    //obtener todos los clientes
@Query("SELECT * FROM clientes")
List<Cliente> obtenerTodos();
//eliminar cliente
@Delete
void delete(Cliente cliente);
//Buscar por nombre
@Query("SELECT * FROM clientes WHERE nombre LIKE :nombre")
List<Cliente> buscarPorNombre(String nombre);
//Clientes con saldo pendiente
@Query("SELECT * FROM clientes WHERE saldo > 0")
List<Cliente> clientesConSaldoPendiente();
}
