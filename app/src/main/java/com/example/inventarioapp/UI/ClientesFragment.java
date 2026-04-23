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
import com.example.inventarioapp.Model.Cliente;
import com.example.inventarioapp.R;

import java.util.List;

public class ClientesFragment extends Fragment {

    private RecyclerView rvClientes;
    private ClienteAdapter adapter;
    private Button btnAgregar, btnVerPedidos, btnAbonar, btnEliminar;
    private EditText etBuscar;
    private AppDataBase db;
    private Cliente clienteSeleccionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        rvClientes    = view.findViewById(R.id.rv_clientes);
        btnAgregar    = view.findViewById(R.id.btn_agregar_cliente);
        btnVerPedidos = view.findViewById(R.id.btn_ver_pedidos);
        btnAbonar     = view.findViewById(R.id.btn_abonar);
        btnEliminar   = view.findViewById(R.id.btn_eliminar_cliente);
        etBuscar      = view.findViewById(R.id.et_buscar_cliente);

        db = AppDataBase.getInstance(requireContext());
        rvClientes.setLayoutManager(new LinearLayoutManager(requireContext()));
        cargarClientes();

        // Botón agregar
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());

        // Botón ver pedidos
        btnVerPedidos.setOnClickListener(v -> {
            if (clienteSeleccionado != null) {
                pedidosFragment fragment = pedidosFragment.newInstance(
                        clienteSeleccionado.getId(),
                        clienteSeleccionado.getNombre()
                );
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Botón abonar
        btnAbonar.setOnClickListener(v -> {
            if (clienteSeleccionado != null) {
                mostrarDialogoAbono(clienteSeleccionado);
            }
        });

        // Botón eliminar
        btnEliminar.setOnClickListener(v -> {
            if (clienteSeleccionado != null) {
                eliminarCliente(clienteSeleccionado);
            }
        });

        // Búsqueda
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarCliente(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void cargarClientes() {
        List<Cliente> clientes = db.clienteDao().obtenerTodos();
        adapter = new ClienteAdapter(clientes, (cliente, posicion) -> {
            clienteSeleccionado = cliente;
            btnVerPedidos.setEnabled(true);
            btnAbonar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }, db); // ← pasamos db para calcular saldo
        rvClientes.setAdapter(adapter);
    }

    private void buscarCliente(String texto) {
        List<Cliente> resultados;
        if (texto.isEmpty()) {
            resultados = db.clienteDao().obtenerTodos();
        } else {
            resultados = db.clienteDao().buscarPorNombre("%" + texto + "%");
        }
        adapter.actualizarLista(resultados);
        clienteSeleccionado = null;
        btnVerPedidos.setEnabled(false);
        btnAbonar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void eliminarCliente(Cliente cliente) {
        db.clienteDao().delete(cliente);
        new FirebaseRepository().eliminarCliente(cliente);
        Toast.makeText(requireContext(),
                cliente.getNombre() + " eliminado ✅",
                Toast.LENGTH_SHORT).show();
        clienteSeleccionado = null;
        btnVerPedidos.setEnabled(false);
        btnAbonar.setEnabled(false);
        btnEliminar.setEnabled(false);
        cargarClientes();
    }

    private void mostrarDialogoAbono(Cliente cliente) {
        // Calcular saldo actual
        double saldoActual = db.clienteDao().getpendingaccount(cliente.getId());

        EditText etMonto = new EditText(requireContext());
        etMonto.setHint("Monto a abonar");
        etMonto.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        android.widget.TextView tvSaldo = new android.widget.TextView(requireContext());
        tvSaldo.setText(String.format("Saldo actual: L.%.2f", saldoActual));
        tvSaldo.setTextSize(16);
        tvSaldo.setPadding(0, 0, 0, 20);

        layout.addView(tvSaldo);
        layout.addView(etMonto);

        new AlertDialog.Builder(requireContext())
                .setTitle("Abonar — " + cliente.getNombre())
                .setView(layout)
                .setPositiveButton("Abonar", (dialog, which) -> {
                    String montoStr = etMonto.getText().toString().trim();
                    if (montoStr.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Ingresa el monto ⚠️",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double monto = Double.parseDouble(montoStr);

                    if (monto > saldoActual) {
                        Toast.makeText(requireContext(),
                                "El abono no puede ser mayor al saldo ⚠️",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Reducir el total del pedido más antiguo pendiente
                    List<com.example.inventarioapp.Model.Pedido> pendientes =
                            db.pedidoDao().obtenerPendientesPorCliente(cliente.getId());

                    double montoRestante = monto;
                    for (com.example.inventarioapp.Model.Pedido pedido : pendientes) {
                        if (montoRestante <= 0) break;

                        if (montoRestante >= pedido.getTotal()) {
                            // Paga este pedido completo
                            montoRestante -= pedido.getTotal();
                            pedido.setPagado(true);
                            db.pedidoDao().actualizar(pedido);
                        } else {
                            // Abono parcial
                            pedido.setTotal(pedido.getTotal() - montoRestante);
                            montoRestante = 0;
                            db.pedidoDao().actualizar(pedido);
                        }
                    }

                    Toast.makeText(requireContext(),
                            String.format("Abono de L.%.2f registrado ✅", monto),
                            Toast.LENGTH_SHORT).show();
                    cargarClientes();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoAgregar() {
        View vistaDialogo = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialogo_cliente, null);

        EditText etNombre   = vistaDialogo.findViewById(R.id.et_nombre_cliente);
        EditText etTelefono = vistaDialogo.findViewById(R.id.et_telefono_cliente);

        new AlertDialog.Builder(requireContext())
                .setTitle("Agregar cliente")
                .setView(vistaDialogo)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre   = etNombre.getText().toString().trim();
                    String telefono = etTelefono.getText().toString().trim();

                    if (nombre.isEmpty() || telefono.isEmpty()) {
                        Toast.makeText(requireContext(),
                                "Nombre y teléfono son obligatorios ⚠️",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cliente nuevo = new Cliente(nombre, telefono,0);
                    db.clienteDao().insert(nuevo);
                    new FirebaseRepository().guardarCliente(nuevo);

                    Toast.makeText(requireContext(),
                            nombre + " agregado ✅",
                            Toast.LENGTH_SHORT).show();
                    cargarClientes();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}