package com.example.inventarioapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Model.Producto;
import com.example.inventarioapp.R;

import java.util.List;

public class StockBajoAdapter extends RecyclerView.Adapter<StockBajoAdapter.StockViewHolder> {

    private List<Producto> productos;

    public StockBajoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stockbajo, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvMarca.setText(producto.getMarca());
        holder.tvCantidad.setText(producto.getCantidad() + " und.");

        // Resaltar en rojo si está en 0
        if (producto.getCantidad() == 0) {
            holder.tvCantidad.setTextColor(0xFFB71C1C); // rojo oscuro
            holder.itemView.setBackgroundColor(0xFFFFEBEE); // fondo rojo claro
        } else {
            holder.tvCantidad.setTextColor(0xFFE65100); // naranja
            holder.itemView.setBackgroundColor(0xFFFFFFFF); // blanco
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvCantidad;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre   = itemView.findViewById(R.id.tv_nombre_stock);
            tvMarca    = itemView.findViewById(R.id.tv_marca_stock);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad_stock);
        }
    }
}