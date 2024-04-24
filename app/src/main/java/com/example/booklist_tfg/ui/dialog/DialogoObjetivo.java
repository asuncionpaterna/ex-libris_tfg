package com.example.booklist_tfg.ui.dialog;

import static com.example.booklist_tfg.ui.Inicio.InicioFragment.porcentajeLectura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.booklist_tfg.MainActivity;
import com.example.booklist_tfg.R;
import com.example.booklist_tfg.utils.HorizontalNumberPicker;

public class DialogoObjetivo {
    private Context context;

    public DialogoObjetivo(Context context) {
        this.context = context;
    }

    Button guardarObjeBTN;
    HorizontalNumberPicker objetivoHNP;

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_objetivo, null);
        builder.setView(view);

        guardarObjeBTN = view.findViewById(R.id.idBtnGuardarObje);
        objetivoHNP = view.findViewById(R.id.idHNPObjetivo);

        objetivoHNP.setMin(0);
        objetivoHNP.setMax(200);

        objetivoHNP.setValue(MainActivity.objetivoLectura);

        AlertDialog dialog = builder.create();
        guardarObjeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.objetivoLectura = objetivoHNP.getValue();
                SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                editor.putInt("objetivo_lectura", MainActivity.objetivoLectura);
                editor.apply();
                porcentajeLectura();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
