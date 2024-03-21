package com.example.booklist_tfg.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "libros")
@Setter
@Getter
@ToString
public class Libro {
    //
    @PrimaryKey(autoGenerate = true)
    private int id;

    //Info sacada de la API GOOGLE
    @NonNull
    @ColumnInfo(name = "titulo")
    private String titulo;

    private ArrayList<String> nombreAutoria;

    private String editorial;
    private String genero;
    @ColumnInfo(name = "anio_publicacion")
    private String anioPublicacion;

    private int paginas;
    private String portada;


    //Añadido por Usuario
    private Boolean favorito;
    @NonNull
    @ColumnInfo(name = "fecha_lectura")
    private Date fechaLectura;
    @NonNull
    @ColumnInfo(name = "es_papel")
    private Boolean esPapel;

    public Libro(@NonNull String titulo, ArrayList<String> nombreAutoria, String editorial, String genero, String anioPublicacion, int paginas, String portada) {
        this.titulo = titulo;
        this.nombreAutoria = nombreAutoria;
        this.editorial = editorial;
        this.genero = genero;
        this.anioPublicacion = anioPublicacion;
        this.paginas = paginas;
        this.portada = portada;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitulo() {
        return titulo;
    }

    public ArrayList<String> getNombreAutoria() {

        return new ArrayList<>(nombreAutoria); //Consejo profesional
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
