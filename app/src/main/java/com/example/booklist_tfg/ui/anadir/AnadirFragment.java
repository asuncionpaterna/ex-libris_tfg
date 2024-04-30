package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
import static com.example.booklist_tfg.MainActivity.mostrarListaPeq;
import static com.example.booklist_tfg.utils.Utils.establecerTema;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.booklist_tfg.Model.Libro;
import com.example.booklist_tfg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Funcionalidad para añadir libros mediante la búsqueda en la API Google Books
public class AnadirFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private ArrayList<Libro> libroInfoArrayList;
    //Elementos de la pantalla
    FrameLayout anadirFL;
    private ProgressBar progressBar;
    private EditText buscarET;
    private Button buscarBTN;
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Se crea la vista del fragmento configurando los botones del menú y el botón añadir
        View view = inflater.inflate(R.layout.fragment_anadir, container, false);
        floatingBTN.hide();
        mostrarBusquedaAvanzada = false;
        mostrarListaPeq = false;

        requireActivity().invalidateOptionsMenu();
        //Se inicializan los elementos
        progressBar = view.findViewById(R.id.idLoadingPB);
        anadirFL = view.findViewById(R.id.anadirFL);
        buscarET = view.findViewById(R.id.idEdtSearchBooks);
        buscarBTN = view.findViewById(R.id.idBtnSearch);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.idRVBooks);

        //Se comprueba el tema del terminal (oscuro o claro) y se establece en la aplicación
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        establecerTema(modoOscuro, anadirFL);
        //Se configura la funcionalidad del botón de búsqueda
        buscarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //Se comprueba que el edit text tenga datos mediante una estructura de control if/else
                if (buscarET.getText().toString().isEmpty()) {
                    //Si está vacio se notifica mediante un error
                    buscarET.setError(getString(R.string.error_campo_vacio_busqueda));
                    return;
                }
                //Se llama al método getLibrosInfo con los datos introducidos por el usuario
                getLibrosInfo(buscarET.getText().toString());
            }
        });
        return view;
    }

    //Método para obtener información de los libros desde la API
    private void getLibrosInfo(String query) {

        // Se crea una variable para almacenar los resultados
        libroInfoArrayList = new ArrayList<>();

        // Se inicializa la cola de peticiones
        mRequestQueue = Volley.newRequestQueue(getContext());

        // Se limpia la memoria caché antes de realizar búsquedas nuevas
        mRequestQueue.getCache().clear();

        // URL para conseguir los datos en formato JSON desde la API de Google Books
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        // Se crea la variable de cola con el contexto
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // A continuación se crea un nuevo JsonObjectRequest para obtener los datos de la API
        JsonObjectRequest librosObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE); //Se oculta la barra de progreso
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        try {
                            // Mediante un bloque try/catch se extraen todos los datos json
                            JSONObject itemsObj = itemsArray.getJSONObject(i);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                            String titulo = volumeObj.optString("title");
                            JSONArray autoresArray = volumeObj.getJSONArray("authors");
                            ArrayList<String> autoresArrayList = new ArrayList<>();
                            if (autoresArray.length() != 0) {
                                for (int j = 0; j < autoresArray.length(); j++) {
                                    autoresArrayList.add(autoresArray.optString(j));
                                }
                            }
                            String genero = volumeObj.optString("categories");
                            String anioPublicacion = volumeObj.optString("publishedDate");
                            String editorial = volumeObj.optString("publisher");
                            int paginas = volumeObj.optInt("pageCount");
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String portada = imageLinks.optString("thumbnail");

                            // Una vez almacenados los datos se crea un libro y se almacena en el array list
                            Libro libroInfo = new Libro(titulo, autoresArrayList, editorial, genero, anioPublicacion, paginas, portada);
                            libroInfoArrayList.add(libroInfo);


                        } catch (Exception e) {
                            //Se capturan las posibles excepciones de la extracción
                            e.printStackTrace();
                        }
                        //Se configura RecycleView con el adaptador y el diseño
                        LibroAdapter adapter = new LibroAdapter(libroInfoArrayList, getContext());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Se captura la excepción en caso de error al procesar el JSON y se muestra
                    Toast.makeText(getContext(), getString(R.string.error_no_datos) + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Se muestran y manejan errores de la petición
                Toast.makeText(getContext(), getString(R.string.error_encontrado)+ error, Toast.LENGTH_SHORT).show();
            }
        });
        // Se añade la solicitud a la cola para su posterior ejecución
        queue.add(librosObjrequest);
    }
}