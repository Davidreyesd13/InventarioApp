package com.example.inventarioapp.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventarioapp.Database.AppDataBase;
import com.example.inventarioapp.Model.Cliente;
import com.example.inventarioapp.R;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private int posicionSeleccionada = -1;
    private OnClienteClickListener listener;
    private AppDataBase db; // ← para calcular saldo

    public interface OnClienteClickListener {
        void onClienteClick(Cliente cliente, int posicion);
    }

    public ClienteAdapter(List<Cliente> clientes,
                          OnClienteClickListener listener,
                          AppDataBase db) {
        this.clientes = clientes;
        this.listener = listener;
        this.db       = db;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);

        holder.tvNombre.setText(cliente.getNombre());
        holder.tvTelefono.setText(cliente.getNumeroTelefono());

        // Calcular saldo desde pedidos
        double saldo = db.clienteDao().getpendingaccount(cliente.getId());
        holder.tvSaldo.setText(String.format("L.%.2f", saldo));

        // Color según saldo
        if (saldo > 0) {
            holder.tvSaldo.setTextColor(0xFFC62828); // rojo — debe dinero
        } else {
            holder.tvSaldo.setTextColor(0xFF2E7D32); // verde — sin deuda
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
            listener.onClienteClick(cliente, posicionSeleccionada);
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void actualizarLista(List<Cliente> nuevaLista) {
        this.clientes = nuevaLista;
        posicionSeleccionada = -1;
        notifyDataSetChanged();
    }

    public static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvSaldo;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre   = itemView.findViewById(R.id.tv_nombre_cliente);
            tvTelefono = itemView.findViewById(R.id.tv_telefono_cliente);
            tvSaldo    = itemView.findViewById(R.id.tv_saldo_cliente);
        }
    }
}