package com.example.booklist_tfg.ui.listado;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookAdapterList.BookViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapterList.BookViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
        TextView tituloTV, editorialTV, generoTV, fechaLecturaTV, autoriaTV;
        ImageView portadaIV;

        public BookViewHolder(View itemView) { // Esto es lo que sale al buscar, las tarjetas
            super(itemView);
            tituloTV = itemView.findViewById(R.id.idTVTituloList);
            autoriaTV = itemView.findViewById(R.id.idTVAutoriaList);
            editorialTV = itemView.findViewById(R.id.idTVEditorialList);
            generoTV = itemView.findViewById(R.id.idTGeneroList);
            fechaLecturaTV = itemView.findViewById(R.id.idTVFechaLecturaList);
            portadaIV = itemView.findViewById(R.id.idIVPortadaList);

        }
    }
}
