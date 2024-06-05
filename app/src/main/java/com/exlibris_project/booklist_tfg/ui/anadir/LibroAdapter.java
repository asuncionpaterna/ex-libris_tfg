package com.exlibris_project.booklist_tfg.ui.anadir;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.BookViewHolder> {

    // Variables para la lista de libros y el contexto.
    private ArrayList<Libro> libroInfoArrayList;
    private Context mcontext;

    // Constructor para la lista de libros y el contexto.
    public LibroAdapter(ArrayList<Libro> libroInfoArrayList, Context mcontext) {
        this.libroInfoArrayList = libroInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para el ítem del RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_anadir_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // En el método onBindViewHolder, estamos configurando los datos para cada componente de la UI.
        Libro libroInfo = libroInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.editorialTV.setText(libroInfo.getEditorial());
        String fechaPublicacion =libroInfo.getFechaPublicacion();
        holder.fechaTV.setText(fechaPublicacion);
        holder.paginasTV.setText(mcontext.getString(R.string.label_paginas) + libroInfo.getPaginas());
        // Configurar la imagen desde la URL en el ImageView.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        // Agregar un listener de click para cada ítem del RecyclerView.
        StringBuilder sb = new StringBuilder();
            ArrayList<String> autores = libroInfo.getNombreAutoria();
            for (int i = 0; i < autores.size(); i++){
                sb.append(autores.get(i));
                sb.append(i < autores.size() - 1 ? ", " : "");
        }
        holder.autoriaTV.setText(mcontext.getString(R.string.label_autoria) +" "+ sb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dentro del listener de click, estamos llamando a una nueva actividad
                // y pasando la información a los detalles del libro.
                Intent i = new Intent(mcontext, LibroDetalles.class);
                i.putExtra("libro", libroInfo);
                //Se inicia la actividad
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // En el método getItemCount, estamos devolviendo el tamaño de nuestra lista.
        return libroInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // Inicializar los TextViews y ImageViews
        TextView tituloTV, editorialTV, paginasTV, fechaTV, autoriaTV;
        ImageView portadaIV;

        public BookViewHolder(View itemView) { // Esto es lo que sale al buscar, las tarjetas
            super(itemView);
            tituloTV = itemView.findViewById(R.id.idTVBookTitle);
            editorialTV = itemView.findViewById(R.id.idTVEditorialLibroDetalles);
            paginasTV = itemView.findViewById(R.id.idTVPageCount);
            fechaTV = itemView.findViewById(R.id.idTVDate);
            portadaIV = itemView.findViewById(R.id.idIVPortadaList);
            autoriaTV = itemView.findViewById(R.id.idTVautoria);
        }
    }
}
