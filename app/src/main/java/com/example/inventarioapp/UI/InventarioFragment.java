package com.example.inventarioapp.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Database.FirebaseRepository;
import com.example.inventarioapp.Model.Producto;
import com.example.inventarioapp.R;

import java.util.List;

public class InventarioFragment extends Fragment {

    private RecyclerView rvProductos;
    private ProductAdapter adapter;
    private Button btnAgregar, btnEditar, btnEliminar;
    private EditText etBuscar;
    private AppDataBase db;
    private Producto productoSeleccionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);

        // Conectar componentes
        rvProductos  = view.findViewById(R.id.rv_productos);
        btnAgregar   = view.findViewById(R.id.btn_agregar);
        btnEditar    = view.findViewById(R.id.btn_editar);
        btnEliminar  = view.findViewById(R.id.btn_eliminar);
        etBuscar     = view.findViewById(R.id.et_buscar);

        // Base de datos
        db = AppDataBase.getInstance(requireContext());

        // Configurar RecyclerView
        rvProductos.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Cargar productos
        cargarProductos();

        // Botón agregar
        btnAgregar.setOnClickListener(v -> {
            mostrarDialogoAgregar();
        });

        // Botón editar
        btnEditar.setOnClickListener(v -> {
            if (productoSeleccionado != null) {
                mostrarDialogoEditar(productoSeleccionado);
            }
        });

        // Botón eliminar
        btnEliminar.setOnClickListener(v -> {
            if (productoSeleccionado != null) {
                eliminarProducto(productoSeleccionado);
            }
        });

        // Búsqueda en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarProducto(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void cargarProductos() {
        List<Producto> productos = db.productoDao().getAllProductos();
        adapter = new ProductAdapter(productos, (producto, posicion) -> {
            // Cuando se toca una fila
            productoSeleccionado = producto;

            // Activar botones
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);
        });
        rvProductos.setAdapter(adapter);
    }

    private void buscarProducto(String texto) {
        List<Producto> resultados;
        if (texto.isEmpty()) {
            resultados = db.productoDao().getAllProductos();
        } else {
            resultados = db.productoDao().buscarPorNombre("%" + texto + "%");
        }
        adapter.actualizarLista(resultados);

        // Desactivar botones al buscar
        productoSeleccionado = null;
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void eliminarProducto(Producto producto) {
        db.productoDao().delete(producto);
        Toast.makeText(requireContext(),
                producto.getNombre() + " eliminado ✅",
                Toast.LENGTH_SHORT).show();

        // Recargar lista
        productoSeleccionado = null;
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        cargarProductos();
    }

    private void mostrarDialogoAgregar() {
        // Inflar el layout del diálogo
        View vistaDialogo = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialogo_producto, null);

        // Conectar campos
        EditText etNombre   = vistaDialogo.findViewById(R.id.et_nombre);
        EditText etMarca    = vistaDialogo.findViewById(R.id.et_marca);
        EditText etPrecio   = vistaDialogo.findViewById(R.id.et_precio);
        EditText etCantidad = vistaDialogo.findViewById(R.id.et_cantidad);

        // Construir el diálogo
        new AlertDialog.Builder(requireContext())
                .setTitle("Agregar producto")
                .setView(vistaDialogo)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    // Validar que no estén vacíos
                    String nombre   = etNombre.getText().toString().trim();
                    String marca    = etMarca.getText().toString().trim();
                    String precioStr   = etPrecio.getText().toString().trim();
                    String cantidadStr = etCantidad.getText().toString().trim();

                    if (nombre.isEmpty() || marca.isEmpty() ||
                            precioStr.isEmpty() || cantidadStr.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Por favor llena todos los campos ⚠️",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Crear el producto
                    double precio   = Double.parseDouble(precioStr);
                    int cantidad    = Integer.parseInt(cantidadStr);
                    Producto nuevo  = new Producto(nombre, precio, cantidad, marca);

                    // Guardar en Room
                    db.productoDao().insert(nuevo);

                    // Guardar en Firebase
                    new FirebaseRepository().subirProducto(nuevo);

                    Toast.makeText(requireContext(),
                            nombre + " agregado ✅",
                            Toast.LENGTH_SHORT).show();

                    // Recargar lista
                    cargarProductos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void mostrarDialogoEditar(Producto producto) {
        View vistaDialogo = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialogo_producto, null);

        EditText etNombre   = vistaDialogo.findViewById(R.id.et_nombre);
        EditText etMarca    = vistaDialogo.findViewById(R.id.et_marca);
        EditText etPrecio   = vistaDialogo.findViewById(R.id.et_precio);
        EditText etCantidad = vistaDialogo.findViewById(R.id.et_cantidad);

        // Prellenar con datos actuales
        etNombre.setText(producto.getNombre());
        etMarca.setText(producto.getMarca());
        etPrecio.setText(String.valueOf(producto.getPrecio()));
        etCantidad.setText(String.valueOf(producto.getCantidad()));

        new AlertDialog.Builder(requireContext())
                .setTitle("Editar producto")
                .setView(vistaDialogo)
                .setPositiveButton("Guardar", (dialog, which) -> {

                    String nombre      = etNombre.getText().toString().trim();
                    String marca       = etMarca.getText().toString().trim();
                    String precioStr   = etPrecio.getText().toString().trim();
                    String cantidadStr = etCantidad.getText().toString().trim();

                    if (nombre.isEmpty() || marca.isEmpty() ||
                            precioStr.isEmpty() || cantidadStr.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Por favor llena todos los campos ⚠️",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Actualizar el producto
                    producto.setNombre(nombre);
                    producto.setMarca(marca);
                    producto.setPrecio(Double.parseDouble(precioStr));
                    producto.setCantidad(Integer.parseInt(cantidadStr));

                    // Guardar en Room
                    db.productoDao().update(producto);

                    // Actualizar en Firebase
                    new FirebaseRepository().subirProducto(producto);

                    Toast.makeText(requireContext(),
                            nombre + " actualizado ✅",
                            Toast.LENGTH_SHORT).show();

                    // Recargar lista
                    productoSeleccionado = null;
                    btnEditar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                    cargarProductos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
