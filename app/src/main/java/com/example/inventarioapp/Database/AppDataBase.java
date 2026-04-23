package com.example.inventarioapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.inventarioapp.Model.Cliente;
import com.example.inventarioapp.Model.DetallePedido;
import com.example.inventarioapp.Model.Pedido;
import com.example.inventarioapp.Model.Producto;

@Database(
        entities = {Producto.class, Cliente.class, Pedido.class, DetallePedido.class},

        version = 3
)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase instancia;

    public abstract ProductoDao productoDao();
    public abstract ClientDao clienteDao();
    public abstract PedidoDao pedidoDao();              // ← nuevo
    public abstract DetallePedidoDao detallePedidoDao(); // ← nuevo

    public static synchronized AppDataBase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "inventario_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instancia;
    }
}