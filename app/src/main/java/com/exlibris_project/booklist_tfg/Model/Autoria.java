package com.exlibris_project.booklist_tfg.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "autorias")
@Builder
@Getter
@Setter
@ToString
public class Autoria {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_autoria")
    private int idAutoria;
    @ColumnInfo(name = "nombre_autoria")
    @NonNull
    private String nombreAutoria;
    @ColumnInfo(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Ignore
    private List<Libro> libros;
}
