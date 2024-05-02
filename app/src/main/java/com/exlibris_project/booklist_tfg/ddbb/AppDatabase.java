package com.exlibris_project.booklist_tfg.ddbb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.exlibris_project.booklist_tfg.Model.Libro;

@Database(entities = {Libro.class}, version = 1)
@androidx.room.TypeConverters({TypeConverters.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract LibroDAO libroDAO();

}