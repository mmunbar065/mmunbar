package com.example.my_application.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import com.example.my_application.R;
import com.example.my_application.Tarea;

public class DetalleTareaActivity extends AppCompatActivity {

    private Button btnCancelar, btnAceptar;
    private TextView tvNombre, tvDocFile, tvImgFile, tvAudFile, tvVidFile, tvDescription;
    private Tarea tarea;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        btnAceptar = findViewById(R.id.btn_aceptar);

        tvNombre = findViewById(R.id.titulo_descripcion);
        tvDescription = findViewById(R.id.seccion_descripcion);
        tvDocFile = findViewById(R.id.seccion_files_doc);
        tvImgFile = findViewById(R.id.seccion_files_img);
        tvAudFile = findViewById(R.id.seccion_files_aud);
        tvVidFile = findViewById(R.id.seccion_files_vid);

        Bundle bundle = getIntent().getExtras();

        try {
            if (bundle != null) {
                this.tarea = bundle.getParcelable("TareaEditable");
            }
        } catch (NullPointerException e) {
            Log.e("Bundle recibido nulo", e.toString());
        }
        tvNombre.setText(tarea.getTitulo());
        if (!(tarea.getDescripcion()==null)) {
            tvDescription.setText(tarea.getDescripcion());
        }
        if (!(tarea.getUrlDocumento()==null)) {
            tvDocFile.setText(Uri.parse(tarea.getUrlDocumento()).getLastPathSegment());
        }
        if (!(tarea.getUrlImagen()==null)) {

                tvImgFile.setText(Uri.parse(tarea.getUrlImagen()).getLastPathSegment());
            /*
                tvImgFile.set*/

        }
        if (!(tarea.getUrlAudio()==null)) {
            tvAudFile.setText(Uri.parse(tarea.getUrlAudio()).getLastPathSegment());
        }
       if (!(tarea.getUrlVideo()==null)) {
            tvVidFile.setText(Uri.parse(tarea.getUrlVideo()).getLastPathSegment());
            //tvDocFile.setTooltipText(tarea.getUrlDocumento().);
        }
    }
        public void onBotonAceptarClicked(View v) {
        Intent aListado = new Intent();
        //Indicamos en el resultado que ha sido cancelada la actividad
        setResult(RESULT_CANCELED, aListado);
        //Volvemos a la actividad Listado
        finish();
    }

    public void onImgClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleTareaActivity.this);
        builder.setTitle(R.string.archivo);
        if (!(tarea.getUrlImagen()==null)) {
            builder.setMessage("Nombre del archivo: "+Uri.parse(tarea.getUrlImagen()).getLastPathSegment()+"\n"+
                    "Ruta del archivo: "+tarea.getUrlImagen());
        }else {
            builder.setMessage(getString(R.string.no_archivo));

        }
        builder.setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void onDocClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleTareaActivity.this);
        builder.setTitle(R.string.archivo);
        if (!(tarea.getUrlImagen()==null)) {
            builder.setMessage("Nombre del archivo: "+Uri.parse(tarea.getUrlDocumento()).getLastPathSegment()+"\n"+
                    "Ruta del archivo: "+tarea.getUrlDocumento());
        }else {
            builder.setMessage(getString(R.string.no_archivo));

        }
        builder.setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void onAudClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleTareaActivity.this);
        builder.setTitle(R.string.archivo);
        if (!(tarea.getUrlImagen()==null)) {
            builder.setMessage("Nombre del archivo: "+Uri.parse(tarea.getUrlAudio()).getLastPathSegment()+"\n"+
                    "Ruta del archivo: "+tarea.getUrlAudio());
        }else {
            builder.setMessage(getString(R.string.no_archivo));

        }
        builder.setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void onVidClicked(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleTareaActivity.this);
        builder.setTitle(R.string.archivo);
        if (!(tarea.getUrlImagen()==null)) {
            builder.setMessage("Nombre del archivo: "+Uri.parse(tarea.getUrlVideo()).getLastPathSegment()+"\n"+
                    "Ruta del archivo: "+tarea.getUrlVideo());
        }else {
            builder.setMessage(getString(R.string.no_archivo));

        }
        builder.setPositiveButton(R.string.dialog_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
