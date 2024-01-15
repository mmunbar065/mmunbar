package com.example.my_application.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import com.example.my_application.fragments.SettingsFragment;


public class PreferencesActivity extends AppCompatActivity implements SettingsFragment.ComunicacionSettingsFragment {

    ListPreference sizeList;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Mostamos título
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //Establecemos el contenido del título
        getSupportActionBar().setTitle("Preferencias de usuario");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //El recurso 'android.R.id.home' es el botón 'home' (flecha atrás) en la barra de acción
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

public void restablecerPreferencias(){

}
    @Override
    public void onBotonRestablecerClicked(View view) {

       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentById(android.R.id.content);
        if (settingsFragment != null) {
           settingsFragment.findPreference("size").;
        }*/
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("nightMode", false);
        editor.putBoolean("contraste", false);
        editor.putString("size", "2");
        editor.putString("criterios", "2");
        editor.putBoolean("orden", true);
        editor.putBoolean("sd",false);
        editor.putString("limpieza", "0");
        editor.putBoolean("bd", false);
        editor.putString("nombrebd", "bd");
        editor.putString("ip", "10.0.2.2");
        editor.putString("puerto", "1001");
        editor.putString("usuario", "Usuario");
        editor.putString("password", "");
        editor.apply();
        recreate();

        // Your implementation here

        Toast.makeText(this, "Preferencias restablecidas", Toast.LENGTH_SHORT).show();

    }
}