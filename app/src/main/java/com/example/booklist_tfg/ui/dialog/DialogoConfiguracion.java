package com.example.booklist_tfg.ui.dialog;

import static com.example.booklist_tfg.utils.Json.crearJsonBackup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;

import com.example.booklist_tfg.R;
import com.example.booklist_tfg.utils.SelectorArchivos;
//Clase para dar funcionalidad al diálogo de configuración
public class DialogoConfiguracion {

    private Context context;
    private ActivityResultLauncher<Intent> lanzadorSelectorArchivos;
    //Constructor
    public DialogoConfiguracion(Context context,ActivityResultLauncher<Intent> filePickerLauncher) {
        this.context = context;
        this.lanzadorSelectorArchivos = filePickerLauncher;
    }
    //Se crean las variables de almacenamiento y los elementos de la pantalla de diálogo
    CheckBox sistemaIdiomaCB;
    Spinner idiomasSP;
    Button backupBtn, restaurarBtn;
    //Se implementa la funcionalidad de mostrar el diálogo
    public void showDialog() {
        //Se infla el dialogo, relacionandolo con su layout y estableciendo el diseño
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_configuracion, null);
        builder.setView(view);

        // Se inicializan los elementos del diálogo
        sistemaIdiomaCB = view.findViewById(R.id.idCBSistemaIdiom);
        idiomasSP = view.findViewById(R.id.idSPIdiomas);
        backupBtn = view.findViewById(R.id.idBackupBTN);
        restaurarBtn = view.findViewById(R.id.idRestaurarBTN);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idiomasSP.setAdapter(adapter);

        //Se configura la funcionalidad del CheckBox de idiomas por sistema
        sistemaIdiomaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                idiomasSP.setEnabled(!isChecked);
            }
        });


        // Se configura la funcionalidad del botón de back up
        backupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearJsonBackup();
            }
        });
        // Se configura la funcionalidad del botón de restaurar
        restaurarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectorArchivos selectorArchivos = new SelectorArchivos(context, lanzadorSelectorArchivos);
                selectorArchivos.abrirSelectorArchivos();
            }
        });
        // Se crea el diálogo con el diseño inflado y posteriormente se muestra
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
