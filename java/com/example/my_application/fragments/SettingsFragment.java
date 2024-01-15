package com.example.my_application.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.my_application.R;
import com.example.my_application.activities.PreferencesActivity;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences.OnSharedPreferenceChangeListener listener;
    private static final int REQUEST_CODE = 11;
    private ComunicacionSettingsFragment comunicacionSettingsFragment;
    private Context fragmentContext;
    private PreferencesActivity preferencesActivity = (PreferencesActivity) getActivity();
    private Preference pref_restablecer, themeSpinner, contraste, tamanhoLetra, criterio, orden;
    private Button restablecer;
    private Float fontSize = 1.0f;


    //Sobrescribimos onAttach para establecer la comunicación entre el fragmento y la actividad
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragment.ComunicacionSettingsFragment) {  //Si la clase implementa la interfaz
            comunicacionSettingsFragment = (SettingsFragment.ComunicacionSettingsFragment) context; //La clase se convierte en escuchadora
        } else {
            throw new ClassCastException(context + " debe implementar interfaz de comunicación con el primer fragmento");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        preferencesActivity = (PreferencesActivity) getActivity();

        addPreferencesFromResource(R.xml.mis_preferencias);

        themeSpinner = findPreference("nightMode");
        contraste = findPreference("contraste");
        tamanhoLetra = findPreference("size");
        criterio = findPreference("criterios");
        orden = findPreference("orden");



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(preferencesActivity.getApplicationContext());
        String tamanhoLetra = prefs.getString("size", "2");

        if (Integer.valueOf(tamanhoLetra) == 1) {
            fontSize = 0.8f;
        } else if (Integer.valueOf(tamanhoLetra) == 3) {
            fontSize = 1.3f;

        }
        adjustFontScale(preferencesActivity.getResources().getConfiguration(), fontSize);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (key.equals("size")) {
                    String tamanhoLetra = prefs.getString("size", "2");

                    float fontSize = 1.0f;
                    if (Integer.valueOf(tamanhoLetra) == 1) {
                        fontSize = 0.8f;
                    } else if (Integer.valueOf(tamanhoLetra) == 3) {
                        fontSize = 1.3f;

                    }
                    adjustFontScale(preferencesActivity.getResources().getConfiguration(), fontSize);
                    preferencesActivity.recreate();

                }

            }


        };
        prefs.registerOnSharedPreferenceChangeListener(listener);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //updatePreference(findPreference(key), key);

    }


    private void updatePreference(Preference preference, String key) {
        if (preference == null || preference instanceof CheckBoxPreference) {
            return;
        }
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            listPreference.setSummary(listPreference.getEntry());
            return;
        }

        SharedPreferences sharedPrefs = getPreferenceManager().getSharedPreferences();
        preference.setSummary(sharedPrefs.getString(key, "Default"));
    }


    public interface ComunicacionSettingsFragment {
        void onBotonRestablecerClicked(View view);
    }

    public void adjustFontScale(Configuration configuration, float scale) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = preferencesActivity.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) preferencesActivity.getSystemService(preferencesActivity.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        preferencesActivity.getResources().updateConfiguration(configuration, metrics);


    }
}
