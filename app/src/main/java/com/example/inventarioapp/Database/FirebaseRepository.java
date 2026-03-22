package com.example.inventarioapp.Database;
import com.example.inventarioapp.Model.Cliente;
import com.example.inventarioapp.Model.Producto;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;
public class FirebaseRepository {
    private FirebaseFirestore db;
    //nombres de las colleciones en firebase
    private static final String COL_Products="productos";
    private static final String COL_Clients="clientes";

    public FirebaseRepository() {
    }

    public FirebaseRepository(FirebaseFirestore db) {
        db = FirebaseFirestore.getInstance();
    }
    //subir un producto a Firebase
    public void subirProducto(Producto producto) {
        db.collection(COL_Products).document(String.valueOf(producto.getId())).set(producto)
                .addOnSuccessListener(unused -> System.out.println("Producto guardado en Firebase ✅"))
                .addOnFailureListener(e -> System.out.println("Error al guardar: " + e.getMessage()));

    }
    public void eliminarProducto(Producto producto) {
        db.collection(COL_Products)
                .document(String.valueOf(producto.getId()))
                .delete()
                .addOnSuccessListener(unused ->
                        System.out.println("Producto eliminado de Firebase ✅")
                );
    }
    public void sincronizarProductos(AppDataBase roomDb) {
        db.collection(COL_Products)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Producto> productos = querySnapshot
                            .toObjects(Producto.class);
                    // Guardar cada producto en Room
                    for (Producto p : productos) {
                        roomDb.productoDao().insert(p);
                    }
                    System.out.println("Sincronización completada ✅");
                })
                .addOnFailureListener(e ->
                        System.out.println("Error al sincronizar: " + e.getMessage())
                );
    }
    // ─────────────────────────────────────
    // CLIENTES
    // ─────────────────────────────────────

    // Subir un cliente a Firebase
    public void guardarCliente(Cliente cliente) {
        db.collection(COL_Clients)
                .document(String.valueOf(cliente.getId()))
                .set(cliente)
                .addOnSuccessListener(unused ->
                        System.out.println("Cliente guardado en Firebase ✅")
                )
                .addOnFailureListener(e ->
                        System.out.println("Error al guardar: " + e.getMessage())
                );
    }

    // Eliminar un cliente de Firebase
    public void eliminarCliente(Cliente cliente) {
        db.collection(COL_Clients)
                .document(String.valueOf(cliente.getId()))
                .delete()
                .addOnSuccessListener(unused ->
                        System.out.println("Cliente eliminado de Firebase ✅")
                );
    }
}
