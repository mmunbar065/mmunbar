package com.example.my_application;

import android.content.Context;
import android.view.View;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities={Tarea.class}, version=3, exportSchema=false)
public abstract class BaseDatosApp extends RoomDatabase {
    private static BaseDatosApp INSTANCIA;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BaseDatosApp getInstance(Context context) {
      if(INSTANCIA==null) {
          synchronized (BaseDatosApp.class) {
              INSTANCIA = Room.databaseBuilder(
                      context.getApplicationContext(),
                      BaseDatosApp.class,
                      "dbTask").fallbackToDestructiveMigration().build();

          }
      }
        return INSTANCIA;


    }
    public static void destroyInstance() {
        INSTANCIA = null;
    }

    //Método para construir el objeto ProductoDAO con el que accederemos
    //a la base de datos.
    public abstract TareaDAO tareaDAO();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Tu lógica de migración aquí
        }
    };
}
