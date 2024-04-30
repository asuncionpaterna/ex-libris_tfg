package com.example.booklist_tfg.ui.anadir;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.BookViewHolder> {

    // creating variables for arraylist and context.
    private ArrayList<Libro> libroInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public LibroAdapter(ArrayList<Libro> libroInfoArrayList, Context mcontext) {
        this.libroInfoArrayList = libroInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_anadir_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // inside on bind view holder method we are
        // setting our data to each UI component.
        Libro libroInfo = libroInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.editorialTV.setText(libroInfo.getEditorial());
        holder.fechaTV.setText("" + libroInfo.getAnioPublicacion());
        holder.paginasTV.setText(mcontext.getString(R.string.label_paginas) + libroInfo.getPaginas());
        // below line is use to set image from URL in our image view.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        // below line is use to add on click listener for our item of recycler view.
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
                // inside on click listener method we are calling a new activity
                // La informacióin que se pasa a los detalles del libro
                Intent i = new Intent(mcontext, LibroDetalles.class);
                i.putExtra("title", libroInfo.getTitulo());

                i.putExtra("authors", libroInfo.getNombreAutoria());
                i.putExtra("publisher", libroInfo.getEditorial());
                i.putExtra("publishedDate", libroInfo.getAnioPublicacion());

                i.putExtra("pageCount", libroInfo.getPaginas());
                i.putExtra("thumbnail", libroInfo.getPortada());
                i.putExtra("libro", libroInfo);


                // after passing that data we are
                // starting our new intent.
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // inside get item count method we
        // are returning the size of our array list.
        return libroInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
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
