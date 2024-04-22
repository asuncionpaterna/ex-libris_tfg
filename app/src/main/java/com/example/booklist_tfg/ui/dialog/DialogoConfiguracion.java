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

    CheckBox sistemaIdiomaCB;
    Spinner idiomasSP;

    public void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_configuracion, null);
        builder.setView(view);

        sistemaIdiomaCB = view.findViewById(R.id.idCBSistemaIdiom);
        idiomasSP = view.findViewById(R.id.idSPIdiomas);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.idiomas_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        idiomasSP.setAdapter(adapter);


        sistemaIdiomaCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                idiomasSP.setEnabled(!isChecked);
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
