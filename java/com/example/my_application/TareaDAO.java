package com.example.my_application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TareaDAO {
    @Query("SELECT * FROM tarea ")
    LiveData<List<Tarea>> getAll();

    @Query("DELETE FROM tarea")
    void borrarTareas();

    @Query("SELECT * FROM tarea WHERE prioritaria=true")
    LiveData<List<Tarea>> getPrioritarias();

    @Query("SELECT * FROM tarea ORDER BY titulo ASC")
    LiveData<List<Tarea>> getOrderByTituloAsc();
    @Query("SELECT * FROM tarea ORDER BY progreso ASC")
    LiveData<List<Tarea>> getOrderByProgresoAsc();
    @Query("SELECT * FROM tarea ORDER BY fechaInicio ASC")
    LiveData<List<Tarea>> getOrderByFechaCreacionAsc();
    @Query("SELECT * FROM tarea ORDER BY (CURRENT_DATE- fechaObjetivo) ASC")
    LiveData<List<Tarea>> getOrderByDiasRestantesAsc();

    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY titulo ASC")
    LiveData<List<Tarea>> getOrderByTituloPrioritariasAsc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY progreso ASC")
    LiveData<List<Tarea>> getOrderByProgresoPrioritariasAsc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY fechaInicio ASC")
    LiveData<List<Tarea>> getOrderByFechaCreacionPrioritariasAsc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY (CURRENT_DATE- fechaObjetivo) ASC")
    LiveData<List<Tarea>> getOrderByDiasRestantesPrioritariasAsc();
    @Query("SELECT * FROM tarea ORDER BY titulo DESC")
    LiveData<List<Tarea>> getOrderByTituloDesc();
    @Query("SELECT * FROM tarea ORDER BY progreso DESC")
    LiveData<List<Tarea>> getOrderByProgresoDesc();
    @Query("SELECT * FROM tarea ORDER BY fechaInicio DESC")
    LiveData<List<Tarea>> getOrderByFechaCreacionDesc();
    @Query("SELECT * FROM tarea ORDER BY (CURRENT_DATE- fechaObjetivo) DESC")
    LiveData<List<Tarea>> getOrderByDiasRestantesDesc();

    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY titulo DESC")
    LiveData<List<Tarea>> getOrderByTituloPrioritariasDesc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY progreso DESC")
    LiveData<List<Tarea>> getOrderByProgresoPrioritariasDesc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY fechaInicio DESC")
    LiveData<List<Tarea>> getOrderByFechaCreacionPrioritariasDesc();
    @Query("SELECT * FROM tarea WHERE prioritaria=true ORDER BY (CURRENT_DATE- fechaObjetivo) DESC")
    LiveData<List<Tarea>> getOrderByDiasRestantesPrioritariasDesc();
    @Insert
    void insertAll(Tarea... tareas);

    @Delete
        //Método que realiza el borrado anterior
    void delete(Tarea tarea);
}
