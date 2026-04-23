package com.example.inventarioapp.UI;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Model.DetallePedido;
import com.example.inventarioapp.Model.Pedido;
import com.example.inventarioapp.Model.Producto;
import com.example.inventarioapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewShopFragment extends Fragment {

    public static final String ARG_CLIENTE_ID     = "clienteId";
    public static final String ARG_CLIENTE_NOMBRE = "clienteNombre";

    private RecyclerView rvDetalle;
    private DetallepedidoAdapter adapter;
    private Button btnAgregarProducto, btnCobrado, btnFiado;
    private TextView tvTotal;
    private AppDataBase db;
    private int clienteId;
    private String clienteNombre;

    public static NewShopFragment newInstance(int clienteId, String clienteNombre) {
        NewShopFragment fragment = new NewShopFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLIENTE_ID, clienteId);
        args.putString(ARG_CLIENTE_NOMBRE, clienteNombre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_shop, container, false);

        // Recibir datos
        if (getArguments() != null) {
            clienteId     = getArguments().getInt(ARG_CLIENTE_ID);
            clienteNombre = getArguments().getString(ARG_CLIENTE_NOMBRE);
        }

        // Conectar componentes
        rvDetalle          = view.findViewById(R.id.rv_detalle);
        btnAgregarProducto = view.findViewById(R.id.btn_agregar_producto);
        btnCobrado         = view.findViewById(R.id.btn_cobrado);
        btnFiado           = view.findViewById(R.id.btn_fiado);
        tvTotal            = view.findViewById(R.id.tv_total_compra);

        db = AppDataBase.getInstance(requireContext());

        // Configurar RecyclerView
        adapter = new DetallepedidoAdapter();
        rvDetalle.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDetalle.setAdapter(adapter);

        // Botón agregar producto
        btnAgregarProducto.setOnClickListener(v -> mostrarDialogoProducto());

        // Botón cobrado
        btnCobrado.setOnClickListener(v -> guardarPedido(true));

        // Botón fiado
        btnFiado.setOnClickListener(v -> guardarPedido(false));

        return view;
    }

    private void mostrarDialogoProducto() {
        // Obtener productos disponibles
        List<Producto> productos = db.productoDao().getAllProductos();

        if (productos.isEmpty()) {
            Toast.makeText(requireContext(),
                    "No hay productos en inventario ⚠️",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear lista de nombres para el Spinner
        String[] nombres = new String[productos.size()];
        for (int i = 0; i < productos.size(); i++) {
            nombres[i] = productos.get(i).getNombre() +
                    " - " + productos.get(i).getMarca() +
                    " (L." + productos.get(i).getPrecio() + ")";
        }

        // Layout del diálogo
        View vistaDialogo = LayoutInflater.from(requireContext())
                .inflate(android.R.layout.select_dialog_item, null);

        // Construir diálogo con Spinner
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(android.R.layout.simple_list_item_1, null);

        // Crear diálogo manual
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar producto");

        // Spinner para seleccionar producto
        Spinner spinner = new Spinner(requireContext());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombres
        );
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(spinnerAdapter);

        // EditText para cantidad
        EditText etCantidad = new EditText(requireContext());
        etCantidad.setHint("Cantidad");
        etCantidad.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        // Layout del diálogo
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        layout.addView(spinner);
        layout.addView(etCantidad);

        builder.setView(layout);
        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String cantidadStr = etCantidad.getText().toString().trim();

            if (cantidadStr.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Ingresa la cantidad ⚠️",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int cantidad = Integer.parseInt(cantidadStr);
            int indice   = spinner.getSelectedItemPosition();
            Producto productoSeleccionado = productos.get(indice);

            // Verificar stock disponible
            if (cantidad > productoSeleccionado.getCantidad()) {
                Toast.makeText(requireContext(),
                        "Stock insuficiente — disponible: " +
                                productoSeleccionado.getCantidad(),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Agregar al carrito
            DetallepedidoAdapter.DetalleItem item =
                    new DetallepedidoAdapter.DetalleItem(
                            productoSeleccionado.getNombre() +
                                    " - " + productoSeleccionado.getMarca(),
                            cantidad,
                            productoSeleccionado.getPrecio(),
                            productoSeleccionado.getId()
                    );
            adapter.agregarItem(item);

            // Actualizar total
            tvTotal.setText(String.format("TOTAL: L.%.2f", adapter.calcularTotal()));
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void guardarPedido(boolean pagado) {
        if (adapter.getItems().isEmpty()) {
            Toast.makeText(requireContext(),
                    "Agrega al menos un producto ⚠️",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Fecha actual
        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date());

        // Crear pedido
        Pedido pedido = new Pedido(clienteId, adapter.calcularTotal(), pagado, fecha);
        long pedidoId = db.pedidoDao().insertar(pedido);

        // Guardar detalles y descontar inventario
        for (DetallepedidoAdapter.DetalleItem item : adapter.getItems()) {

            // Insertar detalle
            DetallePedido detalle = new DetallePedido(
                    (int) pedidoId,
                    item.productoId,
                    item.cantidad,
                    item.precioUnitario
            );
            db.detallePedidoDao().insertar(detalle);

            // Descontar del inventario
            Producto producto = db.productoDao().getProductoById(item.productoId);

            if (producto != null) {
                producto.setCantidad(producto.getCantidad() - item.cantidad);
                db.productoDao().update(producto);
            }
        }

        String mensaje = pagado ? "Compra cobrada ✅" : "Compra registrada como fiado 💳";
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();

        // Volver a pedidos
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}