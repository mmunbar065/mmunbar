package com.example.my_application.activities;
import com.example.my_application.BaseDatosApp;
import com.example.my_application.TareaRepository;
import com.example.my_application.fragments.Fragment_A;
import com.example.my_application.fragments.Fragment_A.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.room.Insert;

import com.example.my_application.R;
import com.example.my_application.SharedViewModel;
import com.example.my_application.Tarea;
import com.example.my_application.fragments.Fragment_A;
import com.example.my_application.fragments.Fragment_B;

import java.util.Objects;

public class EditarTareaActivity extends AppCompatActivity implements Fragment_A.ComunicacionPrimerFragmento,
        Fragment_B.ComunicacionSegundoFragmento {

    public final Fragment_A fragmentA=new Fragment_A();
    private final Fragment_B fragmentB = new Fragment_B();

    private final int mode = 2;
    private Tarea tareaEditable;
    private FragmentManager fragmentManager;
    private SharedViewModel sharedViewModel;
    private String titulo, descripcion;
    private String fechaCreacion, diasRestantes, fechaObjetivo, urlDoc=null, urlImg=null, urlVid=null, urlAud=null;
    private Integer progreso;
    private Boolean prioritaria;
    private BaseDatosApp baseDatosApp;
    private TareaRepository tareaRepository;
    private long fontSize;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Crea el repositorio
        tareaRepository = new TareaRepository(getApplication());

        Bundle bundle = getIntent().getExtras();

        try {
            if (bundle != null) {
                this.tareaEditable = bundle.getParcelable("TareaEditable");
            }
        } catch (NullPointerException e) {
            Log.e("Bundle recibido nulo", e.toString());
        }
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            int fragmentId = savedInstanceState.getInt("fragmentoId", -1);
            if (fragmentId != -1) {
                // Usar el ID o información para encontrar y restaurar el fragmento
                cambiarFragmento(Objects.requireNonNull(fragmentManager.findFragmentById(fragmentId)));
            } else {
                //Si no tenemos ID de fragmento cargado, cargamos el primer fragmento
                cambiarFragmento(fragmentA);
            }
        } else {
            //Si no hay estado guardado, cargamos el primer fragmento
            cambiarFragmento(fragmentA);
            sharedViewModel.setNombre(tareaEditable.getTitulo());
            sharedViewModel.setFechaIni(tareaEditable.getFecha());
            sharedViewModel.setFechaFin(tareaEditable.getFechaObjetivo());
            sharedViewModel.setProgreso(tareaEditable.getProgreso());
            sharedViewModel.setIsPriority(tareaEditable.isPrioritaria());
            sharedViewModel.setDescripcion(tareaEditable.getDescripcion());
            sharedViewModel.setUrlDoc(tareaEditable.getUrlDocumento());
            sharedViewModel.setUrlImg(tareaEditable.getUrlImagen());
            sharedViewModel.setUrlAud(tareaEditable.getUrlAudio());
            sharedViewModel.setUrlVid(tareaEditable.getUrlVideo());
        }




    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int fragmentID = Objects.requireNonNull(getSupportFragmentManager().
                findFragmentById(R.id.Contenedor_C)).getId();
        outState.putInt("fragmentoId", fragmentID);
    }

    public void cambiarFragmento(Fragment fragment) {
        if (!fragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .replace(R.id.Contenedor_C, fragment)
                    .commit();
        }
    }

     //Implementamos los métodos de la interfaz de comunicación con el primer fragmento
    @Override
    public void onBotonSiguienteClicked() {

        //Leemos los valores del formulario del fragmento 1
        titulo = sharedViewModel.getNombre().getValue();
        fechaCreacion = sharedViewModel.getFechaIni().getValue();
        fechaObjetivo = sharedViewModel.getFechaFin().getValue();
        progreso = sharedViewModel.getProgreso().getValue();
        prioritaria = sharedViewModel.getIsPriority().getValue();
        if(!(sharedViewModel.getUrlDoc().getValue()==null)){
        urlDoc=sharedViewModel.getUrlDoc().getValue();}
        if(!(sharedViewModel.getUrlImg().getValue()==null)){
        urlImg=sharedViewModel.getUrlImg().getValue();}
        if(!(sharedViewModel.getUrlAud().getValue()==null)){
        urlAud=sharedViewModel.getUrlAud().getValue();}
        if(!(sharedViewModel.getUrlVid().getValue()==null)){
        urlVid=sharedViewModel.getUrlVid().getValue();}


        //Cambiamos el fragmento
        cambiarFragmento(fragmentB);
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
        //Creamos un nuevo objeto tarea con los campos editados
        Tarea tareaEditada = new Tarea(titulo, fechaCreacion,fechaObjetivo, progreso,prioritaria, descripcion, urlDoc, urlImg, urlAud, urlVid);
       tareaRepository.insertTareas(tareaEditada);

        //Creamos un intent de vuelta para la actividad Listado
        Intent aListado = new Intent();
        //Creamos un Bundle para introducir la tarea editada
        Bundle bundle = new Bundle();
        bundle.putParcelable("TareaEditada", tareaEditada);
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
        cambiarFragmento(fragmentA);
    }
}



