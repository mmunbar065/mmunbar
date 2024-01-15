package com.example.my_application.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.my_application.R;
import com.example.my_application.SharedViewModel;
import com.example.my_application.Tarea;

import java.util.ArrayList;

public class Fragment_B extends Fragment {
    private Button volver, guardar;

    private SharedViewModel sharedViewModel;


    private EditText textDescripcion;


    private TextView tvDescription;

    public interface ComunicacionSegundoFragmento {
        void onBotonGuardarClicked();
        void onBotonVolverClicked();
    }

    private ComunicacionSegundoFragmento comunicadorSegundoFragmento;
    private static ArrayList<Tarea> datos = new ArrayList<>();


    public Fragment_B() {
    }

    //Sobrescribimos onAttach para establecer la comunicación entre el fragmento y la actividad
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicacionSegundoFragmento) { //Si la actividad implementa la interfaz
            comunicadorSegundoFragmento = (ComunicacionSegundoFragmento) context; //La actividad se convierte en escuchadora
        } else {
            throw new ClassCastException(context + " debe implementar la interfaz de comunicación con el segundo fragmento");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        return  inflater.inflate(R.layout.fragment__b, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textDescripcion = view.findViewById(R.id.edDescripcion);
        textDescripcion.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_italic));
        tvDescription = view.findViewById(R.id.tvDescripcion);
        tvDescription.setTypeface(ResourcesCompat.getFont(getContext(), R.font.courierprime_bold));

        sharedViewModel.getDescripcion().observe(getViewLifecycleOwner(), s -> textDescripcion.setText(s));

        volver = view.findViewById(R.id.btnVolver);
        guardar = view.findViewById(R.id.btnGuardar);
        volver.setOnClickListener(v -> {
            //Escribimos en el ViewModel
            escribirViewModel();
            //Llamamos al método onBotonVolverClicked que está implementado en la actividad
            if (comunicadorSegundoFragmento != null)
                comunicadorSegundoFragmento.onBotonVolverClicked();
        });

        //Binding y config boton Guardar
        guardar.setOnClickListener(v -> {
            //Escribimos en el ViewModel
            sharedViewModel.setDescripcion(textDescripcion.getText().toString());

            //Llamamos al método onBotonGuardarClicked que está implementado en la actividad.
            if (comunicadorSegundoFragmento != null)
                comunicadorSegundoFragmento.onBotonGuardarClicked();
        });

    }
    //Método para guardar el estado del formulario en un Bundle
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("descripcion",  textDescripcion.getText().toString());
        //Sincronizamos la información salvada también en el ViewModel
        escribirViewModel();
    }

    //Método para restaurar el estado del formulario
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null) {
            textDescripcion.setText(savedInstanceState.getString("descripcion"));
        }
    }
    private void escribirViewModel() {
        sharedViewModel.setDescripcion(textDescripcion.getText().toString());
    }
}