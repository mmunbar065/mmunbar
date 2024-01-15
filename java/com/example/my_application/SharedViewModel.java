package com.example.my_application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> nombre = new MutableLiveData<String>();
    private MutableLiveData<String> descripcion = new MutableLiveData<String>();
    private MutableLiveData<String> fechaIni = new MutableLiveData<>();
    private MutableLiveData<String> fechaFin = new MutableLiveData<>();
    private MutableLiveData<String> urlDoc = new MutableLiveData<String>();
    private MutableLiveData<String> urlImg= new MutableLiveData<String>();
    private MutableLiveData<String> urlAud = new MutableLiveData<String>();
    private MutableLiveData<String> urlVid = new MutableLiveData<String>();

    private MutableLiveData<String> pathImg = new MutableLiveData<String>();

    private MutableLiveData<Boolean> isPriority = new MutableLiveData<Boolean>();
    private MutableLiveData<Integer> progreso = new MutableLiveData<>();
    private MutableLiveData<Integer> diasRestantes = new MutableLiveData<>();

    private MutableLiveData<Integer> posicion = new MutableLiveData<>();

    public MutableLiveData<Integer> getProgreso() {
        return progreso;
    }

    public MutableLiveData<Integer> getPosicion() {
        return posicion;
    }

    public MutableLiveData<Integer> getDiasRestantes() {
        return diasRestantes;
    }

    public void setProgreso(Integer progreso) {
        this.progreso.setValue(progreso);
    }

    public void setPosicion(Integer posicion) {
        this.progreso.setValue(posicion);
    }

    public void setDiasRestantes(Integer diasRestantes) {
        if (progreso.equals(100)) {
            this.diasRestantes.setValue(0);
        } else {
            this.diasRestantes.setValue(diasRestantes);
        }
    }

    public void setNombre(String nombre) {
        this.nombre.setValue(nombre);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }
    public void setUrlDoc(String nombre) {
        this.urlDoc.setValue(nombre);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }
    public void setUrlImg(String nombre) {
        this.urlImg.setValue(nombre);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }
    public void setUrlAud(String nombre) {
        this.urlAud.setValue(nombre);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }
    public void setUrlVid(String nombre) {
        this.urlVid.setValue(nombre);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }

    public void setPathImg(String path){
        this.pathImg.setValue(path);
    }
    public LiveData<String> getPathImg(){
        return pathImg;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion.setValue(descripcion);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }
    public void setFechaIni(String fechaIni) {
        this.fechaIni.setValue(fechaIni);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin.setValue(fechaFin);
    }


    public void setIsPriority(Boolean isPriority) {
        this.isPriority.setValue(isPriority);
        //nombre.postValue(nomb); //para llamadas desde hilos en background
    }


    public LiveData<String> getNombre() {
        return nombre;
    }

    public LiveData<String> getUrlDoc() {
        return urlDoc;
    }
    public LiveData<String> getUrlImg() {
        return urlImg;
    }
    public LiveData<String> getUrlAud() {
        return urlAud;
    }
    public LiveData<String> getUrlVid() {
        return urlVid;
    }

    public LiveData<String> getDescripcion() {
        return descripcion;
    }

    public LiveData<String> getFechaIni() {
        return fechaIni;
    }

    public LiveData<String> getFechaFin() {
        return fechaFin;
    }

    public LiveData<Boolean> getIsPriority() {
        return isPriority;
    }



}
