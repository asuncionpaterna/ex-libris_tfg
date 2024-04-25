package com.example.booklist_tfg.ui.dialog;

import static com.example.booklist_tfg.utils.Json.crearJsonBackup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

public class DialogoConfiguracion {

    private Context context;
    private ActivityResultLauncher<Intent> lanzadorSelectorArchivos;
    public DialogoConfiguracion(Context context,ActivityResultLauncher<Intent> filePickerLauncher) {
        this.context = context;
        this.lanzadorSelectorArchivos = filePickerLauncher;
    }
    CheckBox sistemaIdiomaCB;
    Spinner idiomasSP;
    Button mBackupBtn, mRestoreBtn;

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_configuracion, null);
        builder.setView(view);

        sistemaIdiomaCB = view.findViewById(R.id.idCBSistemaIdiom);
        idiomasSP = view.findViewById(R.id.idSPIdiomas);
        mBackupBtn = view.findViewById(R.id.idBackupBTN);
        mRestoreBtn = view.findViewById(R.id.idRestoreBTM);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idiomasSP.setAdapter(adapter);


        sistemaIdiomaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                idiomasSP.setEnabled(!isChecked);
            }
        });



        mBackupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearJsonBackup();
            }
        });

        mRestoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectorArchivos selectorArchivos = new SelectorArchivos(context, lanzadorSelectorArchivos);
                selectorArchivos.abrirSelectorArchivos();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
