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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private int posicionSeleccionada = -1; // -1 = ninguno seleccionado
    private OnProductoClickListener listener;

    // Interface para avisar al Fragment qué producto se seleccionó
    public interface OnProductoClickListener {
        void onProductoClick(Producto producto, int posicion);
    }

    public ProductAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener  = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Convierte item_producto.xml en una View
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Llenar los datos en cada columna
        holder.tvNombre.setText(producto.getNombre());
        holder.tvMarca.setText(producto.getMarca());
        holder.tvPrecio.setText(String.format("L.%.2f", producto.getPrecio()));
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()));

        // Mostrar alerta si stock bajo
        if (producto.getCantidad() <= 5) {
            holder.tvAlerta.setVisibility(View.VISIBLE);
        } else {
            holder.tvAlerta.setVisibility(View.GONE);
        }

        // Resaltar fila seleccionada
        if (posicionSeleccionada == position) {
            holder.itemView.setBackgroundColor(0xFFBBDEFB); // azul claro
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF); // blanco
        }

        // Click en la fila
        holder.itemView.setOnClickListener(v -> {
            int anteriorSeleccionado = posicionSeleccionada;
            posicionSeleccionada = holder.getAdapterPosition();

            // Actualiza la fila anterior y la nueva
            notifyItemChanged(anteriorSeleccionado);
            notifyItemChanged(posicionSeleccionada);

            // Avisa al Fragment
            listener.onProductoClick(producto, posicionSeleccionada);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    // Actualizar la lista completa
    public void actualizarLista(List<Producto> nuevaLista) {
        this.productos = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    // Obtener producto seleccionado
    public Producto getProductoSeleccionado() {
        if (posicionSeleccionada >= 0) {
            return productos.get(posicionSeleccionada);
        }
        return null;
    }

    // ViewHolder — representa una fila
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvMarca, tvPrecio, tvCantidad, tvAlerta;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre   = itemView.findViewById(R.id.tv_nombre);
            tvMarca    = itemView.findViewById(R.id.tv_marca);
            tvPrecio   = itemView.findViewById(R.id.tv_precio);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad);
            tvAlerta   = itemView.findViewById(R.id.tv_alerta);
        }
    }
}