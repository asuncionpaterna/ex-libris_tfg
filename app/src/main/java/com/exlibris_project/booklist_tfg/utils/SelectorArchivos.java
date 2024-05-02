package com.exlibris_project.booklist_tfg.utils;


import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;

public class SelectorArchivos {

    private final Context context;
    private final ActivityResultLauncher<Intent> filePickerLauncher;

    public SelectorArchivos(Context context, ActivityResultLauncher<Intent> filePickerLauncher) {
        this.context = context;

        this.filePickerLauncher = filePickerLauncher;
    }

    public void abrirSelectorArchivos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        intent = Intent.createChooser(intent, "Selecciona un archivo");
        filePickerLauncher.launch(intent);
    }

}
