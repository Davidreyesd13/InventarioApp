package com.example.inventarioapp.Database;
import androidx.room.Dao;
import  androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import com.example.inventarioapp.Model.Producto;
import java.util.List;

@Dao
public interface ProductoDao {
    @Insert
    void insert(Producto producto);

    @Update
    void update(Producto producto);

    @Delete
    void delete(Producto producto);
//Obtener todos los productos
    @Query("SELECT * FROM productos")
    List<Producto> getAllProductos();
//Buscar por nombre
    @Query("SELECT * FROM productos WHERE nombre  LIKE :nombre")
    List<Producto> buscarPorNombre(String nombre);
//Productos con Stock Bajo
    @Query("SELECT * FROM productos WHERE Cantidad <= :Cantidad")
    List<Producto> productosConStockBajo(int Cantidad);
    // Productos de un cliente específico
    @Query("SELECT * FROM productos WHERE idCliente =  :IdCliente")
    List<Producto> obtenerporCliente(int IdCliente);
}
