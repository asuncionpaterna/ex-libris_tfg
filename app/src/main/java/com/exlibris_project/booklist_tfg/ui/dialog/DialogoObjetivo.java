package com.exlibris_project.booklist_tfg.ui.dialog;

import static com.exlibris_project.booklist_tfg.ui.Inicio.InicioFragment.porcentajeLectura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.exlibris_project.booklist_tfg.MainActivity;
import com.example.booklist_tfg.R;
import com.exlibris_project.booklist_tfg.utils.HorizontalNumberPicker;

//Clase para dar funcionalidad al diálogo de objetivo lectura
public class DialogoObjetivo {
    private Context context;
    //Constructor
    public DialogoObjetivo(Context context) {
        this.context = context;
    }
    // Se crean las variables de los elementos de pantalla
    Button guardarObjeBTN;
    HorizontalNumberPicker objetivoHNP;
    //Se implementa la funcionalidad de mostrar el diálogo de Objetivo lectura
    public void showDialog() {
        //Se infla el dialogo, relacionandolo con su layout y estableciendo el diseño
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_objetivo, null);
        builder.setView(view);
        // Se inicializan los elementos del diálogo
        guardarObjeBTN = view.findViewById(R.id.idBtnGuardarObje);
        objetivoHNP = view.findViewById(R.id.idHNPObjetivo);
        //Se establecen los limites máximos y mínimos del selector numérico (horizontal number picker)
        objetivoHNP.setMin(0);
        objetivoHNP.setMax(200);
        //Se establece el valor con el objetivo lectura
        objetivoHNP.setValue(MainActivity.objetivoLectura);
        //Se crea el diálogo
        AlertDialog dialog = builder.create();
        // Se configura la funcionalidad del botón de guardar el objetivo de lectura
        guardarObjeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Se obtiebe el valor actual del selector numérico
                MainActivity.objetivoLectura = objetivoHNP.getValue();
                //Se guarda el valor del objetivo de lectura en las preferencias guardadas
                SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                editor.putInt("objetivo_lectura", MainActivity.objetivoLectura);
                editor.apply();
                //Se llama a la función de actualizar el porcentaje de lectura
                porcentajeLectura();
                //Se cierra el diálogo
                dialog.dismiss();
            }
        });
        //Se muestra el diálogo
        dialog.show();
    }
}
