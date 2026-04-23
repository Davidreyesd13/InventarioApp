package com.example.inventarioapp.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Model.Pedido;
import com.example.inventarioapp.R;

import java.util.List;

public class pedidosFragment extends Fragment {

    // Clave para recibir el id del cliente
    public static final String ARG_CLIENTE_ID     = "clienteId";
    public static final String ARG_CLIENTE_NOMBRE = "clienteNombre";

    private RecyclerView rvPedidos;
    private pedidoAdapter adapter;
    private Button btnNuevaCompra, btnMarcarPagado;
    private TextView tvSaldoTotal;
    private AppDataBase db;
    private int clienteId;
    private String clienteNombre;
    private Pedido pedidoSeleccionado;

    // Método estático para crear el Fragment con datos
    public static pedidosFragment newInstance(int clienteId, String clienteNombre) {
        pedidosFragment fragment = new pedidosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLIENTE_ID, clienteId);
        args.putString(ARG_CLIENTE_NOMBRE, clienteNombre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        // Recibir datos del cliente
        if (getArguments() != null) {
            clienteId     = getArguments().getInt(ARG_CLIENTE_ID);
            clienteNombre = getArguments().getString(ARG_CLIENTE_NOMBRE);
        }

        // Conectar componentes
        rvPedidos      = view.findViewById(R.id.rv_pedidos);
        btnNuevaCompra = view.findViewById(R.id.btn_nueva_compra);
        btnMarcarPagado= view.findViewById(R.id.btn_marcar_pagado);
        tvSaldoTotal   = view.findViewById(R.id.tv_saldo_total);

        db = AppDataBase.getInstance(requireContext());

        rvPedidos.setLayoutManager(new LinearLayoutManager(requireContext()));
        cargarPedidos();

        // Botón nueva compra
        btnNuevaCompra.setOnClickListener(v -> {
            NewShopFragment fragment = NewShopFragment
                    .newInstance(clienteId, clienteNombre);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // ← permite volver atrás
                    .commit();
        });

        // Botón marcar pagado
        btnMarcarPagado.setOnClickListener(v -> {
            if (pedidoSeleccionado != null && !pedidoSeleccionado.isPagado()) {
                pedidoSeleccionado.setPagado(true);
                db.pedidoDao().actualizar(pedidoSeleccionado);
                Toast.makeText(requireContext(),
                        "Pedido marcado como pagado ✅",
                        Toast.LENGTH_SHORT).show();
                pedidoSeleccionado = null;
                btnMarcarPagado.setEnabled(false);
                cargarPedidos();
            }
        });

        return view;
    }

    private void cargarPedidos() {
        List<Pedido> pedidos = db.pedidoDao().obtenerPorCliente(clienteId);
        adapter = new pedidoAdapter(pedidos, (pedido, posicion) -> {
            pedidoSeleccionado = pedido;
            // Solo activa el botón si el pedido está pendiente
            btnMarcarPagado.setEnabled(!pedido.isPagado());
        });
        rvPedidos.setAdapter(adapter);

        // Actualizar saldo pendiente
        double saldo = db.clienteDao().getpendingaccount(clienteId);
        tvSaldoTotal.setText(String.format("Saldo pendiente: L.%.2f", saldo));
    }
}
