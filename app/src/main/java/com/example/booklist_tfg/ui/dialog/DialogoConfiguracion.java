package com.example.booklist_tfg.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.example.booklist_tfg.R;

public class DialogoConfiguracion {

    private Context context;

    public DialogoConfiguracion(Context context) {
        this.context = context;
    }

    CheckBox modoCB, sistemaModoCB, sistemaIdiomaCB;
    Spinner idiomasSP;

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_configuracion, null);
        builder.setView(view);

        modoCB = view.findViewById(R.id.idCBModo);
        sistemaModoCB = view.findViewById(R.id.idCBSistemaModo);
        sistemaIdiomaCB = view.findViewById(R.id.idCBSistemaIdiom);
        idiomasSP = view.findViewById(R.id.idSPIdiomas);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idiomasSP.setAdapter(adapter);

        modoCB.setEnabled(false);
        sistemaModoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Habilitar o deshabilitar el CheckBox modoCB según el estado de sistemaModoCB
                modoCB.setEnabled(!isChecked);
            }
        });
        sistemaIdiomaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                idiomasSP.setEnabled(!isChecked);
            }
        });


        modoCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Cambiar el tema de la aplicación al modo oscuro si el CheckBox está marcado
                if (isChecked) {
                    context.getResources().getConfiguration().uiMode = Configuration.UI_MODE_NIGHT_YES;

                } else {
                    // Restablecer el tema a modo claro si el CheckBox está desmarcado
                    context.getResources().getConfiguration().uiMode = ~Configuration.UI_MODE_NIGHT_YES;
                }
                // Notificar al sistema que el tema ha cambiado
                context.getResources().updateConfiguration(context.getResources().getConfiguration(), null);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
