package com.exlibris_project.booklist_tfg.ui.listadoLibros;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;

import com.exlibris_project.booklist_tfg.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibroAdapterListaPeq extends RecyclerView.Adapter<LibroAdapterListaPeq.BookViewHolder> {
    // Variables para la lista de libros y el contexto.
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // Constructor para la lista de libros y el contexto.
    public LibroAdapterListaPeq(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para el ítem del RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_lista_rv_item_peq, parent, false);
        return new LibroAdapterListaPeq.BookViewHolder(view);    }


    @Override
    public void onBindViewHolder(@NonNull LibroAdapterListaPeq.BookViewHolder holder, int position) {
        // En el método onBindViewHolder, estamos configurando los datos para cada componente de la UI.
        Libro libroInfo = bookInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.favoritoCB.setChecked(libroInfo.getFavorito());

        ArrayList<String> autores = libroInfo.getNombreAutoria();
        holder.autoriaTV.setText(Utils.formateoAutoria(autores));
        // Configurar la imagen desde la URL en el ImageView.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        // Agregar un listener de click para cada ítem del RecyclerView.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dentro del listener de click, estamos llamando a una nueva actividad
                // y pasando la información a los detalles del libro.
                Intent i = new Intent(mcontext, LibroDetailsLista.class);
                i.putExtra("title", libroInfo.getTitulo());

                i.putExtra("authors", libroInfo.getNombreAutoria());
                i.putExtra("publisher", libroInfo.getEditorial());
                i.putExtra("publishedDate", libroInfo.getFechaPublicacion());

                i.putExtra("pageCount", libroInfo.getPaginas());
                i.putExtra("thumbnail", libroInfo.getPortada());
                i.putExtra("libro", libroInfo);

                //Se inicia la actividad
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // En el método getItemCount, estamos devolviendo el tamaño de nuestra lista.
        return bookInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // Inicializar los TextViews y ImageViews
        TextView tituloTV, autoriaTV;
        ImageView portadaIV;

        CheckBox favoritoCB;
        public BookViewHolder(View itemView) { // Esto es lo que sale al buscar, las tarjetas
            super(itemView);
            tituloTV = itemView.findViewById(R.id.idTVTituloList);
            autoriaTV = itemView.findViewById(R.id.idTVAutoriaList);
            portadaIV = itemView.findViewById(R.id.idIVPortadaList);
            favoritoCB = itemView.findViewById(R.id.idCBFavorito);

        }
    }
}