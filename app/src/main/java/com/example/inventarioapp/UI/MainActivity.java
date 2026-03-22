package com.example.inventarioapp.UI;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.inventarioapp.R;
import com.example.inventarioapp.UI.ClientesFragment;
import com.example.inventarioapp.UI.InventarioFragment;
import com.example.inventarioapp.UI.ResumenFragment;
import com.example.inventarioapp.UI.StockBajoFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navegación
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_resumen) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ResumenFragment())
                        .commit();

            } else if (id == R.id.nav_stock) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new StockBajoFragment())
                        .commit();

            } else if (id == R.id.nav_clientes) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ClientesFragment())
                        .commit();

            } else if (id == R.id.nav_inventario) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new InventarioFragment())
                        .commit();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Pantalla inicial
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ResumenFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}