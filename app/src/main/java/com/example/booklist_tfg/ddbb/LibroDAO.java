package com.example.booklist_tfg.ddbb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.booklist_tfg.Model.Libro;

import java.util.List;

@Dao
public interface LibroDAO {

    @Query("SELECT * FROM libros")
    List<Libro> getAll();

    @Query("SELECT * FROM libros WHERE id = :id")
    Libro getById(int id);


    @Query("SELECT * FROM libros WHERE fecha_lectura LIKE :anio || '%'")
    List<Libro> getByAnio(String anio);

    @Query("SELECT * FROM libros WHERE favorito = true")
    List<Libro> getbyFavorito();

    @Query("SELECT * FROM libros WHERE es_papel = :formato")
    List<Libro> getbyFormato(boolean formato);

    @Query("SELECT COUNT(*) FROM libros WHERE fecha_lectura like :anioActual || '%'")
    int getCountLecturabyAnio(String anioActual);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Libro libro);

    @Update
    void update(Libro libro);

    @Delete
    void delete(Libro libro);

    @Transaction
    @Delete
    void deleteAll(List<Libro> libros);
}