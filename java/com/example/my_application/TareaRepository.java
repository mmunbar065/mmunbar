package com.example.my_application;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TareaRepository {
    private final TareaDAO tareaDAO;
    private BaseDatosApp baseDatosApp;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


    public TareaRepository(Application application) {
        this.baseDatosApp = BaseDatosApp.getInstance(application);
        this.tareaDAO = baseDatosApp.tareaDAO();
    }

    /**
     * @param order
     * @param asc_desc
     * @param boolPrior
     * @return
     */
    public LiveData<List<Tarea>> getAllTareas(String order, boolean asc_desc, boolean boolPrior) {
        LiveData<List<Tarea>> listaTareas = this.tareaDAO.getAll();
        if (!boolPrior) {
            if (asc_desc) {
                if (order.equals("0")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionAsc();
                } else if (order.equals("1")) {
                    listaTareas = this.tareaDAO.getOrderByTituloAsc();

                } else if (order.equals("2")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionAsc();

                } else if (order.equals("3")) {
                    listaTareas = this.tareaDAO.getOrderByDiasRestantesAsc();

                } else if (order.equals("4")) {
                    listaTareas = this.tareaDAO.getOrderByProgresoAsc();
                }
            } else if (!asc_desc) {
                if (order.equals("0")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionDesc();
                } else if (order.equals("1")) {
                    listaTareas = this.tareaDAO.getOrderByTituloDesc();

                } else if (order.equals("2")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionDesc();

                } else if (order.equals("3")) {
                    listaTareas = this.tareaDAO.getOrderByDiasRestantesDesc();
                } else if (order.equals("4")) {
                    listaTareas = this.tareaDAO.getOrderByProgresoDesc();
                }
            }
        } else if (boolPrior) {
            if (asc_desc) {
                if (order.equals("0")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionPrioritariasAsc();
                } else if (order.equals("1")) {
                    listaTareas = this.tareaDAO.getOrderByTituloPrioritariasAsc();
                } else if (order.equals("2")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionPrioritariasAsc();
                } else if (order.equals("3")) {
                    listaTareas = this.tareaDAO.getOrderByDiasRestantesPrioritariasAsc();
                } else if (order.equals("4")) {
                    listaTareas = this.tareaDAO.getOrderByProgresoPrioritariasAsc();
                }
            } else if (!asc_desc) {
                if (order.equals("0")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionPrioritariasDesc();
                } else if (order.equals("1")) {
                    listaTareas = this.tareaDAO.getOrderByTituloPrioritariasDesc();
                } else if (order.equals("2")) {
                    listaTareas = this.tareaDAO.getOrderByFechaCreacionPrioritariasDesc();
                } else if (order.equals("3")) {
                    listaTareas = this.tareaDAO.getOrderByDiasRestantesPrioritariasDesc();
                } else if (order.equals("4")) {
                    listaTareas = this.tareaDAO.getOrderByProgresoPrioritariasDesc();
                }
            }
        }
        return listaTareas;
    }



    public void insertTareas(Tarea... tareas)     {   executorService.execute(() -> tareaDAO.insertAll(tareas));

    }

    public void deleteTarea(Tarea tarea) {
        executorService.execute(() -> tareaDAO.delete(tarea));
    }

    public void deleteAll() {
        executorService.execute(() -> tareaDAO.borrarTareas());
    }


}

