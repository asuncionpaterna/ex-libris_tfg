package com.example.booklist_tfg.ui.anadir;

import static com.example.booklist_tfg.MainActivity.floatingBTN;
import static com.example.booklist_tfg.MainActivity.mostrarBusquedaAvanzada;
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

public class AnadirFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private ArrayList<Libro> libroInfoArrayList;
    FrameLayout anadirFL;
    private ProgressBar progressBar;
    private EditText buscarET;
    private Button buscarBTN;
    RecyclerView mRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anadir, container, false);
        floatingBTN.hide();
        mostrarBusquedaAvanzada = false;
        requireActivity().invalidateOptionsMenu();
        // inicializando las vistas.
        progressBar = view.findViewById(R.id.idLoadingPB);
        anadirFL = view.findViewById(R.id.anadirFL);
        int modoOscuro = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        establecerTema(modoOscuro,anadirFL);
        buscarET = view.findViewById(R.id.idEdtSearchBooks);
        buscarBTN = view.findViewById(R.id.idBtnSearch);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.idRVBooks);

        buscarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (buscarET.getText().toString().isEmpty()) {
                    buscarET.setError("Introduce los datos de búsqueda");
                    return;
                }

                getBooksInfo(buscarET.getText().toString());
            }
        });
        return view;
    }

    private void getBooksInfo(String query) {

        // creating a new array list.
        libroInfoArrayList = new ArrayList<>();

        // below line is use to initialize
        // the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(getContext());

        // below line is use to clear cache this
        // will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;
        // below line we are creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                // inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        try {
                            JSONObject itemsObj = itemsArray.getJSONObject(i);
                            JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                            String titulo = volumeObj.optString("title");

                            JSONArray authorsArray = volumeObj.getJSONArray("authors");
                            ArrayList<String> authorsArrayList = new ArrayList<>();
                            if (authorsArray.length() != 0) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    authorsArrayList.add(authorsArray.optString(j));
                                }
                            }

                            String genero = volumeObj.optString("categories");
                            String anioPublicacion = volumeObj.optString("publishedDate");
                            String editorial = volumeObj.optString("publisher");
                            int paginas = volumeObj.optInt("pageCount");
                            JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                            String portada = imageLinks.optString("thumbnail");
                            // after extracting all the data we are
                            // saving this data in our modal class.

                            // below line is use to ---st."-
                            Libro bookInfo = new Libro(titulo, authorsArrayList, editorial, genero, anioPublicacion, paginas, portada);
                            libroInfoArrayList.add(bookInfo);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // below line is use to pass our
                        // array list in adapter class.
                        LibroAdapter adapter = new LibroAdapter(libroInfoArrayList, getContext());


                        // below line is use to add linear layout
                        // manager for our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

                        // in below line we are setting layout manager and
                        // adapter to our recycler view.
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(getContext(), "No se han encontrado datos" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(getContext(), "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }
}