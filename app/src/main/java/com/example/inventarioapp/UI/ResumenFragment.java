package com.example.inventarioapp.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Model.Producto;
import com.example.inventarioapp.R;

import java.util.List;

public class ResumenFragment extends Fragment {

    // Referencias a los TextViews
    private TextView tvTotalProductos;
    private TextView tvTotalDinero;
    private TextView tvStockBajo;
    private TextView tvClientesSaldo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Conecta el Fragment con su XML
        View view = inflater.inflate(R.layout.fragment_resumen, container, false);

        // Conectar los TextViews con su ID
        tvTotalProductos = view.findViewById(R.id.tv_total_productos);
        tvTotalDinero    = view.findViewById(R.id.tv_total_dinero);
        tvStockBajo      = view.findViewById(R.id.tv_stock_bajo);
        tvClientesSaldo  = view.findViewById(R.id.tv_clientes_saldo);

        // Cargar los datos
        cargarResumen();

        return view;
    }

    private void cargarResumen() {
        // Obtener la base de datos
        AppDataBase db = AppDataBase.getInstance(requireContext());

        // 1. Total de productos
        List<Producto> productos = db.productoDao().getAllProductos();
        tvTotalProductos.setText(String.valueOf(productos.size()));

        // 2. Total dinero en inventario
        double totalDinero = 0;
        for (Producto p : productos) {
            totalDinero += p.getPrecio() * p.getCantidad();
        }
        tvTotalDinero.setText(String.format("L. %.2f", totalDinero));

        // 3. Productos con stock bajo (menos de 5 unidades)
        List<Producto> stockBajo = db.productoDao().productosConStockBajo(5);
        tvStockBajo.setText(String.valueOf(stockBajo.size()));

        // 4. Clientes con saldo pendiente
        int clientesConSaldo = db.clienteDao().clientesConSaldoPendiente().size();
        tvClientesSaldo.setText(String.valueOf(clientesConSaldo));
    }
}
