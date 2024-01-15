package com.example.my_application.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.my_application.DateMask;
import com.example.my_application.R;
import com.example.my_application.SharedViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_A#} factory method to
 * create an instance of this fragment.
 */
public class Fragment_A extends Fragment {
    private static final int REQUEST_CODE = 11;
    private static final int FILE_SELECT_CODE = 0;

    private SharedViewModel sharedViewModel;

    private ComunicacionPrimerFragmento comunicacionPrimerFragmento;
    private SharedPreferences sharedPreferences;

    /*
    Controles del formulario
     */

    private TextInputEditText editTextNombre, etPlannedDate, etEndDate;
    private static TextInputEditText onEdit;
    private Spinner spinner;
    private Button siguiente, btnCancel, addFileDoc, addFileImg, addFileAudio, addFileVideo;
    private CheckBox checkBox;
    private String fileType, fechaTarea;
    private TextView tvNombre, tvFechaInicio, tvFechaFin, tvProgreso, tvFavorita, tvFile, tvFileImg, tvFileAud, tvFileVid, tvUpload;
    private boolean sdCard;
    private ImageButton deleteDoc, deleteImg, deleteAud, deleteVid;

    /*
    Constructor con parámetro
    @param modo - 1 para modo crear y 2 para modo edición
     */
    public Fragment_A() {
    }

    private int setIndex(int progreso) {
        int index = 0;
        if (progreso == 0) {
            index = 0;
        } else if (progreso <= 25 && progreso > 0) {
            index = 1;
        } else if (progreso <= 50 && progreso > 25) {
            index = 2;
        } else if (progreso <= 75 && progreso > 50) {
            index = 3;
        } else if (progreso <= 100 && progreso > 75) {
            index = 4;
        }
        return index;
    }

    //Sobrescribimos onAttach para establecer la comunicación entre el fragmento y la actividad
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ComunicacionPrimerFragmento) {  //Si la clase implementa la interfaz
            comunicacionPrimerFragmento = (ComunicacionPrimerFragmento) context; //La clase se convierte en escuchadora
        } else {
            throw new ClassCastException(context + " debe implementar interfaz de comunicación con el primer fragmento");
        }
    }


    /**
     * Método que define la lógica del botón de abrir archivo según su tipo
     * @param fileType
     */
    public void buttonOpenFile(String fileType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        if (fileType.equals("txt")) {
            intent.setType("text/*");
        } else if (fileType.equals("img")) {
            intent.setType("image/*");
        } else if (fileType.equals("audio")) {
            intent.setType("audio/*");
        } else if (fileType.equals("video")) {
            intent.setType("video/*");
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return inflater.inflate(R.layout.fragment__a, container, false);
    }

    /**
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        sdCard = sharedPreferences.getBoolean("sd", false);


        //Tamaño de letra de los tv y edit text
        String tamanhoLetra = sharedPreferences.getString("size", "2");
        float fontSize = 1.0f;
        float textSize = 16f;
        if (Integer.valueOf(tamanhoLetra) == 1) {
            fontSize = 0.8f;
            textSize = 14f;
        } else if (Integer.valueOf(tamanhoLetra) == 3) {
            fontSize = 1.3f;
            textSize = 18f;
        } else if (Integer.valueOf(tamanhoLetra) == 3) {
            fontSize = 1.0f;
            textSize = 12f;
        }
        adjustFontScale(getActivity().getResources().getConfiguration(), fontSize);

        attachmentButtons(view);

        componentsBinding(view, textSize);

        btnCancel.setOnClickListener(v -> {
            //Llamamos al método onBotonCancelarClicked que está implementado en la actividad.
            if (comunicacionPrimerFragmento != null) {
                comunicacionPrimerFragmento.onBotonCancelarClicked();
            }
        });

        ArrayAdapter<CharSequence> adaptadorProv = ArrayAdapter.createFromResource(
                getContext(),
                R.array.progress_array,
                android.R.layout.simple_spinner_item
        );
        adaptadorProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptadorProv);        // Inflate the layout for this fragment


        sharedViewModelObservers();


        //Date Pickers
        etPlannedDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onEdit = etPlannedDate;
                AppCompatDialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(Fragment_A.this, REQUEST_CODE);

                datePickerFragment.show(fm, "example");
            }

        });
        etEndDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                onEdit = etEndDate;
                AppCompatDialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setTargetFragment(Fragment_A.this, REQUEST_CODE);

                datePickerFragment.show(fm, "example");

            }
        });

        //Binding y config Button Siguiente
        siguiente.setTextSize(textSize);
        siguiente.setOnClickListener(v -> {
            //Validación de campos
            if (editTextNombre.getText().toString().isEmpty()
                    || etPlannedDate.getText().toString().isEmpty()
                    || etEndDate.getText().toString().isEmpty()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.ad_campos_incompletos))
                        .setMessage(getString(R.string.ad_campos_contenido))
                        .setPositiveButton(R.string.bt_aceptar, (dialog, id) -> {
                            dialog.dismiss();
                        })
                        .show();

            } else {//Escribimos en el ViewModel
                escribirViewModel();
                //Llamamos al método onBotonSiguienteClicked que está implementado en la actividad.
                if (comunicacionPrimerFragmento != null) {
                    comunicacionPrimerFragmento.onBotonSiguienteClicked();
                }
            }
        });
    }

    /**
     * Método para las acciones de los botones de archivos adjuntos
     *
     * @param view
     */
    private void attachmentButtons(@NonNull View view) {
        //Buttons upload
        Button btnDoc = view.findViewById(R.id.addFileDoc);
        btnDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene el directorio público de documentos
                fileType = "txt";
                buttonOpenFile(fileType);
            }
        });
        Button btnImagen = view.findViewById(R.id.addFileImg);
        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileType = "img";
                buttonOpenFile(fileType);
            }
        });

        Button btnAudio = view.findViewById(R.id.addFileAudio);
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileType = "audio";
                buttonOpenFile(fileType);

            }
        });

        Button btnVideo = view.findViewById(R.id.addFileVideo);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileType = "video";
                buttonOpenFile(fileType);
            }
        });
    }

    /**
     * Se definen los observadores de los distintos parámetros del viewModel
     */
    private void sharedViewModelObservers() {
        sharedViewModel.getNombre().observe(getViewLifecycleOwner(), s -> editTextNombre.setText(s));
        sharedViewModel.getFechaIni().observe(getViewLifecycleOwner(), s -> etPlannedDate.setText(s));
        sharedViewModel.getFechaFin().observe(getViewLifecycleOwner(), s -> etEndDate.setText(s));
        sharedViewModel.getProgreso().observe(getViewLifecycleOwner(), s -> spinner.setSelection(setIndex(s), true));
        sharedViewModel.getIsPriority().observe(getViewLifecycleOwner(), s -> checkBox.setChecked(s));
        sharedViewModel.getUrlDoc().observe(getViewLifecycleOwner(), s -> tvFile.setText(s));
        sharedViewModel.getUrlImg().observe(getViewLifecycleOwner(), s -> tvFileImg.setText(s));
        sharedViewModel.getUrlAud().observe(getViewLifecycleOwner(), s -> tvFileAud.setText(s));
        sharedViewModel.getUrlVid().observe(getViewLifecycleOwner(), s -> tvFileVid.setText(s));
    }

    /**
     * Personalización de los campos de texto y botones
     *
     * @param view
     * @param textSize
     */
    private void componentsBinding(@NonNull View view, float textSize) {

        tvFile = view.findViewById(R.id.tvFile);
        tvFileImg = view.findViewById(R.id.tvFileImg);
        tvFileAud = view.findViewById(R.id.tvFileAud);
        tvFileVid = view.findViewById(R.id.tvFileVid);
        deleteDoc = view.findViewById(R.id.deleteDoc);
        deleteImg = view.findViewById(R.id.deleteImg);
        deleteAud = view.findViewById(R.id.deleteAud);
        deleteVid = view.findViewById(R.id.deleteVid);
        //Binding y set EditText Título
        editTextNombre = view.findViewById(R.id.edNombre);
        siguiente= view.findViewById(R.id.btnSiguiente);

        //Añadimos los controles de la interfaz
        etPlannedDate = view.findViewById(R.id.etPlannedDate);
        etPlannedDate.addTextChangedListener(new DateMask());
        etEndDate = view.findViewById(R.id.etEndDate);
        etEndDate.addTextChangedListener(new DateMask());

        checkBox = view.findViewById(R.id.chk);

        editTextNombre.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_italic));
        editTextNombre.setTextSize(textSize);
        etPlannedDate.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_italic));
        etPlannedDate.setTextSize(textSize);

        etEndDate.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_italic));
        etEndDate.setTextSize(textSize);

        //Formato de fuente de cada TextView:
        tvNombre = view.findViewById(R.id.tvNombre);
        tvNombre.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_regular));
        tvNombre.setTextSize(textSize);

        tvFechaInicio = view.findViewById(R.id.tvFechaInicio);
        tvFechaInicio.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_regular));
        tvFechaInicio.setTextSize(textSize);

        tvFechaFin = view.findViewById(R.id.tvFechaFin);
        tvFechaFin.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.courierprime_regular));
        tvFechaFin.setTextSize(textSize);

        tvFavorita = view.findViewById(R.id.tvFavorita);
        tvFavorita.setTypeface(ResourcesCompat.getFont(getContext(), R.font.courierprime_regular));
        tvFavorita.setTextSize(textSize);

        tvProgreso = view.findViewById(R.id.tvProgreso);
        tvProgreso.setTypeface(ResourcesCompat.getFont(getContext(), R.font.courierprime_regular));
        tvProgreso.setTextSize(textSize);

        tvUpload = view.findViewById(R.id.tvUpload);
        tvUpload.setTypeface(ResourcesCompat.getFont(getContext(), R.font.courierprime_regular));
        tvUpload.setTextSize(textSize);


        //Dropdown menu config
        spinner = view.findViewById(R.id.spProgress);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setTextSize(textSize);

        addFileAudio = view.findViewById(R.id.addFileAudio);
        addFileAudio.setTextSize(textSize);

        addFileDoc = view.findViewById(R.id.addFileDoc);
        addFileDoc.setTextSize(textSize);

        addFileImg = view.findViewById(R.id.addFileImg);
        addFileImg.setTextSize(textSize);

        addFileVideo = view.findViewById(R.id.addFileVideo);
        addFileVideo.setTextSize(textSize);


    }


    /**
     * Método para guardar el estado del formulario en un Bundle
     */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("titulo", editTextNombre.getText().toString());
        outState.putString("fechaCreacion", etPlannedDate.getText().toString());
        outState.putString("fechaObjetivo", etEndDate.getText().toString());
        outState.putInt("progreso", spinner.getSelectedItemPosition());
        outState.putBoolean("prioritaria", checkBox.isChecked());
        outState.putString("urlDoc", tvFile.getText().toString());
        outState.putString("urlImg", tvFileImg.getText().toString());
        outState.putString("urlAud", tvFileAud.getText().toString());
        outState.putString("urlVid", tvFileVid.getText().toString());

        //Sincronizamos la información salvada también en el ViewModel
        escribirViewModel();
    }

    /**
     * Método para restaurar el estado del formulario
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            editTextNombre.setText(savedInstanceState.getString("titulo"));
            etPlannedDate.setText(savedInstanceState.getString("fechaCreacion"));
            etEndDate.setText(savedInstanceState.getString("fechaObjetivo"));
            spinner.setSelection(savedInstanceState.getInt("progreso"));
            checkBox.setChecked(savedInstanceState.getBoolean("prioritaria"));
            tvFile.setText(savedInstanceState.getString("urlDoc"));
            tvFileImg.setText(savedInstanceState.getString("urlImg"));
            tvFileAud.setText(savedInstanceState.getString("urlAud"));
            tvFileVid.setText(savedInstanceState.getString("urlVid"));

        }

    }

    /**
     * Método que ajusta el tamaño de letra de algunos recursos de la actividad
     *
     * @param configuration
     * @param scale
     */
    public void adjustFontScale(Configuration configuration, float scale) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(getActivity().WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getActivity().getResources().updateConfiguration(configuration, metrics);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        // check for the results
        if (requestCode == 11 && resultCode == RESULT_OK) {
            // get date from string
            fechaTarea = data.getStringExtra("selectedDate");
            LocalDate fecha1 = LocalDate.parse(fechaTarea, formatter).plusDays(1);

            String fechaTareaFinalDefault = fecha1.format(formatter).toString();
            // set the value of the editText
            if (onEdit == etPlannedDate) {
                etEndDate.setText(fechaTareaFinalDefault);
                onEdit.setText(fechaTarea);

            } else if (onEdit == etEndDate && (etPlannedDate.getText().toString().trim().length() > 0)) {
                if (LocalDate.parse(fechaTarea, formatter).isBefore(LocalDate.parse(etPlannedDate.getText().toString(), formatter))) {
                    etEndDate.setText(etPlannedDate.getText().toString());
                } else {
                    onEdit.setText(fechaTarea);
                }
            } else {
                onEdit.setText(fechaTarea);

            }
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (fileType.equals("txt")) {
                tvFile.setText(uri.getPath());
                sharedViewModel.setUrlDoc(tvFile.getText().toString());
                deleteDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Llamada al método para eliminar el archivo
                        deleteFile(tvFile.getText().toString());
                        // Limpiar la vista y el ViewModel
                        tvFile.setText("");
                        sharedViewModel.setUrlDoc("");
                    }
                });
            } else if (fileType.equals("img")) {
                tvFileImg.setText(uri.getPath());
                sharedViewModel.setUrlImg(tvFileImg.getText().toString());
                deleteImg.setVisibility(View.VISIBLE);
                deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Llamada al método para eliminar el archivo
                        deleteFile(tvFileImg.getText().toString());
                        // Limpiar la vista y el ViewModel
                        tvFileImg.setText("");
                        sharedViewModel.setUrlImg("");
                    }
                });
            } else if (fileType.equals("audio")) {
                tvFileAud.setText(uri.getPath());
                sharedViewModel.setUrlAud(tvFileAud.getText().toString());
            } else if (fileType.equals("video")) {
                tvFileVid.setText(uri.getPath());
                sharedViewModel.setUrlVid(tvFileVid.getText().toString());
            }

            try {
                copyFile(getContext(), uri, createFolder(getContext(), sdCard));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


                /* // Aquí puedes manejar el URI del archivo seleccionado según el tipo de archivo
            createFile(uri);

            // Por ejemplo, puedes mostrar el nombre del archivo o realizar acciones específicas

            String mensaje = "Archivo seleccionado: " + uri.toString();
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();*/
        }

    }



    private void deleteFile(String filePath) {
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (deleted) {
                Toast.makeText(getContext(), "Archivo eliminado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No se pudo eliminar el archivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void copyFile(Context context, Uri sourceUri, File targetLocation)
            throws IOException {
        try (InputStream in = context.getContentResolver().openInputStream(sourceUri);
             OutputStream out = new FileOutputStream(new File(targetLocation, getFileNameFromUri(sourceUri)))) {
            if (in == null) {
                throw new FileNotFoundException("Error opening input stream for URI");
            }

            // Copia los bits desde el flujo de entrada al flujo de salida
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        }
    }

    public File createFolder(Context context, boolean isExternal) {
        File folder;
        String estado = Environment.getExternalStorageState();
        if (isExternal && estado.equals(Environment.MEDIA_MOUNTED)) {
            // Crear carpeta en almacenamiento externo
            File documentsDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS);

            folder = new File(documentsDir, "Tareas");
        } else {

            // Crear carpeta en almacenamiento interno
            folder = new File(getContext().getFilesDir(), "Tareas");
        }

        if (!folder.exists()) {
            folder.mkdirs(); // Crea la carpeta y sus directorios padres si no existen
        }

        return folder;
    }


    void savefile() {
        String sourceFilename = "content://com.android.providers.media.document/documents_root";
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "abc.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void saveTxtfile() {
        String sourceFilename = "content://com.android.providers.media.document/documents_root";
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "abc.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileNameFromUri(Uri uri) {
        String fileName = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            // If the URI is a content URI, try to query the file name using a content resolver
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        fileName = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        if (fileName == null) {
            // If the file name couldn't be obtained from the content resolver, try to get it from the last path segment
            fileName = uri.getLastPathSegment();
        }

        return fileName;
    }

    /**
     * Interfaz
     */
    public interface ComunicacionPrimerFragmento {
        void onBotonSiguienteClicked();

        void onBotonCancelarClicked();
    }

    /**
     * Escribir en el viewModel la información introducida
     */
    private void escribirViewModel() {
        sharedViewModel.setNombre(editTextNombre.getText().toString());
        sharedViewModel.setFechaIni(etPlannedDate.getText().toString());
        sharedViewModel.setFechaFin(etEndDate.getText().toString());
        sharedViewModel.setProgreso(25 * spinner.getSelectedItemPosition());
        sharedViewModel.setIsPriority(checkBox.isChecked());
        sharedViewModel.setUrlDoc(tvFile.getText().toString());
        sharedViewModel.setUrlImg(tvFileImg.getText().toString());
        sharedViewModel.setUrlAud(tvFileAud.getText().toString());
        sharedViewModel.setUrlVid(tvFileVid.getText().toString());
    }

}


