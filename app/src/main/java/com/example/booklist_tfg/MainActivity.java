package com.example.booklist_tfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.ddbb.AppDatabase;
import com.example.booklist_tfg.ddbb.LibroDAO;
import com.example.booklist_tfg.ui.dialog.DialogoConfiguracion;
import com.example.booklist_tfg.ui.dialog.DialogoObjetivo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.booklist_tfg.databinding.ActivityMainBinding;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static FloatingActionButton floatingBTN;

    public static AppDatabase database;
    public static List<Libro> listaLibros = new ArrayList<>();

    public static int objetivoLectura = 0;
    public static SharedPreferences sharedPreferences;

    static Year anioActual =Year.parse(Year.now().toString());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "BaseDatos").build();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences.Editor editor = sharedPreferences.edit();

        floatingBTN = binding.appBarMain.fabAnadir;


        setSupportActionBar(binding.appBarMain.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        floatingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_anadir);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Realiza la navegación al destino seleccionado en el menú
                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                //Se verifica si la navegación se maneja correctamente
                if (handled) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                return handled;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_configuracion) {
            DialogoConfiguracion dialogoConfiguracion = new DialogoConfiguracion(this);
            dialogoConfiguracion.showDialog();
            return true;
        } else if (id == R.id.menu_objetivo) {
            DialogoObjetivo dialogoObjetivo = new DialogoObjetivo(this);
            dialogoObjetivo.showDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}