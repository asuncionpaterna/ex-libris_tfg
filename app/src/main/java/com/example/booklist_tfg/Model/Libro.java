package com.example.booklist_tfg.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Se define la tabla de base de datos en Room llamada 'libros'
@Entity(tableName = "libros")
@Setter
@Getter
@ToString
//Clase del objeto Libro
public class Libro implements Serializable {
    //Clave primaria autogenerada para la columna id
    @PrimaryKey(autoGenerate = true)
    private int id;

    //La columna que almacena el título del libro, no puede ser nula
    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    //Columnas de los atributos del libro
    private ArrayList<String> nombreAutoria;
    private String editorial;
    private String genero;

    @ColumnInfo(name = "anio_publicacion")
    private String anioPublicacion;

    private int paginas;
    private String portada;

    private Boolean favorito;
    @NonNull
    @ColumnInfo(name = "fecha_lectura")
    private Date fechaLectura;
    @NonNull
    @ColumnInfo(name = "es_papel")
    private Boolean esPapel;

    //Constructor que inicializa un objeto Libro con los detalles básicos
    public Libro(@NonNull String titulo, ArrayList<String> nombreAutoria, String editorial, String genero, String anioPublicacion, int paginas, String portada) {
        this.titulo = titulo;
        this.nombreAutoria = nombreAutoria;
        this.editorial = editorial;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.paginas = paginas;
        this.portada = portada;
    }
    //Métodos para acceder a los elementos del libro
    public int getId() {
        return id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public ArrayList<String> getNombreAutoria() {
        return new ArrayList<>(nombreAutoria);
    }

    public String getEditorial() {
        return editorial;
    }

    public String getGenero() {
        return genero;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public int getPaginas() {
        return paginas;
    }

    public String getPortada() {
        return portada;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    @NonNull
    public Date getFechaLectura() {
        return fechaLectura;
    }

    @NonNull
    public Boolean getEsPapel() {
        return esPapel;
    }
}
