package com.example.booklist_tfg.ui.listado;
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
import com.example.booklist_tfg.ui.anadir.BookDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapterList extends RecyclerView.Adapter<BookAdapterList.BookViewHolder> {
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public BookAdapterList(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
// inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item_personal, parent, false);
        return new BookAdapterList.BookViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapterList.BookViewHolder holder, int position) {
        // inside on bind view holder method we are
        // setting our data to each UI component.
        Libro bookInfo = bookInfoArrayList.get(position);
        holder.tituloTV.setText(bookInfo.getTitulo());
        holder.editorialTV.setText(bookInfo.getEditorial());
        holder.fechaLecturaTV.setText("" + bookInfo.getFechaLectura());
        holder.favoritoCB.setChecked(bookInfo.getFavorito());
        holder.esPapelCB.setChecked(bookInfo.getEsPapel());

        // below line is use to set image from URL in our image view.
        Picasso.get().load(bookInfo.getPortada()).into(holder.portadaIV);
        // below line is use to add on click listener for our item of recycler view.
        StringBuilder sb = new StringBuilder();
        ArrayList<String> autores = bookInfo.getNombreAutoria();
        for (int i = 0; i < autores.size(); i++){
            sb.append(autores.get(i));
            sb.append(i < autores.size() - 1 ? ", " : "");
        }
        holder.autoriaTV.setText("Autoria: " + sb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // La informacióin que se pasa a los detalles del libro
                Intent i = new Intent(mcontext, BookDetailsList.class);
                i.putExtra("title", bookInfo.getTitulo());

                i.putExtra("authors", bookInfo.getNombreAutoria());
                i.putExtra("publisher", bookInfo.getEditorial());
                i.putExtra("publishedDate", bookInfo.getAnioPublicacion());

                i.putExtra("pageCount", bookInfo.getPaginas());
                i.putExtra("thumbnail", bookInfo.getPortada());
                i.putExtra("libro", bookInfo);


                // after passing that data we are
                // starting our new intent.
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
