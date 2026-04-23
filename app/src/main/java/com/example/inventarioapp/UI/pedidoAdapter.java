package com.example.inventarioapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Model.Pedido;
import com.example.inventarioapp.R;

import java.util.List;

public class pedidoAdapter extends RecyclerView.Adapter<pedidoAdapter.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private int posicionSeleccionada = -1;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido, int posicion);
    }

    public pedidoAdapter(List<Pedido> pedidos, OnPedidoClickListener listener) {
        this.pedidos  = pedidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        holder.tvFecha.setText(pedido.getFecha());
        holder.tvTotal.setText(String.format("L.%.2f", pedido.getTotal()));

        // Estado del pedido
        if (pedido.isPagado()) {
            holder.tvEstado.setText("Pagado ✅");
            holder.tvEstado.setTextColor(0xFF2E7D32); // verde
        } else {
            holder.tvEstado.setText("Pendiente");
            holder.tvEstado.setTextColor(0xFFC62828); // rojo
        }

        // Resaltar fila seleccionada
        if (posicionSeleccionada == position) {
            holder.itemView.setBackgroundColor(0xFFBBDEFB);
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF);
        }

        // Click en la fila
        holder.itemView.setOnClickListener(v -> {
            int anteriorSeleccionado = posicionSeleccionada;
            posicionSeleccionada = holder.getAdapterPosition();

            notifyItemChanged(anteriorSeleccionado);
            notifyItemChanged(posicionSeleccionada);

            listener.onPedidoClick(pedido, posicionSeleccionada);
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void actualizarLista(List<Pedido> nuevaLista) {
        this.pedidos = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvTotal, tvEstado;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha  = itemView.findViewById(R.id.tv_fecha_pedido);
            tvTotal  = itemView.findViewById(R.id.tv_total_pedido);
            tvEstado = itemView.findViewById(R.id.tv_estado_pedido);
        }
    }
}
