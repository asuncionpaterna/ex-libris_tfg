package com.example.booklist_tfg.ui.listadoLibros;

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

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;
import static com.example.booklist_tfg.utils.Utils.formateoAutoria;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LibroAdapterListaPeq extends RecyclerView.Adapter<LibroAdapterListaPeq.BookViewHolder> {
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public LibroAdapterListaPeq(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.libro_lista_rv_item_peq, parent, false);
        return new LibroAdapterListaPeq.BookViewHolder(view);    }


    @Override
    public void onBindViewHolder(@NonNull LibroAdapterListaPeq.BookViewHolder holder, int position) {
        // inside on bind view holder method we are
        // setting our data to each UI component.
        Libro libroInfo = bookInfoArrayList.get(position);
        holder.tituloTV.setText(libroInfo.getTitulo());
        holder.favoritoCB.setChecked(libroInfo.getFavorito());

        ArrayList<String> autores = libroInfo.getNombreAutoria();
        holder.autoriaTV.setText(formateoAutoria(autores));
        // below line is use to set image from URL in our image view.
        Picasso.get().load(libroInfo.getPortada()).into(holder.portadaIV);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // La informacióin que se pasa a los detalles del libro
                Intent i = new Intent(mcontext, LibroDetailsLista.class);
                i.putExtra("title", libroInfo.getTitulo());

                i.putExtra("authors", libroInfo.getNombreAutoria());
                i.putExtra("publisher", libroInfo.getEditorial());
                i.putExtra("publishedDate", libroInfo.getAnioPublicacion());

                i.putExtra("pageCount", libroInfo.getPaginas());
                i.putExtra("thumbnail", libroInfo.getPortada());
                i.putExtra("libro", libroInfo);

                // after passing that data we are
                // starting our new intent.
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
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