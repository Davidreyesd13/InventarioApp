package com.example.inventarioapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Model.DetallePedido;
import com.example.inventarioapp.R;

import java.util.ArrayList;
import java.util.List;

public class DetallepedidoAdapter extends RecyclerView.Adapter<DetallepedidoAdapter.DetalleViewHolder> {

    // Clase interna para mostrar nombre + detalle
    public static class DetalleItem {
        public String nombreProducto;
        public int cantidad;
        public double precioUnitario;
        public int productoId;

        public DetalleItem(String nombreProducto, int cantidad,
                           double precioUnitario, int productoId) {
            this.nombreProducto = nombreProducto;
            this.cantidad       = cantidad;
            this.precioUnitario = precioUnitario;
            this.productoId     = productoId;
        }
    }

    private List<DetalleItem> items;

    public DetallepedidoAdapter() {
        this.items = new ArrayList<>();
    }

    // Agregar producto al carrito
    public void agregarItem(DetalleItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    // Obtener todos los items
    public List<DetalleItem> getItems() {
        return items;
    }

    // Calcular total
    public double calcularTotal() {
        double total = 0;
        for (DetalleItem item : items) {
            total += item.precioUnitario * item.cantidad;
        }
        return total;
    }

    @NonNull
    @Override
    public DetalleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detalle_producto, parent, false);
        return new DetalleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleViewHolder holder, int position) {
        DetalleItem item = items.get(position);

        holder.tvNombre.setText(item.nombreProducto);
        holder.tvCantidad.setText("x" + item.cantidad);
        holder.tvSubtotal.setText(
                String.format("L.%.2f", item.precioUnitario * item.cantidad)
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class DetalleViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvSubtotal;

        public DetalleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre   = itemView.findViewById(R.id.tv_nombre_detalle);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad_detalle);
            tvSubtotal = itemView.findViewById(R.id.tv_subtotal_detalle);
        }
    }
}
