package com.example.my_application.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_application.BaseDatosApp;
import com.example.my_application.R;
import com.example.my_application.SharedViewModel;
import com.example.my_application.Tarea;
import com.example.my_application.TareaAdapter;
import com.example.my_application.TareaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListadoTareasActivity extends AppCompatActivity {
    private RecyclerView rv;
    private ArrayList<Tarea> tareas = new ArrayList<>();
    private ArrayList<Tarea> listaTareas = new ArrayList<>();

    ArrayList<Tarea> prioritarias;
    private TareaAdapter adaptador;
    private MenuItem menuItemPrior;
    private String order, fontSize;
    private SharedViewModel sharedViewModel;

    private Boolean filtroPrioritariasActivado = false, boolPrior = false, nightMode = false, asc_desc=true;
    private Tarea tareaSeleccionada;
    private Integer tareaSeleccionadaPosicion;
    private TextView listadoVacio;
    private SharedPreferences sharedPreferences;
    private int fontSizeInt;
    private TareaRepository tareaRepository;
    private BaseDatosApp baseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crea el repositorio
        tareaRepository = new TareaRepository(getApplication());


        //RESTAURACIÓN DEL ESTADO GLOBAL DE LA ACTIVIDAD
        if (savedInstanceState != null) {
            //Recuperamos la lista de Tareas y el booleano prioritarias
            //tareas = savedInstanceState.getParcelableArrayList("listaTareas");
            boolPrior = savedInstanceState.getBoolean("boolPrior");

        } else {
            //Inicializamos la lista de tareas y el booleano prioritarias
            tareaRepository.deleteAll();
            inicializarTareas();
            boolPrior = false;
        }
    }

    //SALVADO DEL ESTADO GLOBAL DE LA ACTIVIDAD
    //Salva la lista de tareas y el valor booleano de prioritarias para el caso en que la actividad
    // sea destruida por ejemplo al cambiar la orientación del dispositivo
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("listaTareas", tareas);
        outState.putBoolean("prioritarias", boolPrior);
    }


    @Override
    protected void onResume() {
        super.onResume();
        tareaRepository = new TareaRepository(getApplication());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (!nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        //Binding del textview
        setContentView(R.layout.activity_listado_tareas);


        //Binding del TextView
        listadoVacio = findViewById(R.id.tvEmptyRv);

        //Binding del RecyclerView
        rv = findViewById(R.id.rvTareas);

        order = sharedPreferences.getString("criterios", "2");
        fontSize = sharedPreferences.getString("size", "2");
        asc_desc=sharedPreferences.getBoolean("orden", true);


        if (fontSize.equals("2")) {
            fontSizeInt = 12;
        } else if (fontSize.equals("1")) {
            fontSizeInt = 9;
        } else if (fontSize.equals("3")) {
            fontSizeInt = 16;
        }


        adaptador = new TareaAdapter(this, new ArrayList<>(), boolPrior);
        adaptador.setFontSize(fontSizeInt);

        List<Tarea> initialTareas = new ArrayList<>();
        tareaRepository.getAllTareas(order, asc_desc, boolPrior).observe(this, listaTareas ->
        {
            if (listaTareas != null && !listaTareas.isEmpty()) {
                adaptador.setTareas(listaTareas);
                rv.setAdapter(adaptador);
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        registerForContextMenu(rv);
        rv.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                // Toast.makeText(getBaseContext(), "Posicion:" + holder.getAdapterPosition(), Toast.LENGTH_LONG).show();
            }
        });
        comprobarListadoVacio();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_contextual, menu);
        menu.setGroupVisible(R.id.it_group_preferencias, true);

        menuItemPrior = menu.findItem(R.id.btnPrioritarias);
        //Colocamos el icono adecuado
        iconoPrioritarias();
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnAcercaDe) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.trasstarea);
            imageView.requestLayout();

            //Creamos un AlertDialog como cuadro de diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.about_title);
            builder.setView(imageView);
            builder.setMessage(R.string.about_msg);
            // Botón "Aceptar"
            builder.setPositiveButton(R.string.about_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Cerrar el cuadro de diálogo.
                }
            });
            // Mostrar el cuadro de diálogo
            builder.create().show();
        } else if (item.getItemId() == R.id.btnSalir) {
            Toast.makeText(this, "Hasta pronto", Toast.LENGTH_SHORT).show();
            finishAffinity();
        } else if (item.getItemId() == R.id.btnAddTask) {
            //Llamada al launcher con contrato y respuesta definidos
            lanzadorCrearTarea.launch(null);
        } else if (item.getItemId() == R.id.btnPrioritarias) {
            //Conmutamos el valor booleando
            boolPrior = !boolPrior;
            //Colocamos el icono adecuado
            iconoPrioritarias();
            tareaRepository.getAllTareas(order, asc_desc, boolPrior).observe(this, listaTareas ->
            {
                if (listaTareas != null && !listaTareas.isEmpty()) {
                    adaptador.setTareas(listaTareas);
                    rv.setAdapter(adaptador);
                }
            });

            //Comprobamos que hay algún elemento que mostrar
            comprobarListadoVacio();
        } else if (item.getItemId() == R.id.preferencias) {
            startActivity(new Intent(this, PreferencesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void inicializarTareas() {
        tareaRepository.insertTareas(new Tarea("Hacer el cuestionario inicial", "10/09/2023", "17/09/2023", 100, true, "", null, null, null, null));
        tareaRepository.insertTareas(new Tarea("Hacer la tarea UT01", "18/09/2023", "03/10/2023", 100, true, "", null, null, null, null));
        tareaRepository.insertTareas(new Tarea("Hacer cuestionarios UT01", "18/09/2023", "01/10/2023", 100, false, "", null, null, null, null));
        tareaRepository.insertTareas(new Tarea("Hacer cuestionarios UT05", "13/02/2024", "07/04/2024", 0, false, "", null, null, null, null));
        tareaRepository.insertTareas(new Tarea("Hacer la tarea UT06", "08/04/2024", "20/05/2024", 0, true, "", null, null, null, null));


    }


    /**
     * Método onContextItemSelected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //Leemos la tarea seleccionada en el evento de mostrar el menú contextual
        tareaSeleccionada = adaptador.getTareaSeleccionada();

        int itemId = item.getItemId();
        if (itemId == R.id.btnDescripcion) {
            // Mostrar un cuadro de diálogo con la descripción de la tarea
            lanzadorActividadDetalle.launch(tareaSeleccionada);
            return true;
        }
        if (itemId == R.id.btnEditar) {
            lanzadorActividadEditar.launch(tareaSeleccionada);
            return true;
        }
        if (itemId == R.id.btnBorrar) {


/*                if (!filtroPrioritariasActivado) {
                    tareas.remove(tareaSeleccionadaPosicion);
                } else if (filtroPrioritariasActivado) {
                    prioritarias.remove(tareaSeleccionadaPosicion);
                    tareas.remove(tareaSeleccionadaPosicion);
                }*/

                tareaRepository.deleteTarea(tareaSeleccionada);
                // Notificar al adaptador que los datos han cambiado
/*
                adaptador.notifyItemRemoved(tareaSeleccionadaPosicion);
*/
                comprobarListadoVacio();



        }

        comprobarListadoVacio();

        return super.

                onContextItemSelected(item);

    }

    ////////////////////////// COMUNICACIONES CON ACTIVIDADES SECUNDARIAS //////////////////////////
    //Contrato personalizado para el lanzador hacia la actividad CrearTareaActivity
    ActivityResultContract<Tarea, Tarea> contratoDetalle = new ActivityResultContract<Tarea, Tarea>() {
        @Override
        public Tarea parseResult(int i, @Nullable Intent intent) {
            return null;
        }

        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Tarea tarea) {
            Intent intent = new Intent(context, DetalleTareaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("TareaEditable", tarea);
            intent.putExtras(bundle);
            return intent;
        }

    };


    //Registramos el lanzador hacia la actividad CrearTareaActivity con el contrato personalizado y respuesta con implementación anónima
    ActivityResultLauncher<Tarea> lanzadorActividadDetalle = registerForActivityResult(contratoDetalle, new ActivityResultCallback<Tarea>() {
        @Override
        public void onActivityResult(Tarea result) {
            // Aquí puedes manejar la Tarea devuelta por la actividad DetalleTareaActivity
            if (result != null) {
                // Hacer algo con la tarea recibida
            }
        }
    });

    //Contrato personalizado para el lanzador hacia la actividad CrearTareaActivity
    ActivityResultContract<Intent, Tarea> contratoCrear = new ActivityResultContract<Intent, Tarea>() {
        //En primer lugar se define el Intent de ida
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Intent intent) {
            return new Intent(context, CrearTareaActivity.class);
        }

        //Ahora se define el método de parseo de la respuesta. En este caso se recibe un objeto Tarea
        @Override
        public Tarea parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                try {
                    return (Tarea) Objects.requireNonNull(intent.getExtras()).get("NuevaTarea");
                } catch (NullPointerException e) {
                    Log.e("Error en intent devuelto", Objects.requireNonNull(e.getLocalizedMessage()));
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), R.string.action_canceled, Toast.LENGTH_SHORT).show();
            }
            return null; // Devuelve null si no se pudo obtener una Tarea válida.
        }
    };

    //Registramos el lanzador hacia la actividad CrearTareaActivity con el contrato personalizado y respuesta con implementación anónima
    private final ActivityResultLauncher<Intent> lanzadorCrearTarea = registerForActivityResult(contratoCrear, new ActivityResultCallback<Tarea>() {
        @Override
        public void onActivityResult(Tarea nuevaTarea) {
            if (nuevaTarea != null) {
                //tareas.add(nuevaTarea);
                ////////////////////////
                adaptador.notifyItemInserted(tareas.size() - 1); // Agregar el elemento nuevo al adaptador.
                Toast.makeText(ListadoTareasActivity.this.getApplicationContext(), R.string.tarea_add, Toast.LENGTH_SHORT).show();
                ListadoTareasActivity.this.comprobarListadoVacio();
            }
        }
    });


    //Contrato para el lanzador hacia la actividad EditarTareaActivity
    ActivityResultContract<Tarea, Tarea> contratoEditar = new ActivityResultContract<Tarea, Tarea>() {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Tarea tarea) {
            Intent intent = new Intent(context, EditarTareaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("TareaEditable", tarea);
            intent.putExtras(bundle);
            return intent;
        }

        @Override
        public Tarea parseResult(int i, @Nullable Intent intent) {
            if (i == Activity.RESULT_OK && intent != null) {
                try {
                    return (Tarea) Objects.requireNonNull(intent.getExtras()).get("TareaEditada");
                } catch (NullPointerException e) {
                    Log.e("Error en intent devuelto", Objects.requireNonNull(e.getLocalizedMessage()));
                }
            } else if (i == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), R.string.action_canceled, Toast.LENGTH_SHORT).show();
            }
            return null; // Devuelve null si no se pudo obtener una Tarea válida.
        }
    };

    //Respuesta para el lanzador hacia la actividad EditarTareaActivity
    ActivityResultCallback<Tarea> resultadoEditar = new ActivityResultCallback<Tarea>() {
        @Override
        public void onActivityResult(Tarea tareaEditada) {
            if (tareaEditada != null) {
                //Seteamos el id de la tarea recibida para que coincida con el de la tarea editada
                tareaEditada.setId(tareaSeleccionada.getId());

                //Procedemos a la sustitución de la tarea editada por la seleccionada.
                int posicionEnColeccion = tareas.indexOf(tareaSeleccionada);
               // tareas.remove(tareaSeleccionada);
                //tareas.add(posicionEnColeccion, tareaEditada);
                tareaRepository.deleteTarea(tareaSeleccionada);

                //Notificamos al adaptador y comprobamos si el listado ha quedado vacío
                adaptador.notifyItemChanged(posicionEnColeccion);
                comprobarListadoVacio();

                //Comunicamos que la tarea ha sido editada al usuario
                Toast.makeText(getApplicationContext(), R.string.tarea_edit, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //Registramos el lanzador hacia la actividad EditarTareaActivity con el contrato y respuesta personalizados
    ActivityResultLauncher<Tarea> lanzadorActividadEditar = registerForActivityResult(contratoEditar, resultadoEditar);

    private void iconoPrioritarias() {
        if (boolPrior)
            //Ponemos en la barra de herramientas el icono PRIORITARIAS
            menuItemPrior.setIcon(R.drawable.baseline_star_outline_24);
        else
            //Ponemos en la barra de herramientas el icono NO PRIORITARIAS
            menuItemPrior.setIcon(R.drawable.baseline_star_outline_24_crossed);
    }

    //Método que comprueba si el listado de tareas está vacío.
    //Cuando está vacío oculta el RecyclerView y muestra el TextView con el texto correspondiente.
    private void comprobarListadoVacio() {

        tareaRepository.getAllTareas(order, asc_desc, boolPrior).observe(this, listaTareas ->
        {
            if (listaTareas.isEmpty()) {
                listadoVacio.setText(boolPrior ? R.string.listado_tv_no_prioritarias : R.string.texto2);
                listadoVacio.setVisibility(View.VISIBLE);
            }else{
                listadoVacio.setVisibility(View.GONE);

            }
        });
        ViewTreeObserver vto = rv.getViewTreeObserver();

/*
        //Observamos cuando el RecyclerView esté completamente terminado
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Contamos la altura total del RecyclerView
                int alturaRV = 0;
                for (int i = 0; i < adaptador.getItemCount(); i++) {
                    View itemView = rv.getChildAt(i);
                    if (itemView != null)
                        alturaRV += itemView.getHeight();
                }

                if (alturaRV == 0) {
                    listadoVacio.setText(boolPrior ? R.string.listado_tv_no_prioritarias : R.string.texto2);
                    listadoVacio.setVisibility(View.VISIBLE);
                } else {
                    listadoVacio.setVisibility(View.GONE);
                }

                // Remueve el oyente para evitar llamadas innecesarias
                rv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
*/


    }


}
