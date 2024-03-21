package com.example.booklist_tfg.ui.anadir;


import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    // creating variables for arraylist and context.
    private ArrayList<Libro> bookInfoArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public BookAdapter(ArrayList<Libro> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // inside on bind view holder method we are
        // setting our data to each UI component.
        Libro bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitulo());
        holder.publisherTV.setText(bookInfo.getEditorial());
        holder.dateTV.setText("" + bookInfo.getAnioPublicacion());
        holder.pageCountTV.setText("Páginas: " + bookInfo.getPaginas());
        // below line is use to set image from URL in our image view.
        Picasso.get().load(bookInfo.getPortada()).into(holder.bookIV);
        // below line is use to add on click listener for our item of recycler view.
        StringBuilder sb = new StringBuilder();
            ArrayList<String> autores = bookInfo.getNombreAutoria();
            for (int i = 0; i < autores.size(); i++){
                sb.append(autores.get(i));
                if(i< autores.size()-1){
                    sb.append(", ");
                }
        }
        holder.autoriaTV.setText("Autoria: " + sb);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // La informacióin que se pasa a los detalles del libro
                Intent i = new Intent(mcontext, BookDetails.class);
                i.putExtra("title", bookInfo.getTitulo());

                i.putExtra("authors", bookInfo.getNombreAutoria());
                i.putExtra("publisher", bookInfo.getEditorial());
                i.putExtra("publishedDate", bookInfo.getAnioPublicacion());

                i.putExtra("pageCount", bookInfo.getPaginas());
                i.putExtra("thumbnail", bookInfo.getPortada());


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
        return bookInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
        TextView nameTV, publisherTV, pageCountTV, dateTV, autoriaTV;
        ImageView bookIV;

        public BookViewHolder(View itemView) { // Esto es lo que sale al buscar, las tarjetas
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
            autoriaTV = itemView.findViewById(R.id.idTVautoria);

        }
    }


}
