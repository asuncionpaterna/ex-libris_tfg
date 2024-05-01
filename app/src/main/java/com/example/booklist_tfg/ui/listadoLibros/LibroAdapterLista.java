package com.example.booklist_tfg.ui.listadoLibros;
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

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import static com.example.booklist_tfg.utils.Utils.formateoAutoria;
import static com.example.booklist_tfg.utils.Utils.formateoFecha;
import static com.example.booklist_tfg.utils.Utils.verificarDatos;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibroAdapterLista extends RecyclerView.Adapter<LibroAdapterLista.BookViewHolder> {
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public LibroAdapterLista(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_lista_rv_item, parent, false);
        return new LibroAdapterLista.BookViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull LibroAdapterLista.BookViewHolder holder, int position) {
        // inside on bind view holder method we are
        // setting our data to each UI component.
        Resources resources = mcontext.getResources(); // Asegúrate de que mcontext es una actividad
        String labelFecha = resources.getString(R.string.label_fecha);

        Libro libroInfo = bookInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.editorialTV.setText(verificarDatos(libroInfo.getEditorial(),mcontext));
        holder.fechaLecturaTV.setText(labelFecha+formateoFecha(libroInfo.getFechaLectura()));
        holder.favoritoCB.setChecked(libroInfo.getFavorito());
        holder.esPapelCB.setChecked(libroInfo.getEsPapel());

        // below line is use to set image from URL in our image view.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        // below line is use to add on click listener for our item of recycler view.
        ArrayList<String> autores = libroInfo.getNombreAutoria();
        holder.autoriaTV.setText(formateoAutoria(autores));
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

                // after passing that data we are
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // our text view and image views.
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
