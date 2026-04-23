package com.example.inventarioapp.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Model.Producto;
import com.example.inventarioapp.R;

import java.util.List;

public class StockBajoFragment extends Fragment {

    private RecyclerView rvStockBajo;
    private TextView tvSinAlertas;
    private AppDataBase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_bajo, container, false);

        rvStockBajo  = view.findViewById(R.id.rv_stock_bajo);
        tvSinAlertas = view.findViewById(R.id.tv_sin_alertas);

        db = AppDataBase.getInstance(requireContext());
        rvStockBajo.setLayoutManager(new LinearLayoutManager(requireContext()));

        cargarStockBajo();

        return view;
    }

    private void cargarStockBajo() {
        List<Producto> productos = db.productoDao().productosConStockBajo(5);

        if (productos.isEmpty()) {
            // No hay productos con stock bajo
            tvSinAlertas.setVisibility(View.VISIBLE);
            rvStockBajo.setVisibility(View.GONE);
        } else {
            tvSinAlertas.setVisibility(View.GONE);
            rvStockBajo.setVisibility(View.VISIBLE);

            StockBajoAdapter adapter = new StockBajoAdapter(productos);
            rvStockBajo.setAdapter(adapter);
        }
    }
}