package com.example.my_application;

import static java.security.AccessController.getContext;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.io.File;

public class CleaningService extends IntentService {

    private SharedPreferences sharedPreferences;
    public CleaningService() {
        super("CleaningService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
       String valor= sharedPreferences.getString("limpieza", "0");

        // Obtener la lista de archivos adjuntos
        File attachmentDir = getAttachmentDirectory();
        File[] attachments = attachmentDir.listFiles();

        if(!valor.equals("0")) {
            int valorDias= Integer.valueOf(valor);
            if (attachments != null) {
                // Fecha límite para conservar archivos (por ejemplo, archivos más antiguos de 2 días)
                long retentionTime = System.currentTimeMillis() - (valorDias * 24 * 60 * 60 * 1000); // 2 días en milisegundos

                // Iterar sobre los archivos y eliminar los que cumplen con los criterios
                for (File attachment : attachments) {
                    if (attachment.lastModified() < retentionTime) {
                        attachment.delete();
                    }
                }
            }
        }
    }

    private File getAttachmentDirectory() {
        // Obtener el directorio donde se almacenan los archivos adjuntos
        // Puedes personalizar esto según la estructura de tu aplicación
        return new File(getFilesDir(), "attachments");
    }
}

