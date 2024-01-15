package com.example.my_application;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@TypeConverters(Tarea.class)

public class Tarea implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "progreso", defaultValue = "0")
    private int progreso;

    @ColumnInfo(name = "titulo")
    @NonNull
    private String titulo;
    @ColumnInfo(name = "fechaInicio", defaultValue = "CURRENT_DATE")
    @NonNull
    private String fecha;
    @ColumnInfo(name = "descripcion", defaultValue = "null")
    private String descripcion;
    @ColumnInfo(name = "prioritaria", defaultValue = "false")
    private boolean prioritaria;
    @ColumnInfo(name = "fechaObjetivo")
    @NonNull
    private String fechaObjetivo;
    @ColumnInfo(name = "contadorId")
    private static long contador_id = 0;
    @ColumnInfo(name = "url_doc", defaultValue = "null")
    private String urlDocumento;

    @ColumnInfo(name = "url_img", defaultValue = "null")
    private String urlImagen;

    @ColumnInfo(name = "url_aud", defaultValue = "null")
    private String urlAudio;

    @ColumnInfo(name = "url_vid", defaultValue = "null")
    private String urlVideo;


    // Constructor

    /*    public Tarea(String titulo, String fecha, String fechaObjetivo, int progreso, boolean prioritaria, String descripcion) {
            this.titulo = titulo;
            this.fecha = fecha;
            this.fechaObjetivo = fechaObjetivo;
            this.progreso = progreso;
            this.prioritaria = prioritaria;
            this.descripcion = descripcion;
            this.urlDocumento=null;
            this.urlImagen=null;
            this.urlAudio=null;
            this.urlVideo=null;
        }*/
    public Tarea(String titulo, String fecha, String fechaObjetivo, int progreso, boolean prioritaria, String descripcion, String urlDocumento, String urlImagen, String urlAudio, String urlVideo) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.fechaObjetivo = fechaObjetivo;
        this.progreso = progreso;
        this.prioritaria = prioritaria;
        this.descripcion = descripcion;
        this.urlDocumento = urlDocumento;
        this.urlImagen = urlImagen;
        this.urlAudio = urlAudio;
        this.urlVideo = urlVideo;
    }

    public void setUrlDocumento(String urlDocumento) {
        this.urlDocumento = urlDocumento;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlDocumento() {

        return this.urlDocumento;
    }

    public String getUrlImagen() {
        return this.urlImagen;
    }

    public String getUrlAudio() {
        return this.urlAudio;
    }

    public String getUrlVideo() {

        return this.urlVideo;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return date == null ? null : new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
    }

    @TypeConverter
    public static Date stringToDate(String dateString) {
        if (dateString == null) {
            return null;
        }

        try {
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Otros métodos
    private boolean validarFormatoFecha(@NonNull String fecha) {
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(19|20)\\d\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fecha);
        return matcher.matches();
    }

    public int quedanDias() {
        Date hoy = new Date(); // Obtener la fecha actual
        long diferenciaMillis = stringToDate(this.fechaObjetivo).getTime() - hoy.getTime();
        long diasDiferencia = TimeUnit.DAYS.convert(diferenciaMillis, TimeUnit.MILLISECONDS);
        return (int) diasDiferencia;
    }

    //Métodos equals y hashCode para que funcione bien buscar en la colección usando indexOf()
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Tarea tarea = (Tarea) other;
        return id == tarea.id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }


    public int getDiasRestantes() {
        Date hoy = new Date(); // Obtener la fecha actual
        long diferenciaMillis = stringToDate(this.fechaObjetivo).getTime() - hoy.getTime();
        long diasDiferencia = TimeUnit.DAYS.convert(diferenciaMillis, TimeUnit.MILLISECONDS);
        return (int) diasDiferencia;
    }
/*
    public void setDiasRestantes(String diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
*/

    public String getFecha() {
        return this.fecha;
    }

    public String getFechaObjetivo() {
        return this.fechaObjetivo;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date validarFecha(@NonNull String fechaCreacion) {
        Date fecha = new Date(); //Para evitar devolver null
        if (validarFormatoFecha(fechaCreacion)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                fecha = sdf.parse(fechaCreacion);
            } catch (Exception e) {
                Log.e("Error fecha", "Parseo de fecha no válido");
            }
        } else {
            Log.e("Error fecha", "Formato de fecha no válido");
        }
        return fecha;
    }


/*    public String getFechaFin(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        final Pattern pattern = Pattern.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(this.fecha);
        String fechaFinString = "";
        if (matcher.matches()) {
            LocalDate fechaInicio = LocalDate.parse(this.fecha, formatter);
            this.fecha = fechaInicio.format(formatter).toString();
            LocalDate fechaFin = LocalDate.now().plusDays(Long.parseLong(this.diasRestantes));
            fechaFinString = fechaFin.format(formatter).toString();
        }
        return fechaFinString;
    }*/

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //Métodos para la interfaz Parcelable


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.titulo);
        dest.writeString(this.fecha);
        dest.writeString(this.fechaObjetivo);
        dest.writeInt(this.progreso);
        dest.writeByte(this.prioritaria ? (byte) 1 : (byte) 0);
        dest.writeString(this.descripcion);
        dest.writeString(this.urlDocumento);
        dest.writeString(this.urlImagen);
        dest.writeString(this.urlAudio);
        dest.writeString(this.urlVideo);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.titulo = source.readString();
        this.fecha = source.readString();
        this.fechaObjetivo = source.readString();
        this.progreso = source.readInt();
        this.prioritaria = source.readByte() != 0;
        this.descripcion = source.readString();
        this.urlDocumento=source.readString();
        this.urlImagen=source.readString();
        this.urlAudio= source.readString();
        this.urlVideo= source.readString();
    }

    protected Tarea(Parcel in) {
        this.id = in.readLong();
        this.titulo = in.readString();
        this.fecha = in.readString();
        this.fechaObjetivo = in.readString();
        this.progreso = in.readInt();
        this.prioritaria = in.readByte() != 0;
        this.descripcion = in.readString();
        this.urlDocumento=in.readString();
        this.urlImagen=in.readString();
        this.urlAudio=in.readString();
        this.urlVideo=in.readString();
    }

    public static final Parcelable.Creator<Tarea> CREATOR = new Parcelable.Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel source) {
            return new Tarea(source);
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size];
        }
    };


}
