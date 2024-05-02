package com.exlibris_project.booklist_tfg;

import static com.exlibris_project.booklist_tfg.utils.Json.gestionarJson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.databinding.ActivityMainBinding;
import com.exlibris_project.booklist_tfg.ddbb.AppDatabase;
import com.exlibris_project.booklist_tfg.ui.dialog.DialogoBusquedaAvanzada;
import com.exlibris_project.booklist_tfg.ui.dialog.DialogoConfiguracion;
import com.exlibris_project.booklist_tfg.ui.dialog.DialogoObjetivo;
import com.exlibris_project.booklist_tfg.ui.Inicio.InicioFragment;
import com.exlibris_project.booklist_tfg.ui.listadoLecturas.ListadoLecturasFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static boolean peq = false;

    public static boolean inicio = true;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static FloatingActionButton floatingBTN;

    public static AppDatabase database;
    public static List<Libro> listaLibros = new ArrayList<>();
    public  static String codigoIdioma = "es";
    public static boolean mostrarBusquedaAvanzada;
    public static boolean mostrarListaPeq;
    public static int objetivoLectura = 0;
    public static SharedPreferences sharedPreferences;
    private NavController navController;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "BaseDatos").build();

        Resources resources = getResources();

        codigoIdioma = sharedPreferences.getString("idioma", codigoIdioma);
        Locale locale = new Locale(codigoIdioma);
        Locale.setDefault(locale);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        floatingBTN = binding.appBarMain.fabAnadir;
        activity = this;



        setSupportActionBar(binding.appBarMain.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setOpenableLayout(drawer).build();

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
        // Ocultar un elemento específico
        MenuItem itemBusquedaAvanzada = menu.findItem(R.id.menu_busqueda_avanzada);
        MenuItem itemListadoPeq = menu.findItem(R.id.menu_peq);
        if (itemBusquedaAvanzada != null) {
            itemBusquedaAvanzada.setVisible(mostrarBusquedaAvanzada ? true : false); // Oculta el item del menú
        }
        if (itemListadoPeq != null) {
            itemListadoPeq.setIcon(peq ? getDrawable(R.drawable.ic_grande) : getDrawable(R.drawable.ic_pequeno));
            itemListadoPeq.setVisible(mostrarListaPeq ? true : false); // Oculta el item del menú
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_peq) {

            if (peq) peq = false;
            else peq = true;

            if (item != null) {
                item.setIcon(peq ? getDrawable(R.drawable.ic_grande) : getDrawable(R.drawable.ic_pequeno));
            }
            if (inicio) {
                InicioFragment.mostrarLibrosInicio(getBaseContext());
            } else ListadoLecturasFragment.mostrarLibrosLecturas(getBaseContext());

            return true;
        } else if (id == R.id.menu_configuracion) {
            DialogoConfiguracion dialogoConfiguracion = new DialogoConfiguracion(this, filePickerLauncher);
            dialogoConfiguracion.showDialog();
            return true;
        } else if (id == R.id.menu_objetivo) {
            DialogoObjetivo dialogoObjetivo = new DialogoObjetivo(this);
            dialogoObjetivo.showDialog();

            return true;
        } else if (id == R.id.menu_busqueda_avanzada) {
            DialogoBusquedaAvanzada dialogoBusquedaAvanzada = new DialogoBusquedaAvanzada(this);
            dialogoBusquedaAvanzada.showDialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void solicitarPermisos() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri uri = data.getData();

                            gestionarJson(activity, uri);

                        }
                    }
                }
            });


}