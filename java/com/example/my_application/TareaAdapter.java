package com.example.my_application;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Formatter;
import java.util.List;


public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TaskViewHolder>{
    private long fontSize;
    private final Context contexto;
    private List<Tarea> listaTareas;
    private Tarea tareaSeleccionada;

    private boolean boolPrior;
    public void setBoolPrior(boolean boolPrior){
        this.boolPrior = boolPrior;
    }


    //Constructor
    public TareaAdapter(Context contexto, List<Tarea> tasks, boolean boolPrior) {
        this.contexto = contexto;
        this.listaTareas = tasks;// Observar cambios en la lista de tareas y actualizar el adaptador
        this.boolPrior = boolPrior;
    }
    public void setTareas(List<Tarea> tareas) {
        this.listaTareas = tareas;
    }
    public void setFontSize(long fontSize) {
        this.fontSize = fontSize;

    }
  /*  public void setOrder(String order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (order.equals("2")) {
            tasks.sort(new Comparator<Tarea>() {
                @Override
                public int compare(Tarea tarea1, Tarea tarea2) {
                    return LocalDate.parse(tarea1.getFecha(), formatter).compareTo(LocalDate.parse(tarea2.getFecha(), formatter));
                }
            });
        }else if(order.equals("1")){
            tasks.sort(new Comparator<Tarea>() {
                @Override
                public int compare(Tarea tarea1, Tarea tarea2) {
                    return tarea1.getTitulo().compareTo(tarea2.getTitulo());
                }
            });
        }else if(order.equals("3")){
            tasks.sort(new Comparator<Tarea>() {
                @Override
                public int compare(Tarea tarea1, Tarea tarea2) {
                    return Integer.compare(tarea1.getDiasRestantes(), tarea2.getDiasRestantes());
                }
            });
        }else if(order.equals("4")){
            tasks.sort(new Comparator<Tarea>() {
                @Override
                public int compare(Tarea tarea1, Tarea tarea2) {
                    return Integer.compare(tarea1.getProgreso(), tarea2.getProgreso());
                }
            });
        }
    }
*/
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elemento = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemtarea,parent,false);
        return new TaskViewHolder(elemento);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {

        Tarea tarea = listaTareas.get(position);
        taskViewHolder.bind(tarea);
        taskViewHolder.itemView.setTag(tarea); //Adjuntamos la tarea en la vista del ViewHolder
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    @Nullable
    public Tarea getTareaSeleccionada() {
        return tareaSeleccionada;
    } //Pasar la tarea seleccionada a la actividad

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitulo;
        private final ProgressBar pgProgreso;
        private final TextView tvFechaCreacion;
        private final TextView tvDias;

        //Método constructor
        private TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tituloTarea);
            tvTitulo.setTextSize(fontSize);

            tvFechaCreacion = itemView.findViewById(R.id.fecha);
            tvFechaCreacion.setTextSize(fontSize);

            tvDias = itemView.findViewById(R.id.dias);
            tvDias.setTextSize(fontSize);

            pgProgreso = itemView.findViewById(R.id.progressBar);

            //Mostrar el menú contextual
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                //Hacemos la instantánea de cuál es la tarea seleccionada en el momento que se crea el viewHolder
                tareaSeleccionada = listaTareas.get(getAdapterPosition());
                //Mostramos el menú contextual
                MenuInflater inflater = new MenuInflater(contexto);
                inflater.inflate(R.menu.menu, menu);
            });
        }

        //Método que nos permitirá configurar cada elemento del Recycler con las informaciones
        //de la tarea
        private void bind(Tarea t) {
            tvTitulo.setText(t.getTitulo());
            pgProgreso.setProgress(t.getProgreso());
            tvFechaCreacion.setText(t.getFecha());

            int dias = 0;
            t.getDiasRestantes();

            TextPaint paint = tvTitulo.getPaint();
            //Comprobación de tarea completada
            if(t.getProgreso()<100) { //Si la tarea no está completada
                dias = t.getDiasRestantes();
                //Comprobación de días negativos para color rojo
                if (dias >= 0) {
                    tvDias.setTextColor(contexto.getColor(R.color.oscuro)); // Establece el color a negro

                } else {
                    tvDias.setTextColor(contexto.getColor(R.color.red)); // Establece el color a rojo
                }
                //Si la tarea no está completa bajamos la bandera de tachado
                paint.setFlags(paint.getFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }else{
                tvDias.setTextColor(contexto.getColor(R.color.oscuro)); // Establece el color a negro
                // Si la tarea está completa ponemos la bandera de tachado
                paint.setFlags(paint.getFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
            //Si está completada el valor será 0
            tvDias.setText(String.format(Integer.toString(dias)));

            tvTitulo.setPaintFlags(paint.getFlags());
            tvTitulo.setText(t.getTitulo());

            //Comprobamos si la tarea es prioritaria o no para cambiar la imagen
            if(t.isPrioritaria())
                tvTitulo.setCompoundDrawablesWithIntrinsicBounds(
                        AppCompatResources.getDrawable(contexto,R.drawable.baseline_stars_24_gold),
                        null, null, null);
            else
                tvTitulo.setCompoundDrawablesWithIntrinsicBounds(
                        AppCompatResources.getDrawable(contexto,R.drawable.baseline_stars_24_dark),
                        null, null, null);

            //Comprobamos si se tiene que mostrar el ítem de la lista según esté
            //activado el filtro SOLO prioritarias o no
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();

            if (t.isPrioritaria() || !boolPrior) {
                //Si la tarea es prioritaria o se muestran todas, mostramos el ítem
                layoutParams.height = LayoutParams.WRAP_CONTENT;
                layoutParams.width = LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);

            } else { //En caso contrario ocultamos el ítem
                layoutParams.height = 0;
                layoutParams.width = 0;
                itemView.setVisibility(View.GONE);
            }
        }

    }
}
