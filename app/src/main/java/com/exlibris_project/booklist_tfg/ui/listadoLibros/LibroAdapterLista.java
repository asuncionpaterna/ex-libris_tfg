package com.exlibris_project.booklist_tfg.ui.listadoLibros;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exlibris_project.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;

import com.exlibris_project.booklist_tfg.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibroAdapterLista extends RecyclerView.Adapter<LibroAdapterLista.BookViewHolder> {
    // Variables para la lista de libros y el contexto.
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // Constructor para la lista de libros y el contexto.
    public LibroAdapterLista(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout para el ítem del RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_lista_rv_item, parent, false);
        return new LibroAdapterLista.BookViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull LibroAdapterLista.BookViewHolder holder, int position) {
        // En el método onBindViewHolder, estamos configurando los datos para cada componente de la UI.
        Resources resources = mcontext.getResources(); // Asegúrate de que mcontext es una actividad
        String labelFecha = resources.getString(R.string.label_fecha);

        Libro libroInfo = bookInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.editorialTV.setText(Utils.verificarDatos(libroInfo.getEditorial(),mcontext));
        holder.fechaLecturaTV.setText(labelFecha+ Utils.formateoFecha(libroInfo.getFechaLectura()));
        holder.favoritoCB.setChecked(libroInfo.getFavorito());
        holder.esPapelCB.setChecked(libroInfo.getEsPapel());

        // Configurar la imagen desde la URL en el ImageView.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        // Agregar un listener de click para cada ítem del RecyclerView.
        ArrayList<String> autores = libroInfo.getNombreAutoria();
        holder.autoriaTV.setText(Utils.formateoAutoria(autores));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // La informacióin que se pasa a los detalles del libro
                Intent intent = new Intent(mcontext, LibroDetailsLista.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", libroInfo.getTitulo());
                intent.putExtra("authors", libroInfo.getNombreAutoria());
                intent.putExtra("publisher", libroInfo.getEditorial());
                intent.putExtra("publishedDate", libroInfo.getFechaPublicacion());
                intent.putExtra("pageCount", libroInfo.getPaginas());
                intent.putExtra("thumbnail", libroInfo.getPortada());
                intent.putExtra("libro", libroInfo);

                //Se inicia la actividad
                mcontext.startActivity(intent);
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
        TextView tituloTV, editorialTV, generoTV, fechaLecturaTV, autoriaTV;
        ImageView portadaIV;
        CheckBox favoritoCB, esPapelCB;
        public BookViewHolder(View itemView) { // Esto es lo que sale al buscar, las tarjetas
            super(itemView);
            tituloTV = itemView.findViewById(R.id.idTVTituloList);
            autoriaTV = itemView.findViewById(R.id.idTVAutoriaList);
            editorialTV = itemView.findViewById(R.id.idTVEditorialList);
            generoTV = itemView.findViewById(R.id.idTGeneroList);
            fechaLecturaTV = itemView.findViewById(R.id.idTVFechaLecturaList);
            portadaIV = itemView.findViewById(R.id.idIVPortadaList);
            favoritoCB = itemView.findViewById(R.id.idCBFavorito);
            esPapelCB = itemView.findViewById(R.id.idCBesPapel);

        }
    }
}
