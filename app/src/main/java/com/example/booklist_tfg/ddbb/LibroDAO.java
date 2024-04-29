package com.example.booklist_tfg.ddbb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.booklist_tfg.Model.Libro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Query("SELECT * FROM libros WHERE nombreAutoria LIKE '%'|| :autoriaET ||'%'")
    List<Libro> getByAutoria(String autoriaET);

    @Query("SELECT * FROM libros WHERE genero LIKE '%'|| :generoET ||'%'")
    List<Libro> getByGenero(String generoET);

    @Query("SELECT * FROM libros WHERE editorial LIKE '%'|| :editorialET ||'%'")
    List<Libro> getByEditorial(String editorialET);
    @RawQuery
    List<Libro> getLibrosFiltro(SupportSQLiteQuery query);

    @Query("SELECT COUNT(*) FROM libros WHERE fecha_lectura LIKE '%-' || :mes || '-%'")
    int getCountLecturabyMes(String mes);

    @Query("SELECT * FROM libros WHERE favorito = true AND  es_papel = :formato")
    List<Libro> getByFavoritoFormato(boolean formato);

    @MapInfo(keyColumn = "genero", valueColumn = "count")
    @Query("SELECT genero, COUNT(*) as count FROM libros GROUP BY genero ORDER BY count desc")
    Map<String, Integer> getGeneros();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertListaLibros(List<Libro> libros);

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