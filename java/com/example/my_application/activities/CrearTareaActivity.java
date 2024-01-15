package com.example.my_application.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_application.BaseDatosApp;
import com.example.my_application.R;
import com.example.my_application.SharedViewModel;
import com.example.my_application.Tarea;
import com.example.my_application.TareaRepository;
import com.example.my_application.fragments.Fragment_A;
import com.example.my_application.fragments.Fragment_B;

import java.util.Objects;

public class CrearTareaActivity extends AppCompatActivity implements
        Fragment_A.ComunicacionPrimerFragmento,
        Fragment_B.ComunicacionSegundoFragmento {

    private String titulo, descripcion;
    private String fechaCreacion, fechaObjetivo;
    private String urlDoc=null, urlImg=null, urlVid=null, urlAud=null;;
    private Integer progreso;
    private Boolean prioritaria = false;
    private final Fragment fragment_A = new Fragment_A();
    private final Fragment fragment_B = new Fragment_B();
    private BaseDatosApp baseDatosApp;
    private SharedViewModel sharedViewModel;
    private FragmentManager miGestorFragmentos;
    private TareaRepository tareaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crea el repositorio
        tareaRepository = new TareaRepository(getApplication());
        setContentView(R.layout.activity_crear_tarea);


        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        // Obtener el ArrayList de objetos Parcelable sin tipo específico


        miGestorFragmentos = getSupportFragmentManager();
        //Si hay estado guardado
        if (savedInstanceState != null) {
            // Recuperar el ID o información del fragmento
            int fragmentId = savedInstanceState.getInt("fragmentoId", -1);

            if (fragmentId != -1) {
                // Usar el ID o información para encontrar y restaurar el fragmento
                cargaFragmento(Objects.requireNonNull(miGestorFragmentos.findFragmentById(fragmentId)));
            } else {
                //Si no tenemos ID de fragmento cargado, cargamos el primer fragmento
                cargaFragmento(fragment_A);
            }
        } else {
            //Si no hay estado guardado, cargamos el primer fragmento
            cargaFragmento(fragment_A);
        }


    }

    @Override
    public void onBotonSiguienteClicked() {
        //Leemos los valores del formulario del fragmento 1
        titulo = sharedViewModel.getNombre().getValue();
        fechaCreacion = sharedViewModel.getFechaIni().getValue();
        fechaObjetivo = sharedViewModel.getFechaFin().getValue();
        progreso = sharedViewModel.getProgreso().getValue();
        prioritaria = sharedViewModel.getIsPriority().getValue();
        if(!(sharedViewModel.getUrlDoc()==null)){
            urlDoc=sharedViewModel.getUrlDoc().getValue();}
        if(!(sharedViewModel.getUrlImg()==null)){
            urlImg=sharedViewModel.getUrlImg().getValue();}
        if(!(sharedViewModel.getUrlAud()==null)){
            urlAud=sharedViewModel.getUrlAud().getValue();}
        if(!(sharedViewModel.getUrlVid()==null)){
            urlVid=sharedViewModel.getUrlVid().getValue();}

        //Cambiamos el fragmento
        cargaFragmento(fragment_B);
    }

    @Override
    public void onBotonCancelarClicked() {
        Intent aListado = new Intent();
        //Indicamos en el resultado que ha sido cancelada la actividad
        setResult(RESULT_CANCELED, aListado);
        //Volvemos a la actividad Listado
        finish();
    }

    //Implementamos los métodos de la interfaz de comunicación con el segundo fragmento
    @Override
    public void onBotonGuardarClicked() {
        //Leemos los valores del formulario del fragmento 2
        descripcion = sharedViewModel.getDescripcion().getValue();
        //Creamos la nueva tarea
        Tarea nuevaTarea = new Tarea(titulo, fechaCreacion, fechaObjetivo, progreso, prioritaria, descripcion,urlDoc, urlImg, urlAud, urlVid);
        tareaRepository.insertTareas(nuevaTarea);

        //Creamos un intent de vuelta para la actividad Listado
        Intent aListado = new Intent();
        //Creamos un Bundle para introducir la nueva tarea
        Bundle bundle = new Bundle();
        bundle.putParcelable("NuevaTarea", nuevaTarea);
        aListado.putExtras(bundle);

        //Indicamos que el resultado ha sido OK
        setResult(RESULT_OK, aListado);

        //Volvemos a la actividad Listado
        finish();
    }

    @Override
    public void onBotonVolverClicked() {
        //Leemos los valores del formulario del fragmento 2
        descripcion = sharedViewModel.getDescripcion().getValue();
        //Cambiamos el fragmento2 por el 1
        cargaFragmento(fragment_A);
    }

    public void cargaFragmento(Fragment fragment) {
        if (!fragment.isAdded()) {
            miGestorFragmentos.beginTransaction()
                    .replace(R.id.Contenedor_B, fragment)
                    .commit();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int fragmentID = Objects.requireNonNull(getSupportFragmentManager().
                findFragmentById(R.id.Contenedor_B)).getId();
        outState.putInt("fragmentoId", fragmentID);
    }


}
