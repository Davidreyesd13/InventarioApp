package com.example.inventarioapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.inventarioapp.Model.Cliente;
import com.example.inventarioapp.Model.Producto;

@Database(
        entities = {Producto.class, Cliente.class},
        version = 1
)
public abstract class AppDataBase extends RoomDatabase {

    // Instancia única
    private static AppDataBase instancia;

    // DAOs
    public abstract ProductoDao productoDao();
    public abstract ClientDao clienteDao();

    // Método para obtener la instancia
    public static synchronized AppDataBase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "inventario_db"
                    )
                    .allowMainThreadQueries() // ← Solo para desarrollo
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instancia;
    }
}